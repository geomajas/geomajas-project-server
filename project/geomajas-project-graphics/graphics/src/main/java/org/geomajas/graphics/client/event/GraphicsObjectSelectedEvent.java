/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.graphics.client.event;

import org.geomajas.graphics.client.object.GraphicsObject;

import com.google.web.bindery.event.shared.Event;

/**
 * Event to signal a graphics object selection.
 * 
 * @author Jan De Moerloose
 * 
 */
public class GraphicsObjectSelectedEvent extends Event<GraphicsObjectSelectedEvent.Handler> {

	private GraphicsObject object;

	private boolean selected;

	public GraphicsObjectSelectedEvent(GraphicsObject object, boolean selected) {
		this.object = object;
		this.selected = selected;
	}

	/**
	 * Get the selected object.
	 * @return the object or null if selected = false
	 */
	public GraphicsObject getObject() {
		return object;
	}

	public boolean isSelected() {
		return selected;
	}

	/**
	 * Handler for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface Handler {

		/**
		 * Notifies selection on graphics object.
		 * 
		 * @param event the event
		 */
		void onSelected(GraphicsObjectSelectedEvent event);

	}

	private static final Type<Handler> TYPE = new Type<Handler>();

	public static Type<Handler> getType() {
		return TYPE;
	}

	@Override
	public Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onSelected(this);
	}

}