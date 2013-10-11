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

package org.geomajas.puregwt.client.event;

import org.geomajas.annotation.Api;
import org.geomajas.puregwt.client.map.ViewPort;

import com.google.web.bindery.event.shared.Event;

/**
 * Intermediate event that is fired when the ViewPort is both scaling and translating during the animation/pinching
 * phase. This event is only interesting for handlers that want to perform extra logic during the animation/pinching
 * phase that is not processing-intensive and does not use any map content at the intermediate scale levels (such
 * content will not be available as intermediate scale levels are obtained by resizing the original scale). Use
 * {@link ViewPortChangedHandler} to react to non-intermediate map changes.
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api(allMethods = true)
public class ViewPortChangingEvent extends Event<ViewPortChangingHandler> {

	private final ViewPort viewPort;

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------
	/**
	 * Create an event for the specified view port.
	 * 
	 * @param viewPort the view port
	 */
	public ViewPortChangingEvent(ViewPort viewPort) {
		this.viewPort = viewPort;
	}

	// -------------------------------------------------------------------------
	// Event implementation:
	// -------------------------------------------------------------------------

	@Override
	public Type<ViewPortChangingHandler> getAssociatedType() {
		return ViewPortChangingHandler.TYPE;
	}

	/**
	 * Get the view port that has changed.
	 * 
	 * @return the view port
	 */
	public ViewPort getViewPort() {
		return viewPort;
	}

	// ------------------------------------------------------------------------
	// Protected methods:
	// ------------------------------------------------------------------------

	protected void dispatch(ViewPortChangingHandler handler) {
		handler.onViewPortChanging(this);
	}
}