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

package org.geomajas.gwt.client.command;

import java.util.ArrayList;
import java.util.List;

import com.smartgwt.client.core.Function;

/**
 * Call-back holder for a command. When a response returns from the server (either successfully or with errors to
 * report), the call-backs within this object will be executed. The idea is that you send out a request with a certain
 * call-back, but perhaps while the command is being send, you may want to do some extra calculations with the results.
 * No need to send out an extra command to the server, just add a call-back here.
 * 
 * @author Pieter De Graef
 */
public class Deferred {

	private List<CommandCallback> onSuccessCallbacks = new ArrayList<CommandCallback>();

	private List<Function> onErrorCallbacks = new ArrayList<Function>();

	private boolean cancelled;

	public Deferred() {
	}

	public void cancel() {
		onSuccessCallbacks.clear();
		onErrorCallbacks.clear();
		cancelled = true;
	}
	
	/**
	 * 
	 * @param callback, despite the method nam, this may also be a commandcallback that implements
	 * CommandExceptionCallback or CommunicationExceptionCallback. 
	 */
	public void addSuccessCallback(CommandCallback callback) {
		onSuccessCallbacks.add(callback);
	}

	public void addErrorCallback(Function onError) {
		onErrorCallbacks.add(onError);
	}

	public List<CommandCallback> getOnSuccessCallbacks() {
		if (cancelled) {
			return new ArrayList<CommandCallback>();
		}
		return onSuccessCallbacks;
	}

	public List<Function> getOnErrorCallbacks() {
		return onErrorCallbacks;
	}

	public boolean isCancelled() {
		return cancelled;
	}
}
