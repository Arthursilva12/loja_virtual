package jdev.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jdev.lojavirtual.model.MarcaProduto;

@Repository
@Transactional
public interface MarcaRepository extends JpaRepository<MarcaProduto, Long>{
	
	@Query("select a from MarcaProduto a where upper(trim(a.nomeDesc)) like %?1%")
	public List<MarcaProduto> buscarMarcaDesc(String desc);
	
}
