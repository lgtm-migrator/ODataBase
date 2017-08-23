package org.fornever.api.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sql.DataSource;

import org.fornever.api.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

/**
 * This class is used for binding interface to its' implementation
 * 
 * @author Theo Sun
 *
 */
public class GuiceBindings extends AbstractModule {

	private final static String PROPERTIES_FILE = "application.properties";
	private Logger logger = LoggerFactory.getLogger(getClass());

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

	@Override
	protected void configure() {
		loadProperties();
		bind(DataSource.class).to(DruidDataSource.class).in(Scopes.SINGLETON);
	}

}
