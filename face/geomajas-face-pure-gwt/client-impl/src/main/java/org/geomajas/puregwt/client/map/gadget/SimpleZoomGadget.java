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

import org.geomajas.puregwt.client.gfx.VectorContainer;
import org.geomajas.puregwt.client.map.MapGadget;
import org.geomajas.puregwt.client.map.ViewPort;
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
 * Map gadget that displays zoom in, zoom out and zoom to maximum extent buttons.
 * 
 * @author Pieter De Graef
 */
public class SimpleZoomGadget implements MapGadget {

	private final int top, left;

	private VectorContainer container;

	// Zooming:
	private String zoomBackgroundImage = GWT.getModuleBaseURL() + "geomajas/images/mapgadget/zoombg.png";

	private String zoomInImage = GWT.getModuleBaseURL() + "geomajas/images/mapgadget/zoomPlus.png";

	private String zoomOutImage = GWT.getModuleBaseURL() + "geomajas/images/mapgadget/zoomMinus.png";

	private String zoomExtentImage = GWT.getModuleBaseURL() + "geomajas/images/mapgadget/maxextent.png";

	// ------------------------------------------------------------------------
	// Constructor:
	// ------------------------------------------------------------------------

	public SimpleZoomGadget(int top, int left) {
		this.top = top;
		this.left = left;
	}

	// ------------------------------------------------------------------------
	// MapGadget implementation:
	// ------------------------------------------------------------------------

	public void onDraw(final ViewPort viewPort, final VectorContainer container) {
		this.container = container;

		StopPropagationHandler handler = new StopPropagationHandler();

		// Zooming buttons:

		Image zoomBg = new Image(left, top, 20, 60, zoomBackgroundImage);
		Image zoomIn = new Image(left, top, 20, 20, zoomInImage);
		Image zoomExtent = new Image(left, top + 20, 20, 20, zoomExtentImage);
		Image zoomOut = new Image(left, top + 40, 20, 20, zoomOutImage);

		DOM.setStyleAttribute(zoomIn.getElement(), "cursor", "pointer");
		DOM.setStyleAttribute(zoomOut.getElement(), "cursor", "pointer");
		DOM.setStyleAttribute(zoomExtent.getElement(), "cursor", "pointer");

		zoomIn.addMouseUpHandler(new MouseUpHandler() {

			public void onMouseUp(MouseUpEvent event) {
				int index = viewPort.getZoomStrategy().getZoomStepIndex(viewPort.getScale());
				try {
					viewPort.applyScale(viewPort.getZoomStrategy().getZoomStepScale(index - 1));
				} catch (IllegalArgumentException e) {
				}
				event.stopPropagation();
			}
		});
		zoomIn.addMouseDownHandler(handler);
		zoomIn.addClickHandler(handler);
		zoomIn.addDoubleClickHandler(handler);

		zoomExtent.addMouseUpHandler(new MouseUpHandler() {

			public void onMouseUp(MouseUpEvent event) {
				viewPort.applyBounds(viewPort.getMaximumBounds());
				event.stopPropagation();
			}
		});
		zoomExtent.addMouseDownHandler(handler);
		zoomExtent.addClickHandler(handler);
		zoomExtent.addDoubleClickHandler(handler);

		zoomOut.addMouseUpHandler(new MouseUpHandler() {

			public void onMouseUp(MouseUpEvent event) {
				int index = viewPort.getZoomStrategy().getZoomStepIndex(viewPort.getScale());
				try {
					viewPort.applyScale(viewPort.getZoomStrategy().getZoomStepScale(index + 1));
				} catch (IllegalArgumentException e) {
				}
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