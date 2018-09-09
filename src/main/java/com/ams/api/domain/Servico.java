/**
 * 
 */
package com.ams.api.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.ams.api.domain.enumeration.TipoServicoEnum;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author vitor
 *
 */

@Entity
@Table(name = "servico")
@Getter @Setter
@EqualsAndHashCode(exclude = {"nome", "descricao", "tipoServico", "precoUnitario"})
public class Servico implements Serializable {

	private static final long serialVersionUID = -1630603012715432870L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	@Column(name="nome_servico")
	private String nome;
	
	@NotEmpty
	@Column(name="descricao_servico")
	private String descricao;
	
	@Enumerated(EnumType.STRING)
	@Column(name="tipo_servico")
	private TipoServicoEnum tipoServico;
	
	@NotNull
	@Column(name="preco_unitario")
	private BigDecimal precoUnitario;
}
