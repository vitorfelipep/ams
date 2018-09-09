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

import com.ams.api.domain.Servico;
import com.ams.api.repository.ServicoRepository;
import com.ams.api.service.exception.ExistingServiceNameException;

/**
 * Classe de responsabilidade de gerenciamento dos dados de servico:
 *  
 * @author vitor
 *
 */

@Service
@Transactional
public class ServicoService implements Serializable{

	private static final long serialVersionUID = 1571494520692139203L;
	
	@Autowired
	private ServicoRepository servicoRepository;
	
	/**
	 * 
	 * @return List<Servico>
	 */
	public List<Servico> findAllServices() {
		return servicoRepository.findAll();
	}
	
	/**
	 * 
	 * @param codigo
	 * @return Optional<Servico>
	 */
	public Optional<Servico> findOneOptional(Long codigo) {
		return servicoRepository.findById(codigo);
	}
	
	/**
	 * 
	 * @param servico
	 * @return Servico
	 */
	public Servico save(@Valid Servico servico) {
		servicoAlreadyExistent(servico);
		return servicoRepository.save(servico);
	}

	/**
	 * 
	 * @param codigo
	 * @param servico
	 * @return Servico
	 */
	public Servico update(Long codigo, @Valid Servico servico) {
		Servico serviceSaved = findOne(codigo);
		BeanUtils.copyProperties(servico, serviceSaved, "id");
		return servicoRepository.save(serviceSaved);
	}
	
	/**
	 * 
	 * @param servico
	 */
	private void servicoAlreadyExistent(@Valid Servico servico) {
		Optional<Servico> servicoOptional = servicoRepository.findByNomeIgnoreCase(servico.getNome());
		if (servicoOptional.isPresent()) {
			throw new ExistingServiceNameException("Servico já existente!");
		}
	}
	
	/**
	 * 
	 * @param codigo
	 * @return Servico
	 */
	public Servico findOne(Long codigo) {
		Optional<Servico> serviceExisting = servicoRepository.findById(codigo);
		return serviceExisting.orElse(new Servico());
	}
	
}
