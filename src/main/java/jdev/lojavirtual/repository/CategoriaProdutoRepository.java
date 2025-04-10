package jdev.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jdev.lojavirtual.model.CategoriaProduto;

@Repository
public interface CategoriaProdutoRepository extends JpaRepository<CategoriaProduto, Long> {

	@Query(nativeQuery = true, value =  "select count(1) > 0 from categoria_produto where upper(trim(nome_desc)) = upper(trim(?1))")
	public boolean existeCategoria(String nomeCategoria);

	@Query("select pd from CategoriaProduto pd where upper(trim(pd.nomeDesc)) like %?1%")
	public List<CategoriaProduto> buscarCategoriaDesc(String nomeDesc);
	
}
