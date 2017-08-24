package org.fornever.api.server;

import javax.servlet.ServletException;
import javax.sound.sampled.Port;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import io.undertow.Undertow;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;

public class Server {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Inject
	public Server(@Named("api.server.port") Integer port, @Named("api.server.addr") String addr)
			throws ServletException {
		DeploymentInfo servletBuilder = Servlets.deployment().setClassLoader(getClass().getClassLoader())
				.setDeploymentName("odata").setContextPath("/")
				.addServlets(Servlets.servlet("odata", ServerServlet.class).addMapping("/*"));
		DeploymentManager manager = Servlets.defaultContainer().addDeployment(servletBuilder);
		manager.deploy();
		Undertow server = Undertow.builder().addHttpListener(port, addr).setHandler(manager.start()).build();
		server.start();
		logger.info("Server started at {}:{}", addr, port);
	}

}
