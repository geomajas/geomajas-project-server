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
package org.geomajas.sld.editor.common.client.presenter.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * Event fired when the user wants to select an SLD.
 * 
 * @author Jan De Moerloose
 * 
 */
public class SldListSelectEvent extends GwtEvent<SldListSelectEvent.SldListSelectHandler> {

	private String name;

	public SldListSelectEvent(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static void fire(HasHandlers source, String name) {
		SldListSelectEvent eventInstance = new SldListSelectEvent(name);
		source.fireEvent(eventInstance);
	}

	public static void fire(HasHandlers source, SldListSelectEvent eventInstance) {
		source.fireEvent(eventInstance);
	}

	/**
	 * {@link HasHandlers} indicator for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface HasSldListSelectHandlers extends HasHandlers {

		HandlerRegistration addSldListSelectHandler(SldListSelectHandler handler);
	}

	/**
	 * {@link EventHandler} interface for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface SldListSelectHandler extends EventHandler {

		/**
		 * Notifies side content child presenter to reveal itself.
		 * 
		 * @param event the event
		 */
		void onSldListSelect(SldListSelectEvent event);
	}

	private static final Type<SldListSelectHandler> TYPE = new Type<SldListSelectHandler>();

	public static Type<SldListSelectHandler> getType() {
		return TYPE;
	}

	@Override
	public Type<SldListSelectHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SldListSelectHandler handler) {
		handler.onSldListSelect(this);
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
		return "SldListSelectEvent[" + "]";
	}
}