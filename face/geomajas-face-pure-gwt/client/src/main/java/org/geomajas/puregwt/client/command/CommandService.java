/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
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