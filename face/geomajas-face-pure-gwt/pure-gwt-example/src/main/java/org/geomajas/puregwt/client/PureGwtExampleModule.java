package org.geomajas.puregwt.client;

import org.geomajas.puregwt.client.widget.MapWidgetImpl;

import com.google.gwt.inject.client.AbstractGinModule;


public class PureGwtExampleModule extends AbstractGinModule {

	protected void configure() {
		bind(MapWidgetImpl.class);
	}

}
