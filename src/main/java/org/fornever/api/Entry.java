package org.fornever.api;

import org.fornever.api.config.GuiceBindings;
import org.fornever.api.server.Server;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Here is the entry of Database API
 * 
 * @author Theo Sun
 *
 */
public class Entry {

	public static Injector injector;

	/**
	 * main method
	 * 
	 * @param args
	 */
	public static void main(String... args) {
		// create injector context
		injector = Guice.createInjector(new GuiceBindings());
		// start Server
		injector.getInstance(Server.class);
	}

}
