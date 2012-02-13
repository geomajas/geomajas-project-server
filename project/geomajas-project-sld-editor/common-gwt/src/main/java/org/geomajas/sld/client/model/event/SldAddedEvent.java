package org.geomajas.sld.client.model.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;


public class SldAddedEvent  extends GwtEvent<SldAddedEvent.SldAddedHandler> {

	public SldAddedEvent() {
		// Possibly for serialization.
	}

	public static void fire(HasHandlers source) {
		SldAddedEvent eventInstance = new SldAddedEvent();
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