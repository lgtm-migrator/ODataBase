package org.fornever.api.config;

import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataHttpHandler;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.api.processor.EntityCollectionProcessor;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class ODataHttpHandlerProvider implements Provider<ODataHttpHandler> {

	@Inject
	private ServiceMetadata edm;

	@Inject
	OData oData;

	@Inject
	EntityCollectionProcessor processor;

	@Override
	public ODataHttpHandler get() {
		ODataHttpHandler handler = this.oData.createHandler(this.edm);
		handler.register(processor);
		return handler;
	}

}
