package com.ifood.tracksuggestionservice.client.spotify;

import com.ifood.tracksuggestionservice.domain.TrackGenre;
import com.ifood.tracksuggestionservice.domain.TracksSuggestions;
import com.ifood.tracksuggestionservice.exception.ClientException;

public interface SpotifyClient {
	TracksSuggestions suggestTracks(TrackGenre genre) throws ClientException;
}
