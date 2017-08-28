package org.fornever.api.guice;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.olingo.commons.api.edm.provider.CsdlEdmProvider;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataHttpHandler;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.api.processor.EntityCollectionProcessor;
import org.apache.olingo.server.api.processor.EntityProcessor;
import org.fornever.api.guice.providers.DataSourceProvider;
import org.fornever.api.guice.providers.ODataHttpHandlerProvider;
import org.fornever.api.guice.providers.ODataProvider;
import org.fornever.api.guice.providers.QueryRunnerProvider;
import org.fornever.api.guice.providers.SchemaMetadataProvider;
import org.fornever.api.guice.providers.ServiceMetadataProvider;
import org.fornever.api.olingo.EdmProviderImpl;
import org.fornever.api.olingo.EntityCollectionProcessorImpl;
import org.fornever.api.olingo.EntityProcessorImpl;
import org.fornever.api.types.SchemaMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.name.Names;

/**
 * This class is used for binding interface to its' implementation
 * 
 * @author Theo Sun
 *
 */
public class GuiceBindings extends AbstractModule {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private PropertyConfigurationHelper config = new PropertyConfigurationHelper();

	@Override
	protected void configure() {
		config.loadProperties();
		Names.bindProperties(binder(), System.getProperties());
		bind(DataSource.class).toProvider(DataSourceProvider.class).in(Scopes.SINGLETON);
		bind(OData.class).toProvider(ODataProvider.class).in(Scopes.SINGLETON);
		bind(ODataHttpHandler.class).toProvider(ODataHttpHandlerProvider.class).in(Scopes.SINGLETON);
		bind(ServiceMetadata.class).toProvider(ServiceMetadataProvider.class).in(Scopes.SINGLETON);
		bind(QueryRunner.class).toProvider(QueryRunnerProvider.class).in(Scopes.SINGLETON);

		bind(CsdlEdmProvider.class).to(EdmProviderImpl.class).in(Scopes.SINGLETON);
		bind(EntityCollectionProcessor.class).to(EntityCollectionProcessorImpl.class).in(Scopes.SINGLETON);
		bind(EntityProcessor.class).to(EntityProcessorImpl.class).in(Scopes.SINGLETON);

		bind(SchemaMetadata.class).toProvider(SchemaMetadataProvider.class).in(Scopes.SINGLETON);
	}

}
