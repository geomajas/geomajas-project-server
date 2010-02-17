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

package org.geomajas.plugin.springsecurity.configuration;

import org.geomajas.security.BaseAuthorization;

import java.util.List;

/**
 * Layer authorization matching LayerAuthorizationInfo.
 *
 * @author Joachim Van der Auwera
 */
public class LayerAuthorization implements BaseAuthorization {

	private LayerAuthorizationInfo info;

	LayerAuthorization(LayerAuthorizationInfo info) {
		this.info = info;
	}

	public String getId() {
		return "LayerAuthorizationInfo." + Integer.toString(info.hashCode());
	}

	private boolean check(String id, List<String> matches) {
		if (null != matches) {
			for (String test : matches) {
				if ("*".equals(test) || id.equals(test)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isToolAuthorized(String toolId) {
		return check(toolId, info.getAllowedTools());
	}

	public boolean isCommandAuthorized(String commandName) {
		return check(commandName, info.getAllowedCommands());
	}

	public boolean isLayerVisible(String layerId) {
		return check(layerId, info.getVisibleLayers());
	}

	public boolean isLayerUpdateAuthorized(String layerId) {
		return check(layerId, info.getUpdateAuthorizedLayers());
	}

	public boolean isLayerCreateAuthorized(String layerId) {
		return check(layerId, info.getCreateAuthorizedLayers());
	}

	public boolean isLayerDeleteAuthorized(String layerId) {
		return check(layerId, info.getDeleteAuthorizedLayers());
	}

}
