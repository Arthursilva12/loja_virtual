package jdev.lojavirtual.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import jdev.lojavirtual.model.PessoaFisica;

@Repository
public interface PessoaFisicaRepository extends CrudRepository<PessoaFisica, Long>{

		
	
}
