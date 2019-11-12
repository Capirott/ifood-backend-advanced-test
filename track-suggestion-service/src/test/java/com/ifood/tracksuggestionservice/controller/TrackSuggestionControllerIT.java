package com.ifood.tracksuggestionservice.controller;

import static com.ifood.tracksuggestionservice.domain.TrackGenre.CLASSICAL;
import static com.ifood.tracksuggestionservice.domain.TrackGenre.PARTY;
import static com.ifood.tracksuggestionservice.util.ClientApiMockUtil.mockClientCredentials;
import static com.ifood.tracksuggestionservice.util.ClientApiMockUtil.mockSpotifyTracks;
import static com.ifood.tracksuggestionservice.util.ClientApiMockUtil.mockWeather;
import static com.ifood.tracksuggestionservice.util.DomainFactoryUtil.ALBUM_NAME;
import static com.ifood.tracksuggestionservice.util.DomainFactoryUtil.GENRE_ID;
import static com.ifood.tracksuggestionservice.util.DomainFactoryUtil.POPULARITY;
import static com.ifood.tracksuggestionservice.util.DomainFactoryUtil.PREVIEW_URL;
import static com.ifood.tracksuggestionservice.util.DomainFactoryUtil.TRACK_NAME;
import static com.ifood.tracksuggestionservice.util.DomainFactoryUtil.createClientCredentials;
import static com.ifood.tracksuggestionservice.util.DomainFactoryUtil.createSpotifyTracks;
import static com.ifood.tracksuggestionservice.util.DomainFactoryUtil.createWeather;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.http.HttpStatus.OK;

import com.ifood.tracksuggestionservice.client.openweather.OpenWeatherClient;
import com.ifood.tracksuggestionservice.client.openweather.impl.OpenWeatherClientFallbackImpl;
import com.ifood.tracksuggestionservice.client.spotify.SpotifyClient;
import com.ifood.tracksuggestionservice.client.spotify.impl.SpotifyClientFallbackImpl;
import com.ifood.tracksuggestionservice.domain.TrackGenre;
import com.ifood.tracksuggestionservice.domain.TrackInfo;
import com.ifood.tracksuggestionservice.domain.TracksSuggestions;
import com.ifood.tracksuggestionservice.domain.openweather.Weather;
import com.ifood.tracksuggestionservice.domain.spotify.ClientCredentials;
import com.ifood.tracksuggestionservice.exception.CityNotFoundException;
import com.ifood.tracksuggestionservice.exception.ClientException;
import com.ifood.tracksuggestionservice.util.CustomSoftAssertion;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException.BadRequest;
import org.springframework.web.client.HttpClientErrorException.NotFound;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;
import org.springframework.web.client.RestTemplate;
import java.net.URI;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TrackSuggestionControllerIT {
	
	private static final String CITY_ENDPOINT = "/city/";
	private static final String COORDINATES_ENDPOINT = "/coordinates/";
	private static final String CITY = "California";
	
	private String cityUrlHelsinki;
	private String coordinatesUrl;
	private CustomSoftAssertion softAssertion;
	
	@LocalServerPort
	private int port;
	
	@SpyBean
	private RestTemplate restTemplate;
	
	@SpyBean
	private OpenWeatherClient openWeatherClient;
	
	@SpyBean
	private SpotifyClient spotifyClient;
	
	@SpyBean
	private OpenWeatherClientFallbackImpl openWeatherClientFallback;
	
	@SpyBean
	private SpotifyClientFallbackImpl spotifyClientFallback;
	
	@BeforeEach
	void setUp() {
		String baseUrl = "http://localhost:" + port + "/suggestions/tracks";
		cityUrlHelsinki = baseUrl + CITY_ENDPOINT + CITY;
		coordinatesUrl = baseUrl + COORDINATES_ENDPOINT;
		softAssertion = new CustomSoftAssertion();
	}
	
	@AfterEach
	void assertAll() {
		softAssertion.assertAll();
	}
	
	@Test
	void suggestTrackByCity_WhenAllInputsCorrect_Ok() {
		mockWeather(restTemplate, createWeather());
		mockClientCredentials(restTemplate, createClientCredentials());
		mockSpotifyTracks(restTemplate, createSpotifyTracks());
		
		final ResponseEntity<TracksSuggestions> entity = restTemplate.getForEntity(cityUrlHelsinki, TracksSuggestions.class);
		final TracksSuggestions tracksSuggestions = requireNonNull(entity.getBody());
		final List<TrackInfo> tracks = tracksSuggestions.getTracks();
		final List<TrackGenre> genres = tracksSuggestions.getGenres();
		
		softAssertion.assertThat(entity.getStatusCode()).isEqualByComparingTo(OK);
		softAssertion.assertThat(genres).hasOnlyOneElementSatisfying(s -> assertEquals(GENRE_ID, s.getId()));
		softAssertion.assertThat(tracks).hasSize(1);
		softAssertion.assertThat(tracks.get(0)).hasAlbumName(ALBUM_NAME).hasName(TRACK_NAME)
				.hasPreviewUrl(PREVIEW_URL).hasPopularity(POPULARITY);
	}
	
	@Test
	void suggestTrackByCity_WhenCityDoesntExists_NotFound() {
		willThrow(NotFound.class).given(restTemplate).getForEntity(any(URI.class), eq(Weather.class));
		
		final NotFound exception = assertThrows(NotFound.class,
				() -> restTemplate.getForEntity(cityUrlHelsinki, TracksSuggestions.class));
		
		softAssertion.assertThat(exception.getResponseBodyAsString()).contains("City not found");
	}
	
	@Test
	void suggestTrackByCity_WhenUnauthorizedWeatherApi_FallbackMethodCalled() throws ClientException {
		willThrow(Unauthorized.class).given(restTemplate).getForEntity(any(URI.class), eq(Weather.class));
		mockClientCredentials(restTemplate, createClientCredentials());
		mockSpotifyTracks(restTemplate, createSpotifyTracks());
		
		final ResponseEntity<TracksSuggestions> entity = restTemplate.getForEntity(cityUrlHelsinki,
				TracksSuggestions.class);
		
		softAssertion.assertThat(entity.getBody()).isNotNull();
		softAssertion.assertThat(entity.getStatusCode()).isEqualByComparingTo(OK);
		
		then(openWeatherClientFallback).should().getWeatherInfoByCity(CITY);
		then(spotifyClient).should().suggestTracks(CLASSICAL);
	}
	
	@Test
	void suggestTrackByCity_WhenUnauthorizedSpotifyApi_FallbackMethodCalled()
			throws ClientException, CityNotFoundException {
		mockWeather(restTemplate, createWeather());
		willThrow(Unauthorized.class).given(restTemplate)
				.postForObject(anyString(), any(HttpEntity.class), eq(ClientCredentials.class));
		
		final ResponseEntity<TracksSuggestions> entity = restTemplate
				.getForEntity(cityUrlHelsinki, TracksSuggestions.class);
		
		softAssertion.assertThat(entity.getBody()).isNotNull();
		softAssertion.assertThat(entity.getStatusCode()).isEqualByComparingTo(OK);
		
		then(openWeatherClient).should().getWeatherInfoByCity(CITY);
		then(spotifyClientFallback).should().suggestTracks(PARTY);
	}
	
	@Test
	void suggestTrackByCoordinates_WhenLatitudeInvalid_BadRequest() {
		final BadRequest exception = assertThrows(BadRequest.class,
				() -> restTemplate.getForEntity(coordinatesUrl + "/91.0/100", TracksSuggestions.class));
		
		softAssertion.assertThat(exception.getResponseBodyAsString()).contains("latitude").doesNotContain("longitude");
	}
	
	@Test
	void suggestTrackByCoordinates_WhenLongitudeInvalid_BadRequest() {
		final BadRequest exception = assertThrows(BadRequest.class,
				() -> restTemplate.getForEntity(coordinatesUrl + "/59.0/181.0", TracksSuggestions.class));
		
		softAssertion.assertThat(exception.getResponseBodyAsString())
				.contains("longitude").doesNotContain("latitude");
	}
	
	@Test
	void suggestTrackByCoordinates_WhenCoordinatesInvalid_BadRequest() {
		final BadRequest exception = assertThrows(BadRequest.class,
				() -> restTemplate.getForEntity(coordinatesUrl + "/-91.0/181.0", TracksSuggestions.class));
		
		softAssertion.assertThat(exception.getResponseBodyAsString())
				.contains("longitude").contains("latitude");
		
	}
	
	@Test
	void suggestTrackByCoordinates_MissingLatitude_NotFound() {
		assertThrows(NotFound.class,
				() -> restTemplate.getForEntity(coordinatesUrl + "//18.0", TracksSuggestions.class));
	}
	
	@Test
	void suggestTrackByCoordinates_MissingLongitude_NotFound() {
		assertThrows(NotFound.class,
				() -> restTemplate.getForEntity(coordinatesUrl + "/19.0/", TracksSuggestions.class));
	}
	
}