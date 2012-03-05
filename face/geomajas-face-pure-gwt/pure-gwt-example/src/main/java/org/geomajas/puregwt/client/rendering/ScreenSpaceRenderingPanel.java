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

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.puregwt.client.ContentPanel;
import org.geomajas.puregwt.client.GeomajasGinjector;
import org.geomajas.puregwt.client.event.MapInitializationEvent;
import org.geomajas.puregwt.client.event.MapInitializationHandler;
import org.geomajas.puregwt.client.gfx.VectorContainer;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.vaadin.gwtgraphics.client.shape.Circle;
import org.vaadin.gwtgraphics.client.shape.Path;
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
 * ContentPanel that demonstrates rendering abilities in screen space.
 * 
 * @author Pieter De Graef
 */
public class ScreenSpaceRenderingPanel extends ContentPanel {

	private final GeomajasGinjector geomajasInjector = GWT.create(GeomajasGinjector.class);

	private MapPresenter mapPresenter;

	private VectorContainer container;

	public String getTitle() {
		return "Drawing in screen space";
	}

	public String getDescription() {
		return "This example shows the vector object drawing capabilities of the Geomajas map. In this particular "
				+ "example, all objects are rendered in screen space. For more information regarding screen space and "
				+ "world space, visit the javadocs (TODO make this a link).";
	}

	public Widget getContentWidget() {
		// Define the left layout:
		VerticalPanel leftLayout = new VerticalPanel();
		leftLayout.setSize("220px", "100%");

		leftLayout.add(new HTML("<h3>Drawing options:</h3>"));

		Button rectangleBtn = new Button("Draw rectangle");
		rectangleBtn.setWidth("200");
		rectangleBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Rectangle rectangle = new Rectangle(60, 40, 200, 80);
				rectangle.setFillColor("#CC9900");
				rectangle.setFillOpacity(0.4);
				container.add(rectangle);
			}
		});
		leftLayout.add(rectangleBtn);

		Button textBtn = new Button("Draw text");
		textBtn.setWidth("200");
		textBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Text text = new Text(70, 60, "Hello World");
				text.setFontFamily("Arial");
				text.setFontSize(16);
				text.setStrokeOpacity(0);
				text.setFillColor("#000000");
				container.add(text);
			}
		});
		leftLayout.add(textBtn);

		Button circleBtn = new Button("Draw circle");
		circleBtn.setWidth("200");
		circleBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Circle circle = new Circle(300, 140, 30);
				circle.setFillColor("#0099CC");
				circle.setFillOpacity(0.4);
				container.add(circle);
			}
		});
		leftLayout.add(circleBtn);

		Button pathBtn = new Button("Draw path");
		pathBtn.setWidth("200");
		pathBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Geometry geometry = new Geometry(Geometry.POLYGON, 0, 0);
				Geometry shell = new Geometry(Geometry.LINEAR_RING, 0, 0);
				shell.setCoordinates(new Coordinate[] { new Coordinate(120, 160), new Coordinate(220, 160),
						new Coordinate(220, 260), new Coordinate(120, 260), new Coordinate(120, 160) });
				Geometry hole = new Geometry(Geometry.LINEAR_RING, 0, 0);
				hole.setCoordinates(new Coordinate[] { new Coordinate(140, 180), new Coordinate(190, 180),
						new Coordinate(190, 230), new Coordinate(140, 230), new Coordinate(140, 180) });
				geometry.setGeometries(new Geometry[] { shell, hole });

				Path path = geomajasInjector.getGfxUtil().toPath(geometry);
				path.setFillColor("#0066AA");
				path.setFillOpacity(0.4);
				path.setStrokeColor("#004499");

				// Alternative way:
				// Path path = new Path(120, 160);
				// path.setFillColor("#0066AA");
				// path.setFillOpacity(0.4);
				// path.setStrokeColor("#004499");
				// path.lineRelativelyTo(100, 0);
				// path.lineRelativelyTo(0, 100);
				// path.lineRelativelyTo(-100, 0);
				// path.lineRelativelyTo(0, -100);
				// path.moveTo(140, 180);
				// path.lineRelativelyTo(50, 0);
				// path.lineRelativelyTo(0, 50);
				// path.lineRelativelyTo(-50, 0);
				// path.lineRelativelyTo(0, -50);
				// path.getElement().getStyle().setProperty("fillRule", "evenOdd");

				container.add(path);
			}
		});
		leftLayout.add(pathBtn);

		Button deleteBtn = new Button("Delete all drawings");
		deleteBtn.setWidth("200");
		deleteBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent arg0) {
				container.clear();
			}
		});
		leftLayout.add(deleteBtn);

		// Create the MapPresenter and add an InitializationHandler:
		mapPresenter = getInjector().getMapPresenter().get();
		mapPresenter.setSize(640, 480);
		mapPresenter.getEventBus().addHandler(MapInitializationEvent.TYPE, new MyMapInitializationHandler());

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
			container = mapPresenter.addScreenContainer();
		}
	}
}