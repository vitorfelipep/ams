/**
 * 
 */
package com.ams.api.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * @author vitor
 *
 */

@ConfigurationProperties("ams-api")
@Getter 
public class AmsProperties {
	
	@Setter
	private String origemPermitida = "http://localhost:8080";
	
	private final SecurityApp security = new SecurityApp();
	
	@Getter @Setter
	public static class SecurityApp {
		
		private boolean enableHttps;
	}
}
