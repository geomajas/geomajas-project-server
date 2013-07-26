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
package org.geomajas.sld.editor.expert.client.presenter.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * Event fired when the cancel event was successful.
 * 
 * @author Kristof Heirwegh
 * 
 */
public class SldCancelledEvent extends GwtEvent<SldCancelledEvent.SldCancelledHandler> {

	public SldCancelledEvent() {
	}

	public static void fire(HasHandlers source) {
		SldCancelledEvent eventInstance = new SldCancelledEvent();
		source.fireEvent(eventInstance);
	}

	public static void fire(HasHandlers source, SldCancelledEvent eventInstance) {
		source.fireEvent(eventInstance);
	}

	/**
	 * {@link HasHandlers} indicator for this event.
	 * 
	 * @author Kristof Heirwegh
	 * 
	 */
	public interface HasSldCancelledHandlers extends HasHandlers {

		HandlerRegistration addSldCancelledHandler(SldCancelledHandler handler);
	}

	/**
	 * {@link EventHandler} interface for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface SldCancelledHandler extends EventHandler {

		/**
		 * @param event the event
		 */
		void onSldCancelled(SldCancelledEvent event);
	}

	private static final Type<SldCancelledHandler> TYPE = new Type<SldCancelledHandler>();

	public static Type<SldCancelledHandler> getType() {
		return TYPE;
	}

	@Override
	public Type<SldCancelledHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SldCancelledHandler handler) {
		handler.onSldCancelled(this);
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
		return "SldCancelledEvent[" + "]";
	}
}