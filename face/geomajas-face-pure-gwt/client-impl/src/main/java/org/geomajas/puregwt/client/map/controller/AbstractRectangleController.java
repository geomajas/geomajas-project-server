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

package org.geomajas.puregwt.client.map.controller;

import java.util.Date;

import org.geomajas.geometry.Coordinate;
import org.geomajas.puregwt.client.map.ScreenContainer;
import org.geomajas.puregwt.client.map.TransformationService;
import org.geomajas.puregwt.client.spatial.Bbox;
import org.geomajas.puregwt.client.spatial.GeometryFactory;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.inject.Inject;

/**
 * ...
 * 
 * @author Pieter De Graef
 * @author Joachim Van der Auwera
 */
public abstract class AbstractRectangleController extends AbstractMapController {

	public static final String CONTAINER_ID = "abstract-rectangle-contoller";

	protected Rectangle rectangle;

	protected boolean dragging;

	protected long timestamp;

	protected Coordinate begin;

	protected boolean shift;

	private ScreenContainer container;

	@Inject
	private GeometryFactory factory;

	public AbstractRectangleController() {
		container = mapPresenter.getScreenContainer(CONTAINER_ID);
	}

	public void onMouseDown(MouseDownEvent event) {
		if (event.getNativeButton() != NativeEvent.BUTTON_RIGHT) {
			dragging = true;
			timestamp = new Date().getTime();
			begin = getScreenPosition(event);
			shift = event.isShiftKeyDown();
			rectangle = new Rectangle((int) begin.getX(), (int) begin.getY(), 0, 0);
			container.add(rectangle);
		}
	}

	public void onMouseUp(MouseUpEvent event) {
		// Assure dragging or clicking started inside this widget
		if (dragging) {
			shift |= event.isShiftKeyDown(); // shift is used when depressed either at beginning or end
			updateRectangle(event);

			TransformationService transformer = mapPresenter.getMapModel().getViewPort().getTransformationService();
			Bbox bounds = factory.createBbox(rectangle.getX(), rectangle.getY(), rectangle.getWidth(),
					rectangle.getHeight());
			execute(transformer.viewToWorld(bounds));

			stopDragging();
		}
	}

	public void onMouseMove(MouseMoveEvent event) {
		if (dragging) {
			updateRectangle(event);
			// mapWidget.render(rectangle, RenderGroup.SCREEN, RenderStatus.UPDATE);
		}
	}

	public void onMouseOut(MouseOutEvent event) {
		stopDragging();
	}

	/**
	 * Overwrite this method to handle the actual selection. The bounds variable contains the selected area.
	 * 
	 * @param worldBounds
	 *            bounds in world coordinates
	 */
	protected abstract void execute(Bbox worldBounds);

	protected void stopDragging() {
		if (dragging) {
			dragging = false;
			container.remove(rectangle);
		}
	}

	private void updateRectangle(MouseEvent<?> event) {
		Coordinate pos = getScreenPosition(event);
		double x = begin.getX();
		double y = begin.getY();
		double width = pos.getX() - x;
		double height = pos.getY() - y;
		if (width < 0) {
			x = pos.getX();
			width = -width;
		}
		if (height < 0) {
			y = pos.getY();
			height = -height;
		}
		rectangle.setX((int) x);
		rectangle.setY((int) y);
		rectangle.setWidth((int) width);
		rectangle.setHeight((int) height);
	}

}