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
import org.springframework.util.CollectionUtils;

import com.ams.api.domain.Cliente;
import com.ams.api.repository.ClienteRepository;
import com.ams.api.service.exception.ExistingClientWithSameCPFException;

/**
 * @author vitor
 *
 */

@Service
@Transactional
public class ClienteService implements Serializable{

	private static final long serialVersionUID = 6026938304657136768L;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	public List<Cliente> findAll() {
		return clienteRepository.findAll();
	}

	public Optional<Cliente> findOne(Long codigo) {
		return clienteRepository.findById(codigo);
	}

	public Cliente save(@Valid Cliente cliente) {
		clienteAlreadyExistent(cliente);
		
		Cliente clienteSaved = clienteRepository.save(cliente);
		if (!CollectionUtils.isEmpty(cliente.getDependentes())) {
			cliente.getDependentes().forEach(d -> {
				if (d.getTitular() == null) {
					d.setTitular(clienteSaved);
				}
			});
			cliente = clienteRepository.save(cliente);
		}
		
		return cliente;
	}

	/**
	 * @param Cliente
	 * @throws ExistingClientWithSameCPFException 
	 */
	private void clienteAlreadyExistent(Cliente cliente) {
		Optional<Cliente> clienteOpcional = clienteRepository.findByPessoaCpfIgnoreCase(cliente.getPessoa().getCpf());
		if(clienteOpcional.isPresent()) {
			throw new ExistingClientWithSameCPFException("Já existe um cliente cadastrado com este cpf. method: clienteAlreadyExistent chamado pela clienteService");
		}
	}

	public Cliente update(Long codigo, @Valid Cliente cliente) {
		Optional<Cliente> clienteSaved = clienteRepository.findById(codigo);
		if (clienteSaved.isPresent()) {
			if (!CollectionUtils.isEmpty(cliente.getDependentes())) {
				cliente.getDependentes().forEach(d -> {
					if (d.getTitular() == null) {
						d.setTitular(clienteSaved.get());
					}
				});
			}
			BeanUtils.copyProperties(cliente, clienteSaved.get(), "id");
			return clienteRepository.save(clienteSaved.get());
		} else {
			return clienteSaved.orElse(null);
		}
	}
	
	
	
	
}
