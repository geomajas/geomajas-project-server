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

package org.geomajas.puregwt.client.general;

import org.geomajas.puregwt.client.ContentPanel;
import org.geomajas.puregwt.client.event.MapInitializationEvent;
import org.geomajas.puregwt.client.event.MapInitializationHandler;
import org.geomajas.puregwt.client.map.MapPresenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * ContentPanel that demonstrates rendering abilities in world space with a map that supports resizing.
 * 
 * @author Pieter De Graef
 */
public class ResizeMapPanel extends ContentPanel {

	private MapPresenter mapPresenter;

	public String getTitle() {
		return "Map resize";
	}

	public String getDescription() {
		return "This example shows map resizing capabilities.";
	}

	public Widget getContentWidget() {
		// Define the left layout:
		VerticalPanel leftLayout = new VerticalPanel();
		leftLayout.setSize("220px", "100%");

		leftLayout.add(new HTML("<h3>Resize the map:</h3>"));

		Button resizeBtn = new Button("Enlarge map");
		resizeBtn.setWidth("200");
		resizeBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent arg0) {
				int width = mapPresenter.getViewPort().getMapWidth() + 20;
				int height = mapPresenter.getViewPort().getMapHeight() + 15;
				mapPresenter.setSize(width, height);
			}
		});
		leftLayout.add(resizeBtn);

		Button shrinkBtn = new Button("Shrink map");
		shrinkBtn.setWidth("200");
		shrinkBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent arg0) {
				int width = mapPresenter.getViewPort().getMapWidth() - 20;
				int height = mapPresenter.getViewPort().getMapHeight() - 15;
				mapPresenter.setSize(width, height);
			}
		});
		leftLayout.add(shrinkBtn);

		// Create the MapPresenter and add an InitializationHandler:
		mapPresenter = getInjector().getMapPresenter();
		mapPresenter.setSize(640, 480);
		mapPresenter.getEventBus().addMapInitializationHandler(new MyMapInitializationHandler());

		// Define the whole layout:
		HorizontalPanel layout = new HorizontalPanel();
		layout.add(leftLayout);
		DecoratorPanel mapDecorator = new DecoratorPanel();
		mapDecorator.add(mapPresenter.asWidget());
		layout.add(mapDecorator);

		// Initialize the map, and return the layout:
		mapPresenter.initialize("pure-gwt-app", "mapOsm");
		return layout;
	}

	/**
	 * Map initialization handler that zooms in.
	 * 
	 * @author Pieter De Graef
	 */
	private class MyMapInitializationHandler implements MapInitializationHandler {

		public void onMapInitialized(MapInitializationEvent event) {
			double scale = mapPresenter.getViewPort().getScale() * 4;
			mapPresenter.getViewPort().applyScale(scale);
		}
	}
}