package org.fornever.api.guice.providers;

import org.apache.olingo.server.api.OData;

import com.google.inject.Provider;

public class ODataProvider implements Provider<OData> {

	@Override
	public OData get() {
		return OData.newInstance();
	}

}
