package com.ifood.tracksuggestionservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TrackInfo {
	private String albumName;
	private String name;
	private Integer popularity;
	private String previewUrl;
}
