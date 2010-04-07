/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.gwt.server;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.CommandResponse;
import org.geomajas.gwt.client.GeomajasService;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.servlet.ApplicationContextUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Geomajas GWT service, implements communication between GWT face and back-end.
 * 
 * @author Pieter De Graef
 * @author Joachim Van der Auwera
 * @author Jan De Moerloose
 */
public class GeomajasServiceImpl extends RemoteServiceServlet implements GeomajasService,
		ApplicationListener<ContextRefreshedEvent> {

	private static final long serialVersionUID = -4388317165048891159L;

	private ApplicationContext applicationContext;

	private CommandDispatcher commandDispatcher;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		// register the controller object
		applicationContext = ApplicationContextUtils.getApplicationContext(config);
		if (applicationContext instanceof ConfigurableApplicationContext) {
			((ConfigurableApplicationContext) applicationContext).addApplicationListener(this);
		}
		initDispatcher();
	}

	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.service(req, resp);
	}

	/**
	 * Execute a GWT RPC command request, and return the response. These request come from the client, and the response
	 * is sent back to the client. We use a {@link CommandDispatcher} to actually execute the command.
	 */
	public CommandResponse execute(GwtCommand request) {
		if (request != null) {
			CommandResponse result = null;
			result = commandDispatcher.execute(request.getCommandName(), request.getCommandRequest(), request
					.getUserToken(), request.getLocale());
			return result;
		}
		return null;
	}

	public void onApplicationEvent(ContextRefreshedEvent event) {
			initDispatcher();
	}

	protected void initDispatcher() {
		commandDispatcher = applicationContext.getBean("command.CommandDispatcher", CommandDispatcher.class);
	}

}
