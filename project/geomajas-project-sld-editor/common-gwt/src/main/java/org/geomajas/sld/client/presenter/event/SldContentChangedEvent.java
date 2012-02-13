/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.sld.client.presenter.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class SldContentChangedEvent  extends GwtEvent<SldContentChangedEvent.SldContentChangedHandler> {

	public SldContentChangedEvent() {
		// Possibly for serialization.
	}

	public static void fire(HasHandlers source) {
		SldContentChangedEvent eventInstance = new SldContentChangedEvent();
		source.fireEvent(eventInstance);
	}

	public static void fire(HasHandlers source, SldContentChangedEvent eventInstance) {
		source.fireEvent(eventInstance);
	}

	/**
	 * {@link HasHandlers} indicator for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface HasSldContentChangedHandlers extends HasHandlers {

		HandlerRegistration addSldContentChangedHandler(SldContentChangedHandler handler);
	}

	/**
	 * {@link EventHandler} interface for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface SldContentChangedHandler extends EventHandler {

		/**
		 * Notifies side content child presenter to reveal itself.
		 * 
		 * @param event the event
		 */
		void onPopupNewList(SldContentChangedEvent event);
	}

	private static final Type<SldContentChangedHandler> TYPE = new Type<SldContentChangedHandler>();

	public static Type<SldContentChangedHandler> getType() {
		return TYPE;
	}

	@Override
	public Type<SldContentChangedHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SldContentChangedHandler handler) {
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
		return "SldContentChangedEvent[" + "]";
	}
}