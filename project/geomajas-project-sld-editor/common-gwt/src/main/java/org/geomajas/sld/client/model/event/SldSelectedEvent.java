package org.geomajas.sld.client.model.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;


public class SldSelectedEvent extends GwtEvent<SldSelectedEvent.SldSelectedHandler> {

	public SldSelectedEvent() {
		// Possibly for serialization.
	}

	public static void fire(HasHandlers source) {
		SldSelectedEvent eventInstance = new SldSelectedEvent();
		source.fireEvent(eventInstance);
	}

	public static void fire(HasHandlers source, SldSelectedEvent eventInstance) {
		source.fireEvent(eventInstance);
	}

	/**
	 * {@link HasHandlers} indicator for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface HasSldSelectedHandlers extends HasHandlers {

		HandlerRegistration addSldSelectedHandler(SldSelectedHandler handler);
	}

	/**
	 * {@link EventHandler} interface for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface SldSelectedHandler extends EventHandler {

		/**
		 * Called when the SLD list has changed.
		 * 
		 * @param event event
		 */
		void onSldSelected(SldSelectedEvent event);
	}

	private static final Type<SldSelectedHandler> TYPE = new Type<SldSelectedHandler>();

	public static Type<SldSelectedHandler> getType() {
		return TYPE;
	}

	@Override
	public Type<SldSelectedHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SldSelectedHandler handler) {
		handler.onSldSelected(this);
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
		return "SldSelectedEvent[" + "]";
	}
}