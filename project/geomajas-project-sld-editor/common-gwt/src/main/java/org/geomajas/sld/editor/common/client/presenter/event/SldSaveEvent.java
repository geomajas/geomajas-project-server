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
 * Event fired when the user clicks the save button.
 * 
 * @author Jan De Moerloose
 * 
 */
public class SldSaveEvent extends GwtEvent<SldSaveEvent.SldSaveHandler> {

	public SldSaveEvent() {
	}

	public static void fire(HasHandlers source) {
		SldSaveEvent eventInstance = new SldSaveEvent();
		source.fireEvent(eventInstance);
	}

	public static void fire(HasHandlers source, SldSaveEvent eventInstance) {
		source.fireEvent(eventInstance);
	}

	/**
	 * {@link HasHandlers} indicator for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface HasSldSaveHandlers extends HasHandlers {

		HandlerRegistration addSldSaveHandler(SldSaveHandler handler);
	}

	/**
	 * {@link EventHandler} interface for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface SldSaveHandler extends EventHandler {

		/**
		 * Notifies side content child presenter to reveal itself.
		 * 
		 * @param event the event
		 */
		void onSldSave(SldSaveEvent event);
	}

	private static final Type<SldSaveHandler> TYPE = new Type<SldSaveHandler>();

	public static Type<SldSaveHandler> getType() {
		return TYPE;
	}

	@Override
	public Type<SldSaveHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SldSaveHandler handler) {
		handler.onSldSave(this);
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
		return "SldSaveEvent[" + "]";
	}
}