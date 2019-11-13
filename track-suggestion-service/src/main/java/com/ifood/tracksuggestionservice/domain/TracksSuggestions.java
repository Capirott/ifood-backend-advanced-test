package com.ifood.tracksuggestionservice.domain;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TracksSuggestions {
	@ApiModelProperty("Genre's list of the recommended songs")
	private List<TrackGenre> genres;
	@ApiModelProperty("The list of recommended tracks")
	private List<TrackInfo> tracks;
}
