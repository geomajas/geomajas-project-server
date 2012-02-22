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
package org.geomajas.sld.editor.common.client.model.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * Fired when the SLD has been changed.
 * 
 * @author An Buyle
 * 
 */
public class SldChangedEvent extends GwtEvent<SldChangedEvent.SldChangedHandler> {

	public SldChangedEvent() {
	}

	/**
	 * @param source
	 * 
	 */
	public static void fire(HasHandlers source) {
		SldChangedEvent eventInstance = new SldChangedEvent();
		source.fireEvent(eventInstance);
	}

	/**
	 * {@link HasHandlers} indicator for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface HasSldChangedHandlers extends HasHandlers {

		HandlerRegistration addSldChangedHandler(SldChangedHandler handler);
	}

	/**
	 * {@link EventHandler} interface for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface SldChangedHandler extends EventHandler {

		/**
		 * Notifies content changed.
		 * 
		 * @param event the event
		 */
		void onChanged(SldChangedEvent event);

	}

	/**
	 * Empty implementation of all handler methods.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public abstract static class SldChangedAdapter implements SldChangedHandler {

		public void onChanged(SldChangedEvent event) {
		}

	}

	private static final Type<SldChangedHandler> TYPE = new Type<SldChangedHandler>();

	public static Type<SldChangedHandler> getType() {
		return TYPE;
	}

	@Override
	public Type<SldChangedHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SldChangedHandler handler) {
		handler.onChanged(this);
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
		return "SldChangedEvent[" + "]";
	}

}