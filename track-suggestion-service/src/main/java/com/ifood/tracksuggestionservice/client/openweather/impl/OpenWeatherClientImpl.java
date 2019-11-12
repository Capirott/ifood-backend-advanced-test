package com.ifood.tracksuggestionservice.client.openweather.impl;

import static java.util.Objects.requireNonNull;

import com.ifood.tracksuggestionservice.client.openweather.OpenWeatherClient;
import com.ifood.tracksuggestionservice.client.openweather.OpenWeatherClientFactory;
import com.ifood.tracksuggestionservice.config.OpenWeatherApiConfig;
import com.ifood.tracksuggestionservice.domain.WeatherInfo;
import com.ifood.tracksuggestionservice.domain.openweather.Weather;
import com.ifood.tracksuggestionservice.exception.CityNotFoundException;
import com.ifood.tracksuggestionservice.exception.ClientException;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;

@Slf4j
@Component
@Primary
public class OpenWeatherClientImpl implements OpenWeatherClient {
	
	private static final String CITY_QUERY_PARAM = "q";
	private static final String UNITS_QUERY_PARAM = "units";
	private static final String APPID_QUERY_PARAM = "appid";
	private static final String LAT_QUERY_PARAM = "lat";
	private static final String LON_QUERY_PARAM = "lon";
	
	private final RestTemplate restTemplate;
	private final OpenWeatherApiConfig apiConfig;
	private final OpenWeatherClientFactory clientFactory;
	private final OpenWeatherClientFallbackImpl clientFallback;
	
	@Autowired
	public OpenWeatherClientImpl(RestTemplate restTemplate,
								 OpenWeatherApiConfig apiConfig,
								 OpenWeatherClientFactory clientFactory,
								 OpenWeatherClientFallbackImpl clientFallback) {
		this.restTemplate = restTemplate;
		this.apiConfig = apiConfig;
		this.clientFactory = clientFactory;
		this.clientFallback = clientFallback;
	}
	
	@Override
	@HystrixCommand(fallbackMethod = "getWeatherInfoByCityFallback", ignoreExceptions = CityNotFoundException.class)
	@Cacheable("openWeatherClientGetWeatherInfoByCity")
	public WeatherInfo getWeatherInfoByCity(String name) throws CityNotFoundException, ClientException {
		final URI uri = getUriFromCurrentWeatherUrl(name);
		return getWeatherInfoByUri(uri);
	}
	
	@Override
	@HystrixCommand(fallbackMethod = "getWeatherInfoByCoordinates")
	@Cacheable("openWeatherClientGetWeatherInfoByCoordinates")
	public WeatherInfo getWeatherInfoByCoordinates(Float latitude, Float longitude)
			throws ClientException, CityNotFoundException {
		final URI uri = getUriFromCurrentWeatherUrl(latitude, longitude);
		return getWeatherInfoByUri(uri);
	}
	
	@SuppressWarnings("unused")
	private WeatherInfo getWeatherInfoByCityFallback(String name) {
		return clientFallback.getWeatherInfoByCity(name);
	}
	
	@SuppressWarnings("unused")
	private WeatherInfo getWeatherInfoByCoordinatesFallback(Float latitude, Float longitude) {
		return clientFallback.getWeatherInfoByCoordinates(latitude, longitude);
	}
	
	WeatherInfo getWeatherInfoByUri(URI uri) throws CityNotFoundException, ClientException {
		final WeatherInfo weatherInfo;
		
		try {
			final ResponseEntity<Weather> entity = restTemplate.getForEntity(uri, Weather.class);
			final Weather weather = requireNonNull(entity.getBody());

			weatherInfo = clientFactory.convertWeatherToWeatherInfo(weather);
		} catch (HttpClientErrorException.NotFound e) {
			throw new CityNotFoundException(e);
		} catch (Exception e) {
			log.error("An exception occurred when obtaining weather info", e);
			throw new ClientException(e);
		}
		
		log.debug("Returning {}", weatherInfo);
		return weatherInfo;
	}
	
	private URI getUriFromCurrentWeatherUrl(Float latitude, Float longitude) {
		return getUriComponentsBuilder()
				.queryParam(LAT_QUERY_PARAM, latitude)
				.queryParam(LON_QUERY_PARAM, longitude)
				.build().toUri();
	}
	private URI getUriFromCurrentWeatherUrl(String cityName) {
		return getUriComponentsBuilder()
				.queryParam(CITY_QUERY_PARAM, cityName)
				.build().toUri();
	}
	
	private UriComponentsBuilder getUriComponentsBuilder() {
		return UriComponentsBuilder.fromHttpUrl(apiConfig.getUrlCurrentWeather())
				.queryParam(UNITS_QUERY_PARAM, apiConfig.getUnits())
				.queryParam(APPID_QUERY_PARAM, apiConfig.getAppId());
	}
	
}
