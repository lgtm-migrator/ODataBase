package org.fornever.api.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.olingo.commons.api.edm.provider.CsdlEdmProvider;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataHttpHandler;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.api.processor.EntityCollectionProcessor;
import org.apache.olingo.server.api.processor.EntityProcessor;
import org.fornever.api.Entry;
import org.fornever.api.olingo.MySQLEdmProvider;
import org.fornever.api.olingo.MySQLEntityCollectionProcessor;
import org.fornever.api.olingo.MySQLEntityProcessor;
import org.fornever.api.types.SchemaMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidDataSource;
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

	private final static String PROPERTIES_FILE = "application.properties";
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	protected void configure() {
		loadProperties();
		Names.bindProperties(binder(), System.getProperties());
		bind(DataSource.class).to(DruidDataSource.class).in(Scopes.SINGLETON);
		bind(SchemaMetadata.class).in(Scopes.SINGLETON);
		bind(OData.class).toProvider(ODataProvider.class).in(Scopes.SINGLETON);
		bind(ODataHttpHandler.class).toProvider(ODataHttpHandlerProvider.class).in(Scopes.SINGLETON);
		bind(ServiceMetadata.class).toProvider(ServiceMetadataProvider.class).in(Scopes.SINGLETON);
		bind(QueryRunner.class).toProvider(QueryRunnerProvider.class).in(Scopes.SINGLETON);

		bind(CsdlEdmProvider.class).to(MySQLEdmProvider.class).in(Scopes.SINGLETON);
		bind(EntityCollectionProcessor.class).to(MySQLEntityCollectionProcessor.class).in(Scopes.SINGLETON);
		bind(EntityProcessor.class).to(MySQLEntityProcessor.class).in(Scopes.SINGLETON);
	}

	/**
	 * load configuration properties from properties file
	 */
	private void loadProperties() {
		try {
			// load application.properties from src/main/resources
			InputStream iStream = Entry.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);
			if (iStream == null) {
				logger.warn("missing {} file from src", PROPERTIES_FILE);
			} else {
				System.getProperties().load(iStream);
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		try {
			File file = new File(PROPERTIES_FILE);
			if (file.exists()) {
				// load application.properties from application path
				InputStream iStream = new FileInputStream(PROPERTIES_FILE);
				System.getProperties().load(iStream);
			} else {
				logger.info("missing user configuration file");
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

}
