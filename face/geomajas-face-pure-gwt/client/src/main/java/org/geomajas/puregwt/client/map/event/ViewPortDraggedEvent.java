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
import org.geomajas.puregwt.client.map.ViewPort;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event that is fired when the user has been panning on the map. The panning results in a quick succession of
 * translations and therefore deserves it's own event.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class ViewPortDraggedEvent extends GwtEvent<ViewPortChangedHandler> {

	private static Type<ViewPortChangedHandler> TYPE;

	private ViewPort viewPort;

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	public ViewPortDraggedEvent(ViewPort viewPort) {
		this.viewPort = viewPort;
	}

	// -------------------------------------------------------------------------
	// Event implementation:
	// -------------------------------------------------------------------------

	/**
	 * Get the type associated with this event.
	 * 
	 * @return returns the handler type
	 */
	public static Type<ViewPortChangedHandler> getType() {
		if (TYPE == null) {
			TYPE = new Type<ViewPortChangedHandler>();
		}
		return TYPE;
	}

	public final Type<ViewPortChangedHandler> getAssociatedType() {
		return TYPE;
	}

	public ViewPort getViewPort() {
		return viewPort;
	}

	// ------------------------------------------------------------------------
	// Protected methods:
	// ------------------------------------------------------------------------

	protected void dispatch(ViewPortChangedHandler handler) {
		handler.onViewPortDragged(this);
	}
}