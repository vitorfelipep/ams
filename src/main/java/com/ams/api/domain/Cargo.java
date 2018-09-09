/**
 * 
 */
package com.ams.api.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import com.ams.api.domain.enumeration.TipoFuncaoEnum;

import lombok.Getter;
import lombok.Setter;

/**
 * @author vitor
 *
 */
@Getter @Setter
public class Cargo implements Serializable {

	private static final long serialVersionUID = 7267586993616485510L;
	
	private Long id;
	private String nomeFuncao;
	private String descricao;
	private TipoFuncaoEnum tipoFuncao;
	private BigDecimal salarioBruto;
	private BigDecimal salarioLiqido;
	private BigDecimal commissao;
	
}
