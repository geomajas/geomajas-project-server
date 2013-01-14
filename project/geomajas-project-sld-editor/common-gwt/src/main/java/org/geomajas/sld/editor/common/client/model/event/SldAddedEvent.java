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
package org.geomajas.sld.editor.common.client.model.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * Event fired when an SLD has been added to the server.
 * 
 * @author Jan De Moerloose
 * 
 */
public class SldAddedEvent extends GwtEvent<SldAddedEvent.SldAddedHandler> {
	
	private String sldName;

	public SldAddedEvent(String sldName) {
		this.sldName = sldName;
	}

	public String getSldName() {
		return this.sldName;
	}

	public static void fire(HasHandlers source, String sldName) {
		SldAddedEvent eventInstance = new SldAddedEvent(sldName);
		source.fireEvent(eventInstance);
	}

	public static void fire(HasHandlers source, SldAddedEvent eventInstance) {
		source.fireEvent(eventInstance);
	}

	/**
	 * {@link HasHandlers} indicator for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface HasSldAddedHandlers extends HasHandlers {

		HandlerRegistration addSldAddedHandler(SldAddedHandler handler);
	}

	/**
	 * {@link EventHandler} interface for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface SldAddedHandler extends EventHandler {

		/**
		 * Notifies side content child presenter to reveal itself.
		 * 
		 * @param event the event
		 */
		void onSldAdded(SldAddedEvent event);
	}

	private static final Type<SldAddedHandler> TYPE = new Type<SldAddedHandler>();

	public static Type<SldAddedHandler> getType() {
		return TYPE;
	}

	@Override
	public Type<SldAddedHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SldAddedHandler handler) {
		handler.onSldAdded(this);
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
		return "SldAddedEvent[" + "]";
	}
}