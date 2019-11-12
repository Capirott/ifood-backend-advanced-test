package com.ifood.tracksuggestionservice.domain.openweather;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Weather {
	
	@NotNull
	@JsonProperty("coord")
	private Coordinates coordinates;
	
	@NotNull
	@JsonProperty("sys")
	private Sys sys;
	
	@NotNull
	@JsonProperty("main")
	private Main main;
	
}
