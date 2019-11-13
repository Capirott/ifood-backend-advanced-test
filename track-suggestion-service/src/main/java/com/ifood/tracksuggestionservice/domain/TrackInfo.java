package com.ifood.tracksuggestionservice.domain;

import io.swagger.annotations.ApiModelProperty;
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
	@ApiModelProperty("The track's album name")
	private String albumName;
	@ApiModelProperty("The track's name")
	private String name;
	@ApiModelProperty(value = "The popularity of the track. The value will be between 0 and 100, " +
			"with 100 being the most popular", allowableValues = "range[0, 100]")
	private Integer popularity;
	@ApiModelProperty("A 30 seconds preview of the track")
	private String previewUrl;
}
