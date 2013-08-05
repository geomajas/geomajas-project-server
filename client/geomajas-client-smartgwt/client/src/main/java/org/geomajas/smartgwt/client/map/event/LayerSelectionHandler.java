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

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent.Type;
import org.geomajas.annotation.UserImplemented;

/**
 * Interface for handling select layer events.
 *
 * @author Pieter De Graef
 * @since 1.6.0
 */
@Api(allMethods = true)
@UserImplemented
public interface LayerSelectionHandler extends EventHandler {

	/** Event type. */
	Type<LayerSelectionHandler> TYPE = new Type<LayerSelectionHandler>();

	/**
	 * Called when layer is selected.
	 *
	 * @param event layer selected event
	 */
	void onSelectLayer(LayerSelectedEvent event);

	/**
	 * Called when layer is deselected.
	 *
	 * @param event layer deselected event
	 */
	void onDeselectLayer(LayerDeselectedEvent event);
}
