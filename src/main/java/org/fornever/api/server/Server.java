package org.fornever.api.server;

import static spark.Spark.*;

import org.fornever.api.service.HelloService;

import com.google.inject.Inject;

public class Server {

	@Inject
	private HelloService helloService;

	public HelloService getTestService() {
		return helloService;
	}

	public void setTestService(HelloService testService) {
		this.helloService = testService;
	}

	public Server() {
		port(Integer.parseInt(System.getProperty("api.server.port")));
		get("/hello", (req, res) -> helloService.getHello());
	}
	
}
