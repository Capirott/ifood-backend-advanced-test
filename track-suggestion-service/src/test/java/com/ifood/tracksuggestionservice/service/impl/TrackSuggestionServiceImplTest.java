package com.ifood.tracksuggestionservice.service.impl;

import static com.ifood.tracksuggestionservice.domain.TrackGenre.CLASSICAL;
import static com.ifood.tracksuggestionservice.domain.TrackGenre.PARTY;
import static com.ifood.tracksuggestionservice.domain.TrackGenre.POP;
import static com.ifood.tracksuggestionservice.domain.TrackGenre.ROCK;
import static com.ifood.tracksuggestionservice.service.impl.TrackSuggestionServiceImpl.CHILLING_TEMPERATURE;
import static com.ifood.tracksuggestionservice.service.impl.TrackSuggestionServiceImpl.HOT_TEMPERATURE;
import static com.ifood.tracksuggestionservice.service.impl.TrackSuggestionServiceImpl.NORMAL_TEMPERATURE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.ifood.tracksuggestionservice.client.openweather.OpenWeatherClient;
import com.ifood.tracksuggestionservice.client.spotify.SpotifyClient;
import com.ifood.tracksuggestionservice.domain.TracksSuggestions;
import com.ifood.tracksuggestionservice.domain.WeatherInfo;
import com.ifood.tracksuggestionservice.exception.CityNotFoundException;
import com.ifood.tracksuggestionservice.exception.ClientException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrackSuggestionServiceImplTest {
	
	private TrackSuggestionServiceImpl sut;
	
	@Mock
	private SpotifyClient spotifyClient;
	
	@Mock
	private OpenWeatherClient openWeatherClient;
	
	@Mock
	private WeatherInfo weatherInfo;
	
	@Mock
	private TracksSuggestions tracksSuggestions;
	
	@BeforeEach
	void init() {
		sut = spy(new TrackSuggestionServiceImpl(spotifyClient, openWeatherClient));
	}
	
	@Test
	void suggestTracks_ByCityName_ShouldCallOpenWeatherClient() throws ClientException, CityNotFoundException {
		final String city = "Helsinki";
		
		given(openWeatherClient.getWeatherInfoByCity(city)).willReturn(weatherInfo);
		willReturn(tracksSuggestions).given(sut).getSuggestedTrackByWeatherInfo(weatherInfo);
		
		final TracksSuggestions actual = sut.suggestTracksByCityName(city);
		
		verifyNoMoreInteractions(openWeatherClient);
		assertEquals(tracksSuggestions, actual);
	}
	
	@Test
	void suggestTracks_ByCoordinates_ShouldCallOpenWeatherClient() throws ClientException, CityNotFoundException {
		final float longitude = 10;
		final float latitude = 11;
		
		given(openWeatherClient.getWeatherInfoByCoordinates(latitude, longitude)).willReturn(weatherInfo);
		willReturn(tracksSuggestions).given(sut).getSuggestedTrackByWeatherInfo(weatherInfo);
		
		final TracksSuggestions actual = sut.suggestTracksByCoordinates(latitude, longitude);
		
		verifyNoMoreInteractions(openWeatherClient);
		assertEquals(tracksSuggestions, actual);
	}
	
	@Test
	void getSuggestedTrackByWeatherInfo() throws ClientException {
		willReturn(POP).given(sut).getTrackGenre(anyFloat());
		given(spotifyClient.suggestTracks(POP)).willReturn(tracksSuggestions);
		
		final TracksSuggestions actual = sut.getSuggestedTrackByWeatherInfo(weatherInfo);
		
		verifyNoMoreInteractions(spotifyClient);
		assertEquals(tracksSuggestions, actual);
	}
	
	@Test
	void getTrackGenre_WhenTemperatureAboveHotTemperature_Party() {
		assertEquals(PARTY, sut.getTrackGenre(HOT_TEMPERATURE + 1.0f));
	}
	
	@Test
	void getTrackGenre_WhenTemperatureEqualsHotTemperature_Pop() {
		assertEquals(POP, sut.getTrackGenre(HOT_TEMPERATURE));
	}
	
	@Test
	void getTrackGenre_WhenTemperatureAboveNormalTemperature_Pop() {
		assertEquals(POP, sut.getTrackGenre(NORMAL_TEMPERATURE + 1.0f));
	}
	
	@Test
	void getTrackGenre_WhenTemperatureEqualsNormalTemperature_Pop() {
		assertEquals(POP, sut.getTrackGenre(NORMAL_TEMPERATURE));
	}
	
	@Test
	void getTrackGenre_WhenTemperatureAboveChillingTemperature_Rock() {
		assertEquals(ROCK, sut.getTrackGenre(CHILLING_TEMPERATURE + 1.0f));
	}
	
	@Test
	void getTrackGenre_WhenTemperatureEqualsChillingTemperature_Rock() {
		assertEquals(CLASSICAL, sut.getTrackGenre(CHILLING_TEMPERATURE));
	}
	
	@Test
	void getTrackGenre_WhenTemperatureBellowChillingTemperature_Classical() {
		assertEquals(CLASSICAL, sut.getTrackGenre(CHILLING_TEMPERATURE - 1.0f));
	}
	
}