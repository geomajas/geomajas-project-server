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

package org.geomajas.gwt.client.event;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;

import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.Event.Type;

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
	 * The type of the handler.
	 */
	Type<ViewPortChangedHandler> TYPE = new Type<ViewPortChangedHandler>();

	/**
	 * Catches events where the <code>ViewPort</code> has both scaled and translated.
	 * 
	 * @param event
	 *            The actual {@link org.geomajas.gwt.client.event.ViewPortChangedEvent}.
	 */
	void onViewPortChanged(ViewPortChangedEvent event);

	/**
	 * Catches events where the <code>ViewPort</code> scaled only.
	 * 
	 * @param event
	 *            The actual {@link org.geomajas.gwt.client.event.ViewPortScaledEvent}.
	 */
	void onViewPortScaled(ViewPortScaledEvent event);

	/**
	 * Catches events where the <code>ViewPort</code> has translated only.
	 * 
	 * @param event
	 *            The actual {@link org.geomajas.gwt.client.event.ViewPortTranslatedEvent}.
	 */
	void onViewPortTranslated(ViewPortTranslatedEvent event);
}