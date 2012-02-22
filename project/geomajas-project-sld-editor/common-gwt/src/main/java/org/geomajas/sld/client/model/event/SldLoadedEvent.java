/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.sld.client.model.event;

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
public class SldLoadedEvent extends GwtEvent<SldLoadedEvent.SldLoadedHandler> {

	public SldLoadedEvent() {
		// Possibly for serialization.
	}

	public static void fire(HasHandlers source) {
		SldLoadedEvent eventInstance = new SldLoadedEvent();
		source.fireEvent(eventInstance);
	}

	public static void fire(HasHandlers source, SldLoadedEvent eventInstance) {
		source.fireEvent(eventInstance);
	}

	/**
	 * {@link HasHandlers} indicator for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface HasSldLoadedHandlers extends HasHandlers {

		HandlerRegistration addSldLoadedHandler(SldLoadedHandler handler);
	}

	/**
	 * {@link EventHandler} interface for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface SldLoadedHandler extends EventHandler {

		/**
		 * Called when the SLD list has changed.
		 * 
		 * @param event event
		 */
		void onSldLoaded(SldLoadedEvent event);
	}

	private static final Type<SldLoadedHandler> TYPE = new Type<SldLoadedHandler>();

	public static Type<SldLoadedHandler> getType() {
		return TYPE;
	}

	@Override
	public Type<SldLoadedHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SldLoadedHandler handler) {
		handler.onSldLoaded(this);
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
		return "SldLoadedEvent[" + "]";
	}
}