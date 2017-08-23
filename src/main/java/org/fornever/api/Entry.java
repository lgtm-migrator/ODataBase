package org.fornever.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.fornever.api.config.GuiceBindings;
import org.fornever.api.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Here is the entry of Database API
 * 
 * @author Theo Sun
 *
 */
public class Entry {

	private final static String PROPERTIES_FILE = "application.properties";
	private static Logger logger = LoggerFactory.getLogger(Entry.class);

	/**
	 * main method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		loadProperties();
		Injector injector = Guice.createInjector(new GuiceBindings()); // create injector context
		injector.getInstance(Server.class); // start Server
	}

	/**
	 * load configuration properties from properties file
	 */
	private static void loadProperties() {
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
