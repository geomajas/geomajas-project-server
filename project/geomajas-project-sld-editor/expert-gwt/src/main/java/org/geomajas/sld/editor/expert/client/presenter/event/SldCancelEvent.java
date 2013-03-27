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
 * Event fired when the user clicks the cancel button.
 * 
 * @author Kristof Heirwegh
 * 
 */
public class SldCancelEvent extends GwtEvent<SldCancelEvent.SldCancelHandler> {

	public SldCancelEvent() {
	}

	public static void fire(HasHandlers source) {
		SldCancelEvent eventInstance = new SldCancelEvent();
		source.fireEvent(eventInstance);
	}

	public static void fire(HasHandlers source, SldCancelEvent eventInstance) {
		source.fireEvent(eventInstance);
	}

	/**
	 * {@link HasHandlers} indicator for this event.
	 * 
	 * @author Kristof Heirwegh
	 * 
	 */
	public interface HasSldCancelHandlers extends HasHandlers {

		HandlerRegistration addSldCancelHandler(SldCancelHandler handler);
	}

	/**
	 * {@link EventHandler} interface for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface SldCancelHandler extends EventHandler {

		/**
		 * Notifies side content child presenter to reveal itself.
		 * 
		 * @param event the event
		 */
		void onSldCancel(SldCancelEvent event);
	}

	private static final Type<SldCancelHandler> TYPE = new Type<SldCancelHandler>();

	public static Type<SldCancelHandler> getType() {
		return TYPE;
	}

	@Override
	public Type<SldCancelHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SldCancelHandler handler) {
		handler.onSldCancel(this);
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
		return "SldCancelEvent[" + "]";
	}
}