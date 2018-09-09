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
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

/**
 * @author vitor
 *
 */

@Entity
@Table(name = "telefone")
@Getter @Setter
public class Telefone implements Serializable {

	private static final long serialVersionUID = 6883725996981761814L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "telefone_residencial")
	private String telefoneResidencial;
	
	@Column(name = "telefone_comercial")
	private String telefoneComercial;
	
	@NotEmpty
	private String celular;
}
