package org.fornever.api.server;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.olingo.server.api.ODataHttpHandler;
import org.fornever.api.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

public class ODataServlet extends HttpServlet {

	private static final long serialVersionUID = -5439922120117724291L;
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Inject
	private ODataHttpHandler handler;

	@Override
	public void init() throws ServletException {
		super.init();
		Entry.injector.injectMembers(this);
	}

	@Override
	protected void service(final HttpServletRequest req, final HttpServletResponse res)
			throws ServletException, IOException {
		try {
			this.handler.process(req, res);
		} catch (RuntimeException e) {
			logger.error("Server Error occurred: ", e);
			throw new ServletException(e);
		}
	}
}