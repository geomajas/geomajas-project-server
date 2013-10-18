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
package org.geomajas.graphics.client.object;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.service.BboxService;
import org.geomajas.graphics.client.object.role.Textable;
import org.geomajas.graphics.client.util.FlipState;
import org.vaadin.gwtgraphics.client.VectorObject;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * DecoratedPopupPanel centered around it's position.
 * 
 * @author Jan Venstermans
 * 
 */
public class DraggableDecoratedPopupPanel extends DecoratedPopupPanel implements Resizable, Draggable,
		Textable, MouseDownHandler, MouseUpHandler, MouseMoveHandler, MouseOutHandler {

	private Label label;
	
	private Coordinate coordinate;
	
	private boolean dragging;
	
	public DraggableDecoratedPopupPanel(String text) {
		label = new Label();
		label.setText(text);
		add(label);
		setGlassEnabled(false);
		
		addDomHandler(this, MouseDownEvent.getType());
		addDomHandler(this, MouseUpEvent.getType());
		addDomHandler(this, MouseMoveEvent.getType());
		addDomHandler(this, MouseOutEvent.getType());
	}

	@Override
	public void setLabel(String text) {
		label.setText(text);
	}

	@Override
	public String getLabel() {
		return label.getText();
	}

	@Override
	public void setFontColor(String color) {
		label.getElement().getStyle().setColor(color);
	}

	@Override
	public String getFontColor() {
		return label.getElement().getStyle().getColor();
	}

	@Override
	public void setFontSize(int size) {
		label.getElement().getStyle().setFontSize(size, Style.Unit.PX);
	}

	@Override
	public int getFontSize() {
		return Integer.parseInt(label.getElement().getStyle().getFontSize());
	}

	@Override
	public void setFontFamily(String font) {
//		labeler.setFontFamily(font);
	}

	@Override
	public String getFontFamily() {
//		return label.getElement().getStyle().getfo.getFontFamily();
		return "arial";
	}

	@Override
	public VectorObject asObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPosition(Coordinate position) {
		this.coordinate = position;
		centerText();
	}

	private void centerText() {
		// int upperLeftX = (int) coordinate.getX() - (getOffsetWidth()) /2;
//		int upperLeftY = (int) coordinate.getY() - (getOffsetHeight()) /2;
		int upperLeftX = (int) coordinate.getX();
		int upperLeftY = (int) coordinate.getY();
		setPopupPosition(upperLeftX, upperLeftY);
	}

	@Override
	public Coordinate getPosition() {
		return coordinate;
	}

	@Override
	public Bbox getUserBounds() {
		// TODO
		return new Bbox(getAbsoluteLeft(), getAbsoluteTop(), getOffsetWidth(), getOffsetHeight());
	}

	@Override
	public void flip(FlipState state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUserBounds(Bbox bounds) {
		setPosition(BboxService.getCenterPoint(bounds));
		setWidth(bounds.getWidth() + "");
		setHeight(bounds.getHeight() + "");
	}

	@Override
	public Bbox getBounds() {
		return new Bbox(getAbsoluteLeft(), getAbsoluteTop(), getOffsetWidth(), getOffsetHeight());
	}

	@Override
	public boolean isPreserveRatio() {
		return false;
	}

	@Override
	public boolean isAutoHeight() {
		return false;
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) {
//		if(dragging){
//			setPosition(new Coordinate(event.getScreenX(), event.getScreenY()));
//		}
	}

	@Override
	public void onMouseUp(MouseUpEvent event) {
		dragging = false;
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
		dragging = true;
		getPosition();
		setPosition(new Coordinate(event.getClientX(), event.getClientY()));
		getOffsetWidth();
		getOffsetHeight();
		getAbsoluteLeft();
		getAbsoluteTop();
		event.getClientX();
		event.getClientY();
	}

	@Override
	public void onMouseOut(MouseOutEvent event) {
//		dragging = false;
	}

}
