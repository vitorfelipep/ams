/**
 * 
 */
package com.ams.api.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author vitor
 *
 */

@Entity
@Table(name = "endereco")
@Getter @Setter
@EqualsAndHashCode(exclude = {"logradouro", "pontoReferencia", "cep", "cidade"})
public class Endereco implements Serializable {

	private static final long serialVersionUID = -1696352685946590035L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	private String logradouro;
	
	
	@Column(name="ponto_referencia")
	private String pontoReferencia;
	
	@NotEmpty
	private String cep;
	
	@OneToOne
	@JoinColumn(name="cidade_id")
	private Cidade cidade;
}
