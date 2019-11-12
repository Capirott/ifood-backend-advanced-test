package com.ifood.tracksuggestionservice.client.openweather.impl;

import com.ifood.tracksuggestionservice.client.openweather.OpenWeatherClient;
import com.ifood.tracksuggestionservice.config.OpenWeatherApiFallbackConfig;
import com.ifood.tracksuggestionservice.domain.WeatherInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OpenWeatherClientFallbackImpl implements OpenWeatherClient {
	
	private final OpenWeatherApiFallbackConfig fallbackConfig;
	
	@Autowired
	public OpenWeatherClientFallbackImpl(OpenWeatherApiFallbackConfig fallbackConfig) {
		this.fallbackConfig = fallbackConfig;
	}
	
	@Override
	public WeatherInfo getWeatherInfoByCity(String name) {
		log.error("Error in obtaining weather for city {}", name);
		return getWeatherInfoFromConfiguration();
	}
	
	@Override
	public WeatherInfo getWeatherInfoByCoordinates(Float latitude, Float longitude) {
		log.error("Error in obtaining weather for coordinates latitude: {} longitude: {}", latitude, longitude);
		return getWeatherInfoFromConfiguration();
	}
	
	private WeatherInfo getWeatherInfoFromConfiguration() {
		final WeatherInfo weatherInfo = fallbackConfig.getWeatherInfo();
		log.info("Returning configured weather {}", weatherInfo);
		return weatherInfo;
	}
}
