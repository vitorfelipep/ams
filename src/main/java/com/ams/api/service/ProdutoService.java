/**
 * 
 */
package com.ams.api.service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ams.api.domain.Produto;
import com.ams.api.repository.ProdutoRepository;
import com.ams.api.service.exception.ExistingProductNameException;
import com.ams.api.service.exception.NegocioException;

/**
 * @author vitor
 *
 */

@Service
@Transactional
public class ProdutoService implements Serializable {

	/**
	 * Static serial version
	 */
	private static final long serialVersionUID = -6187530379873983110L;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	/**
	 * List all products
	 * @return List<Produto>
	 */
	public List<Produto> findAllServices() {
		return produtoRepository.findAll();
	}

	/**
	 * 
	 * @param codigo
	 * @return Optional<Produto>
	 */
	public Optional<Produto> findOneOptional(Long codigo) {
		return produtoRepository.findById(codigo);
	}

	/**
	 * 
	 * @param produto
	 * @return Produto
	 */
	public Produto save(@Valid Produto produto) {
		verifyProductExistentBySku(produto.getSku());
		produtoAlreadyExistent(produto);
		return produtoRepository.save(produto);
	}
	
	/**
	 * 
	 * @param codigo
	 * @param produto
	 * @return Produto
	 */
	public Produto update(Long codigo, @Valid Produto produto) {
		Produto productSaved = findOne(codigo);
		BeanUtils.copyProperties(produto, productSaved, "id");
		return produtoRepository.save(productSaved);
	}
	
	/**
	 * 
	 * @param produto
	 */
	private void produtoAlreadyExistent(@Valid Produto produto) {
		Optional<Produto> produtoOptional = produtoRepository.findByNomeIgnoreCase(produto.getNome());
		if (produtoOptional.isPresent()) {
			throw new ExistingProductNameException("Produto com nome já existente!");
		}
	}
	
	/**
	 * 
	 * @param codigo
	 * @return Produto
	 */
	private Produto findOne(Long codigo) {
		Optional<Produto> produtoExisting = produtoRepository.findById(codigo);
		return produtoExisting.orElse(new Produto());
	}
	
	/**
	 * 
	 * @param sku
	 * @return
	 */
	private void verifyProductExistentBySku(String sku) {
		Optional<Produto> produtoExistente = produtoRepository.findBySkuIgnoreCase(sku);
		if (produtoExistente.isPresent()) {
			throw new NegocioException("Já existe um produto com o SKU informado.");
		}
	}
	
	/**
	 * 
	 * @param codigo
	 * @param newAmount
	 * @return
	 */
	public Produto addStockInToProduct(Long codigo, @Valid Integer newAmount) {
		Produto produtoSaved = findOne(codigo);
		produtoSaved.adicionarEstoque(newAmount);
		return produtoRepository.save(produtoSaved);
	}
	
	/**
	 * 
	 * @param codigo
	 * @param subtractValue
	 * @return
	 */
	public Produto subtractStockInToProduct(Long codigo, @Valid Integer subtractValue) {
		Produto produtoSaved = findOne(codigo);
		produtoSaved.baixarEstoque(subtractValue);
		return produtoRepository.save(produtoSaved);
	}
}
