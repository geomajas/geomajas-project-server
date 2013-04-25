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

package org.geomajas.puregwt.example.client.sample.rendering;

import org.geomajas.puregwt.client.event.MapInitializationEvent;
import org.geomajas.puregwt.client.event.MapInitializationHandler;
import org.geomajas.puregwt.client.gfx.VectorContainer;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.example.client.ContentPanel;
import org.vaadin.gwtgraphics.client.shape.Rectangle;
import org.vaadin.gwtgraphics.client.shape.Text;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * ContentPanel that demonstrates user interaction on custom drawings.
 * 
 * @author Pieter De Graef
 */
public class DrawingInteractionPanel extends ContentPanel {

	private Rectangle rectangle;

	private Text text;

	public DrawingInteractionPanel(MapPresenter mapPresenter) {
		super(mapPresenter);
	}

	public String getTitle() {
		return "Drawing interaction";
	}

	public String getDescription() {
		return "This example shows how to add custom user interaction on custom drawings. In this particular case a "
				+ "rectangle is drawn that can be dragged around.";
	}

	public Widget getContentWidget() {
		// Create the MapPresenter and add an InitializationHandler:
		mapPresenter.setSize(480, 480);
		mapPresenter.getEventBus().addMapInitializationHandler(new MyMapInitializationHandler());

		// Define the whole layout:
		DecoratorPanel mapDecorator = new DecoratorPanel();
		mapDecorator.add(mapPresenter.asWidget());

		// Initialize the map, and return the layout:
		mapPresenter.initialize("puregwt-app", "mapOsm");
		return mapDecorator;
	}
	
	/**
	 * Map initialization handler that draws a rectangle in screen space.
	 * 
	 * @author Pieter De Graef
	 */
	private class MyMapInitializationHandler implements MapInitializationHandler {

		public void onMapInitialized(MapInitializationEvent event) {
			VectorContainer container = mapPresenter.addScreenContainer();
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
		}
	}

	/**
	 * A mouse handler that lets the rectangle (and the accompanying text) move around the map by dragging it.
	 * 
	 * @author Pieter De Graef
	 */
	private class DragHandler implements MouseDownHandler, MouseUpHandler, MouseMoveHandler {

		private boolean dragging;

		private int startX;

		private int startY;

		public void onMouseMove(MouseMoveEvent event) {
			if (dragging) {
				// notice that you can drag outside the visible map bounds !!!
				// applications may limit dragging to mouse positions inside the map
				int deltaX = event.getX() - startX;
				int deltaY = event.getY() - startY;
				rectangle.setX(rectangle.getX() + deltaX);
				rectangle.setY(rectangle.getY() + deltaY);
				text.setX(text.getX() + deltaX);
				text.setY(text.getY() + deltaY);
				event.stopPropagation();
			}
		}

		public void onMouseDown(MouseDownEvent event) {
			// let the rectangle capture all events
			DOM.setCapture(rectangle.getElement());
			dragging = true;
			startX = event.getX();
			startY = event.getY();
			// prevent native drag/drop
			event.getNativeEvent().preventDefault();
			// Stop the event from reaching the map controller: no panning while we're dragging the rectangle.
			event.stopPropagation();
		}

		public void onMouseUp(MouseUpEvent event) {
			dragging = false;
			// release event capture
			DOM.releaseCapture(rectangle.getElement());
			event.stopPropagation();
		}

	}
}