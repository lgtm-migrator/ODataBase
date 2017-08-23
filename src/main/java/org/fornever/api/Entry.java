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

	private static Logger logger = LoggerFactory.getLogger(Entry.class);

	/**
	 * main method
	 * 
	 * @param args
	 */
	public static void main(String... args) {
		// create injector context
		Injector injector = Guice.createInjector(new GuiceBindings());
		// start Server
		injector.getInstance(Server.class); 
	}

}
