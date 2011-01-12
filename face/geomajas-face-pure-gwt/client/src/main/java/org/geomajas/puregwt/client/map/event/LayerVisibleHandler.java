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
package org.geomajas.puregwt.client.map.event;

import org.geomajas.global.Api;
import org.geomajas.global.UserImplemented;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent.Type;

/**
 * Interface for handling layer visibility events.
 * 
 * @author Frank Wynants
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
@UserImplemented
public interface LayerVisibleHandler extends EventHandler {

	Type<LayerVisibleHandler> TYPE = new Type<LayerVisibleHandler>();

	/**
	 * Called when labels are shown on the layer.
	 * 
	 * @param event
	 *            event
	 */
	void onShow(LayerShowEvent event);

	/**
	 * Called when labels are disabled on the layer.
	 * 
	 * @param event
	 *            event
	 */
	void onHide(LayerHideEvent event);
}