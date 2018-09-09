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

import com.ams.api.domain.Plano;
import com.ams.api.event.RecursoCriadoEvent;
import com.ams.api.exceptionHandler.AmsExceptionHandler.Erro;
import com.ams.api.service.PlanoService;
import com.ams.api.service.exception.ExistingPlanNameException;

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
@RequestMapping("/planos")
public class PlanoResource implements Serializable {

	private static final long serialVersionUID = 7732787693663626901L;
	
	private final Logger log = LoggerFactory.getLogger(PlanoResource.class);
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private PlanoService planoService;
	
	@ApiOperation(value = "View a list of available of plans", httpMethod = "GET", nickname = "listAllPlans")
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
	public List<Plano> findAll() {
		return planoService.findAllPlans();
	}
	
	@ApiOperation(value = "Recovery plans by id", httpMethod = "GET", nickname = "findPlansById")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved item"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@GetMapping("/{codigo}")
	public ResponseEntity<Plano> findPlansById(@PathVariable Long codigo) {
		Optional<Plano> plano = planoService.findOneOptional(codigo);
		return plano.isPresent() ? ResponseEntity.ok().body(plano.get()) : ResponseEntity.notFound().build();
	}
	
	@ApiOperation(value = "Create a new plan", httpMethod = "POST", nickname = "createPlan")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully created plan"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@PostMapping
	public ResponseEntity<Plano> create(@Valid @RequestBody Plano plano, HttpServletResponse response) {
		log.debug("REST request to save Client : {}", plano);
		Plano planoSaved = planoService.save(plano);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, planoSaved.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(planoSaved);
	}
	
	@ApiOperation(value = "Update a existing customer", httpMethod = "PUT", nickname = "updateCustomer")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully updated plan"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@PutMapping("/{codigo}")
	public ResponseEntity<Plano> update(@PathVariable Long codigo, @Valid @RequestBody Plano plano) {
		Plano planoSaved = planoService.update(codigo, plano);
		return ResponseEntity.ok().body(planoSaved);
	}
	
	@ExceptionHandler( { ExistingPlanNameException.class } )
	public ResponseEntity<Object> handleCustomerWithSameCpfException(ExistingPlanNameException ex) {
		String mensagemUsuario = messageSource.getMessage("plano.nome.existente",null, LocaleContextHolder.getLocale());  
		String mensagemDesenvolvedor = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return ResponseEntity.badRequest().body(erros);
	}
}
