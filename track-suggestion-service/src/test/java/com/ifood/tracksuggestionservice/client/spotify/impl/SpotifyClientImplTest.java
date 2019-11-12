package com.ifood.tracksuggestionservice.client.spotify.impl;

import static com.ifood.tracksuggestionservice.client.spotify.impl.SpotifyClientImpl.SUGGEST_TRACKS_UNKNOWN_GENRE_NOT_ALLOWED;
import static com.ifood.tracksuggestionservice.domain.TrackGenre.POP;
import static com.ifood.tracksuggestionservice.domain.TrackGenre.UNKNOWN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.spy;

import com.ifood.tracksuggestionservice.client.spotify.SpotifyClientFactory;
import com.ifood.tracksuggestionservice.config.SpotifyApiConfig;
import com.ifood.tracksuggestionservice.domain.TracksSuggestions;
import com.ifood.tracksuggestionservice.domain.spotify.ClientCredentials;
import com.ifood.tracksuggestionservice.exception.ClientException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class SpotifyClientImplTest {
	
	private static final String ACCESS_TOKEN = "access-token";
	
	private SpotifyClientImpl sut;
	
	@Mock
	private RestTemplate restTemplate;
	
	@Mock
	private SpotifyApiConfig apiConfig;
	
	@Mock
	private SpotifyClientFactory clientFactory;
	
	@Mock
	private MessageSource messageSource;
	
	@Mock
	private SpotifyClientFallbackImpl clientFallback;
	
	@Mock
	private ClientCredentials clientCredentials;
	
	@Mock
	private TracksSuggestions tracksSuggestions;
	
	@BeforeEach
	void init() {
		sut = spy(new SpotifyClientImpl(restTemplate, apiConfig, clientFactory, messageSource, clientFallback));
	}
	
	@Test
	void suggestTrack_WhenGenreUnknown_ExceptionThrown() {
		willReturn(SUGGEST_TRACKS_UNKNOWN_GENRE_NOT_ALLOWED).given(sut).getMessageUnknownGenreException();
		
		final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> sut.suggestTracks(UNKNOWN));
		
		assertThat(exception).hasMessage(SUGGEST_TRACKS_UNKNOWN_GENRE_NOT_ALLOWED);
	}
	
	@Test
	void suggestTrack_ShouldGetCredentialsAndRequestRecommendedTracks_Ok() throws ClientException {
		willReturn(clientCredentials).given(sut).requestClientCredentials();
		given(clientCredentials.getAccessToken()).willReturn(ACCESS_TOKEN);
		willReturn(tracksSuggestions).given(sut).requestRecommendedTracks(POP, ACCESS_TOKEN);
		
		final TracksSuggestions actual = sut.suggestTracks(POP);
		
		assertEquals(tracksSuggestions, actual);
	}
	
	@Test
	void suggestTrack_WhenExceptionThrownWhenRequestingRecommendedTracks_ClientExceptionThrown() {
		willReturn(clientCredentials).given(sut).requestClientCredentials();
		given(clientCredentials.getAccessToken()).willReturn(ACCESS_TOKEN);
		willThrow(RestClientException.class).given(sut).requestRecommendedTracks(POP, ACCESS_TOKEN);
		
		assertThrows(ClientException.class, () -> sut.suggestTracks(POP));
	}
	
	@Test
	void suggestTrack_WhenExceptionThrownWhenRequestingClientCredentials_ClientExceptionThrown() {
		willThrow(RestClientException.class).given(sut).requestClientCredentials();
		
		assertThrows(ClientException.class, () -> sut.suggestTracks(POP));
	}
}