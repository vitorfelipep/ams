/**
 * 
 */
package com.ams.api.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

/**
 * @author vitor
 *
 */

@Getter @Setter
public class Funcionario implements Serializable {

	private static final long serialVersionUID = -352291641285050255L;
	
	private Long id;
	
	private Pessoa pessoa;
	private Cargo funcao;
	private LocalDateTime dataCadastro;
	private LocalDateTime dataSaida;
	private Boolean situacaoFuncinario;
	private String emailAMSFuncionario;
	private Usuario usuario;
	
	
}
