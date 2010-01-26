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
 * Base authorization, determines authorization of tools and commands .
 *
 * @author Joachim Van der Auwera
 */
public interface BaseAuthorization {

	/**
	 * Id for this authorization object if any.
	 * <p/>
	 * Lack of id can hamper caching of data.
	 * <p/>
	 * when two {@link org.geomajas.security.Authorization} objects from the same
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
}
