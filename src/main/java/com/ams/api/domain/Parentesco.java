/**
 * 
 */
package com.ams.api.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ams.api.domain.enumeration.TipoGrauParentescoEnum;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author vitor
 *
 */
@Entity
@Table(name = "parentesco")
@Getter @Setter
@EqualsAndHashCode(exclude = {"grauParentesco", "observacao"})
public class Parentesco implements Serializable {

	private static final long serialVersionUID = -3555631874939989130L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(name="grau_parentesco")
	private TipoGrauParentescoEnum grauParentesco;
	
	private String observacao;
}
