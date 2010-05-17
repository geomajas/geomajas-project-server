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

package org.geomajas.plugin.staticsecurity.configuration;

import org.geomajas.global.Api;
import org.geomajas.security.BaseAuthorization;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Layer authorization matching LayerAuthorizationInfo.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class LayerAuthorization implements BaseAuthorization {

	private LayerAuthorizationInfo info;

	LayerAuthorization(LayerAuthorizationInfo info) {
		this.info = info;
	}

	public String getId() {
		return "LayerAuthorizationInfo." + Integer.toString(info.hashCode());
	}

	public boolean isToolAuthorized(String toolId) {
		return check(toolId, info.getToolsInclude(), info.getToolsExclude());
	}

	public boolean isCommandAuthorized(String commandName) {
		return check(commandName, info.getCommandsInclude(), info.getCommandsExclude());
	}

	public boolean isLayerVisible(String layerId) {
		return check(layerId, info.getVisibleLayersInclude(), info.getVisibleLayersExclude());
	}

	public boolean isLayerUpdateAuthorized(String layerId) {
		return check(layerId, info.getUpdateAuthorizedLayersInclude(), info.getUpdateAuthorizedLayersExclude());
	}

	public boolean isLayerCreateAuthorized(String layerId) {
		return check(layerId, info.getCreateAuthorizedLayersInclude(), info.getCreateAuthorizedLayersExclude());
	}

	public boolean isLayerDeleteAuthorized(String layerId) {
		return check(layerId, info.getDeleteAuthorizedLayersInclude(), info.getDeleteAuthorizedLayersExclude());
	}

	protected boolean check(String id, List<String> includes, List<String> excludes) {
		return check(id, includes) && !check(id, excludes);
	}

	protected boolean check(String id, List<String> includes) {
		if (null != includes) {
			for (String check : includes) {
				if (check(id, check)) {
					return true;
				}
			}
		}
		return false;
	}

	protected boolean check(String value, String regex) {
		Pattern pattern = Pattern.compile(regex);
		return pattern.matcher(value).matches();
	}
}
