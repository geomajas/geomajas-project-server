package org.geomajas.puregwt.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerRegistration;

public interface EventBus {

	<H extends EventHandler> HandlerRegistration addHandler(Type<H> type, H handler);

	<H extends EventHandler> HandlerRegistration addHandlerToSource(Type<H> type, Object source, H handler);

	void fireEvent(GwtEvent<?> event);

	void fireEventFromSource(GwtEvent<?> event, Object source);

}
