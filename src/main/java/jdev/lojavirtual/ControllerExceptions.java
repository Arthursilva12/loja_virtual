package jdev.lojavirtual;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jdev.lojavirtual.model.dto.ObjetoErroDTO;

@RestControllerAdvice
@ControllerAdvice
public class ControllerExceptions extends ResponseEntityExceptionHandler{

	// Captura exceptions do projeto 
	@ExceptionHandler({Exception.class, RuntimeException.class, Throwable.class})
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
		ObjetoErroDTO objetoErroDTO = new ObjetoErroDTO();
	
		String msg = "";
		
		if(ex instanceof MethodArgumentNotValidException) {
			List<ObjectError> list = ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors();
			
			for (ObjectError objectError : list) {
				msg += objectError.getDefaultMessage() + "\n";
			}
		}else {
			msg = ex.getMessage();
		}
		
		objetoErroDTO.setError(msg);
		objetoErroDTO.setCode(status.value() + " ==> " + status.getReasonPhrase());
		
		return new ResponseEntity<Object>(objetoErroDTO, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	// Captura erros na parte de banco de dados
	@ExceptionHandler({DataIntegrityViolationException.class, ConstraintViolationException.class, SQLException.class})
	protected ResponseEntity<Object> handleExceptionDataIntegry(Exception ex) {
		ObjetoErroDTO objetoErroDTO = new ObjetoErroDTO();
		
		String msg = "";
		
		if(ex instanceof SQLException) {
			msg = "Erro de integridade no banco: " + ((DataIntegrityViolationException) ex).getCause().getMessage();
		}else if(ex instanceof SQLException) {
			msg = "Erro de chave estrangeira" + ((ConstraintViolationException) ex).getCause().getMessage();
		}else if(ex instanceof SQLException) {
			msg = "Erro de SQL do Banco" + ((SQLException) ex).getCause().getMessage();
		}else {
			msg = ex.getMessage();
		}
		
		objetoErroDTO.setError(msg);
		objetoErroDTO.setCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
		
		return new ResponseEntity<Object>(objetoErroDTO, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
