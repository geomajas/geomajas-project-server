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
package org.geomajas.sld.editor.common.client.model;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * Event that indicates that the list of SLD's has changed.
 * 
 * @author Jan De Moerloose
 * 
 */
public class SldListChangedEvent extends GwtEvent<SldListChangedEvent.SldListChangedHandler> {

	public SldListChangedEvent() {
		// Possibly for serialization.
	}

	public static void fire(HasHandlers source) {
		SldListChangedEvent eventInstance = new SldListChangedEvent();
		source.fireEvent(eventInstance);
	}

	public static void fire(HasHandlers source, SldListChangedEvent eventInstance) {
		source.fireEvent(eventInstance);
	}

	/**
	 * {@link HasHandlers} indicator for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface HasSldListChangedHandlers extends HasHandlers {

		HandlerRegistration addSldListChangedHandler(SldListChangedHandler handler);
	}

	/**
	 * {@link EventHandler} interface for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface SldListChangedHandler extends EventHandler {

		/**
		 * Called when the SLD list has changed.
		 * 
		 * @param event event
		 */
		void onSldListChanged(SldListChangedEvent event);
	}

	private static final Type<SldListChangedHandler> TYPE = new Type<SldListChangedHandler>();

	public static Type<SldListChangedHandler> getType() {
		return TYPE;
	}

	@Override
	public Type<SldListChangedHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SldListChangedHandler handler) {
		handler.onSldListChanged(this);
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public String toString() {
		return "SldListChangedEvent[" + "]";
	}
}