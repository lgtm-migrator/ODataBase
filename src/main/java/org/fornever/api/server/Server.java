package org.fornever.api.server;

import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.support.http.StatViewServlet;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import io.undertow.Undertow;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;

public class Server {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Inject
	public Server(@Named("odata.name") String serviceName, @Named("api.server.port") Integer port,
			@Named("api.server.addr") String addr, @Named("api.server.debug") Boolean debug) throws ServletException {
		// base url
		String baseUrl = "/" + serviceName + ".svc";

		DeploymentInfo servletBuilder = Servlets.deployment().setClassLoader(getClass().getClassLoader())
				.setDeploymentName("servlets").setContextPath("/");

		// odata servlet
		servletBuilder.addServlets(Servlets.servlet("odata", ODataServlet.class).addMapping(baseUrl + "/*"));

		// druid stat view servlet
		if (debug) {
			logger.info("mount druid stat view at /druid/index.html");
			servletBuilder.addServlets(Servlets.servlet("stat", StatViewServlet.class).addMapping("/druid/*"));
		}

		DeploymentManager manager = Servlets.defaultContainer().addDeployment(servletBuilder);
		manager.deploy();

		// start server
		Undertow server = Undertow.builder().addHttpListener(port, addr).setHandler(manager.start()).build();
		server.start();

		logger.info("Server started at {}:{} with path: {}", addr, port, baseUrl);

	}

}
