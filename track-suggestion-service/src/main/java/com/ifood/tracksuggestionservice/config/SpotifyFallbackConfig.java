package com.ifood.tracksuggestionservice.config;

import com.ifood.tracksuggestionservice.domain.TrackGenre;
import com.ifood.tracksuggestionservice.domain.TrackInfo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@ConfigurationProperties("spotify.fallback")
@Validated
@Component
@RefreshScope
public class SpotifyFallbackConfig {
	
	@NotEmpty
	private Map<TrackGenre,
			@NotEmpty(message = "List of tracks mapped to genre cannot be empty") List<TrackInfo>> tracks;
	
}
