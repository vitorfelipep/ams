/**
 * 
 */
package com.ams.api.service.exception;

/**
 * @author vitor
 *
 */
public class ExistingProductNameException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8235309123594131326L;

	/**
	 * @param message
	 */
	public ExistingProductNameException(String message) {
		super(message);
	}
}
