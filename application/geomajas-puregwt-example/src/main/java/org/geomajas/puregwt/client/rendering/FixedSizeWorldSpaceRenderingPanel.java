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

package org.geomajas.puregwt.client.rendering;

import org.geomajas.puregwt.client.ContentPanel;
import org.geomajas.puregwt.client.event.MapInitializationEvent;
import org.geomajas.puregwt.client.event.MapInitializationHandler;
import org.geomajas.puregwt.client.gfx.VectorContainer;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.vaadin.gwtgraphics.client.Image;
import org.vaadin.gwtgraphics.client.shape.Circle;
import org.vaadin.gwtgraphics.client.shape.Ellipse;
import org.vaadin.gwtgraphics.client.shape.Rectangle;
import org.vaadin.gwtgraphics.client.shape.Text;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * ContentPanel that demonstrates fixed size rendering abilities in world space.
 * 
 * @author Pieter De Graef
 */
public class FixedSizeWorldSpaceRenderingPanel extends ContentPanel {

	private VectorContainer container;

	public FixedSizeWorldSpaceRenderingPanel(MapPresenter mapPresenter) {
		super(mapPresenter);
	}

	public String getTitle() {
		return "World space (fixed size)";
	}

	public String getDescription() {
		return "This example shows the fixed size rendering of vector objects in world space. For more information"
				+ " regarding screen space and world space, visit the javadocs (TODO make this a link).<br/>"
				+ "Notice that unlike normal world space rendering the objects retain their size, while still "
				+ "keeping their position in world space.";
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
				Circle circle = new Circle(200000, 5000000, 10);
				circle.setFillColor("#FF0000");
				circle.setFillOpacity(1);
				circle.setStrokeOpacity(0);
				container.add(circle);
			}
		});
		leftLayout.add(circleBtn);

		Button ellipseBtn = new Button("Draw ellipse");
		ellipseBtn.setWidth("200");
		ellipseBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Ellipse ellipse = new Ellipse(300000, 2000000, 20, 30);
				ellipse.setFillColor("#0000FF");
				ellipse.setFillOpacity(1);
				ellipse.setStrokeOpacity(0);
				container.add(ellipse);
			}
		});
		leftLayout.add(ellipseBtn);

		Button rectangleBtn = new Button("Draw rectangle");
		rectangleBtn.setWidth("200");
		rectangleBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Rectangle rectangle = new Rectangle(-5000000, -2000000, 40, 80);
				rectangle.setFillColor("#FFFF00");
				rectangle.setFillOpacity(1);
				rectangle.setStrokeOpacity(0);
				container.add(rectangle);
			}
		});
		leftLayout.add(rectangleBtn);

		Button imageBtn = new Button("Draw image");
		imageBtn.setWidth("200");
		imageBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Image image = new Image(6000000, -3000000, 24, 24, GWT.getModuleBaseURL()
						+ "image/layer/city1.png");
				container.add(image);
			}
		});
		leftLayout.add(imageBtn);

		Button textBtn = new Button("Draw text");
		textBtn.setWidth("200");
		textBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Text text = new Text(-6000000, 1000000, "Hello World");
				text.setFillColor("#0066AA");
				text.setFillOpacity(1);
				text.setStrokeOpacity(0);
				container.add(text);
			}
		});
		leftLayout.add(textBtn);

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
		mapPresenter.initialize("pure-gwt-app", "mapOsm");
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
			container.setFixedSize(true);
		}
	}
}