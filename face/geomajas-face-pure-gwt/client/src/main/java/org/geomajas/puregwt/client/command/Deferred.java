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

/**
 * Call-back holder for a command. When a response returns from the server (either successfully or with errors to
 * report), the call-backs within this object will be executed. The idea is that you send out a request with a certain
 * call-back, but perhaps while the command is being send, you may want to do some extra calculations with the results.
 * No need to send out an extra command to the server, just add a call-back here.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface Deferred {

	/** Cancel the execution of the call-back for the associated command. */
	void cancel();

	/**
	 * Add a call-back to the deferred, to be executed when the response returns from the server. More than 1 call-back
	 * can be added to be executed when the response returns.<br/>
	 * The idea is that you send out a request with a certain call-back, but perhaps while the command is being send,
	 * you may want to do some extra calculations with the results. No need to send out an extra command to the server,
	 * just add a call-back here.
	 * 
	 * @param callback
	 *            The call-back to add.
	 */
	void addCallback(CommandCallback callback);

	/**
	 * Returns wether or not the associated command has been cancelled.
	 * 
	 * @return Returns true or false.
	 */
	boolean isCancelled();
}