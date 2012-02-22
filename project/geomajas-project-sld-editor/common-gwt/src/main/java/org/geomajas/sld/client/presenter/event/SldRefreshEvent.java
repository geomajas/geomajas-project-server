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
 * Event fired when the user accepts to cancel all changes and refresh the current SLD.
 * 
 * @author Jan De Moerloose
 * 
 */
public class SldRefreshEvent extends GwtEvent<SldRefreshEvent.SldRefreshHandler> {

	public SldRefreshEvent() {
	}

	public static void fire(HasHandlers source) {
		SldRefreshEvent eventInstance = new SldRefreshEvent();
		source.fireEvent(eventInstance);
	}

	public static void fire(HasHandlers source, SldRefreshEvent eventInstance) {
		source.fireEvent(eventInstance);
	}

	/**
	 * {@link HasHandlers} indicator for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface HasSldRefreshHandlers extends HasHandlers {

		HandlerRegistration addSldRefreshHandler(SldRefreshHandler handler);
	}

	/**
	 * {@link EventHandler} interface for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface SldRefreshHandler extends EventHandler {

		/**
		 * Notifies side content child presenter to reveal itself.
		 * 
		 * @param event the event
		 */
		void onSldRefresh(SldRefreshEvent event);
	}

	private static final Type<SldRefreshHandler> TYPE = new Type<SldRefreshHandler>();

	public static Type<SldRefreshHandler> getType() {
		return TYPE;
	}

	@Override
	public Type<SldRefreshHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SldRefreshHandler handler) {
		handler.onSldRefresh(this);
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
		return "SldRefreshEvent[" + "]";
	}
}