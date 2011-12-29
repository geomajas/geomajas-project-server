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

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.puregwt.client.map.MapGadget;
import org.geomajas.puregwt.client.map.ViewPort;
import org.geomajas.puregwt.client.map.gfx.VectorContainer;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.Image;
import org.vaadin.gwtgraphics.client.shape.Path;
import org.vaadin.gwtgraphics.client.shape.Rectangle;
import org.vaadin.gwtgraphics.client.shape.path.ClosePath;
import org.vaadin.gwtgraphics.client.shape.path.LineTo;
import org.vaadin.gwtgraphics.client.shape.path.MoveTo;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.DOM;

/**
 * Map gadget that displays four panning arrows at the top-left of the map.
 * 
 * @author Pieter De Graef
 */
public class NavigationGadget implements MapGadget {

	private VectorContainer container;

	private ZoomToRectGroup zoomToRectangleGroup;

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

	// ------------------------------------------------------------------------
	// MapGadget implementation:
	// ------------------------------------------------------------------------

	public void onDraw(final ViewPort viewPort, final VectorContainer container) {
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
				double deltaX = -bounds.getWidth() / 5;
				PanAnimation animation = new PanAnimation(viewPort);
				animation.panTo(deltaX, 0, 300);
				event.stopPropagation();
			}
		});
		left.addMouseDownHandler(handler);
		left.addClickHandler(handler);
		left.addDoubleClickHandler(handler);

		right.addMouseUpHandler(new MouseUpHandler() {

			public void onMouseUp(MouseUpEvent event) {
				Bbox bounds = viewPort.getBounds();
				double deltaX = bounds.getWidth() / 5;
				PanAnimation animation = new PanAnimation(viewPort);
				animation.panTo(deltaX, 0, 300);
				event.stopPropagation();
			}
		});
		right.addMouseDownHandler(handler);
		right.addClickHandler(handler);
		right.addDoubleClickHandler(handler);

		up.addMouseUpHandler(new MouseUpHandler() {

			public void onMouseUp(MouseUpEvent event) {
				Bbox bounds = viewPort.getBounds();
				double deltaY = bounds.getHeight() / 5;
				PanAnimation animation = new PanAnimation(viewPort);
				animation.panTo(0, deltaY, 300);
				event.stopPropagation();
			}
		});
		up.addMouseDownHandler(handler);
		up.addClickHandler(handler);
		up.addDoubleClickHandler(handler);

		down.addMouseUpHandler(new MouseUpHandler() {

			public void onMouseUp(MouseUpEvent event) {
				Bbox bounds = viewPort.getBounds();
				double deltaY = -bounds.getHeight() / 5;
				PanAnimation animation = new PanAnimation(viewPort);
				animation.panTo(0, deltaY, 300);
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
				int index = viewPort.getZoomStrategy().getZoomStepIndex(viewPort.getScale());
				viewPort.applyScale(viewPort.getZoomStrategy().getZoomStepScale(index - 1));
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
				viewPort.applyScale(viewPort.getZoomStrategy().getZoomStepScale(index + 1));
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
		zoomToRectangle.setTitle("Zoom to rectangle by dragging the mouse on the map.");
		DOM.setStyleAttribute(zoomToRectangle.getElement(), "cursor", "pointer");
		zoomToRectangle.addMouseUpHandler(new MouseUpHandler() {

			public void onMouseUp(MouseUpEvent event) {
				zoomToRectangleGroup = new ZoomToRectGroup(viewPort);
				container.add(zoomToRectangleGroup);
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
		if (zoomToRectangleGroup != null) {
			container.remove(zoomToRectangleGroup);
			zoomToRectangleGroup = null;
		}
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
	 * Vector group that lets the user zoom to a rectangle.
	 * 
	 * @author Pieter De Graef
	 */
	private class ZoomToRectGroup extends Group {

		private int offset;

		private Rectangle eventCatcher;

		private Path zoomInRect;

		private boolean dragging;

		private int x;

		private int y;

		private Bbox screenBounds;

		public ZoomToRectGroup(final ViewPort viewPort) {
			eventCatcher = new Rectangle(0, 0, viewPort.getMapWidth(), viewPort.getMapHeight());
			eventCatcher.setFillOpacity(0);
			eventCatcher.setStrokeOpacity(0);

			zoomInRect = new Path(0, 0);
			zoomInRect.setFillColor("#000000");
			zoomInRect.setFillOpacity(0.2);
			zoomInRect.setStrokeColor("#000000");
			zoomInRect.setStrokeWidth(1);
			zoomInRect.setStrokeOpacity(1);
			DOM.setElementAttribute(zoomInRect.getElement(), "fill-rule", "evenodd");
			zoomInRect.lineTo(viewPort.getMapWidth(), 0);
			zoomInRect.lineTo(viewPort.getMapWidth(), viewPort.getMapHeight());
			zoomInRect.lineTo(0, viewPort.getMapHeight());
			zoomInRect.close();
			zoomInRect.moveTo(0, 0);
			zoomInRect.lineTo(0, 0);
			zoomInRect.lineTo(0, 0);
			zoomInRect.lineTo(0, 0);
			zoomInRect.close();

			add(zoomInRect);
			add(eventCatcher);

			eventCatcher.addMouseDownHandler(new MouseDownHandler() {

				public void onMouseDown(MouseDownEvent event) {
					if (event.getNativeButton() != NativeEvent.BUTTON_RIGHT) {
						dragging = true;
						x = event.getX() - offset;
						y = event.getY() - offset;
						updateRectangle(event);
					}
					event.stopPropagation();
					event.preventDefault();
				}
			});
			eventCatcher.addMouseUpHandler(new MouseUpHandler() {

				public void onMouseUp(MouseUpEvent event) {
					if (event.getNativeButton() != NativeEvent.BUTTON_RIGHT && dragging) {
						dragging = false;
						if (screenBounds != null) {
							Bbox worldBounds = viewPort.transform(screenBounds, RenderSpace.SCREEN, RenderSpace.WORLD);
							viewPort.applyBounds(worldBounds);
						}
					}
					event.stopPropagation();
				}
			});
			eventCatcher.addMouseMoveHandler(new MouseMoveHandler() {

				public void onMouseMove(MouseMoveEvent event) {
					if (dragging) {
						updateRectangle(event);
					}
					event.stopPropagation();
				}
			});
			eventCatcher.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					event.stopPropagation();
				}
			});
			eventCatcher.addDoubleClickHandler(new DoubleClickHandler() {

				public void onDoubleClick(DoubleClickEvent event) {
					event.stopPropagation();
				}
			});
		}

		private void updateRectangle(MouseEvent<?> event) {
			int beginX = x;
			int beginY = y;
			int endX = event.getX() - offset;
			int endY = event.getY() - offset;

			// Check if begin and end need to be reversed:
			if (beginX > endX) {
				int temp = endX;
				endX = beginX;
				beginX = temp;
			}
			if (beginY > endY) {
				int temp = endY;
				endY = beginY;
				beginY = temp;
			}

			int width = endX - beginX;
			int height = endY - beginY;
			if (height != 0 && width != 0) {
				zoomInRect.setStep(5, new MoveTo(false, beginX, beginY));
				zoomInRect.setStep(6, new LineTo(false, endX, beginY));
				zoomInRect.setStep(7, new LineTo(false, endX, endY));
				zoomInRect.setStep(8, new LineTo(false, beginX, endY));
				zoomInRect.setStep(9, new ClosePath());
				screenBounds = new Bbox(beginX, beginY, width, height);
			}
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