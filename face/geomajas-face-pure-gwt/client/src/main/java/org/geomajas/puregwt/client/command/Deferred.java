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

import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotation.FutureApi;

/**
 * Call-back holder for a command. When a response returns from the server (either successfully or with errors to
 * report), the call-backs within this object will be executed. The idea is that you send out a request with a certain
 * call-back, but perhaps while the command is being send, you may want to do some extra calculations with the results.
 * No need to send out an extra command to the server, just add a call-back here.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@FutureApi(allMethods = true)
public class Deferred {

	private List<CommandCallback> callbacks = new ArrayList<CommandCallback>();

	private boolean cancelled;

	// ------------------------------------------------------------------------
	// Public methods:
	// ------------------------------------------------------------------------

	/** Cancel the execution of the call-back for the associated command. */
	public void cancel() {
		callbacks.clear();
		cancelled = true;
	}

	/**
	 * Returns whether or not the associated command has been canceled.
	 * 
	 * @return Returns true or false.
	 */
	public boolean isCancelled() {
		return cancelled;
	}

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
	public void addCallback(CommandCallback callback) {
		callbacks.add(callback);
	}

	/**
	 * Return the full list of call-backs stored in this deferred object.
	 * 
	 * @return The full list of call-backs stored in this deferred object.
	 */
	public List<CommandCallback> getCallbacks() {
		return callbacks;
	}
}