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

package org.geomajas.command.configuration;

import java.util.ArrayList;
import java.util.Arrays;

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
@Component()
public class RefreshConfigurationCommand implements Command<RefreshConfigurationRequest, RefreshConfigurationResponse> {

	@Autowired
	private ApplicationContext context;

	private final Logger log = LoggerFactory.getLogger(RefreshConfigurationCommand.class);

	@Override
	public void execute(RefreshConfigurationRequest request, RefreshConfigurationResponse response) throws Exception {
		if (context instanceof ReconfigurableApplicationContext) {
			ReconfigurableApplicationContext rollback = (ReconfigurableApplicationContext) context;
			if (request.getConfigLocations() != null) {
				ArrayList<String> configLocations = new ArrayList<String>();
				configLocations.add("org/geomajas/spring/geomajasContext.xml");

				configLocations.addAll(Arrays.asList(request.getConfigLocations()));
				try {
					rollback.refresh(configLocations.toArray(new String[configLocations.size()]));
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

	@Override
	public RefreshConfigurationResponse getEmptyCommandResponse() {
		return new RefreshConfigurationResponse();
	}
}
