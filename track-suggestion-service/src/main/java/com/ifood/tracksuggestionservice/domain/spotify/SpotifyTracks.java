package com.ifood.tracksuggestionservice.domain.spotify;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpotifyTracks {
	
	@JsonProperty("tracks")
	private List<Track> tracks;
	
	@JsonProperty("seeds")
	private List<Seed> seeds;
	
}
