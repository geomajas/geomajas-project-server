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
package org.geomajas.puregwt.client.command.event;

import org.geomajas.annotation.FutureApi;
import org.geomajas.global.UserImplemented;

import com.google.gwt.event.shared.EventHandler;

/**
 * Interface for event handlers that catch {@link DispatchStoppedEvent}s.
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@FutureApi(allMethods = true)
@UserImplemented
public interface DispatchStoppedHandler extends EventHandler {

	/**
	 * The event catching method.
	 * 
	 * @param event
	 *            The actual {@link DispatchStoppedEvent}.
	 */
	void onDispatchStopped(DispatchStoppedEvent event);
}