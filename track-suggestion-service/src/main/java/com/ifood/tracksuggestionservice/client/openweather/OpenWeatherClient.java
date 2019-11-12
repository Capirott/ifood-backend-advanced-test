package com.ifood.tracksuggestionservice.client.openweather;

import com.ifood.tracksuggestionservice.domain.WeatherInfo;
import com.ifood.tracksuggestionservice.exception.CityNotFoundException;
import com.ifood.tracksuggestionservice.exception.ClientException;

public interface OpenWeatherClient {
	
	WeatherInfo getWeatherInfoByCity(String name) throws CityNotFoundException, ClientException;
	
	WeatherInfo getWeatherInfoByCoordinates(Float latitude, Float longitude)
			throws ClientException, CityNotFoundException;
	
}
