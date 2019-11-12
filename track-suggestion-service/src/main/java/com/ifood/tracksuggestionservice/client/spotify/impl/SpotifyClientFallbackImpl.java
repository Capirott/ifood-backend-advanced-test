package com.ifood.tracksuggestionservice.client.spotify.impl;

import static java.util.Collections.singletonList;

import com.ifood.tracksuggestionservice.client.spotify.SpotifyClient;
import com.ifood.tracksuggestionservice.config.SpotifyFallbackConfig;
import com.ifood.tracksuggestionservice.domain.TrackGenre;
import com.ifood.tracksuggestionservice.domain.TracksSuggestions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SpotifyClientFallbackImpl implements SpotifyClient {
	
	private final SpotifyFallbackConfig fallbackConfig;
	
	@Autowired
	public SpotifyClientFallbackImpl(SpotifyFallbackConfig fallbackConfig) {
		this.fallbackConfig = fallbackConfig;
	}
	
	@Override
	public TracksSuggestions suggestTracks(TrackGenre genre) {
		log.error("Error in obtaining recommendations tracks from spotify");
		return TracksSuggestions.builder()
				.tracks(fallbackConfig.getTracks().get(genre))
				.genres(singletonList(genre))
				.build();
	}
	
}
