package com.xbb.spring_jfinal.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="dev")
public class DevSetting {
	
	private Boolean devMode;

	public Boolean getDevMode() {
		return devMode;
	}

	public void setDevMode(Boolean devMode) {
		this.devMode = devMode;
	}
	
}
