package com.ifood.tracksuggestionservice.service;

import com.ifood.tracksuggestionservice.domain.TracksSuggestions;
import com.ifood.tracksuggestionservice.exception.CityNotFoundException;
import com.ifood.tracksuggestionservice.exception.ClientException;

public interface TrackSuggestionService {
	
	TracksSuggestions suggestTracksByCityName(String cityName) throws ClientException, CityNotFoundException;
	
	TracksSuggestions suggestTracksByCoordinates(Float latitude, Float longitude) throws ClientException, CityNotFoundException;
	
}
