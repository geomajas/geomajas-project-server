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
 * Event fired when the user wants to popup the creation dialog.
 * 
 * @author Jan De Moerloose
 * 
 */
public class SldListPopupNewEvent extends GwtEvent<SldListPopupNewEvent.SldListPopupNewHandler> {

	public SldListPopupNewEvent() {
		// Possibly for serialization.
	}

	public static void fire(HasHandlers source) {
		SldListPopupNewEvent eventInstance = new SldListPopupNewEvent();
		source.fireEvent(eventInstance);
	}

	public static void fire(HasHandlers source, SldListPopupNewEvent eventInstance) {
		source.fireEvent(eventInstance);
	}

	/**
	 * {@link HasHandlers} indicator for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface HasSldListPopupNewHandlers extends HasHandlers {

		HandlerRegistration addSldListPopupNewHandler(SldListPopupNewHandler handler);
	}

	/**
	 * {@link EventHandler} interface for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface SldListPopupNewHandler extends EventHandler {

		/**
		 * Notifies side content child presenter to reveal itself.
		 * 
		 * @param event the event
		 */
		void onPopupNewList(SldListPopupNewEvent event);
	}

	private static final Type<SldListPopupNewHandler> TYPE = new Type<SldListPopupNewHandler>();

	public static Type<SldListPopupNewHandler> getType() {
		return TYPE;
	}

	@Override
	public Type<SldListPopupNewHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SldListPopupNewHandler handler) {
		handler.onPopupNewList(this);
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
		return "SldListPopupNewEvent[" + "]";
	}
}