/**
 * 
 */
package com.ams.api.service.exception;

/**
 * @author vitor
 *
 */
public class ExistingPlanNameException extends RuntimeException {

	private static final long serialVersionUID = -2454812069332138945L;
	
	/**
	 * @param message
	 */
	public ExistingPlanNameException(String message) {
		super(message);
	}

	
	
}
