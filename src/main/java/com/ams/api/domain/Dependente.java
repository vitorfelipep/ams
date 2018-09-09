/**
 * 
 */
package com.ams.api.domain;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.br.CPF;

import com.ams.api.domain.enumeration.TipoDependenteEnum;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author vitor
 *
 */

@Entity
@Table(name = "dependente")
@Getter @Setter
@EqualsAndHashCode(exclude = {"nomeDependente", "situacao", "dataNascimento", "cpf", "tipoDependente", "parentesco", "titular"})
public class Dependente implements Serializable {

	private static final long serialVersionUID = 1808716462194474410L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	@Column(name="nome_dependente")
	private String nomeDependente;
	
	private Boolean situacao;
	
	@NotNull
	@Column(name="data_nascimento")
	private LocalDate dataNascimento;
	
	@CPF
	@NotEmpty
	private String cpf;
	
	@Enumerated(EnumType.STRING)
	@Column(name="tipo_dependente")
	private TipoDependenteEnum tipoDependente;
	
	@OneToOne (cascade = CascadeType.ALL)
	private Parentesco parentesco;
	
	@ManyToOne
	@JoinColumn(name="titular", referencedColumnName="id")
	@JsonBackReference
	private Cliente titular;
}
