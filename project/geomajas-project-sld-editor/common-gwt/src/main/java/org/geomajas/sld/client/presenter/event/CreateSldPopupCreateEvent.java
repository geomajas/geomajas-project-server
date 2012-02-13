package org.geomajas.sld.client.presenter.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;


public class CreateSldPopupCreateEvent extends GwtEvent<CreateSldPopupCreateEvent.CreateSldPopupCreateHandler> {

	public CreateSldPopupCreateEvent() {
		// Possibly for serialization.
	}

	public static void fire(HasHandlers source) {
		CreateSldPopupCreateEvent eventInstance = new CreateSldPopupCreateEvent();
		source.fireEvent(eventInstance);
	}

	public static void fire(HasHandlers source, CreateSldPopupCreateEvent eventInstance) {
		source.fireEvent(eventInstance);
	}

	/**
	 * {@link HasHandlers} indicator for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface HasCreateSldPopupCreateHandlers extends HasHandlers {

		HandlerRegistration addCreateSldPopupCreateHandler(CreateSldPopupCreateHandler handler);
	}

	/**
	 * {@link EventHandler} interface for this event.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public interface CreateSldPopupCreateHandler extends EventHandler {

		/**
		 * Notifies side content child presenter to reveal itself.
		 * 
		 * @param event the event
		 */
		void onCreateSldPopupCreate(CreateSldPopupCreateEvent event);
	}

	private static final Type<CreateSldPopupCreateHandler> TYPE = new Type<CreateSldPopupCreateHandler>();

	public static Type<CreateSldPopupCreateHandler> getType() {
		return TYPE;
	}

	@Override
	public Type<CreateSldPopupCreateHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(CreateSldPopupCreateHandler handler) {
		handler.onCreateSldPopupCreate(this);
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
		return "CreateSldPopupCreateEvent[" + "]";
	}
}
