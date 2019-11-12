package com.ifood.tracksuggestionservice.client.openweather;

import com.ifood.tracksuggestionservice.domain.WeatherInfo;
import com.ifood.tracksuggestionservice.domain.openweather.Weather;

public interface OpenWeatherClientFactory {
	
	WeatherInfo convertWeatherToWeatherInfo(Weather weather);
	
}
