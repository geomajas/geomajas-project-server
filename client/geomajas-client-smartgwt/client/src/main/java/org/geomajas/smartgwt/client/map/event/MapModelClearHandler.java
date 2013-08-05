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

package org.geomajas.smartgwt.client.map.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;

/**
 * Interface for handling when the map model will be cleared.
 *
 * @author Joachim Van der Auwera
 * @since 1.10.0
 */
@Api(allMethods = true)
@UserImplemented
public interface MapModelClearHandler extends EventHandler {

	/**
	 * Event type.
	 */
	GwtEvent.Type<MapModelClearHandler> TYPE = new GwtEvent.Type<MapModelClearHandler>();

	/**
	 * Called when the map model will be cleared.
	 * 
	 * @param event changed event
	 */
	void onMapModelClear(MapModelClearEvent event);
}
