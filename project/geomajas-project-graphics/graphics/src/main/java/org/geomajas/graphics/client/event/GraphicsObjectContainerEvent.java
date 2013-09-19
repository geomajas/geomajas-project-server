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
 * Event to signal adding/removing/updating graphics objects in the container.
 * 
 * @author Jan De Moerloose
 * 
 */
public class GraphicsObjectContainerEvent extends Event<GraphicsObjectContainerEvent.Handler> {

	/**
	 * CRUD Action type.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public enum ActionType {
		ADD, REMOVE, UPDATE, CLEAR
	}

	private GraphicsObject object;

	private ActionType actionType;


	public GraphicsObjectContainerEvent(ActionType actionType) {
		this.actionType = actionType;
	}
	
	public GraphicsObjectContainerEvent(GraphicsObject object, ActionType actionType) {
		this.object = object;
		this.actionType = actionType;
	}

	public GraphicsObject getObject() {
		return object;
	}

	public ActionType getActionType() {
		return actionType;
	}

	/**
	 * Handler for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface Handler {

		/**
		 * Notifies action on graphics container.
		 * 
		 * @param event the event
		 */
		void onAction(GraphicsObjectContainerEvent event);

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
		handler.onAction(this);
	}

}