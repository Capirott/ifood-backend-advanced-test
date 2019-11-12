package com.ifood.tracksuggestionservice.domain.spotify;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Track {
	
	@JsonProperty("album")
	private Album album;
	
	@JsonProperty("available_markets")
	@ToString.Exclude
	private List<String> availableCountries;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("popularity")
	private Integer popularity;
	
	@JsonProperty("preview_url")
	private String previewUrl;
	
}
