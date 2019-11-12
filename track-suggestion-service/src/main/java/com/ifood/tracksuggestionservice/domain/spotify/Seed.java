package com.ifood.tracksuggestionservice.domain.spotify;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Seed {
	
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("type")
	private String type;
	
}
