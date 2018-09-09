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

import com.ams.api.domain.Cliente;
import com.ams.api.event.RecursoCriadoEvent;
import com.ams.api.exceptionHandler.AmsExceptionHandler.Erro;
import com.ams.api.service.ClienteService;
import com.ams.api.service.exception.ExistingClientWithSameCPFException;

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
@RequestMapping("/clientes")
public class ClienteResource implements Serializable {

	private static final long serialVersionUID = -721263191742053614L;
	
	private final Logger log = LoggerFactory.getLogger(ClienteResource.class);
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private MessageSource messageSource;
	
	@ApiOperation(value = "View a list of available of customers", httpMethod = "GET", nickname = "listAllCustomers")
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
	public List<Cliente> findAll() {
		return clienteService.findAll();
	}
	
	@ApiOperation(value = "Recovery customer by id", httpMethod = "GET", nickname = "findCustomerById")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved item"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@GetMapping("/{codigo}")
	public ResponseEntity<Cliente> findById(@PathVariable Long codigo) {
		Optional<Cliente> cliente = clienteService.findOne(codigo);
		return cliente.isPresent() ? ResponseEntity.ok().body(cliente.get()) : ResponseEntity.notFound().build();
	}
	
	@ApiOperation(value = "Create a new Customer", httpMethod = "POST", nickname = "createCustomer")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully created Customer"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@PostMapping
	public ResponseEntity<Cliente> create(@Valid @RequestBody Cliente cliente, HttpServletResponse response) {
		log.debug("REST request to save Client : {}", cliente);
		Cliente clientSaved = clienteService.save(cliente);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, clientSaved.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(clientSaved);
	}
	
	@ApiOperation(value = "Update a existing customer", httpMethod = "PUT", nickname = "updateCustomer")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully updated customer"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@PutMapping("/{codigo}")
	public ResponseEntity<Cliente> update(@PathVariable Long codigo, @Valid @RequestBody Cliente cliente) {
		Cliente clienteSaved = clienteService.update(codigo, cliente);
		return ResponseEntity.ok().body(clienteSaved);
	}
	
	@ExceptionHandler( { ExistingClientWithSameCPFException.class } )
	public ResponseEntity<Object> handleCustomerWithSameCpfException(ExistingClientWithSameCPFException ex) {
		String mensagemUsuario = messageSource.getMessage("pessoa.com.cpf.existente",null, LocaleContextHolder.getLocale());  
		String mensagemDesenvolvedor = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return ResponseEntity.badRequest().body(erros);
	}
}
