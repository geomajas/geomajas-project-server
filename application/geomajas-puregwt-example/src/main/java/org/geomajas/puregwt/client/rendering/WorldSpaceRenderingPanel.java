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

package org.geomajas.puregwt.client.rendering;

import org.geomajas.puregwt.client.ContentPanel;
import org.geomajas.puregwt.client.event.MapInitializationEvent;
import org.geomajas.puregwt.client.event.MapInitializationHandler;
import org.geomajas.puregwt.client.gfx.VectorContainer;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.vaadin.gwtgraphics.client.shape.Circle;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * ContentPanel that demonstrates rendering abilities in world space.
 * 
 * @author Pieter De Graef
 */
public class WorldSpaceRenderingPanel extends ContentPanel {

	private VectorContainer container;

	public WorldSpaceRenderingPanel(MapPresenter mapPresenter) {
		super(mapPresenter);
	}

	public String getTitle() {
		return "Drawing in world space";
	}

	public String getDescription() {
		return "This example shows the vector object drawing capabilities of the Geomajas map. In this particular "
				+ "example, all objects are rendered in world space. For more information regarding screen space and "
				+ "world space, visit the javadocs (TODO make this a link).<br/>Try navigating the map to see the "
				+ "difference with screen space.";
	}

	public Widget getContentWidget() {
		// Define the left layout:
		VerticalPanel leftLayout = new VerticalPanel();
		leftLayout.setSize("220px", "100%");

		leftLayout.add(new HTML("<h3>Drawing options:</h3>"));

		Button circleBtn = new Button("Draw circle");
		circleBtn.setWidth("200");
		circleBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Circle circle = new Circle(0, 0, 3000000);
				circle.setFillColor("#66CC66");
				circle.setFillOpacity(0.4);
				container.add(circle);
			}
		});
		leftLayout.add(circleBtn);

		Button rectangleBtn = new Button("Draw rectangle");
		rectangleBtn.setWidth("200");
		rectangleBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Rectangle rectangle = new Rectangle(1000000, 1000000, 2000000, 1000000);
				rectangle.setFillColor("#CC9900");
				rectangle.setFillOpacity(0.4);
				container.add(rectangle);
			}
		});
		leftLayout.add(rectangleBtn);

		Button deleteBtn = new Button("Delete all drawings");
		deleteBtn.setWidth("200");
		deleteBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent arg0) {
				container.clear();
			}
		});
		leftLayout.add(deleteBtn);

		// Create the MapPresenter and add an InitializationHandler:
		mapPresenter.setSize(640, 480);
		mapPresenter.getEventBus().addMapInitializationHandler(new MyMapInitializationHandler());

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
	 * Map initialization handler that adds a CheckBox to the layout for every layer. With these CheckBoxes, the user
	 * can toggle the layer's visibility.
	 * 
	 * @author Pieter De Graef
	 */
	private class MyMapInitializationHandler implements MapInitializationHandler {

		public void onMapInitialized(MapInitializationEvent event) {
			container = mapPresenter.addWorldContainer();
		}
	}
}