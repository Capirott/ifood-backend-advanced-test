package com.ifood.tracksuggestionservice.util;


import com.ifood.tracksuggestionservice.domain.TrackInfo;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.internal.Objects;

public class TrackInfoAssert extends AbstractObjectAssert<TrackInfoAssert, TrackInfo> {
	
	private Objects objects = Objects.instance();
	
	public TrackInfoAssert(TrackInfo actual) {
		super(actual, TrackInfoAssert.class);
	}
	
	public TrackInfoAssert hasName(String expected) {
		objects.assertEqual(info, actual.getName(), expected);
		return myself;
	}
	
	public TrackInfoAssert hasPreviewUrl(String expected) {
		objects.assertEqual(info, actual.getPreviewUrl(), expected);
		return myself;
	}
	
	
	public TrackInfoAssert hasAlbumName(String expected) {
		objects.assertEqual(info, actual.getAlbumName(), expected);
		return myself;
	}
	
	
	public TrackInfoAssert hasPopularity(Integer expected) {
		objects.assertEqual(info, actual.getPopularity(), expected);
		return myself;
	}
	
	
}
