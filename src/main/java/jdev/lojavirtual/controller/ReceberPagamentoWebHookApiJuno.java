package jdev.lojavirtual.controller;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jdev.lojavirtual.model.BoletoJuno;
import jdev.lojavirtual.model.dto.AttibutesNotificacaoPagaApiJunoDto;
import jdev.lojavirtual.model.dto.DataNotificacaoJunoPagamentoDTO;
import jdev.lojavirtual.repository.BoletoJunoRepository;

@Controller
@RestController(value = "/requisicaojunoboleto")
public class ReceberPagamentoWebHookApiJuno implements Serializable{

	private static final long serialVersionUID = 1L;

	@Autowired
	private BoletoJunoRepository boletoJunoRepository;
	
	@ResponseBody
	@RequestMapping(value = "notificacaoapiv2", consumes = {"application/json;charset=UTF-8"},
	headers = "Content-Type=application/json;charset=UTF-8", method = RequestMethod.POST)
	private HttpStatus recebeNoficacaopagamentojunoapiv2(@RequestBody DataNotificacaoJunoPagamentoDTO dataNotificacaoJunoPagamentoDTO) {
		
		dataNotificacaoJunoPagamentoDTO.getData();
		
		for(AttibutesNotificacaoPagaApiJunoDto data : dataNotificacaoJunoPagamentoDTO.getData()) {
			
			String codigoBoletoPix = data.getAttributes().getCharge().getCode();
			
			String status = data.getAttributes().getStatus();
			
			boolean boletoPago = status.equalsIgnoreCase("CONFIRMED") ? true : false;
			
			BoletoJuno boletoJuno = boletoJunoRepository.findByCode(codigoBoletoPix);
			
			if (!boletoJuno.isQuitada() && boletoPago) {
				boletoJunoRepository.quitarBoletoById(boletoJuno.getId());	
				System.out.println("Boleto: " + boletoJuno.getCode()+ " foi quiatadas");
			}
		}
		
		return HttpStatus.OK;
	}
	
}
