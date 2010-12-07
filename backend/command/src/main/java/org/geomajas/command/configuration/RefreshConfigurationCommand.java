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

package org.geomajas.command.configuration;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;

import org.geomajas.command.Command;
import org.geomajas.command.dto.RefreshConfigurationRequest;
import org.geomajas.command.dto.RefreshConfigurationResponse;
import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.spring.ReconfigurableApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * This commands forces a complete reload of the configuration based on the provided list of configuration locations.
 * 
 * @author Jan De Moerloose
 */
@Component
public class RefreshConfigurationCommand implements Command<RefreshConfigurationRequest, RefreshConfigurationResponse> {

	@Autowired
	private ApplicationContext context;

	private final Logger log = LoggerFactory.getLogger(RefreshConfigurationCommand.class);

	public void execute(RefreshConfigurationRequest request, RefreshConfigurationResponse response) throws Exception {
		if (context instanceof ReconfigurableApplicationContext) {
			ReconfigurableApplicationContext rollback = (ReconfigurableApplicationContext) context;
			if (request.getConfigLocations() != null) {
				ArrayList<String> configLocations = new ArrayList<String>();
				configLocations.add("org/geomajas/spring/geomajasContext.xml");

				ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
				if (null == classLoader) {
					classLoader = this.getClass().getClassLoader();
				}
				
				configLocations.addAll(Arrays.asList(request.getConfigLocations()));
				try {
					rollback.refresh(configLocations.toArray(new String[0]));
				} catch (GeomajasException e) {
					log.error("Could not refresh context", e);
					rollback.rollback();
					response.setApplicationNames(context.getBeanNamesForType(ClientApplicationInfo.class));
					throw e;
				}
				response.setApplicationNames(context.getBeanNamesForType(ClientApplicationInfo.class));
			}
		} else {
			response.setApplicationNames(context.getBeanNamesForType(ClientApplicationInfo.class));
			throw new GeomajasException(ExceptionCode.REFRESH_CONFIGURATION_FAILED);
		}
	}

	public RefreshConfigurationResponse getEmptyCommandResponse() {
		return new RefreshConfigurationResponse();
	}
}
