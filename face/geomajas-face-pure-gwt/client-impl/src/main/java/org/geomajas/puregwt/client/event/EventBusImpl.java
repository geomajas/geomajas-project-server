package org.geomajas.puregwt.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;


public class EventBusImpl implements EventBus {

	private SimpleEventBus eventBus = new SimpleEventBus();

	public <H extends EventHandler> HandlerRegistration addHandler(Type<H> type, H handler) {
		return eventBus.addHandler(type, handler);
	}

	public <H extends EventHandler> HandlerRegistration addHandlerToSource(Type<H> type, Object source, H handler) {
		return eventBus.addHandlerToSource(type, source, handler);
	}

	public void fireEvent(GwtEvent<?> event) {
		eventBus.fireEvent(event);
	}

	public void fireEventFromSource(GwtEvent<?> event, Object source) {
		eventBus.fireEventFromSource(event, source);
	}
	
	
}
