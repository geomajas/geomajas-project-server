package org.geomajas.puregwt.client;

import org.geomajas.puregwt.client.event.EventBus;
import org.geomajas.puregwt.client.event.EventBusImpl;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

public class GeomajasPureModule extends AbstractGinModule {

	protected void configure() {
		bind(EventBus.class).to(EventBusImpl.class).in(Singleton.class);
	}

}
