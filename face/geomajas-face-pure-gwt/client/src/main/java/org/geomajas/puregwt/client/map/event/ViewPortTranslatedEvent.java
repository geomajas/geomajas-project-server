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

import org.geomajas.global.FutureApi;
import org.geomajas.puregwt.client.map.ViewPort;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event that is fired when a translation on the {@link ViewPort} has occurred, while the scale has remained the same.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@FutureApi(allMethods = true)
public class ViewPortTranslatedEvent extends GwtEvent<ViewPortChangedHandler> {

	private ViewPort viewPort;

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	public ViewPortTranslatedEvent(ViewPort viewPort) {
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
		handler.onViewPortTranslated(this);
	}
}