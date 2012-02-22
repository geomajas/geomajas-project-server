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
package org.geomajas.sld.client.presenter.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * 
 * @author Jan De Moerloose
 * 
 */
public class InitSldLayoutEvent extends GwtEvent<InitSldLayoutEvent.InitSldLayoutHandler> {

	public InitSldLayoutEvent() {
		// Possibly for serialization.
	}

	public static void fire(HasHandlers source) {
		InitSldLayoutEvent eventInstance = new InitSldLayoutEvent();
		source.fireEvent(eventInstance);
	}

	public static void fire(HasHandlers source, InitSldLayoutEvent eventInstance) {
		source.fireEvent(eventInstance);
	}

	/**
	 * {@link HasHandlers} indicator for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface HasRevealSideContentHandlers extends HasHandlers {

		HandlerRegistration addRevealSideContentHandler(InitSldLayoutHandler handler);
	}

	/**
	 * {@link EventHandler} interface for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface InitSldLayoutHandler extends EventHandler {

		/**
		 * Notifies side content child presenter to reveal itself.
		 * 
		 * @param event the event
		 */
		void onInitSldLayout(InitSldLayoutEvent event);
	}

	private static final Type<InitSldLayoutHandler> TYPE = new Type<InitSldLayoutHandler>();

	public static Type<InitSldLayoutHandler> getType() {
		return TYPE;
	}

	@Override
	public Type<InitSldLayoutHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(InitSldLayoutHandler handler) {
		handler.onInitSldLayout(this);
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
		return "RevealSideContentEvent[" + "]";
	}
}