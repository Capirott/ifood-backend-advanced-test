package com.ifood.tracksuggestionservice.client.spotify.impl;

import static com.ifood.tracksuggestionservice.domain.TrackGenre.UNKNOWN;
import static org.springframework.http.HttpHeaders.encodeBasicAuth;
import static org.springframework.http.HttpMethod.GET;

import com.ifood.tracksuggestionservice.client.spotify.SpotifyClient;
import com.ifood.tracksuggestionservice.client.spotify.SpotifyClientFactory;
import com.ifood.tracksuggestionservice.config.SpotifyApiConfig;
import com.ifood.tracksuggestionservice.domain.TrackGenre;
import com.ifood.tracksuggestionservice.domain.TracksSuggestions;
import com.ifood.tracksuggestionservice.domain.spotify.ClientCredentials;
import com.ifood.tracksuggestionservice.domain.spotify.SpotifyTracks;
import com.ifood.tracksuggestionservice.exception.ClientException;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Locale;

@Slf4j
@Component
@Primary
public class SpotifyClientImpl implements SpotifyClient {
	
	private static final String LIMIT_QUERY_PARAM = "limit";
	private static final String SEED_GENRES_QUERY_PARAM = "seed_genres";
	private static final String MIN_POPULARITY_QUERY_PARAM = "min_popularity";
	static final String SUGGEST_TRACKS_UNKNOWN_GENRE_NOT_ALLOWED = "suggest.tracks.unknown.genre.not-allowed";
	
	private final RestTemplate restTemplate;
	private final SpotifyApiConfig apiConfig;
	private final SpotifyClientFactory clientFactory;
	private final MessageSource messageSource;
	private final SpotifyClientFallbackImpl clientFallback;
	
	@Autowired
	public SpotifyClientImpl(RestTemplate restTemplate, SpotifyApiConfig apiConfig, SpotifyClientFactory clientFactory,
							 MessageSource messageSource, SpotifyClientFallbackImpl clientFallback) {
		this.restTemplate = restTemplate;
		this.apiConfig = apiConfig;
		this.clientFactory = clientFactory;
		this.messageSource = messageSource;
		this.clientFallback = clientFallback;
	}
	
	@HystrixCommand(fallbackMethod = "suggestTracksFallBack")
	@Override
	@Cacheable("spotifyClientSuggestTracks")
	public TracksSuggestions suggestTracks(TrackGenre genre) throws ClientException {
		Assert.isTrue(!genre.equals(UNKNOWN), this::getMessageUnknownGenreException);
		
		final TracksSuggestions result;
		try {
			final ClientCredentials credentials = requestClientCredentials();
			result = requestRecommendedTracks(genre, credentials.getAccessToken());
		} catch (Exception e) {
			log.error("An exception occurred when obtaining tracks for {} ", genre, e);
			throw new ClientException(e);
		}
		
		log.debug("Returning {}", result);
		return result;
	}
	
	@SuppressWarnings("unused")
	public TracksSuggestions suggestTracksFallBack(TrackGenre genre) {
		return clientFallback.suggestTracks(genre);
	}
	
	TracksSuggestions requestRecommendedTracks(TrackGenre genre, String accessToken) {
		final URI uri = createUriFromRecommendationUrl(genre);
		final HttpHeaders httpHeaders = clientFactory.createBearerHttpHeader(accessToken);
		final HttpEntity<Object> requestEntity = new HttpEntity<>(httpHeaders);
		final ResponseEntity<SpotifyTracks> entity = restTemplate
				.exchange(uri, GET, requestEntity, SpotifyTracks.class);
		
		return clientFactory.convertToTracksSuggestions(entity.getBody());
	}
	
	ClientCredentials requestClientCredentials() {
		final HttpHeaders httpHeaders = clientFactory.createBasicHttpHeader(getBasicAuthToken());
		final MultiValueMap requestBody = clientFactory.createRequestBody();
		final HttpEntity request = new HttpEntity<>(requestBody, httpHeaders);
		
		return restTemplate.postForObject(apiConfig.getUrlToken(), request, ClientCredentials.class);
	}
	
	private URI createUriFromRecommendationUrl(TrackGenre genre) {
		return UriComponentsBuilder.fromHttpUrl(apiConfig.getUrlRecommendation())
				.queryParam(LIMIT_QUERY_PARAM, apiConfig.getTrackRecommendationLimit())
				.queryParam(SEED_GENRES_QUERY_PARAM, genre.getId())
				.queryParam(MIN_POPULARITY_QUERY_PARAM, apiConfig.getTrackRecommendationMinPopularity()).build()
				.toUri();
	}
	
	String getMessageUnknownGenreException() {
		return messageSource.getMessage(SUGGEST_TRACKS_UNKNOWN_GENRE_NOT_ALLOWED, null, Locale.getDefault());
	}
	
	private String getBasicAuthToken() {
		return encodeBasicAuth(apiConfig.getClientId(), apiConfig.getClientSecret(), Charset.defaultCharset());
	}
	
}
