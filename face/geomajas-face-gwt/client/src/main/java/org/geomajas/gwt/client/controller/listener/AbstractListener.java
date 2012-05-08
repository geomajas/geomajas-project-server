/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt.client.controller.listener;

import org.geomajas.annotation.Api;

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

	/** {@inheritDoc} */
	public void onMouseDown(ListenerEvent event) {
	}

	/** {@inheritDoc} */
	public void onMouseUp(ListenerEvent event) {
	}

	/** {@inheritDoc} */
	public void onMouseMove(ListenerEvent event) {
	}

	/** {@inheritDoc} */
	public void onMouseOut(ListenerEvent event) {
	}

	/** {@inheritDoc} */
	public void onMouseOver(ListenerEvent event) {
	}

	/** {@inheritDoc} */
	public void onMouseWheel(ListenerEvent event) {
	}
}