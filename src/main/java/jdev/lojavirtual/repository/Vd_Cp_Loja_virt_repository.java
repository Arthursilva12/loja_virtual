package jdev.lojavirtual.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jdev.lojavirtual.model.VendaCompraLojaVirtual;


@Repository
@Transactional
public interface Vd_Cp_Loja_virt_repository extends JpaRepository<VendaCompraLojaVirtual, Long> {


	@Query(value = "select a from VendaCompraLojaVirtual a where a.id = ?1 and a.excluido = false")
	VendaCompraLojaVirtual findByIdExclusao(Long id);
	
	@Query(value = "select i.vendaCompraLojaVirtual from ItemVendaLoja i "
			+ "where i.vendaCompraLojaVirtual.excluido = false and i.produto.id = ?1")
	List<VendaCompraLojaVirtual> vendaPorProduto(Long idProduto);

	@Query(value = "select distinct(i.vendaCompraLojaVirtual) from ItemVendaLoja i "
			+ "where i.vendaCompraLojaVirtual.excluido = false and i.vendaCompraLojaVirtual.pessoa.id = ?1")
	List<VendaCompraLojaVirtual> vendaPorCliente(Long idCliente);

	
	@Query(value = "select distinct(i.vendaCompraLojaVirtual) from ItemVendaLoja i "
			+ "where i.vendaCompraLojaVirtual.excluido = false and upper(trim(i.produto.nome)) like %?1%")
	List<VendaCompraLojaVirtual> vendaPorNomeProduto(String valor);

	
	@Query(value = "select distinct(i.vendaCompraLojaVirtual) from ItemVendaLoja i "
			+ "where i.vendaCompraLojaVirtual.excluido = false "
			+ "and upper(trim(i.vendaCompraLojaVirtual.pessoa.nome)) like %?1%")
	List<VendaCompraLojaVirtual> vendaPorNomeCliente(String nomePessoa);
	
	@Query(value = "select distinct(i.vendaCompraLojaVirtual) from ItemVendaLoja i "
			+ "where i.vendaCompraLojaVirtual.excluido = false "
			+ "and upper(trim(i.vendaCompraLojaVirtual.pessoa.nome)) like %?1%"
			+ "and i.vendaCompraLojaVirtual.pessoa.cpf = ?2")
	List<VendaCompraLojaVirtual> vendaPorNomeCliente(String nomePessoa, String cpf);
	
	@Query(value = "select distinct(i.vendaCompraLojaVirtual) from ItemVendaLoja i "
			+ "where i.vendaCompraLojaVirtual.excluido = false "
			+ "and upper(trim(i.vendaCompraLojaVirtual.enderecoCobranca.rua)) like %?1%")
	List<VendaCompraLojaVirtual> vendaPorEnderecoCobranca(String enderecoCobranca);

	@Query(value = "select distinct(i.vendaCompraLojaVirtual) from ItemVendaLoja i "
			+ "where i.vendaCompraLojaVirtual.excluido = false "
			+ "and upper(trim(i.vendaCompraLojaVirtual.enderecoEntrega.rua)) like %?1%")
	List<VendaCompraLojaVirtual> vendaPorEnderecoEntrega(String enderecoEntrega);

	@Query(value = "select distinct(i.vendaCompraLojaVirtual) from ItemVendaLoja i "
			+ "where i.vendaCompraLojaVirtual.excluido = false "
			+ "and upper(trim(i.vendaCompraLojaVirtual.pessoa.cpf)) like %?1%")
	List<VendaCompraLojaVirtual> vendaPorCpfCliente(String cpf);

	@Query(value = "select distinct(i.vendaCompraLojaVirtual) from ItemVendaLoja i "
			+ "where i.vendaCompraLojaVirtual.excluido = false "
			+ "and upper(trim(i.vendaCompraLojaVirtual.pessoa.cpf)) = ?1")
	List<VendaCompraLojaVirtual> vendaPorCpfClienteLike(String cpf);

	
	@Query(value = "select distinct(i.vendaCompraLojaVirtual) from ItemVendaLoja i "
								+ "where excluido = false and i.vendaCompraLojaVirtual.excluido = false "
								+ "and i.vendaCompraLojaVirtual.dataVenda >= ?1 "
								+ "and i.vendaCompraLojaVirtual.dataVenda <= ?2 ")
	List<VendaCompraLojaVirtual> consultaVendaFaixaData(Date data1, Date data2);
	
}
