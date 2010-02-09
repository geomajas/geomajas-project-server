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
 * Allow configuring authorizations at tool, command and layer level.
 *
 * @author Joachim Van der Auwera
 */
public class LayerAuthorizationInfo implements AuthorizationInfo {

	private List<String> allowedTools;
	private List<String> allowedCommands;
	private List<String> visibleLayers;
	private List<String> updateAuthorizedLayers;
	private List<String> createAuthorizedLayers;
	private List<String> deleteAuthorizedLayers;

	/**
	 * Set list of allowed tools. You can include "*" to allow all tools.
	 *
	 * @param allowedTools list of tool ids which are authorized
	 */
	public void setAllowedTools(List<String> allowedTools) {
		this.allowedTools = allowedTools;
	}

	/**
	 * Set list of allowed commands. You can include "*" to allow all commands.
	 *
	 * @param allowedCommands list of command names which are authorized
	 */
	public void setAllowedCommands(List<String> allowedCommands) {
		this.allowedCommands = allowedCommands;
	}

	/**
	 * Set list of layers which should be visible. You can include "*" to allow all layers.
	 *
	 * @param visibleLayers list of layer ids which are authorized
	 */
	public void setVisibleLayers(List<String> visibleLayers) {
		this.visibleLayers = visibleLayers;
	}

	/**
	 * Set list of layers for which updates are authorized. You can include "*" to allow all layers.
	 *
	 * @param updateAuthorizedLayers list of layer ids which are authorized
	 */
	public void setUpdateAuthorizedLayers(List<String> updateAuthorizedLayers) {
		this.updateAuthorizedLayers = updateAuthorizedLayers;
	}

	/**
	 * Set list of layers for which creating features is authorized. You can include "*" to allow all layers.
	 *
	 * @param createAuthorizedLayers list of layer ids which are authorized
	 */
	public void setCreateAuthorizedLayers(List<String> createAuthorizedLayers) {
		this.createAuthorizedLayers = createAuthorizedLayers;
	}

	/**
	 * Set list of layers for which deleting features is authorized. You can include "*" to allow all layers.
	 *
	 * @param deleteAuthorizedLayers list of layer ids which are authorized
	 */
	public void setDeleteAuthorizedLayers(List<String> deleteAuthorizedLayers) {
		this.deleteAuthorizedLayers = deleteAuthorizedLayers;
	}

	private Object getLocalHash() {
		return hashCode();
	}

	public BaseAuthorization getAuthorization() {
		return new BaseAuthorization() {
			public String getId() {
				return "LayerAuthorizationInfo." + getLocalHash().toString();
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
				return check(toolId, allowedTools);
			}

			public boolean isCommandAuthorized(String commandName) {
				return check(commandName, allowedCommands);
			}

			public boolean isLayerVisible(String layerId) {
				return check(layerId, visibleLayers);
			}

			public boolean isLayerUpdateAuthorized(String layerId) {
				return check(layerId, updateAuthorizedLayers);
			}

			public boolean isLayerCreateAuthorized(String layerId) {
				return check(layerId, createAuthorizedLayers);
			}

			public boolean isLayerDeleteAuthorized(String layerId) {
				return check(layerId, deleteAuthorizedLayers);
			}
		};
	}
}
