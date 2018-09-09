/**
 * 
 */
package com.ams.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ams.api.domain.Produto;

/**
 * @author vitor
 *
 */
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
	
	Optional<Produto> findByNomeIgnoreCase(String nome);

	Optional<Produto> findBySkuIgnoreCase(String sku);
}
