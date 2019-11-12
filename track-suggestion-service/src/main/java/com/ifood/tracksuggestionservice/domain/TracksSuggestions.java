package com.ifood.tracksuggestionservice.domain;


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
	private List<TrackGenre> genres;
	private List<TrackInfo> tracks;
}
