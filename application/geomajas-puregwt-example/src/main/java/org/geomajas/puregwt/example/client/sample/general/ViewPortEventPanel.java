/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.puregwt.example.client.sample.general;

import org.geomajas.puregwt.client.event.ViewPortChangedEvent;
import org.geomajas.puregwt.client.event.ViewPortChangedHandler;
import org.geomajas.puregwt.client.event.ViewPortScaledEvent;
import org.geomajas.puregwt.client.event.ViewPortTranslatedEvent;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.example.client.ContentPanel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * ContentPanel that demonstrates view port events.
 * 
 * @author Jan De Moerloose
 */
public class ViewPortEventPanel extends ContentPanel {

	private VerticalPanel vpEventLayout;

	public ViewPortEventPanel(MapPresenter mapPresenter) {
		super(mapPresenter);
	}

	public String getTitle() {
		return "ViewPort Events";
	}

	public String getDescription() {
		return "This example shows the events that accompany the changes to the view port or visible"
				+ " area of the map.<br/>Events are fired when the view port is translated (panning),"
				+ " scaled (zooming) or changed in a non-trivial way (e.g resize)";
	}


	public Widget getContentWidget() {
		// Define the left layout:
		VerticalPanel leftLayout = new VerticalPanel();
		leftLayout.setSize("220px", "100%");

		leftLayout.add(new HTML("<h3>Events:</h3>"));
		Button clearBtn = new Button("Clear events");
		clearBtn.setWidth("200");
		clearBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent arg0) {
				vpEventLayout.clear();
			}
		});
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
		leftLayout.add(clearBtn);
		vpEventLayout = new VerticalPanel();
		leftLayout.add(vpEventLayout);

		// Create the MapPresenter and add an InitializationHandler:
		mapPresenter.setSize(640, 480);
		mapPresenter.getEventBus().addViewPortChangedHandler(new MyViewPortChangedHandler());

		// Define the whole layout:
		HorizontalPanel layout = new HorizontalPanel();
		layout.add(leftLayout);
		DecoratorPanel mapDecorator = new DecoratorPanel();
		mapDecorator.add(mapPresenter.asWidget());
		layout.add(mapDecorator);

		// Initialize the map, and return the layout:
		mapPresenter.initialize("puregwt-app", "mapOsm");
		return layout;
	}

	/**
	 * Handler that catches view port events.
	 * 
	 * @author Jan De Moerloose
	 */
	private class MyViewPortChangedHandler implements ViewPortChangedHandler {

		@Override
		public void onViewPortChanged(ViewPortChangedEvent event) {
			vpEventLayout.add(new Label("onViewPortChanged: " + event.getViewPort().getBounds()));
		}

		@Override
		public void onViewPortScaled(ViewPortScaledEvent event) {
			vpEventLayout.add(new Label("onViewPortScaled: " + event.getViewPort().getBounds()));
		}

		@Override
		public void onViewPortTranslated(ViewPortTranslatedEvent event) {
			vpEventLayout.add(new Label("onViewPortTranslated: " + event.getViewPort().getBounds()));
		}
	}
}