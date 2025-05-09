package jdev.lojavirtual.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jdev.lojavirtual.model.VendaCompraLojaVirtual;


@Repository
@Transactional
public interface Vd_Cp_Loja_virt_repository extends JpaRepository<VendaCompraLojaVirtual, Long> {

	@Modifying(flushAutomatically = true)
	@Query(nativeQuery = true, value = 
			  "begin;"
			+ "UPDATE nota_fiscal_venda set venda_compra_loja_virtual_id  = null where venda_compra_loja_virtual_id= :idVenda;"
			+ "delete from status_rastreio where venda_compra_loja_virtual_id = :idVenda;"
			+ "delete from item_venda_loja where venda_compra_loja_virtual_id = :idVenda;"
			+ "delete from nota_fiscal_venda where venda_compra_loja_virtual_id = :idVenda;"
			+ "delete from vd_cp_loja_virt  where id = :idVenda;"
			+ "commit;")
	void exclusaoTotalVendaBanco(@Param("idVenda") Long idVenda);
	
}
