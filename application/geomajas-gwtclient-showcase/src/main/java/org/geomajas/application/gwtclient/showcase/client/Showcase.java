package org.geomajas.application.gwtclient.showcase.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import org.geomajas.puregwt.example.base.client.ExampleBase;

/**
 * Entry point of Geomajas GWT showcase application.
 *
 * @author Dosi Bingov
 */
public class Showcase implements EntryPoint {

	public void onModuleLoad() {
		RootLayoutPanel.get().add(ExampleBase.getLayout());
	}
}
