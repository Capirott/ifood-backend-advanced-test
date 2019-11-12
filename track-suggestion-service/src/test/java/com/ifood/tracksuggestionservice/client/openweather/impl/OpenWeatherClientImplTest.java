package com.ifood.tracksuggestionservice.client.openweather.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import com.ifood.tracksuggestionservice.client.openweather.OpenWeatherClientFactory;
import com.ifood.tracksuggestionservice.config.OpenWeatherApiConfig;
import com.ifood.tracksuggestionservice.domain.openweather.Weather;
import com.ifood.tracksuggestionservice.exception.CityNotFoundException;
import com.ifood.tracksuggestionservice.exception.ClientException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.HttpClientErrorException.NotFound;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;
import org.springframework.web.client.RestTemplate;
import java.net.URI;

@ExtendWith(MockitoExtension.class)
class OpenWeatherClientImplTest {
	
	private OpenWeatherClientImpl sut;
	
	private final URI uri = URI.create("");
	
	@Mock
	private RestTemplate restTemplate;
	
	@Mock
	private OpenWeatherApiConfig apiConfig;
	
	@Mock
	private OpenWeatherClientFallbackImpl clientFallback;
	
	@Mock
	private OpenWeatherClientFactory clientFactory;
	
	@BeforeEach
	void init() {
		sut = new OpenWeatherClientImpl(restTemplate, apiConfig, clientFactory, clientFallback);
	}
	
	@Test
	void getWeatherInfoByUri_WhenResponseEntityHasEmptyBody_ExceptionThrown() {
		final ClientException exception = assertThrows(ClientException.class,
				() -> sut.getWeatherInfoByUri(uri));
		
		assertThat(exception).hasCauseExactlyInstanceOf(NullPointerException.class);
	}
	
	@Test
	void getWeatherInfoByUri_WhenUnauthorized_ExceptionThrown() {
		given(restTemplate.getForEntity(uri, Weather.class)).willThrow(Unauthorized.class);
		
		final ClientException exception = assertThrows(ClientException.class,
				() -> sut.getWeatherInfoByUri(uri));
		
		assertThat(exception).hasCauseExactlyInstanceOf(Unauthorized.class);
	}
	
	@Test
	void getWeatherInfoByUri_WhenNotFound_ExceptionThrown() {
		given(restTemplate.getForEntity(uri, Weather.class)).willThrow(NotFound.class);
		
		final CityNotFoundException exception = assertThrows(CityNotFoundException.class,
				() -> sut.getWeatherInfoByUri(uri));
		
		assertThat(exception).hasCauseExactlyInstanceOf(NotFound.class);
	}
	
}