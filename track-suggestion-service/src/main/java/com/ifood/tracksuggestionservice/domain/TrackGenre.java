package com.ifood.tracksuggestionservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import java.util.stream.Stream;

@Getter
@ToString
@AllArgsConstructor
public enum TrackGenre {
	CLASSICAL("classical"),
	PARTY("party"),
	POP("pop"),
	ROCK("rock"),
	UNKNOWN("unknown");
	
	private final String id;
	
	public static TrackGenre fromId(String id) {
		return Stream.of(values()).filter(genre -> genre.id.equals(id)).findAny().orElse(UNKNOWN);
	}
}
