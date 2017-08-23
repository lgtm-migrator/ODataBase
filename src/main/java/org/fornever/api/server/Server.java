package org.fornever.api.server;

import static spark.Spark.*;

import org.fornever.api.service.HelloService;

import com.google.inject.Inject;

public class Server {

	@Inject
	private HelloService testService;

	public HelloService getTestService() {
		return testService;
	}

	public void setTestService(HelloService testService) {
		this.testService = testService;
	}

	public Server() {
		port(Integer.parseInt(System.getProperty("api.server.port")));
		get("/hello", (req, res) -> testService.getHello());
	}
	
}
