package org.geomajas.puregwt.client;

import org.geomajas.puregwt.client.widget.MapWidgetImpl;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules({ GeomajasPureModule.class, PureGwtExampleModule.class })
public interface PureGwtExampleGinjector extends Ginjector {

	MapWidgetImpl getMap();
}