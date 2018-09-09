/**
 * 
 */
package com.ams.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ams.api.domain.Cliente;

/**
 * @author vitor
 *
 */
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

	Optional<Cliente> findByPessoaCpfIgnoreCase(String cpf);

}
