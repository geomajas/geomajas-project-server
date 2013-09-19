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

import org.geomajas.graphics.client.operation.GraphicsOperation;

import com.google.web.bindery.event.shared.Event;

/**
 * Event to signal a graphics operation.
 * 
 * @author Jan De Moerloose
 * 
 */
public class GraphicsOperationEvent extends Event<GraphicsOperationEvent.Handler> {
	
	private GraphicsOperation operation;

	public GraphicsOperationEvent(GraphicsOperation operation) {
		this.operation = operation;
	}

	/**
	 * Handler for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface Handler {

		/**
		 * Notifies operation on graphics.
		 * 
		 * @param event the event
		 */
		void onOperation(GraphicsOperationEvent event);

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
		handler.onOperation(this);
	}

	
	public GraphicsOperation getOperation() {
		return operation;
	}

}