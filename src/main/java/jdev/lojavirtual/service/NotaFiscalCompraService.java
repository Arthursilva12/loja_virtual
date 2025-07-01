package jdev.lojavirtual.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jdev.lojavirtual.model.dto.ObjetoRelatorioStatusCompraDTO;
import jdev.lojavirtual.model.dto.ObjetoRequisicaoRelatorioProdutoAlertaEstoqueBaixoDTO;
import jdev.lojavirtual.model.dto.ObjetoRequisicaoRelatorioProdutoNotaFiscalDTO;

@Service
public class NotaFiscalCompraService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 *  Este relatorio permite saber os produtos comprados para serem vendidos pela loja virtual,
	 *  todos os produtos tem relação com a nota fiscal de compra/venda.
	 *  @param ObjetoRequisicaoRelatorioProdutoNotaFiscalDTO
	 *  @param dataInicio e dataFinal são parametros obrigatorio 
	 * 	@return List<ObjetoRequisicaoRelatorioProdutoNotaFiscalDTO> 
	 */
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
	
	/**
	 * Este relatório retorna os produtos que estão em estoque menor ou igual a 
	 * quantidade definida no campo qrd_alerta_estoque_baixo. 
	 * 
	 * @param AlertaEstoque ObjetoRequisicaoRelatorioProdutoAlertaEstoqueBaixoDTO
	 * @return List<ObjetoRequisicaoRelatorioProdutoAlertaEstoqueBaixoDTO>
	 */
	public List<ObjetoRequisicaoRelatorioProdutoAlertaEstoqueBaixoDTO> gerarRelatorioAlertEstoque(
								ObjetoRequisicaoRelatorioProdutoAlertaEstoqueBaixoDTO alertaEstoque) {
		
		List<ObjetoRequisicaoRelatorioProdutoAlertaEstoqueBaixoDTO> retorno = 
				new ArrayList<ObjetoRequisicaoRelatorioProdutoAlertaEstoqueBaixoDTO>();
		
		String sql = " select p.id as codigoProduto, p.nome as nomeProduto,"
				+ " p.valor_venda as valorVendaProduto, ntp.quantidade as qtdComprada, "
				+ " pj.id as codigoFornecedor, pj.nome as nomeFornecedor, cfc.data_compra as dataCompra, "
				+ " p.qtd_estoque as qtdEstoque, p.qrd_alerta_estoque_baixo as qtdAlertaEstoqueBaixo "
				+ " from nota_fiscal_compra  as cfc "
				+ " inner join nota_item_produto as ntp on cfc.id = nota_fiscal_compra_id "
				+ " inner join produto as p on p.id = ntp.produto_id "
				+ " inner join pessoa_juridica as pj on pj.id = cfc.pessoa_id  where";
		
		// faixa de data é obrigatoria
		sql += " cfc.data_compra >= '"+alertaEstoque.getDataInicial()+"' ";
		sql += " and cfc.data_compra <= '"+alertaEstoque.getDataFinal()+"' ";
		sql += " and p.alerta_qtde_estoque = true and p.qtd_estoque <= p.qrd_alerta_estoque_baixo ";
				
		if (!alertaEstoque.getCodigoNota().isEmpty()) {
			sql += " and cfc.id = "+ alertaEstoque.getCodigoNota()+" ";
		}
		
		if (!alertaEstoque.getCodigoProduto().isEmpty()) {
			sql += " and p.id = "+alertaEstoque.getCodigoProduto()+" ";
		}
		
		if (!alertaEstoque.getNomeProduto().isEmpty()) {
			sql += " and upper(p.nome) like upper('%"+alertaEstoque.getNomeProduto()+"%') ";
		}
		
		if (!alertaEstoque.getNomeFornecedor().isEmpty()) {
			sql += " and upper(pj.nome) like upper('%"+alertaEstoque.getNomeFornecedor()+"%')";
		}
		
		retorno = jdbcTemplate.query(sql, new BeanPropertyRowMapper(ObjetoRequisicaoRelatorioProdutoAlertaEstoqueBaixoDTO.class));
		
		
		return retorno;
	}
	
	/**
	 * Este relatorio retorna os produtos que tiveram compra cancelada ou abandonou o carrinho 
	 * 
	 * @param objetoRequisicaoRelatorioCompraCanceladaDTO
	 * @return List<ObjetoRequisicaoRelatorioCompraCanceladaDTO>
	 */
	public List<ObjetoRelatorioStatusCompraDTO> gerarRelatorioStatusVendaLojaVirtual(
									ObjetoRelatorioStatusCompraDTO objetoRequisicaoRelatorioCompraCanceladaDTO) {
		
		List<ObjetoRelatorioStatusCompraDTO> retorno = 
				new ArrayList<ObjetoRelatorioStatusCompraDTO>();
		
		String sql = " select p.id as codigoProduto, p.nome as nomeProduto, "
				+ " pf.email as emailCliente, pf.telefone as foneCliente,"
				+ " p.valor_venda as valorVendaProduto, pf.id as codigoCliente, "
				+ " pf.nome as nomeCliente, p.qtd_estoque as qtdEstoque,"
				+ " cfc.id as codigoVenda, cfc.status_venda_loja_virtual as statusVenda "
				+ " from vd_cp_loja_virt as cfc"
				+ " inner join item_venda_loja as ntp on ntp.venda_compra_loja_virtual_id = cfc.id "
				+ " inner join produto as p on p.id = ntp.produto_id "
				+ " inner join pessoa_fisica as pf on pf.id = cfc.pessoa_id";
		
		// faixa de data é obrigatoria
		sql += " where cfc.data_venda >= '"+objetoRequisicaoRelatorioCompraCanceladaDTO.getDataInicial()+"' and cfc.data_venda <= '"+objetoRequisicaoRelatorioCompraCanceladaDTO.getDataFinal()+"' ";
		
		if(!objetoRequisicaoRelatorioCompraCanceladaDTO.getNomeCliente().isEmpty()) {
			sql += "and upper(pf.nome) like upper('"+objetoRequisicaoRelatorioCompraCanceladaDTO.getNomeCliente()+"') ";
		}
		
		if(!objetoRequisicaoRelatorioCompraCanceladaDTO.getNomeProduto().isEmpty()) {
			sql += " and upper(p.nome) like upper('%"+objetoRequisicaoRelatorioCompraCanceladaDTO.getNomeProduto()+"%') ";
		}
		
		if(!objetoRequisicaoRelatorioCompraCanceladaDTO.getStatusVenda().isEmpty()) {
			sql += " and cfc.status_venda_loja_virtual in ('"+objetoRequisicaoRelatorioCompraCanceladaDTO.getStatusVenda()+"') ";
		}
	
		
		retorno = jdbcTemplate.query(sql, new BeanPropertyRowMapper(ObjetoRelatorioStatusCompraDTO.class));
		
		return retorno;
	}

}
