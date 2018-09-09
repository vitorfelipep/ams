/**
 * 
 */
package com.ams.api.domain;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.br.CPF;

import com.ams.api.domain.enumeration.EstadoCivilEnum;
import com.ams.api.domain.enumeration.SexoEnum;

import lombok.Getter;
import lombok.Setter;

/**
 * @author vitor
 *
 */

@Embeddable
@Getter @Setter
public class Pessoa {

	@NotNull
	@CPF
	private String cpf;
	
	private String rg;
	
	@Column(name="orgao_expedidor")
	private String orgaoExpedidor;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private SexoEnum sexo;
	
	
	@NotEmpty
	@Column(name="nome_completo")
	private String nomeCompleto;
	
	@NotNull
	@Column(name="data_nascimento")
	private LocalDate dataNascimento;
	
	@NotEmpty
	@Column(name="nome_mae")
	private String nomeMae;
	
	@NotEmpty
	@Column(name="nome_pai")
	private String nomePai;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name="estado_civil")
	private EstadoCivilEnum estadoCivil;
	
	@Email
	private String email;
}
