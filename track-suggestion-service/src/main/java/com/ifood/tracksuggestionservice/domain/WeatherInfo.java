package com.ifood.tracksuggestionservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WeatherInfo {
	
	@NotNull
	private String country;
	
	@NotNull
	private Float latitude;
	
	@NotNull
	private Float longitude;
	
	@NotNull
	private Float temperature;
	
}
