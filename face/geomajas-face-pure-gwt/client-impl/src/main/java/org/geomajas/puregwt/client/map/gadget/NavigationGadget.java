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

package org.geomajas.puregwt.client.map.gadget;

import org.geomajas.puregwt.client.map.MapGadget;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.client.map.ScreenContainer;
import org.geomajas.puregwt.client.map.ViewPort;
import org.geomajas.puregwt.client.map.ZoomOption;
import org.geomajas.puregwt.client.spatial.Bbox;
import org.vaadin.gwtgraphics.client.Image;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.DOM;

/**
 * Map gadget that displays four panning arrows at the top-left of the map.
 * 
 * @author Pieter De Graef
 */
public class NavigationGadget implements MapGadget {
	
	private MapPresenter mapPresenter;

	private ScreenContainer container;

	// Panning:
	private String panBackgroundImage = GWT.getModuleBaseURL() + "geomajas/images/mapgadget/panbg.png";

	private String panLeftImage = GWT.getModuleBaseURL() + "geomajas/images/mapgadget/pan_left.gif";

	private String panRightImage = GWT.getModuleBaseURL() + "geomajas/images/mapgadget/pan_right.gif";

	private String panUpImage = GWT.getModuleBaseURL() + "geomajas/images/mapgadget/pan_up.gif";

	private String panDownImage = GWT.getModuleBaseURL() + "geomajas/images/mapgadget/pan_down.gif";

	// Zooming:
	private String zoomBackgroundImage = GWT.getModuleBaseURL() + "geomajas/images/mapgadget/zoombg.png";

	private String zoomInImage = GWT.getModuleBaseURL() + "geomajas/images/mapgadget/zoomPlus.png";

	private String zoomOutImage = GWT.getModuleBaseURL() + "geomajas/images/mapgadget/zoomMinus.png";

	private String zoomExtentImage = GWT.getModuleBaseURL() + "geomajas/images/mapgadget/maxextent.png";

	// Zoom to rectangle:
	private String zoomToRectangleImage = GWT.getModuleBaseURL() + "geomajas/images/mapgadget/zoom_rectangle.png";

	public NavigationGadget(MapPresenter mapPresenter) {
		this.mapPresenter = mapPresenter;
	}

	// ------------------------------------------------------------------------
	// MapGadget implementation:
	// ------------------------------------------------------------------------

	public void onDraw(final ViewPort viewPort, ScreenContainer container) {
		this.container = container;

		Image panBg = new Image(5, 5, 50, 50, panBackgroundImage);
		Image left = new Image(5, 21, 18, 18, panLeftImage);
		Image right = new Image(37, 21, 18, 18, panRightImage);
		Image up = new Image(21, 5, 18, 18, panUpImage);
		Image down = new Image(21, 37, 18, 18, panDownImage);

		DOM.setStyleAttribute(left.getElement(), "cursor", "pointer");
		DOM.setStyleAttribute(right.getElement(), "cursor", "pointer");
		DOM.setStyleAttribute(up.getElement(), "cursor", "pointer");
		DOM.setStyleAttribute(down.getElement(), "cursor", "pointer");

		StopPropagationHandler handler = new StopPropagationHandler();
		left.addMouseUpHandler(new MouseUpHandler() {

			public void onMouseUp(MouseUpEvent event) {
				Bbox bounds = viewPort.getBounds();
				double dX = -bounds.getWidth() / 3;
				viewPort.translate(dX, 0);
				event.stopPropagation();
			}
		});
		left.addMouseDownHandler(handler);
		left.addClickHandler(handler);
		left.addDoubleClickHandler(handler);

		right.addMouseUpHandler(new MouseUpHandler() {

			public void onMouseUp(MouseUpEvent event) {
				Bbox bounds = viewPort.getBounds();
				double dX = bounds.getWidth() / 3;
				viewPort.translate(dX, 0);
				event.stopPropagation();
			}
		});
		right.addMouseDownHandler(handler);
		right.addClickHandler(handler);
		right.addDoubleClickHandler(handler);

		up.addMouseUpHandler(new MouseUpHandler() {

			public void onMouseUp(MouseUpEvent event) {
				Bbox bounds = viewPort.getBounds();
				double dY = bounds.getHeight() / 3;
				viewPort.translate(0, dY);
				event.stopPropagation();
			}
		});
		up.addMouseDownHandler(handler);
		up.addClickHandler(handler);
		up.addDoubleClickHandler(handler);

		down.addMouseUpHandler(new MouseUpHandler() {

			public void onMouseUp(MouseUpEvent event) {
				Bbox bounds = viewPort.getBounds();
				double dY = -bounds.getHeight() / 3;
				viewPort.translate(0, dY);
				event.stopPropagation();
			}
		});
		down.addMouseDownHandler(handler);
		down.addClickHandler(handler);
		down.addDoubleClickHandler(handler);

		container.add(panBg);
		container.add(left);
		container.add(right);
		container.add(up);
		container.add(down);

		// Zooming buttons:

		Image zoomBg = new Image(20, 60, 20, 60, zoomBackgroundImage);
		Image zoomIn = new Image(20, 60, 20, 20, zoomInImage);
		Image zoomExtent = new Image(20, 80, 20, 20, zoomExtentImage);
		Image zoomOut = new Image(20, 100, 20, 20, zoomOutImage);

		DOM.setStyleAttribute(zoomIn.getElement(), "cursor", "pointer");
		DOM.setStyleAttribute(zoomOut.getElement(), "cursor", "pointer");
		DOM.setStyleAttribute(zoomExtent.getElement(), "cursor", "pointer");

		zoomIn.addMouseUpHandler(new MouseUpHandler() {

			public void onMouseUp(MouseUpEvent event) {
				viewPort.scale(2, ZoomOption.LEVEL_CHANGE);
				event.stopPropagation();
			}
		});
		zoomIn.addMouseDownHandler(handler);
		zoomIn.addClickHandler(handler);
		zoomIn.addDoubleClickHandler(handler);

		zoomExtent.addMouseUpHandler(new MouseUpHandler() {

			public void onMouseUp(MouseUpEvent event) {
				viewPort.applyMaximumBounds();
				event.stopPropagation();
			}
		});
		zoomExtent.addMouseDownHandler(handler);
		zoomExtent.addClickHandler(handler);
		zoomExtent.addDoubleClickHandler(handler);

		zoomOut.addMouseUpHandler(new MouseUpHandler() {

			public void onMouseUp(MouseUpEvent event) {
				viewPort.scale(0.5, ZoomOption.LEVEL_CHANGE);
				event.stopPropagation();
			}
		});
		zoomOut.addMouseDownHandler(handler);
		zoomOut.addClickHandler(handler);
		zoomOut.addDoubleClickHandler(handler);

		container.add(zoomBg);
		container.add(zoomIn);
		container.add(zoomExtent);
		container.add(zoomOut);
		
		// Zoom to rectangle buttons:
		
		Image zoomToRectangle = new Image(20, 130, 20, 20, zoomToRectangleImage);
		DOM.setStyleAttribute(zoomToRectangle.getElement(), "cursor", "pointer");
		zoomToRectangle.addMouseUpHandler(new MouseUpHandler() {

			public void onMouseUp(MouseUpEvent event) {
				event.stopPropagation();
			}
		});
		zoomToRectangle.addMouseDownHandler(handler);
		zoomToRectangle.addClickHandler(handler);
		zoomToRectangle.addDoubleClickHandler(handler);

		container.add(zoomToRectangle);
	}

	public void onTranslate() {
		// Do nothing.
	}

	public void onScale() {
		// Do nothing.
	}

	public void onResize() {
		// Do nothing.
	}

	public void onDestroy() {
		if (container != null) {
			container.clear();
		}
	}

	// ------------------------------------------------------------------------
	// Private classes:
	// ------------------------------------------------------------------------

	/**
	 * Combination of different handlers with a single goal: stop all the events from propagating to the map.
	 * 
	 * @author Pieter De Graef
	 */
	private class StopPropagationHandler implements MouseDownHandler, ClickHandler, DoubleClickHandler {

		public void onDoubleClick(DoubleClickEvent event) {
			event.stopPropagation();
		}

		public void onClick(ClickEvent event) {
			event.stopPropagation();
		}

		public void onMouseDown(MouseDownEvent event) {
			event.stopPropagation();
		}
	}
}