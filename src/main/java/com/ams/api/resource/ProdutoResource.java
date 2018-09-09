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

import com.ams.api.domain.Produto;
import com.ams.api.event.RecursoCriadoEvent;
import com.ams.api.exceptionHandler.AmsExceptionHandler.Erro;
import com.ams.api.service.ProdutoService;
import com.ams.api.service.exception.ExistingProductNameException;
import com.ams.api.service.exception.NegocioException;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Produto resource é a camada de API Rest da aplicação
 * 
 * @author vitor
 *
 */

@RestController
@RequestMapping("/produtos")
public class ProdutoResource implements Serializable {

	private static final long serialVersionUID = -2098990928059188000L;
	
	private final Logger log = LoggerFactory.getLogger(ProdutoResource.class);
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private MessageSource messageSource;
	
	
	@Autowired
	private ProdutoService produtoService;
	
	@ApiOperation(value = "View a list of available of products", httpMethod = "GET", nickname = "listAllProducts")
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
	public List<Produto> listAllProducts() {
		return produtoService.findAllServices();
	}
	
	@ApiOperation(value = "Recovery product by id", httpMethod = "GET", nickname = "findProductById")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved item"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@GetMapping("/{codigo}")
	public ResponseEntity<Produto> findProductById(@PathVariable Long codigo) {
		Optional<Produto> serviceOptional = produtoService.findOneOptional(codigo);
		return serviceOptional.isPresent() ? ResponseEntity.ok().body(serviceOptional.get()) : ResponseEntity.notFound().build();
	}
	
	@ApiOperation(value = "Create a new product", httpMethod = "POST", nickname = "createProduct")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully created Product"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@PostMapping
	public ResponseEntity<Produto> create(@Valid @RequestBody Produto produto, HttpServletResponse response) {
		log.debug("REST request to save Produto : {}", produto);
		Produto produtctSaved = produtoService.save(produto);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, produtctSaved.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(produtctSaved);
	}
	
	@ApiOperation(value = "Update a existing product", httpMethod = "PUT", nickname = "updateProduct")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully updated product"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@PutMapping("/{codigo}")
	public ResponseEntity<Produto> updateProduct(@PathVariable Long codigo, @Valid @RequestBody Produto produto) {
		Produto productSaved = produtoService.update(codigo, produto);
		return ResponseEntity.ok().body(productSaved);
	}
	
	@ApiOperation(value = "Add Stock at existing product", httpMethod = "PUT", nickname = "addStockProduct")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully updated product"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@PutMapping("/{codigo}/addStock")
	public ResponseEntity<Produto> addStockProduct(@PathVariable Long codigo, @RequestBody Integer newAmount) {
		Produto productWithNewStock = produtoService.addStockInToProduct(codigo, newAmount);
		return ResponseEntity.ok().body(productWithNewStock);
	}
	
	@ApiOperation(value = "Substract Stock at existing product", httpMethod = "PUT", nickname = "subTractStockProduct")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully updated product"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@PutMapping("/{codigo}/subtractStock")
	public ResponseEntity<Produto> subTractStockProduct(@PathVariable Long codigo, @RequestBody Integer subtractValue) {
		Produto productWithNewStock = produtoService.subtractStockInToProduct(codigo, subtractValue);
		return ResponseEntity.ok().body(productWithNewStock);
	}
	
	@ExceptionHandler( { ExistingProductNameException.class } )
	public ResponseEntity<Object> handleProductWithSameNameException(ExistingProductNameException ex) {
		String mensagemUsuario = messageSource.getMessage("product.nome.existente",null, LocaleContextHolder.getLocale());  
		String mensagemDesenvolvedor = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return ResponseEntity.badRequest().body(erros);
	}
	
	@ExceptionHandler( { NegocioException.class } )
	public ResponseEntity<Object> handleProductWithSameSKUException(NegocioException ex) {
		String mensagemUsuario = messageSource.getMessage("product.sku.repetido", null, LocaleContextHolder.getLocale());  
		String mensagemDesenvolvedor = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return ResponseEntity.badRequest().body(erros);
	}
}
