package jdev.lojavirtual.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jdev.lojavirtual.model.Acesso;
import jdev.lojavirtual.repository.AcessoRepository;

@Service
public class AcessoService {

	@Autowired
	private AcessoRepository acessoRepository;

	public Acesso save(Acesso acesso) {
		// Qualquer tipo de validação
		return acessoRepository.save(acesso);
	}

}
