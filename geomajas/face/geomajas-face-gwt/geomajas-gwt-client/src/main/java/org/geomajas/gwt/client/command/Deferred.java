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

package org.geomajas.gwt.client.command;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.core.Function;

/**
 * Callback holder for a command. When a response returns from the server (either successfully or with errors to
 * report), the callbacks within this object will be executed. The idea is that you send out a request with a certain
 * callback, but perhaps while the command is being send, you may want to do some extra calculations with the results.
 * No need to send out an extra command to the server, just add a callback here.
 * 
 * @author Pieter De Graef
 */
public class Deferred {

	private List<CommandCallback> onSuccessCallbacks = new ArrayList<CommandCallback>();

	private List<Function> onErrorCallbacks = new ArrayList<Function>();

	private boolean canceled;

	public Deferred() {
	}

	public void cancel() {
		onSuccessCallbacks.clear();
		onErrorCallbacks.clear();
		canceled = true;
	}

	public void addSuccesCallback(CommandCallback onSuccess) {
		onSuccessCallbacks.add(onSuccess);
		if (onSuccessCallbacks.size() > 1) {
			GWT.log("Adding another callback", null);
		}
	}

	public void addErrorCallback(Function onError) {
		onErrorCallbacks.add(onError);
	}

	public List<CommandCallback> getOnSuccessCallbacks() {
		return onSuccessCallbacks;
	}

	public List<Function> getOnErrorCallbacks() {
		return onErrorCallbacks;
	}

	public boolean isCanceled() {
		return canceled;
	}
}
