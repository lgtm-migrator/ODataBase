package org.fornever.api.config;

import java.util.ArrayList;

import org.apache.olingo.commons.api.edm.provider.CsdlEdmProvider;
import org.apache.olingo.commons.api.edmx.EdmxReference;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ServiceMetadata;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class ServiceMetadataProvider implements Provider<ServiceMetadata> {

	@Inject
	private CsdlEdmProvider provider;

	@Override
	public ServiceMetadata get() {
		OData odata = OData.newInstance();
		return odata.createServiceMetadata(this.provider, new ArrayList<EdmxReference>());
	}

}
