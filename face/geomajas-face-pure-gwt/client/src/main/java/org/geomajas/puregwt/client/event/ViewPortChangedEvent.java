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

package org.geomajas.puregwt.client.event;

import org.geomajas.annotation.Api;
import org.geomajas.puregwt.client.map.ViewPort;

import com.google.web.bindery.event.shared.Event;

/**
 * Event that is fired when the view on the {@link ViewPort} has been changed so that both scaling and translation have
 * occurred.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class ViewPortChangedEvent extends Event<ViewPortChangedHandler> {

	private final ViewPort viewPort;

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	public ViewPortChangedEvent(ViewPort viewPort) {
		this.viewPort = viewPort;
	}

	// -------------------------------------------------------------------------
	// Event implementation:
	// -------------------------------------------------------------------------

	public final Type<ViewPortChangedHandler> getAssociatedType() {
		return ViewPortChangedHandler.TYPE;
	}

	public ViewPort getViewPort() {
		return viewPort;
	}

	// ------------------------------------------------------------------------
	// Protected methods:
	// ------------------------------------------------------------------------

	protected void dispatch(ViewPortChangedHandler handler) {
		handler.onViewPortChanged(this);
	}
}