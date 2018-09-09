/**
 * 
 */
package com.ams.api.service.exception;

/**
 * @author vitor
 *
 */
public class NegocioException extends RuntimeException {

	private static final long serialVersionUID = -619873074592148520L;

	/**
	 * @param message
	 */
	public NegocioException(String message) {
		super(message);
	}
}
