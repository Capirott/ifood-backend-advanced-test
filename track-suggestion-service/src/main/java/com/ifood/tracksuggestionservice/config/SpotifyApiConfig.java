package com.ifood.tracksuggestionservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@ConfigurationProperties("spotify.api")
@Validated
@Component
@RefreshScope
public class SpotifyApiConfig {
	@NotBlank
	private String clientId;
	
	@NotBlank
	private String clientSecret;
	
	@NotBlank
	private String urlRecommendation;
	
	@NotBlank
	private String urlToken;
	
	@Min(3)
	@Max(100)
	@NotNull
	private Integer trackRecommendationLimit;
	
	@Min(0)
	private Integer trackRecommendationMinPopularity;
	
}
