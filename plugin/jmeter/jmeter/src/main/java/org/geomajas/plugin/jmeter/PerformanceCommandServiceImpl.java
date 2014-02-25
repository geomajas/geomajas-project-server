/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.jmeter;

import java.util.HashMap;

import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.CommandRequest;
import org.geomajas.command.CommandResponse;
import org.geomajas.command.dto.GetConfigurationRequest;
import org.geomajas.command.dto.GetConfigurationResponse;
import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientWidgetInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Default implementation of {@link PerformanceCommandService}.
 * 
 * @author Jan De Moerloose
 * 
 */
public class PerformanceCommandServiceImpl implements PerformanceCommandService {

	private final Logger log = LoggerFactory.getLogger(PerformanceCommandServiceImpl.class);

	@Autowired
	private CommandDispatcher commandDispatcher;

	public CommandResponse execute(String commandName, CommandRequest commandRequest, String userToken, String locale) {
		int port = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest()
				.getRemotePort();
		log.debug("Command on port " + port);
		CommandResponse response = commandDispatcher.execute(commandName, commandRequest, userToken, locale);
		filter(response);
		return response;
	}

	private void filter(CommandResponse response) {
		if (response instanceof GetConfigurationResponse) {
			GetConfigurationResponse getConfigurationResponse = (GetConfigurationResponse) response;
			ClientApplicationInfo appInfo = getConfigurationResponse.getApplication();
			appInfo.setWidgetInfo(new HashMap<String, ClientWidgetInfo>());
			appInfo.setUserData(null);
			for (ClientMapInfo mapInfo : appInfo.getMaps()) {
				mapInfo.setWidgetInfo(new HashMap<String, ClientWidgetInfo>());
				mapInfo.setUserData(null);
				for (ClientLayerInfo layerInfo : mapInfo.getLayers()) {
					layerInfo.setWidgetInfo(new HashMap<String, ClientWidgetInfo>());
					layerInfo.setUserData(null);
				}
			}
		}

	}

	public long getCommandCount() {
		return commandDispatcher.getCommandCount();
	}

}
