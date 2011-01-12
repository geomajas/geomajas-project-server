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

/**
 * Interface for event handlers that catch changes in the <code>ViewPort</code>.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
@UserImplemented
public interface ViewPortChangedHandler extends EventHandler {

	/**
	 * Catches events where the <code>ViewPort</code> has both scaled and translated.
	 * 
	 * @param event
	 *            The actual {@link org.geomajas.puregwt.client.map.event.ViewPortChangedEvent}.
	 */
	void onViewPortChanged(ViewPortChangedEvent event);

	/**
	 * Catches events where the <code>ViewPort</code> scaled only.
	 * 
	 * @param event
	 *            The actual {@link org.geomajas.puregwt.client.map.event.ViewPortScaledEvent}.
	 */
	void onViewPortScaled(ViewPortScaledEvent event);

	/**
	 * Catches events where the <code>ViewPort</code> has translated only.
	 * 
	 * @param event
	 *            The actual {@link org.geomajas.puregwt.client.map.event.ViewPortTranslatedEvent}.
	 */
	void onViewPortTranslated(ViewPortTranslatedEvent event);

	/**
	 * Catches events where the <code>ViewPort</code> has been dragged (panning).
	 * 
	 * @param event
	 *            The actual {@link org.geomajas.puregwt.client.map.event.ViewPortDraggedEvent}.
	 */
	void onViewPortDragged(ViewPortDraggedEvent event);
}