package com.ifood.tracksuggestionservice.controller;

import com.ifood.tracksuggestionservice.domain.TracksSuggestions;
import com.ifood.tracksuggestionservice.exception.CityNotFoundException;
import com.ifood.tracksuggestionservice.exception.ClientException;
import com.ifood.tracksuggestionservice.service.TrackSuggestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Validated
@RestController
@RequestMapping
public class TrackSuggestionController {
	
	private final TrackSuggestionService trackSuggestionService;
	
	public TrackSuggestionController(TrackSuggestionService trackSuggestionService) {
		this.trackSuggestionService = trackSuggestionService;
	}
	
	@GetMapping("/city/{name}")
	public ResponseEntity<TracksSuggestions> suggestTrackByCity(@PathVariable("name") String cityName)
			throws ClientException, CityNotFoundException {
		return ResponseEntity.ok(trackSuggestionService.suggestTracksByCityName(cityName));
	}
	
	@GetMapping("/coordinates/{latitude}/{longitude}")
	public ResponseEntity<TracksSuggestions> suggestTrackByCoordinates(
			@PathVariable("latitude") @Min(-90) @Max(90) Float latitude,
			@PathVariable("longitude") @Min(-180) @Max(180) Float longitude)
			throws ClientException, CityNotFoundException {
		return ResponseEntity.ok(trackSuggestionService.suggestTracksByCoordinates(latitude, longitude));
	}
	
}
