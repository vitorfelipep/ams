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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.ams.api.domain.enumeration.TipoProdutoEnum;
import com.ams.api.service.exception.NegocioException;
import com.ams.api.validation.SKU;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author vitor
 *
 */

@Entity
@Table(name = "produto")
@Getter @Setter
@EqualsAndHashCode(exclude = {"nome", "descricao", "quantidadeEmEstoque", "tipoProduto"})
public class Produto implements Serializable {

	private static final long serialVersionUID = -2547963153486105997L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@SKU
	@Column(nullable = false, length = 20, unique = true)
	private String sku;
	
	@NotEmpty
	@Size(max = 80)
	@Column(name="nome_produto", nullable = false, length = 80)
	private String nome;
	
	@NotEmpty
	@Column(name="descricao_produto")
	private String descricao;
	
	@NotNull
	@Min(0)
	@Max(9999)
	@Column(name = "quantidade_estoque", nullable = false, length = 5)
	private Integer quantidadeEmEstoque;
	
	
	@Enumerated(EnumType.STRING)
	@Column(name="tipo_produto")
	private TipoProdutoEnum tipoProduto;
	
	@NotNull(message = "é obrigatório!")
	@Column(name = "valor_unitario", nullable = false, precision = 10, scale = 2)
	private BigDecimal valorUnitario;
	
	public void baixarEstoque(Integer quantidade) {
		int novaQuantidade = this.getQuantidadeEmEstoque() - quantidade;

		if (novaQuantidade < 0) {
			throw new NegocioException("Não há disponibilidade no estoque de "
					+ quantidade + " itens do produto " + this.getSku() + ".");
		}

		this.setQuantidadeEmEstoque(novaQuantidade);
	}

	public void adicionarEstoque(Integer quantidade) {
		this.setQuantidadeEmEstoque(quantidadeEmEstoque + quantidade);
	}
}
