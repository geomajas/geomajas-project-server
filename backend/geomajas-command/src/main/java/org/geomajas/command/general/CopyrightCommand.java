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
package org.geomajas.command.general;

import org.geomajas.command.Command;
import org.geomajas.command.EmptyCommandRequest;
import org.geomajas.global.Api;
import org.geomajas.global.CopyrightInfo;
import org.geomajas.global.PluginInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Command which allows fetching the copyright info for back-end and plug-ins.
 * <p/>
 * It also emits the copyright info in the logs for extra assurance of compliance with the requirements.
 *
 * @author Joachim Van der Auwera
 * @since 1.7.0
 */
@Api
@Component()
public class CopyrightCommand implements Command<EmptyCommandRequest, CopyrightResponse> {

	private final Logger log = LoggerFactory.getLogger(CopyrightCommand.class);

	private Map<String, CopyrightInfo> copyrightMap = new HashMap<String, CopyrightInfo>();

	@Autowired(required = false)
	protected Map<String, PluginInfo> declaredPlugins;

	@PostConstruct
	private void buildCopyrightMap() {
		if (null == declaredPlugins) {
			return;
		}
		// go over all plug-ins, adding copyright info, avoiding duplicates (on object key)
		for (PluginInfo plugin : declaredPlugins.values()) {
			for (CopyrightInfo copyright : plugin.getCopyrightInfo()) {
				String key = copyright.getKey();
				if (!copyrightMap.containsKey(key)) {
					log.info(copyright.getKey() + ": " + copyright.getCopyright() + " : licensed as " +
							copyright.getLicenseName() + ", see " + copyright.getLicenseUrl());
					copyrightMap.put(key, copyright);
				}
			}
		}
	}

	public CopyrightResponse getEmptyCommandResponse() {
		return new CopyrightResponse();
	}

	public void execute(EmptyCommandRequest request, CopyrightResponse response) throws Exception {
		response.setCopyrights(copyrightMap.values());
	}

}