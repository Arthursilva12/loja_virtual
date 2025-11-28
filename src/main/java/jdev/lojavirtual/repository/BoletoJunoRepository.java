package jdev.lojavirtual.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jdev.lojavirtual.model.BoletoJuno;

@Repository
public interface BoletoJunoRepository extends JpaRepository<BoletoJuno, Long>{
 
	@Query("select b from BoletoJuno b where b.vendaCompraLojaVirtual.id = ?1 and b.quitado = false")
	public List<BoletoJuno> cobrancaDaVendaCompra(Long idVendaCompra);
	
	@org.springframework.data.jpa.repository.Query("select b from BoletoJuno b where b.code = ?1")
	public BoletoJuno findByCode (String code);
	
	@org.springframework.data.jpa.repository.Modifying(flushAutomatically = true)
	@org.springframework.data.jpa.repository.Query(nativeQuery = true, value = "update boleto_juno set quitado = true where code = ?1")
	public void quitarBoleto(String code);
	
	
	@Transactional
	@org.springframework.data.jpa.repository.Modifying(flushAutomatically = true)
	@org.springframework.data.jpa.repository.Query(nativeQuery = true, value = "update boleto_juno set quitado = true where id = ?1")
	public void quitarBoletoById(Long id);

	@Transactional
	@org.springframework.data.jpa.repository.Modifying(flushAutomatically = true)
	@org.springframework.data.jpa.repository.Query(nativeQuery = true, value = "delete from boleto_juno where code = ?1")
	public void deleteByCode(String code);
	
}
