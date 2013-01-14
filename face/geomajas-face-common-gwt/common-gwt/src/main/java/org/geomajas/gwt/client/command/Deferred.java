/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt.client.command;

import com.smartgwt.client.core.Function;
import org.geomajas.annotation.Api;

import java.util.ArrayList;
import java.util.List;

/**
 * Call-back holder for a command. When a response returns from the server (either successfully or with errors to
 * report), the call-backs within this object will be executed. The idea is that you send out a request with a certain
 * call-back, but perhaps while the command is being send, you may want to do some extra calculations with the results.
 * No need to send out an extra command to the server, just add a call-back here.
 * 
 * @author Pieter De Graef
 * @since 0.0.0
 */
@Api(allMethods = true)
public class Deferred {

	private List<CommandCallback> onSuccessCallbacks = new ArrayList<CommandCallback>();

	private List<Function> onErrorCallbacks = new ArrayList<Function>();

	private boolean cancelled;

	private boolean logCommunicationExceptions = true;

	/** Constructor. */
	public Deferred() {
	}

	/**
	 * Cancel the handling of command callbacks.
	 */
	public void cancel() {
		onSuccessCallbacks.clear();
		onErrorCallbacks.clear();
		cancelled = true;
	}
	
	/**
	 * Add callbacks for the command.
	 * 
	 * @param callback, this can be either a simple {@link CommandCallback} or a version implementing
	 * {@link org.geomajas.gwt.client.command.CommandExceptionCallback} or {@link CommunicationExceptionCallback}.
	 */
	public void addCallback(CommandCallback callback) {
		onSuccessCallbacks.add(callback);
	}

	/**
	 * Add callbacks for the command. Despite the name, this can be for both success or error conditions, see
	 * {@link #addCallback(org.geomajas.gwt.client.command.CommandCallback)}.
	 *
	 * @param callback, despite the method name, this may also be a {@link CommandCallback} that implements
	 * {@link org.geomajas.gwt.client.command.CommandExceptionCallback} or {@link CommunicationExceptionCallback}.
	 * @deprecated use {@link #addCallback(org.geomajas.gwt.client.command.CommandCallback)}
	 */
	@Deprecated
	public void addSuccessCallback(CommandCallback callback) {
		onSuccessCallbacks.add(callback);
	}

	/**
	 * Add callback function which is only called on communication failure.
	 *
	 * @param onError function to call
	 * @deprecated use {@link #addCallback(org.geomajas.gwt.client.command.CommandCallback)}
	 */
	@Deprecated
	public void addErrorCallback(Function onError) {
		onErrorCallbacks.add(onError);
	}

	/**
	 * Get the callbacks for the command (as long as they are not cancelled).
	 *
	 * @return command callbacks
	 */
	public List<CommandCallback> getCallbacks() {
		if (cancelled) {
			return new ArrayList<CommandCallback>();
		}
		return onSuccessCallbacks;
	}

	/**
	 * Get communication error callback functions. In principle this list should be empty and you should check for
	 * {@link CommunicationExceptionCallback} instance in {@link #getCallbacks()}.
	 *
	 * @return communication exception callback functions
	 * @deprecated use {@link #getCallbacks()}
	 */
	@Deprecated
	public List<Function> getErrorCallbacks() {
		return onErrorCallbacks;
	}

	/**
	 * Check whether handling command callbacks was cancelled.
	 *
	 * @return true when no callback processing should be done
	 */
	public boolean isCancelled() {
		return cancelled;
	}

	/**
	 * Should communication exceptions be logged?
	 *
	 * @return true when communication exceptions should be logged on the server side
	 * @since 1.10.0
	 */
	public boolean isLogCommunicationExceptions() {
		return logCommunicationExceptions;
	}

	/**
	 * Should communication exceptions be logged?
	 *
	 * @param logCommunicationExceptions should communication exceptions be logged?
	 * @since 1.10.0
	 */
	public void setLogCommunicationExceptions(boolean logCommunicationExceptions) {
		this.logCommunicationExceptions = logCommunicationExceptions;
	}
}
