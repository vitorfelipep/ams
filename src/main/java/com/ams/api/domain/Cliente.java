package com.ams.api.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.ams.api.domain.enumeration.TipoClienteEnum;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cliente")
@Getter @Setter
@EqualsAndHashCode(exclude = {"tipoCliente", "pessoa", "dependentes", "situacaoTitular", "possuiCartao", "semCarencia", "telefones"})
public class Cliente implements Serializable {

	private static final long serialVersionUID = -7781240571041408331L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name="tipo_cliente")
	private TipoClienteEnum tipoCliente;
	
	@Embedded
	private Pessoa pessoa;
	
	@Column(name="situacao_titular")
	private Boolean situacaoTitular;
	
	@Column(name="possui_cartao")
	private Boolean possuiCartao;
	
	@Column(name="sem_carencia")
	private Boolean semCarencia;
	
	@OneToOne (cascade={ CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
    @JoinColumn(name="endereco_id")
	private Endereco endereco;
	
	@OneToMany(mappedBy = "titular", cascade={ CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	private List<Dependente> dependentes;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade= { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinTable(name = "cliente_telefone", joinColumns = @JoinColumn(name = "id_cliente", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "id_telefone", referencedColumnName = "id"))
	private List<Telefone> telefones;
}
