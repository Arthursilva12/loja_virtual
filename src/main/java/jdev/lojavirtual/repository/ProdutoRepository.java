package jdev.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jdev.lojavirtual.model.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

	@Query(nativeQuery = true, value =  "select count(1) > 0 from produto where upper(trim(nome)) = upper(trim(?1))")
	public boolean existeProduto(String nomeProduto);

	@Query("select pd from Produto pd where upper(trim(pd.nome)) like %?1%")
	public List<Produto> buscarProdutoNome(String nome);
	
	@Query("select pd from Produto pd where upper(trim(pd.nome)) like %?1%and empresa_id = ?2")
	public List<Produto> buscarProdutoNome(String nome, Long idEmpresa);
	
	
	
}
