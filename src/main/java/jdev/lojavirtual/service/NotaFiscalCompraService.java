package jdev.lojavirtual.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jdev.lojavirtual.model.dto.ObjetoRequisicaoRelatorioProdutoNotaFiscalDTO;

@Service
public class NotaFiscalCompraService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<ObjetoRequisicaoRelatorioProdutoNotaFiscalDTO> gerarRelatorioProdCompraNota(
			ObjetoRequisicaoRelatorioProdutoNotaFiscalDTO objetoRequisicaoRelatorioProdutoNotaFiscalDto) {
		
		List<ObjetoRequisicaoRelatorioProdutoNotaFiscalDTO> retorno = new ArrayList<ObjetoRequisicaoRelatorioProdutoNotaFiscalDTO>();
		
		String sql = " select p.id as codigoProduto, p.nome as nomeProduto, p.valor_venda as valorVendaProduto, ntp.quantidade as qtdComprada, "
				+ " pj.id as codigoFornecedor, pj.nome as nomeFornecedor, cfc.data_compra as dataCompra "
				+ " from nota_fiscal_compra  as cfc "
				+ " inner join nota_item_produto as ntp on cfc.id = nota_fiscal_compra_id "
				+ " inner join produto as p on p.id = ntp.produto_id "
				+ " inner join pessoa_juridica as pj on pj.id = cfc.pessoa_id  where";
		
		// faixa de data é obrigatoria
		sql += " cfc.data_compra >= '"+objetoRequisicaoRelatorioProdutoNotaFiscalDto.getDataInicial()+"' ";
		sql += " and cfc.data_compra <= '"+objetoRequisicaoRelatorioProdutoNotaFiscalDto.getDataFinal()+"' ";
		
		if (!objetoRequisicaoRelatorioProdutoNotaFiscalDto.getCodigoNota().isEmpty()) {
			sql += " and cfc.id = "+ objetoRequisicaoRelatorioProdutoNotaFiscalDto.getCodigoNota()+" ";
		}
		
		if (!objetoRequisicaoRelatorioProdutoNotaFiscalDto.getCodigoProduto().isEmpty()) {
			sql += " and p.id = "+objetoRequisicaoRelatorioProdutoNotaFiscalDto.getCodigoProduto()+" ";
		}
		
		if (!objetoRequisicaoRelatorioProdutoNotaFiscalDto.getNomeProduto().isEmpty()) {
			sql += " and upper(p.nome) like upper('%"+objetoRequisicaoRelatorioProdutoNotaFiscalDto.getNomeProduto()+"%') ";
		}
		
		if (!objetoRequisicaoRelatorioProdutoNotaFiscalDto.getNomeFornecedor().isEmpty()) {
			sql += " and upper(pj.nome) like upper('%"+objetoRequisicaoRelatorioProdutoNotaFiscalDto.getNomeFornecedor()+"%')";
		}
		
		retorno = jdbcTemplate.query(sql, new BeanPropertyRowMapper(ObjetoRequisicaoRelatorioProdutoNotaFiscalDTO.class));
		
		
		return retorno;
	}

}
