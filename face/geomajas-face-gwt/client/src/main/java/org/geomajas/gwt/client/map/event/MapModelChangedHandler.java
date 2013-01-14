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

package org.geomajas.gwt.client.map.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;

/**
 * Interface for handling when the map model has changed.
 *
 * @author Joachim Van der Auwera
 * @since 1.10.0
 */
@Api(allMethods = true)
@UserImplemented
public interface MapModelChangedHandler extends EventHandler {

	/** Event type. */
	GwtEvent.Type<MapModelChangedHandler> TYPE = new GwtEvent.Type<MapModelChangedHandler>();

	/**
	 * Called when the map model has changed.
	 * 
	 * @param event changed event
	 */
	void onMapModelChanged(MapModelChangedEvent event);
}
