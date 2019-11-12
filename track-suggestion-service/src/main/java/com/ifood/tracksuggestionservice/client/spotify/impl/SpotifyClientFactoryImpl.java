package com.ifood.tracksuggestionservice.client.spotify.impl;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

import com.ifood.tracksuggestionservice.client.spotify.SpotifyClientFactory;
import com.ifood.tracksuggestionservice.domain.TrackGenre;
import com.ifood.tracksuggestionservice.domain.TrackInfo;
import com.ifood.tracksuggestionservice.domain.TracksSuggestions;
import com.ifood.tracksuggestionservice.domain.spotify.Seed;
import com.ifood.tracksuggestionservice.domain.spotify.SpotifyTracks;
import com.ifood.tracksuggestionservice.domain.spotify.Track;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
public class SpotifyClientFactoryImpl implements SpotifyClientFactory {
	
	private static final String BASIC = "Basic ";
	private static final String BEARER = "Bearer ";
	private static final String CLIENT_CREDENTIALS = "client_credentials";
	private static final String GRANT_TYPE = "grant_type";
	@Override
	public TracksSuggestions convertToTracksSuggestions(SpotifyTracks spotifyTracks) {
		final List<TrackInfo> trackInfoList = convertTrackInfoList(spotifyTracks.getTracks());
		final List<TrackGenre> trackGenreList = convertGenreList(spotifyTracks.getSeeds());
		
		return TracksSuggestions.builder().tracks(trackInfoList).genres(trackGenreList).build();
	}
	
 	 private HttpHeaders createHttpHeaders(Supplier<String> authorizationSupplier) {
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(APPLICATION_FORM_URLENCODED);
		httpHeaders.set(AUTHORIZATION, authorizationSupplier.get());
		return httpHeaders;
	}
	
	@Override
	public HttpHeaders createBasicHttpHeader(String token) {
		return createHttpHeaders(() -> BASIC + token);
	}
	
	@Override
	public HttpHeaders createBearerHttpHeader(String token) {
		return createHttpHeaders(() -> BEARER + token);
	}
	
	@Override
	public MultiValueMap<String, String> createRequestBody() {
		final MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		requestBody.add(GRANT_TYPE, CLIENT_CREDENTIALS);
		return requestBody;
	}
	
	private List<TrackGenre> convertGenreList(List<Seed> seeds) {
		return seeds.stream().map(Seed::getId).map(TrackGenre::fromId).collect(Collectors.toList());
	}
	
	private TrackInfo convertToTrackInfo(Track track) {
		return TrackInfo.builder().name(track.getName()).popularity(track.getPopularity())
				.previewUrl(track.getPreviewUrl()).albumName(track.getAlbum().getName()).build();
	}
	
	private List<TrackInfo> convertTrackInfoList(List<Track> trackList) {
		return trackList.stream().map(this::convertToTrackInfo)
				.sorted(Comparator.comparingInt(TrackInfo::getPopularity).reversed()).collect(Collectors.toList());
	}
	
}