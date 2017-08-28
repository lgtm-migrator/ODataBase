package org.fornever.api.guice;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.fornever.api.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertyConfigurationHelper {

	/**
	 * the properties file name
	 */
	private final String PROPERTIES_FILE_NAME = "application.properties";

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * load configuration properties from properties file to system properties
	 */
	public void loadProperties() {
		try {
			// load application.properties from src/main/resources
			InputStream iStream = Entry.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME);
			if (iStream == null) {
				logger.warn("missing {} file from src", PROPERTIES_FILE_NAME);
			} else {
				System.getProperties().load(iStream);
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		try {
			File file = new File(PROPERTIES_FILE_NAME);
			if (file.exists()) {
				// load application.properties from application path
				InputStream iStream = new FileInputStream(PROPERTIES_FILE_NAME);
				System.getProperties().load(iStream);
			} else {
				logger.info("missing user configuration file");
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}
}
