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
package org.geomajas.project.graphics.example.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.service.BboxService;
import org.geomajas.graphics.client.object.GCircle;
import org.geomajas.graphics.client.object.GEllipse;
import org.geomajas.graphics.client.object.GIcon;
import org.geomajas.graphics.client.object.GImage;
import org.geomajas.graphics.client.object.GPath;
import org.geomajas.graphics.client.object.GRectangle;
import org.geomajas.graphics.client.object.GText;
import org.geomajas.graphics.client.object.ResizableBorderer;
import org.geomajas.graphics.client.object.anchor.Anchored;
import org.geomajas.graphics.client.object.anchor.ResizableAnchorer;
import org.geomajas.graphics.client.service.AbstractGraphicsObjectContainer;
import org.geomajas.graphics.client.shape.MarkerShape;
import org.geomajas.graphics.client.util.BboxPosition;
import org.vaadin.gwtgraphics.client.DrawingArea;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

/**
 * Sample extension of {@link AbstractGraphicsObjectContainer}.
 * 
 * @author Jan De Moerloose
 * 
 */
public class TestContainer extends AbstractGraphicsObjectContainer implements IsWidget, NativePreviewHandler {

	private DrawingArea canvas = new DrawingArea(1000, 1000);

	private TransformingGroup rootContainer = new TransformingGroup();

	public TestContainer(EventBus eventBus) {
		super(eventBus);
		canvas.getElement().setId("TestContainer");
		canvas.add(rootContainer);
		setRootContainer(rootContainer);
		Event.addNativePreviewHandler(this);
		GRectangle rect = new GRectangle(100, 100, 100, 100, "Rectangle label");
		GCircle circle = new GCircle(300, 100, 50, "Circle");
		GEllipse ellipse = new GEllipse(100, 300, 50, 80, "Ellipse");
		GImage image = new GImage(200, 200, 200, 235,
				"http://tuxpaint.org/stamps/stamps/animals/birds/cartoon/tux.png", "Image");
		GPath path = new GPath(new Coordinate[] { new Coordinate(300, 300), new Coordinate(500, 400) }, false, "Path");
		GText text = new GText(50, 50, "test");
		ResizableBorderer borderer = new ResizableBorderer();
		borderer.setFixedSize(true);
		text.addRole(borderer);
		text.addRole(new ResizableAnchorer());
		text.getRole(Anchored.TYPE).setAnchorPosition(new Coordinate(50, 90));
		text.setFontColor("blue");
		GIcon anchoredIcon = new GIcon(500, 80, 20, 20, urls.get(2));
		Coordinate anchoredIconMarkerCoordinate = new Coordinate(anchoredIcon.getPosition());
		anchoredIconMarkerCoordinate.setY(anchoredIconMarkerCoordinate.getY() + 40);
		anchoredIcon.addRole(new ResizableAnchorer(anchoredIconMarkerCoordinate, MarkerShape.CIRCLE));
		add(text);
		add(rect);
		add(circle);
		add(ellipse);
		add(image);
		add(path);
		add(anchoredIcon);
	}
	
	private List<String> urls = new ArrayList<String>(Arrays.asList(GWT.getModuleBaseURL() + "image/slider.gif",
			GWT.getModuleBaseURL() + "image/cloud.png",
			GWT.getModuleBaseURL() + "image/sun.jpg"));
	
	public TransformingGroup getRootContainer() {
		return rootContainer;
	}

	public void onPreviewNativeEvent(NativePreviewEvent event) {
		Element relatedEventTarget = event.getNativeEvent().getEventTarget().cast();
		if (relatedEventTarget != null && canvas.isAttached()) {
			if (DOM.isOrHasChild(canvas.getElement(), relatedEventTarget)) {
				event.getNativeEvent().preventDefault();
			}
		}
	}

	public Widget asWidget() {
		return canvas;
	}

	@Override
	public Coordinate getScreenCoordinate(MouseEvent<?> event) {
		return new Coordinate(event.getRelativeX(canvas.getElement()), event.getRelativeY(canvas.getElement()));
	}

	@Override
	public Coordinate transform(Coordinate coordinate, Space from, Space to) {
		if (from == to) {
			return (Coordinate) coordinate.clone();
		}
		double x = coordinate.getX();
		double y = coordinate.getY();
		switch (to) {
			case SCREEN:
				double xs = x * rootContainer.getScaleX() + rootContainer.getDeltaX();
				double ys = y * rootContainer.getScaleY() + rootContainer.getDeltaY();
				return new Coordinate(xs, ys);
			case USER:
			default:
				double xu = (x - rootContainer.getDeltaX()) / rootContainer.getScaleX();
				double yu = (y - rootContainer.getDeltaY()) / rootContainer.getScaleY();
				return new Coordinate(xu, yu);
		}
	}

	@Override
	public Bbox transform(Bbox bounds, Space from, Space to) {
		Coordinate p1 = transform(BboxService.getOrigin(bounds), from, to);
		Coordinate p2 = transform(BboxService.getEndPoint(bounds), from, to);
		return new Bbox(Math.min(p1.getX(), p2.getX()), Math.min(p1.getY(), p2.getY()),
				Math.abs(p1.getX() - p2.getX()), Math.abs(p1.getY() - p2.getY()));
	}

	@Override
	public BboxPosition transform(BboxPosition position, Space from, Space to) {
		switch (position) {
			case CORNER_LL:
				return BboxPosition.CORNER_UL;
			case CORNER_LR:
				return BboxPosition.CORNER_UR;
			case CORNER_UL:
				return BboxPosition.CORNER_LL;
			case CORNER_UR:
				return BboxPosition.CORNER_LR;
			case MIDDLE_LEFT:
				return BboxPosition.MIDDLE_LEFT;
			case MIDDLE_LOW:
				return BboxPosition.MIDDLE_UP;
			case MIDDLE_RIGHT:
				return BboxPosition.MIDDLE_RIGHT;
			case MIDDLE_UP:
			default:
				return BboxPosition.MIDDLE_LOW;
		}
	}

}
