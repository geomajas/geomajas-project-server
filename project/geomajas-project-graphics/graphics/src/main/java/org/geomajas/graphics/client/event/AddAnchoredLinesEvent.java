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

import java.util.List;

import org.geomajas.graphics.client.object.GraphicsObject;
import org.geomajas.graphics.client.shape.CoordinatePath;

import com.google.web.bindery.event.shared.Event;

/**
 * Is thrown with all the Coordinate paths of all the objects that are anchored to the object.
 * 
 * @author Jan Venstermans
 * 
 */
public class AddAnchoredLinesEvent extends Event<AddAnchoredLinesEvent.Handler> {

	private GraphicsObject object;
	
	private List<CoordinatePath> coordinatePaths;
	
	public AddAnchoredLinesEvent(GraphicsObject object, List<CoordinatePath> coordinatePaths) {
		this.object = object;
		this.coordinatePaths = coordinatePaths;
	}

	public GraphicsObject getObject() {
		return object;
	}
	
	public List<CoordinatePath> getCoordinatePaths() {
		return coordinatePaths;
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
		void onAction(AddAnchoredLinesEvent event);

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