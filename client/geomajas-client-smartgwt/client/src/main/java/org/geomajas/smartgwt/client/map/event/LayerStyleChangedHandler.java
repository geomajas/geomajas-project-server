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
import com.google.gwt.event.shared.GwtEvent.Type;

/**
 * Handler that catches changes in layer style.
 * 
 * @author Pieter De Graef
 * @since 1.8.0
 */
public interface LayerStyleChangedHandler extends EventHandler {

	Type<LayerStyleChangedHandler> TYPE = new Type<LayerStyleChangedHandler>();

	/**
	 * Called when the style of a layer has changed.
	 * 
	 * @param event
	 *            event
	 * @since 1.8.0
	 */
	void onLayerStyleChange(LayerStyleChangeEvent event);
}
