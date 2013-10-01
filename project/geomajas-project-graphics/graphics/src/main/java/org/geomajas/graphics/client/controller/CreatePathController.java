/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.graphics.client.controller;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.geometry.Coordinate;
import org.geomajas.graphics.client.object.GPath;
import org.geomajas.graphics.client.object.labeler.ResizableExternalizableLabeler;
import org.geomajas.graphics.client.object.role.Fillable;
import org.geomajas.graphics.client.object.role.Labeled;
import org.geomajas.graphics.client.operation.AddOperation;
import org.geomajas.graphics.client.service.AbstractGraphicsController;
import org.geomajas.graphics.client.service.GraphicsService;
import org.vaadin.gwtgraphics.client.VectorObjectContainer;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * Controller that creates a {@link GPath}.
 * 
 * @author Jan De Moerloose
 * 
 */
public class CreatePathController extends AbstractGraphicsController implements MouseDownHandler, MouseMoveHandler,
		DoubleClickHandler, Fillable {

	private boolean active;

	private List<HandlerRegistration> registrations = new ArrayList<HandlerRegistration>();

	protected GPath path;

	protected GPath dragLine;

	private GPath previewPath;

	private VectorObjectContainer container;

	private boolean closedPath;

	private boolean showPreview;

	private String fillColor = "#CCFF66";

	private double fillOpacity;

	private String text;

	private String captureCursor;

	public CreatePathController(GraphicsService graphicsService, boolean closedPath) {
		super(graphicsService);
		this.closedPath = closedPath;
		fillOpacity = closedPath ? 1 : 0;
		showPreview = closedPath;
		text = closedPath ? "Polygon" : "Line";
		container = createContainer();
	}

	@Override
	public void setActive(boolean active) {
		this.active = active;
		if (active) {
			container = createContainer();
			registrations.add(getObjectContainer().addMouseDownHandler(this));
		} else {
			for (HandlerRegistration r : registrations) {
				r.removeHandler();
			}
			registrations.clear();
			path = null;
			if (container != null) {
				removeContainer(container);
			}
			container = null;
		}
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void destroy() {
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) {
		if (path != null) {
			Coordinate c1 = path.getLastCoordinate();
			Coordinate c2 = getUserCoordinate(event);
			if (showPreview) {
				previewPath.moveCoordinate(c2, previewPath.getCoordinateCount() - 1);
			}
			dragLine.setCoordinates(new Coordinate[] { c1, c2 });
		}
	}

	@Override
	public void onDoubleClick(DoubleClickEvent event) {
		release(path.asObject().getElement());
		container.clear();
		addObject(path);
		dragLine = null;
		previewPath = null;
		path = null;
		event.stopPropagation();
	}

	protected void addObject(GPath path) {
		path.removeRole(Labeled.TYPE);
		path.addRole(new ResizableExternalizableLabeler(path, 
				text, getService().isExternalizableLabeledOriginallyExternal()));
		execute(new AddOperation(path));
	}

	public boolean isClosedPath() {
		return closedPath;
	}

	public void setClosedPath(boolean closedPath) {
		this.closedPath = closedPath;
	}

	protected void capture(Element element, Cursor cursor) {
		DOM.setCapture(element);
		captureCursor = RootPanel.getBodyElement().getStyle().getCursor();
		RootPanel.getBodyElement().getStyle().setCursor(cursor);
	}

	protected void release(Element element) {
		DOM.releaseCapture(element);
		RootPanel.getBodyElement().getStyle().setProperty("cursor", captureCursor);
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
		if (isSingleClick(event.getNativeEvent())) {
			if (path == null) {
				path = createPath(text);
				path.setCoordinates(new Coordinate[] { getUserCoordinate(event) });
				// we can show a preview of the filled path if necessary
				if (showPreview) {
					previewPath = createPath("");
					previewPath.setCoordinates(new Coordinate[] { getUserCoordinate(event) });
					// add the preview extra point !
					previewPath.addCoordinate(new Coordinate(getUserCoordinate(event)));
					previewPath.setFillOpacity(previewPath.getFillOpacity() * 0.7);
					container.add(previewPath.asObject());
				}
				// we have to show our intermediate result !
				container.add(path.asObject());
				// start the drag line, captures all events from now !
				if (dragLine == null) {
					dragLine = createPath("");
					dragLine.setStrokeOpacity(1);
					container.add(dragLine.asObject());
				}
				Coordinate c1 = path.getLastCoordinate();
				Coordinate c2 = getUserCoordinate(event);
				dragLine.setCoordinates(new Coordinate[] { c1, c2 });
				dragLine.asObject().addMouseMoveHandler(this);
				dragLine.asObject().addMouseDownHandler(this);
				dragLine.asObject().addDoubleClickHandler(this);
				capture(dragLine.asObject().getElement(), Cursor.CROSSHAIR);
			} else {
				path.addCoordinate(getUserCoordinate(event));
				if (showPreview) {
					previewPath.addCoordinate(getUserCoordinate(event));
				}
			}
		}
	}

	private native boolean isSingleClick(NativeEvent event) /*-{
															return !event.detail || event.detail==1;
															}-*/;

	protected GPath createPath(String text) {
		GPath path = new GPath(new Coordinate(0, 0), isClosedPath(), text);
		path.setFillColor(fillColor);
		path.setFillOpacity(fillOpacity);
		return path;
	}

	@Override
	public void setFillColor(String fillColor) {
		this.fillColor = fillColor;
	}

	@Override
	public void setFillOpacity(double fillOpacity) {
		this.fillOpacity = fillOpacity;
	}

	@Override
	public String getFillColor() {
		return fillColor;
	}

	@Override
	public double getFillOpacity() {
		return fillOpacity;
	}

	@Override
	public void setVisible(boolean visible) {
		// TODO Auto-generated method stub
		
	}

}
