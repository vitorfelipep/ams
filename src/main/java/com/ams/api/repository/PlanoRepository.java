package com.ams.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ams.api.domain.Plano;

public interface PlanoRepository extends JpaRepository<Plano, Long> {
	
	Optional<Plano> findByNomeIgnoreCase(String nome);
}
