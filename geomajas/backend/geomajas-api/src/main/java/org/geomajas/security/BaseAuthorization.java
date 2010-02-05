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

package org.geomajas.security;

/**
 * Base authorization, determines authorization of tools and commands and entire layers.
 *
 * @author Joachim Van der Auwera
 */
public interface BaseAuthorization {

	/**
	 * Id for this authorization object if any.
	 * <p/>
	 * Lack of id can hamper caching of data.
	 * <p/>
	 * When two {@link org.geomajas.security.Authorization} objects from the same
	 * {@link org.geomajas.security.SecurityService} have the same id, they should behave exactly the same.
	 * <p/>
	 * The id is not allowed to contain the horizontal bar ("|") or at ("@") characters (except on the
	 * {@link org.geomajas.security.SecurityContext} where these characters are used to make the combination of
	 * contained authentications unique). 
	 *
	 * @return authentication id
	 */
	String getId();

	/**
	 * Check whether the tool with given id is allowed to be used.
	 *
	 * @param toolId tool id, as specified in {@link org.geomajas.configuration.ToolInfo}.
	 * @return true when access is allowed
	 */
	boolean isToolAuthorized(String toolId);

	/**
	 * Check whether the command with given name is allowed to be used.
	 *
	 * @param commandName name of command as passed to {@link org.geomajas.command.CommandDispatcher}
	 * @return true when access is allowed
	 */
	boolean isCommandAuthorized(String commandName);

	/**
	 * Check whether there are features in the layer which are visible.
	 *
	 * @param layerId layer id for which the visibility should be tested
	 * @return true when the logged in user is allowed to see part of the layer
	 */
	boolean isLayerVisible(String layerId);

	/**
	 * Check whether there are features in the layer which can be updated.
	 * <p/>
	 * When isLayerUpdateAuthorized() is true, then isLayerVisible() should also be true.
	 *
	 * @param layerId layer id for which the visible area should be returned
	 * @return true when the logged in user is allowed to edit the visible part of the layer
	 */
	boolean isLayerUpdateAuthorized(String layerId);

	/**
	 * Check whether creating new features may be allowed.
	 * <p/>
	 * When isLayerCreateAuthorized() is true, then isLayerVisible() should also be true.
	 *
	 * @param layerId layer id for which the visible area should be returned
	 * @return true when the logged in user is allowed to edit the visible part of the layer
	 */
	boolean isLayerCreateAuthorized(String layerId);

	/**
	 * Check whether there are features in the layer which can be deleted.
	 * <p/>
	 * When isLayerDeleteAuthorized() is true, then isLayerVisible() should also be true.
	 *
	 * @param layerId layer id for which the visible area should be returned
	 * @return true when the logged in user is allowed to edit the visible part of the layer
	 */
	boolean isLayerDeleteAuthorized(String layerId);	
}
