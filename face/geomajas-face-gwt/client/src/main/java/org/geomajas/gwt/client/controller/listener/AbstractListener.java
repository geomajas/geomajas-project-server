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

package org.geomajas.gwt.client.controller.listener;

import org.geomajas.global.Api;

/**
 * <p>
 * Abstract implementation for passive listeners on a map. These listeners receive notifications of mouse events on the
 * map, but cannot interfere. That is why they receive a replacement event ({@link ListenerEvent}) instead of the real
 * mouse events.
 * </p>
 * <p>
 * This class has nothing but empty methods. A base to start working from when using Listeners.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.8.0
 */
@Api(allMethods = true)
public abstract class AbstractListener implements Listener {

	public void onMouseDown(ListenerEvent event) {
	}

	public void onMouseUp(ListenerEvent event) {
	}

	public void onMouseMove(ListenerEvent event) {
	}

	public void onMouseOut(ListenerEvent event) {
	}

	public void onMouseOver(ListenerEvent event) {
	}

	public void onMouseWheel(ListenerEvent event) {
	}
}