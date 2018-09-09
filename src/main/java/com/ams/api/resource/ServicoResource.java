/**
 * 
 */
package com.ams.api.resource;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ams.api.domain.Servico;
import com.ams.api.event.RecursoCriadoEvent;
import com.ams.api.exceptionHandler.AmsExceptionHandler.Erro;
import com.ams.api.service.ServicoService;
import com.ams.api.service.exception.ExistingServiceNameException;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author vitor
 *
 */

@RestController
@RequestMapping("/servicos")
public class ServicoResource implements Serializable {

	private static final long serialVersionUID = -5634035901059577609L;
	
	private final Logger log = LoggerFactory.getLogger(ServicoResource.class);
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private ServicoService servicoService;
	
	
	@ApiOperation(value = "View a list of available of services", httpMethod = "GET", nickname = "listAllServices")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved list"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", dataType = "string", paramType = "query", value = "Results page you want to retrieve (0..N)"),
			@ApiImplicitParam(name = "size", dataType = "string", paramType = "query", value = "Number of records per page."),
			@ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "Sorting criteria in the format: property(,asc|desc). "
					+ "Default sort order is ascending. " + "Multiple sort criteria are supported.") })
	@GetMapping
	public List<Servico> listAllServices() {
		return servicoService.findAllServices();
	}
	
	@ApiOperation(value = "Recovery service by id", httpMethod = "GET", nickname = "findServiceById")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved item"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@GetMapping("/{codigo}")
	public ResponseEntity<Servico> findServiceById(@PathVariable Long codigo) {
		Optional<Servico> serviceOptional = servicoService.findOneOptional(codigo);
		return serviceOptional.isPresent() ? ResponseEntity.ok().body(serviceOptional.get()) : ResponseEntity.notFound().build();
	}
	
	@ApiOperation(value = "Create a new service", httpMethod = "POST", nickname = "createService")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully created Customer"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@PostMapping
	public ResponseEntity<Servico> create(@Valid @RequestBody Servico servico, HttpServletResponse response) {
		log.debug("REST request to save Servico : {}", servico);
		Servico servicoSaved = servicoService.save(servico);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, servicoSaved.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(servicoSaved);
	}
	
	@ApiOperation(value = "Update a existing service", httpMethod = "PUT", nickname = "updateService")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully updated service"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@PutMapping("/{codigo}")
	public ResponseEntity<Servico> update(@PathVariable Long codigo, @Valid @RequestBody Servico servico) {
		Servico servicoSaved = servicoService.update(codigo, servico);
		return ResponseEntity.ok().body(servicoSaved);
	}
	
	@ExceptionHandler( { ExistingServiceNameException.class } )
	public ResponseEntity<Object> handleCustomerWithSameCpfException(ExistingServiceNameException ex) {
		String mensagemUsuario = messageSource.getMessage("servico.nome.existente",null, LocaleContextHolder.getLocale());  
		String mensagemDesenvolvedor = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return ResponseEntity.badRequest().body(erros);
	}
}
