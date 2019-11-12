package com.ifood.tracksuggestionservice.util;

import com.ifood.tracksuggestionservice.domain.TrackInfo;
import org.assertj.core.api.SoftAssertions;

public class CustomSoftAssertion extends SoftAssertions {
	
	public TrackInfoAssert assertThat(TrackInfo actual) {
		return proxy(TrackInfoAssert.class, TrackInfo.class, actual);
	}

}
