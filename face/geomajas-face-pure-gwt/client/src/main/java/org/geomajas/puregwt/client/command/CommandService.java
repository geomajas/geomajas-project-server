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
package org.geomajas.puregwt.client.command;

import org.geomajas.global.Api;
import org.geomajas.puregwt.client.command.event.DispatchStartedHandler;
import org.geomajas.puregwt.client.command.event.DispatchStoppedHandler;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Central service for executing commands. These commands are sent to the server for execution.
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface CommandService {

	/**
	 * Add a handler for catching events that signal the start of a command.
	 * 
	 * @param handler
	 *            The handler object.
	 * @return Returns the registration for the handler.
	 */
	HandlerRegistration addDispatchStartedHandler(DispatchStartedHandler handler);

	/**
	 * Add a handler for catching events that signal the return of the response for a command.
	 * 
	 * @param handler
	 *            The handler object.
	 * @return Returns the registration for the handler.
	 */
	HandlerRegistration addDispatchStoppedHandler(DispatchStoppedHandler handler);

	/**
	 * The execution function. Executes a server side command.
	 * 
	 * @param command
	 *            The command to be executed. This command is a wrapper around the actual request object.
	 * @param onSuccess
	 *            A <code>CommandCallback</code> function to be executed when the command successfully returns.
	 * @return deferred object which can be used to add extra callbacks
	 */
	Deferred execute(GwtCommand command, final CommandCallback... onSuccess);

	/**
	 * Is the dispatcher busy ?
	 * 
	 * @return true if there are outstanding commands
	 */
	boolean isBusy();

	/**
	 * Set the user token, so it can be sent in very command.
	 * 
	 * @param userToken
	 *            user token
	 */
	void setUserToken(String userToken);

	/**
	 * Is lazy feature loading enabled ?
	 * 
	 * @return true when lazy feature loading is enabled
	 */
	boolean isUseLazyLoading();

	/**
	 * Set lazy feature loading status.
	 * 
	 * @param useLazyLoading
	 *            lazy feature loading status
	 */
	void setUseLazyLoading(boolean useLazyLoading);

	/**
	 * Get default value for "featureIncludes" when getting features.
	 * 
	 * @return default "featureIncludes" value
	 */
	int getLazyFeatureIncludesDefault();

	/**
	 * Set default value for "featureIncludes" when getting features.
	 * 
	 * @param lazyFeatureIncludesDefault
	 *            default for "featureIncludes"
	 */
	void setLazyFeatureIncludesDefault(int lazyFeatureIncludesDefault);

	/**
	 * Get "featureIncludes" to use when selecting features.
	 * 
	 * @return default "featureIncludes" for select commands
	 */
	int getLazyFeatureIncludesSelect();

	/**
	 * Set default "featureIncludes" for select commands.
	 * 
	 * @param lazyFeatureIncludesSelect
	 *            default "featureIncludes" for select commands
	 */
	void setLazyFeatureIncludesSelect(int lazyFeatureIncludesSelect);

	/**
	 * Value to use for "featureIncludes" when all should be included.
	 * 
	 * @return value for "featureIncludes" when all should be included
	 */
	int getLazyFeatureIncludesAll();

	/**
	 * Set "featureIncludes" value when all should be included.
	 * 
	 * @param lazyFeatureIncludesAll
	 *            "featureIncludes" value when all should be included
	 */
	void setLazyFeatureIncludesAll(int lazyFeatureIncludesAll);
}