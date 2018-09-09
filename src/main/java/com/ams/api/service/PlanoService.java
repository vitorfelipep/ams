/**
 * 
 */
package com.ams.api.service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ams.api.domain.Plano;
import com.ams.api.repository.PlanoRepository;
import com.ams.api.service.exception.ExistingPlanNameException;

/**
 * @author vitor
 *
 */

@Service
@Transactional
public class PlanoService implements Serializable {

	private static final long serialVersionUID = -2595688091600569337L;
	
	@Autowired
	private PlanoRepository planoRepository;
	
	public List<Plano> findAllPlans() {
		return planoRepository.findAll();
	}

	public Plano findOne(Long codigo) {
		Optional<Plano> planExisting = planoRepository.findById(codigo);
		return planExisting.orElse(new Plano());
	}

	public Plano save(Plano plano) {
		planAlreadyExistent(plano);
		plano.setDataCriaçãoPlno(LocalDateTime.now());
		return planoRepository.saveAndFlush(plano);
	}

	public Plano update(Long codigo, Plano plano) {
		Plano planoSaved = findOne(codigo);
		BeanUtils.copyProperties(plano, planoSaved, "id");
		return planoRepository.save(planoSaved);
	}
	
	private void planAlreadyExistent(Plano plano) {
		Optional<Plano> planOptional = planoRepository.findByNomeIgnoreCase(plano.getNome());
		if (planOptional.isPresent()) {
			throw new ExistingPlanNameException("Já existe plano com este nome, por favor mude o nome do plano.");
		}
	}

	public Optional<Plano> findOneOptional(Long codigo) {
		return planoRepository.findById(codigo);
	}
}
