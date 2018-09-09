/**
 * 
 */
package com.ams.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ams.api.domain.Servico;

/**
 * @author vitor
 *
 */
public interface ServicoRepository extends JpaRepository<Servico, Long> {
	
	Optional<Servico> findByNomeIgnoreCase(String nome);
}
