/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.CommandResponse;
import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.GeomajasService;
import org.geomajas.gwt.client.command.GwtCommand;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Geomajas GWT service, implements communication between GWT face and back-end.
 * 
 * @author Pieter De Graef
 * @author Joachim Van der Auwera
 * @author Jan De Moerloose
 * @since 0.0.0
 */
@Api(allMethods = true)
public class GeomajasServiceImpl extends RemoteServiceServlet implements GeomajasService,
		ApplicationListener<ContextRefreshedEvent> {

	private static final long serialVersionUID = -4388317165048891159L;

	private ApplicationContext applicationContext;

	private CommandDispatcher commandDispatcher;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		// register the controller object
		applicationContext = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
		if (applicationContext instanceof ConfigurableApplicationContext) {
			((ConfigurableApplicationContext) applicationContext).addApplicationListener(this);
		}
		initDispatcher();
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.service(req, resp);
	}

	/**
	 * Execute a GWT RPC command request, and return the response. These request come from the client, and the response
	 * is sent back to the client. We use a {@link CommandDispatcher} to actually execute the command.
	 */
	public CommandResponse execute(GwtCommand request) {
		if (request != null) {
			return commandDispatcher.execute(request.getCommandName(), request.getCommandRequest(),
					request.getUserToken(), request.getLocale());
		}
		return null;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		initDispatcher();
	}

	/**
	 * Initialize dispatcher.
	 */
	protected void initDispatcher() {
		commandDispatcher = applicationContext.getBean("command.CommandDispatcher", CommandDispatcher.class);
	}
}