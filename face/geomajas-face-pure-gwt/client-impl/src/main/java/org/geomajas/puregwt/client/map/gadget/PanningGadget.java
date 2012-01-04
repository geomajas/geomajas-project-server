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

package org.geomajas.puregwt.client.map.gadget;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.puregwt.client.gfx.VectorContainer;
import org.geomajas.puregwt.client.map.MapGadget;
import org.geomajas.puregwt.client.map.ViewPort;
import org.vaadin.gwtgraphics.client.Image;

import com.google.gwt.animation.client.Animation;
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
public class PanningGadget implements MapGadget {

	private final int top, left;

	private VectorContainer container;

	// Panning:
	private String panBackgroundImage = GWT.getModuleBaseURL() + "geomajas/images/mapgadget/panbg.png";

	private String panLeftImage = GWT.getModuleBaseURL() + "geomajas/images/mapgadget/pan_left.gif";

	private String panRightImage = GWT.getModuleBaseURL() + "geomajas/images/mapgadget/pan_right.gif";

	private String panUpImage = GWT.getModuleBaseURL() + "geomajas/images/mapgadget/pan_up.gif";

	private String panDownImage = GWT.getModuleBaseURL() + "geomajas/images/mapgadget/pan_down.gif";

	// ------------------------------------------------------------------------
	// Constructor:
	// ------------------------------------------------------------------------

	public PanningGadget(int top, int left) {
		this.top = top;
		this.left = left;
	}

	// ------------------------------------------------------------------------
	// MapGadget implementation:
	// ------------------------------------------------------------------------

	public void onDraw(final ViewPort viewPort, final VectorContainer container) {
		this.container = container;

		Image panBg = new Image(left, top, 50, 50, panBackgroundImage);
		Image leftImg = new Image(left, top + 16, 18, 18, panLeftImage);
		Image rightImg = new Image(left + 32, top + 16, 18, 18, panRightImage);
		Image upImg = new Image(left + 16, top, 18, 18, panUpImage);
		Image downImg = new Image(left + 16, top + 32, 18, 18, panDownImage);

		DOM.setStyleAttribute(leftImg.getElement(), "cursor", "pointer");
		DOM.setStyleAttribute(rightImg.getElement(), "cursor", "pointer");
		DOM.setStyleAttribute(upImg.getElement(), "cursor", "pointer");
		DOM.setStyleAttribute(downImg.getElement(), "cursor", "pointer");

		StopPropagationHandler handler = new StopPropagationHandler();
		leftImg.addMouseUpHandler(new MouseUpHandler() {

			public void onMouseUp(MouseUpEvent event) {
				Bbox bounds = viewPort.getBounds();
				double deltaX = -bounds.getWidth() / 5;
				PanAnimation animation = new PanAnimation(viewPort);
				animation.panTo(deltaX, 0, 300);
				event.stopPropagation();
			}
		});
		leftImg.addMouseDownHandler(handler);
		leftImg.addClickHandler(handler);
		leftImg.addDoubleClickHandler(handler);

		rightImg.addMouseUpHandler(new MouseUpHandler() {

			public void onMouseUp(MouseUpEvent event) {
				Bbox bounds = viewPort.getBounds();
				double deltaX = bounds.getWidth() / 5;
				PanAnimation animation = new PanAnimation(viewPort);
				animation.panTo(deltaX, 0, 300);
				event.stopPropagation();
			}
		});
		rightImg.addMouseDownHandler(handler);
		rightImg.addClickHandler(handler);
		rightImg.addDoubleClickHandler(handler);

		upImg.addMouseUpHandler(new MouseUpHandler() {

			public void onMouseUp(MouseUpEvent event) {
				Bbox bounds = viewPort.getBounds();
				double deltaY = bounds.getHeight() / 5;
				PanAnimation animation = new PanAnimation(viewPort);
				animation.panTo(0, deltaY, 300);
				event.stopPropagation();
			}
		});
		upImg.addMouseDownHandler(handler);
		upImg.addClickHandler(handler);
		upImg.addDoubleClickHandler(handler);

		downImg.addMouseUpHandler(new MouseUpHandler() {

			public void onMouseUp(MouseUpEvent event) {
				Bbox bounds = viewPort.getBounds();
				double deltaY = -bounds.getHeight() / 5;
				PanAnimation animation = new PanAnimation(viewPort);
				animation.panTo(0, deltaY, 300);
				event.stopPropagation();
			}
		});
		downImg.addMouseDownHandler(handler);
		downImg.addClickHandler(handler);
		downImg.addDoubleClickHandler(handler);

		container.add(panBg);
		container.add(leftImg);
		container.add(rightImg);
		container.add(upImg);
		container.add(downImg);
	}

	public void onTranslate() {
		// Do nothing.
	}

	public void onScale() {
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

	/**
	 * Animation for the panning buttons.
	 * 
	 * @author Pieter De Graef
	 */
	private class PanAnimation extends Animation {

		private ViewPort viewPort;

		private double translateX;

		private double translateY;

		private double previousProgress;

		public PanAnimation(ViewPort viewPort) {
			this.viewPort = viewPort;
		}

		public void panTo(double translateX, double translateY, int milliseconds) {
			this.translateX = translateX;
			this.translateY = translateY;
			previousProgress = 0;
			run(milliseconds);
		}

		protected void onUpdate(double progress) {
			double deltaX = (progress * translateX) - (previousProgress * translateX);
			double deltaY = (progress * translateY) - (previousProgress * translateY);

			Coordinate position = viewPort.getPosition();
			viewPort.applyPosition(new Coordinate(position.getX() + deltaX, position.getY() + deltaY));
			previousProgress = progress;
		}
	}
}