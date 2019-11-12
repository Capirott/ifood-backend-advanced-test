package com.ifood.tracksuggestionservice.config;


import com.ifood.tracksuggestionservice.domain.WeatherInfo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Setter
@Getter
@ConfigurationProperties("openweather.fallback.weather.info")
@Validated
@Component
@RefreshScope
public class OpenWeatherApiFallbackConfig {
	
	private String country;
	private Float latitude;
	private Float longitude;
	private Float temperature;
	
	public WeatherInfo getWeatherInfo() {
		return WeatherInfo.builder()
				.country(country)
				.latitude(latitude)
				.longitude(longitude)
				.temperature(temperature).build();
	}
	
}
