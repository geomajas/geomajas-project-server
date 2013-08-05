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
package org.geomajas.smartgwt.client.widget.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event to announce that the graphics context is ready for drawing. This event is necessary because drawing can only
 * take place when the graphics context is attached to the DOM. On startup and during resizing the graphics context is
 * detached. Users should ensure that their content is correctly rendered with respect to the new size by reacting to
 * this event.
 * 
 * @author Jan De Moerloose
 * 
 */
public class GraphicsReadyEvent extends GwtEvent<GraphicsReadyHandler> {

	public static final Type<GraphicsReadyHandler> TYPE = new Type<GraphicsReadyHandler>();

	@SuppressWarnings("unchecked")
	public Type getAssociatedType() {
		return TYPE;
	}

	protected void dispatch(GraphicsReadyHandler handler) {
		handler.onReady(this);
	}
}