package com.ifood.tracksuggestionservice.client.spotify;

import com.ifood.tracksuggestionservice.domain.TracksSuggestions;
import com.ifood.tracksuggestionservice.domain.spotify.SpotifyTracks;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;

public interface SpotifyClientFactory {
	
	TracksSuggestions convertToTracksSuggestions(SpotifyTracks spotifyTracks);
	
	HttpHeaders createBasicHttpHeader(String token);
	
	HttpHeaders createBearerHttpHeader(String token);
	
	MultiValueMap<String, String> createRequestBody();
	
}
