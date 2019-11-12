package com.ifood.tracksuggestionservice.util;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.willReturn;
import static org.springframework.http.HttpMethod.GET;

import com.ifood.tracksuggestionservice.domain.openweather.Weather;
import com.ifood.tracksuggestionservice.domain.spotify.ClientCredentials;
import com.ifood.tracksuggestionservice.domain.spotify.SpotifyTracks;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.net.URI;


public class ClientApiMockUtil {
	
	public static void mockSpotifyTracks(RestTemplate restTemplate, SpotifyTracks spotifyTracks) {
		willReturn(ResponseEntity.ok(spotifyTracks)).given(restTemplate)
				.exchange(any(URI.class), eq(GET), any(HttpEntity.class), eq(SpotifyTracks.class));
	}
	
	public static void mockClientCredentials(RestTemplate restTemplate, ClientCredentials clientCredentials) {
		willReturn(clientCredentials).given(restTemplate)
				.postForObject(anyString(), any(HttpEntity.class), eq(ClientCredentials.class));
	}
	
	public static void mockWeather(RestTemplate restTemplate, Weather weather) {
		willReturn(ResponseEntity.ok(weather)).given(restTemplate).getForEntity(any(URI.class), eq(Weather.class));
	}
	
}
