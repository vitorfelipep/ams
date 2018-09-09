/**
 * 
 */
package com.ams.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ams.api.domain.Dependente;

/**
 * @author vitor
 *
 */
public interface DependenteRepository extends JpaRepository<Dependente, Long> {
	
	List<Optional<Dependente>> findByCpfIgnoreCase(String cpf);
}
