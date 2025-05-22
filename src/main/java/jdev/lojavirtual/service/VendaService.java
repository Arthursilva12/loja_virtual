package jdev.lojavirtual.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jdev.lojavirtual.model.VendaCompraLojaVirtual;
import jdev.lojavirtual.repository.Vd_Cp_Loja_virt_repository;

@Service
public class VendaService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private Vd_Cp_Loja_virt_repository vd_Cp_Loja_virt_repository;
	

	public void ativaRegistroVendaBanco(Long idVenda) {
		String sql = "begin; update vd_cp_loja_virt set excluido = false where id = " + idVenda + "; commit;";
		jdbcTemplate.execute(sql);
	}
	
	
	public void exclusaoTotalVendaBanco2(Long idVenda) {
		String sql = "begin; update vd_cp_loja_virt set excluido = true where id = " + idVenda + "; commit;";
		jdbcTemplate.execute(sql);
	}
	
	public void exclusaoTotalVendaBanco(Long idVenda) {
		
		String value = 
			" begin;"
			+ "UPDATE nota_fiscal_venda set venda_compra_loja_virtual_id  = null where venda_compra_loja_virtual_id= "+idVenda+";"
			+ "delete from status_rastreio where venda_compra_loja_virtual_id = "+idVenda+";"
			+ "delete from item_venda_loja where venda_compra_loja_virtual_id = "+idVenda+";"
			+ "delete from nota_fiscal_venda where venda_compra_loja_virtual_id = "+idVenda+";"
			+ "delete from vd_cp_loja_virt  where id = "+idVenda+";"
			+ "commit;";
		
		jdbcTemplate.execute(value);
	}
	
	
	public List<VendaCompraLojaVirtual> consultaVendaFaixaData(String data1, String data2) throws ParseException {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		Date d1 = dateFormat.parse(data1);
		Date d2 = dateFormat.parse(data2);

		return vd_Cp_Loja_virt_repository.consultaVendaFaixaData(d1, d2);
	}
	
}
