package jdev.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import jdev.lojavirtual.model.PessoaFisica;

@Repository
public interface PessoaFisicaRepository extends CrudRepository<PessoaFisica, Long>{

	@Query(value = "select pf from PessoaFisica pf where upper(trim(pf.nome)) like %?1%")
	public List<PessoaFisica> pesquisaPorNomePf(String nome);

	@Query(value = "select pf from PessoaFisica pf where pf.cpf = ?1")
	public List<PessoaFisica> pesquisaPorCpfPf(String cpf);
	
	@Query(value = "select pf from PessoaFisica pf where pf.cpf = ?1")
	public PessoaFisica existeCpfCadastrado(String cpf);
	
	@Query(value = "select pf from PessoaFisica pf where pf.cpf = ?1")
	public List<PessoaFisica> existeCpfCadastradoList(String cpf);
	
}
