package com.ifood.tracksuggestionservice.service.impl;

import static com.ifood.tracksuggestionservice.domain.TrackGenre.CLASSICAL;
import static com.ifood.tracksuggestionservice.domain.TrackGenre.PARTY;
import static com.ifood.tracksuggestionservice.domain.TrackGenre.POP;
import static com.ifood.tracksuggestionservice.domain.TrackGenre.ROCK;

import com.ifood.tracksuggestionservice.client.openweather.OpenWeatherClient;
import com.ifood.tracksuggestionservice.client.spotify.SpotifyClient;
import com.ifood.tracksuggestionservice.domain.TrackGenre;
import com.ifood.tracksuggestionservice.domain.TracksSuggestions;
import com.ifood.tracksuggestionservice.domain.WeatherInfo;
import com.ifood.tracksuggestionservice.exception.CityNotFoundException;
import com.ifood.tracksuggestionservice.exception.ClientException;
import com.ifood.tracksuggestionservice.service.TrackSuggestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TrackSuggestionServiceImpl implements TrackSuggestionService {
	
	static final float HOT_TEMPERATURE = 30.0f;
	static final float NORMAL_TEMPERATURE = 15.0f;
	static final float CHILLING_TEMPERATURE = 10.0f;
	
	private final SpotifyClient spotifyClient;
	private final OpenWeatherClient openWeatherClient;
	
	@Autowired
	public TrackSuggestionServiceImpl(SpotifyClient spotifyClient, OpenWeatherClient openWeatherClient) {
		this.spotifyClient = spotifyClient;
		this.openWeatherClient = openWeatherClient;
	}
	
	@Override
	@Cacheable("suggestTracksByCityName")
	public TracksSuggestions suggestTracksByCityName(String name) throws ClientException, CityNotFoundException {
		log.info("Getting weather in {}", name);
		final WeatherInfo weatherInfo = openWeatherClient.getWeatherInfoByCity(name);
		return getSuggestedTrackByWeatherInfo(weatherInfo);
	}
	
	TracksSuggestions getSuggestedTrackByWeatherInfo(WeatherInfo weatherInfo) throws ClientException {
		final Float temperature = weatherInfo.getTemperature();
		final TrackGenre trackGenre = getTrackGenre(temperature);
		
		log.info("Getting recommendations tracks for genre {}", trackGenre);
		return spotifyClient.suggestTracks(trackGenre);
	}
	
	@Override
	@Cacheable("suggestTracksByCoordinates")
	public TracksSuggestions suggestTracksByCoordinates(Float latitude, Float longitude)
			throws ClientException, CityNotFoundException {
		log.info("Getting weather in latitude: {} longitude: {}", latitude, longitude);
		
		final WeatherInfo weatherInfo = openWeatherClient.getWeatherInfoByCoordinates(latitude, longitude);
		return getSuggestedTrackByWeatherInfo(weatherInfo);
	}
	
	TrackGenre getTrackGenre(Float temperature) {
		log.info("Determining TrackGenre for temperature {}ºC", temperature);
		if (temperature > HOT_TEMPERATURE) {
			return PARTY;
		} else if (temperature >= NORMAL_TEMPERATURE) {
			return POP;
		} else if (temperature > CHILLING_TEMPERATURE) {
			return ROCK;
		}
		return CLASSICAL;
	}
	
}
