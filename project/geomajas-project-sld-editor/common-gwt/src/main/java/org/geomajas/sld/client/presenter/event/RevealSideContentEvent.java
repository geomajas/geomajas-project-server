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
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

/**
 * 
 * @author Jan De Moerloose
 * 
 */
public class RevealSideContentEvent extends GwtEvent<RevealSideContentEvent.RevealSideContentHandler> {

	public RevealSideContentEvent() {
		// Possibly for serialization.
	}

	public static void fire(HasHandlers source) {
		RevealSideContentEvent eventInstance = new RevealSideContentEvent();
		source.fireEvent(eventInstance);
	}

	public static void fire(HasHandlers source, RevealSideContentEvent eventInstance) {
		source.fireEvent(eventInstance);
	}

	/**
	 * {@link HasHandlers} indicator for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface HasRevealSideContentHandlers extends HasHandlers {

		HandlerRegistration addRevealSideContentHandler(RevealSideContentHandler handler);
	}

	/**
	 * {@link EventHandler} interface for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface RevealSideContentHandler extends EventHandler {

		/**
		 * Notifies side content child presenter to reveal itself.
		 * 
		 * @param event the event
		 */
		void onRevealSideContent(RevealSideContentEvent event);
	}

	private static final Type<RevealSideContentHandler> TYPE = new Type<RevealSideContentHandler>();

	public static Type<RevealSideContentHandler> getType() {
		return TYPE;
	}

	@Override
	public Type<RevealSideContentHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(RevealSideContentHandler handler) {
		handler.onRevealSideContent(this);
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