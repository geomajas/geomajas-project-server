/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.puregwt.client.rendering;

import org.geomajas.puregwt.client.ContentPanel;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.client.map.event.MapInitializationEvent;
import org.geomajas.puregwt.client.map.event.MapInitializationHandler;
import org.geomajas.puregwt.client.map.gfx.ScreenContainer;
import org.vaadin.gwtgraphics.client.shape.Circle;
import org.vaadin.gwtgraphics.client.shape.Path;
import org.vaadin.gwtgraphics.client.shape.Rectangle;
import org.vaadin.gwtgraphics.client.shape.Text;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
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

	private MapPresenter mapPresenter;

	private ScreenContainer container;

	private Rectangle rectangle;

	private Text text;

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

		Button groupBtn = new Button("Draw rectangle");
		groupBtn.setWidth("200");
		groupBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				if (rectangle == null) {
					rectangle = new Rectangle(60, 40, 200, 80);
					rectangle.setFillColor("#CC9900");
					rectangle.setFillOpacity(0.4);

					text = new Text(70, 60, "Drag me...");
					text.setFontFamily("Arial");
					text.setFontSize(16);
					text.setStrokeOpacity(0);
					text.setFillColor("#000000");
					container.add(text);
					container.add(rectangle);

					DragHandler dragHandler = new DragHandler();
					rectangle.addMouseDownHandler(dragHandler);
					rectangle.addMouseUpHandler(dragHandler);
					rectangle.addMouseMoveHandler(dragHandler);
					rectangle.addMouseOutHandler(dragHandler);
					text.addMouseDownHandler(dragHandler);
					text.addMouseUpHandler(dragHandler);
					text.addMouseMoveHandler(dragHandler);
					text.addMouseOutHandler(dragHandler);
				}
			}
		});
		leftLayout.add(groupBtn);

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
				Path path = new Path(120, 160);
				path.setFillColor("#0066AA");
				path.setFillOpacity(0.4);
				path.setStrokeColor("#004499");
				path.lineRelativelyTo(100, 0);
				path.lineRelativelyTo(0, 100);
				path.lineRelativelyTo(-100, 0);
				path.lineRelativelyTo(0, -100);
				path.moveTo(140, 180);
				path.lineRelativelyTo(50, 0);
				path.lineRelativelyTo(0, 50);
				path.lineRelativelyTo(-50, 0);
				path.lineRelativelyTo(0, -50);
				container.add(path);
			}
		});
		leftLayout.add(pathBtn);

		Button deleteBtn = new Button("Delete all drawings");
		deleteBtn.setWidth("200");
		deleteBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent arg0) {
				container.clear();
				rectangle = null;
				text = null;
			}
		});
		leftLayout.add(deleteBtn);

		// Create the MapPresenter and add an InitializationHandler:
		mapPresenter = getInjector().getMapPresenter();
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

	/**
	 * A mouse handler that lets the rectangle (and the accompanying text) move around the map by dragging it.
	 * 
	 * @author Pieter De Graef
	 */
	private class DragHandler implements MouseDownHandler, MouseUpHandler, MouseMoveHandler, MouseOutHandler {

		private boolean dragging;

		private int startX;

		private int startY;

		public void onMouseMove(MouseMoveEvent event) {
			if (dragging) {
				int deltaX = event.getX() - startX;
				int deltaY = event.getY() - startY;
				rectangle.setX(rectangle.getX() + deltaX);
				rectangle.setY(rectangle.getY() + deltaY);
				text.setX(text.getX() + deltaX);
				text.setY(text.getY() + deltaY);
			}
		}

		public void onMouseUp(MouseUpEvent event) {
			dragging = false;
		}

		public void onMouseDown(MouseDownEvent event) {
			dragging = true;
			startX = event.getX();
			startY = event.getY();

			// Stop the event from reaching the map controller: no panning while we're dragging the rectangle.
			event.stopPropagation();
		}

		public void onMouseOut(MouseOutEvent arg0) {
			dragging = false;
		}
	}
}