/**
 * 
 */
package com.ams.api.service.exception;

/**
 * @author vitor
 *
 */
public class ExistingClientWithSameCPFException extends RuntimeException {
	
	private static final long serialVersionUID = 8593954739459527496L;
	
	/**
	 * @param string
	 */
	public ExistingClientWithSameCPFException(String message) {
		super(message);
	}
}
