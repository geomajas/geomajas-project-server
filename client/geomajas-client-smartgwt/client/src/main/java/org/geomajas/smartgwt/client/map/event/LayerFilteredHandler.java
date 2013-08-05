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

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent.Type;

/**
 * Interface for handling layer filter events.
 * 
 * @author Jan De Moerloose
 * @since 1.9.0
 */
@Api(allMethods = true)
@UserImplemented
public interface LayerFilteredHandler extends EventHandler {

	/** Event type. */
	Type<LayerFilteredHandler> TYPE = new Type<LayerFilteredHandler>();

	/**
	 * Called when filters are added to/removed from on the layer.
	 * 
	 * @param event event
	 */
	void onFilterChange(LayerFilteredEvent event);
}
