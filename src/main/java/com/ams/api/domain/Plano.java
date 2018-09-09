/**
 * 
 */
package com.ams.api.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.ams.api.domain.enumeration.TipoPlanoEnum;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author vitor
 *
 */

@Entity
@Table(name = "plano")
@Getter @Setter
@EqualsAndHashCode(exclude = {"nome", "descricao", "dataCriaçãoPlno", "tipoPlano", "preco", "desconto", "servicos"})
public class Plano implements Serializable{

	private static final long serialVersionUID = 5587620924913102111L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	@Column(name="nome_plano")
	private String nome;
	
	@NotEmpty
	@Column(name="descricao_plano")
	private String descricao;
	
	@Column(name="data_criacao_plano")
	private LocalDateTime dataCriaçãoPlno;
	
	@Enumerated(EnumType.STRING)
	@Column(name="tipo_plano")
	private TipoPlanoEnum tipoPlano;
	
	@Column(name="preco") 
	private BigDecimal preco;
	
	@Column(name="desconto")
	private BigDecimal desconto;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "planos_servicos", joinColumns = @JoinColumn(name = "codigo_plano"),
			inverseJoinColumns = @JoinColumn(name = "codigo_servico"))
	private List<Servico> servicos;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "planos_produtos", joinColumns = @JoinColumn(name = "codigo_plano"),
			inverseJoinColumns = @JoinColumn(name = "codigo_produto"))
	private List<Produto> produtos;
}
