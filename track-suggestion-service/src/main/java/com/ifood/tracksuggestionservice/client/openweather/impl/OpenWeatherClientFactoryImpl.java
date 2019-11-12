package com.ifood.tracksuggestionservice.client.openweather.impl;

import com.ifood.tracksuggestionservice.client.openweather.OpenWeatherClientFactory;
import com.ifood.tracksuggestionservice.domain.WeatherInfo;
import com.ifood.tracksuggestionservice.domain.openweather.Coordinates;
import com.ifood.tracksuggestionservice.domain.openweather.Weather;
import org.springframework.stereotype.Component;

@Component
public class OpenWeatherClientFactoryImpl implements OpenWeatherClientFactory {
	
	@Override
	public WeatherInfo convertWeatherToWeatherInfo(Weather weather) {
		final Coordinates coordinates = weather.getCoordinates();
		
		return WeatherInfo.builder()
				.country(weather.getSys().getCountry())
				.temperature(weather.getMain().getTemperature())
				.latitude(coordinates.getLatitude())
				.longitude(coordinates.getLongitude())
				.build();
	}
	
}
