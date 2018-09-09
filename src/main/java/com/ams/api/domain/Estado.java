/**
 * 
 */
package com.ams.api.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author vitor
 *
 */

@Entity
@Table(name = "estado")
@Getter @Setter
@EqualsAndHashCode(exclude = {"nome", "descricao", "cidades"})
public class Estado implements Serializable {

	private static final long serialVersionUID = -9057868046279979881L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nome;
	
	private String descricao;
	
	@OneToMany(mappedBy = "estado")
	@JsonBackReference
	private List<Cidade> cidades;
}
