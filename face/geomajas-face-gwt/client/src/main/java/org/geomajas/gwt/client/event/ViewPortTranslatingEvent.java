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
import org.geomajas.gwt.client.map.ViewPort;

import com.google.web.bindery.event.shared.Event;

/**
 * Intermediate event that is fired when the ViewPort is translating during the dragging phase. This event is only
 * interesting for handlers that want to perform extra logic during the dragging phase that is not processing-intensive.
 * Use {@link ViewPortChangedHandler} to react to non-intermediate map changes.
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api(allMethods = true)
public class ViewPortTranslatingEvent extends Event<ViewPortChangingHandler> {

	private final ViewPort viewPort;

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------
	/**
	 * Create an event for the specified view port and dragging state.
	 * 
	 * @param viewPort the view port
	 * @param dragging if true, this event is caused by a dragging action
	 */
	public ViewPortTranslatingEvent(ViewPort viewPort) {
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
	 * Get the view port that has translated.
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
		handler.onViewPortTranslating(this);
	}
}