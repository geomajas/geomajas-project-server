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
package org.geomajas.sld.client.presenter;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * 
 * @author Jan De Moerloose
 * 
 */
public class InitLayoutEvent extends GwtEvent<InitLayoutEvent.InitLayoutHandler> {

	public InitLayoutEvent() {
		// Possibly for serialization.
	}

	public static void fire(HasHandlers source) {
		InitLayoutEvent eventInstance = new InitLayoutEvent();
		source.fireEvent(eventInstance);
	}

	public static void fire(HasHandlers source, InitLayoutEvent eventInstance) {
		source.fireEvent(eventInstance);
	}

	/**
	 * {@link HasHandlers} indicator for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface HasRevealSideContentHandlers extends HasHandlers {

		HandlerRegistration addRevealSideContentHandler(InitLayoutHandler handler);
	}

	/**
	 * {@link EventHandler} interface for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface InitLayoutHandler extends EventHandler {

		/**
		 * Notifies side content child presenter to reveal itself.
		 * 
		 * @param event the event
		 */
		void onInitLayout(InitLayoutEvent event);
	}

	private static final Type<InitLayoutHandler> TYPE = new Type<InitLayoutHandler>();

	public static Type<InitLayoutHandler> getType() {
		return TYPE;
	}

	@Override
	public Type<InitLayoutHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(InitLayoutHandler handler) {
		handler.onInitLayout(this);
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