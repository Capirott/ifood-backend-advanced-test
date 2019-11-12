package com.ifood.tracksuggestionservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotBlank;

@Setter
@Getter
@ConfigurationProperties("openweather.api")
@Validated
@Component
@RefreshScope
public class OpenWeatherApiConfig {
	
	@NotBlank
	private String units;
	
	@NotBlank
	private String appId;
	
	@NotBlank
	private String urlCurrentWeather;
	
}
