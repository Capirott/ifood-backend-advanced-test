package com.ifood.tracksuggestionservice.util;

import com.ifood.tracksuggestionservice.domain.openweather.Coordinates;
import com.ifood.tracksuggestionservice.domain.openweather.Main;
import com.ifood.tracksuggestionservice.domain.openweather.Sys;
import com.ifood.tracksuggestionservice.domain.openweather.Weather;
import com.ifood.tracksuggestionservice.domain.spotify.Album;
import com.ifood.tracksuggestionservice.domain.spotify.ClientCredentials;
import com.ifood.tracksuggestionservice.domain.spotify.Seed;
import com.ifood.tracksuggestionservice.domain.spotify.SpotifyTracks;
import com.ifood.tracksuggestionservice.domain.spotify.Track;
import java.util.Collections;
import java.util.List;

public class DomainFactoryUtil {
	
	private static final String ACCESS_TOKEN = "test-access-token";
	private static final String COUNTRY = "United States";
	private static final String GENRE = "GENRE";
	private static final float LATITUDE = 15.0f;
	private static final float LONGITUDE = 66.2f;
	private static final float TEMPERATURE = 35f;
	
	public static final int POPULARITY = 10;
	public static final String ALBUM_NAME = "Test album";
	public static final String GENRE_ID = "party";
	public static final String PREVIEW_URL = "http://localhost:8080";
	public static final String TRACK_NAME = "Track name";
	
	private DomainFactoryUtil() {
	}
	
	public static ClientCredentials createClientCredentials() {
		return ClientCredentials.builder().accessToken(ACCESS_TOKEN).build();
	}
	
	public static SpotifyTracks createSpotifyTracks() {
		final List<Seed> seeds = Collections.singletonList(Seed.builder().id(GENRE_ID).type(GENRE).build());
		final Album album = Album.builder().name(ALBUM_NAME).build();
		final Track track = Track.builder().popularity(POPULARITY).name(TRACK_NAME).album(album).previewUrl(PREVIEW_URL).build();
		return SpotifyTracks.builder().tracks(Collections.singletonList(track)).seeds(seeds).build();
	}
	
	public static Weather createWeather() {
		final Coordinates coordinates = Coordinates.builder().latitude(LATITUDE).longitude(LONGITUDE).build();
		final Main main = Main.builder().temperature(TEMPERATURE).build();
		final Sys sys = Sys.builder().country(COUNTRY).build();
		return Weather.builder().coordinates(coordinates).main(main).sys(sys).build();
	}
	
}