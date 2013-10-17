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
package org.geomajas.graphics.client.service;

import org.vaadin.gwtgraphics.client.VectorObject;
import org.vaadin.gwtgraphics.client.VectorObjectContainer;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent.Type;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * {@link VectorObjectContainer} wrapper that can stop event propagation and act as handler registrar.
 * 
 * @author Jan De Moerloose
 * 
 */
public class HasHandlerVectorObjectContainer implements HasAllMouseAndClickHandlers, VectorObjectContainer, IsWidget {

	private VectorObjectContainer group;

	private boolean stopPropagation;

	public HasHandlerVectorObjectContainer(VectorObjectContainer group, boolean stopPropagation) {
		this.group = group;
		this.stopPropagation = stopPropagation;
		StopPropagation s = new StopPropagation();
		addMouseDownHandler(s);
		addMouseUpHandler(s);
		addMouseWheelHandler(s);
		addMouseMoveHandler(s);
		addMouseOverHandler(s);
		addMouseOutHandler(s);
		addClickHandler(s);
		addDoubleClickHandler(s);
	}

	@Override
	public <H extends EventHandler> HandlerRegistration addDomHandler(H handler, Type<H> type) {
		return ((Widget) group).addDomHandler(handler, type);
	}

	public VectorObject add(VectorObject vo) {
		return group.add(vo);
	}

	public VectorObject insert(VectorObject vo, int beforeIndex) {
		return group.insert(vo, beforeIndex);
	}

	public VectorObject remove(VectorObject vo) {
		return group.remove(vo);
	}

	public VectorObject bringToFront(VectorObject vo) {
		return group.bringToFront(vo);
	}

	public VectorObject moveToBack(VectorObject vo) {
		return group.moveToBack(vo);
	}

	public void clear() {
		group.clear();
	}

	public VectorObject getVectorObject(int index) {
		return group.getVectorObject(index);
	}

	public int indexOf(VectorObject vo) {
		return group.indexOf(vo);
	}

	public int getVectorObjectCount() {
		return group.getVectorObjectCount();
	}

	public boolean isStopPropagation() {
		return stopPropagation;
	}

	public void setStopPropagation(boolean stopPropagation) {
		this.stopPropagation = stopPropagation;
	}

	@Override
	public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
		return ((Widget) group).addDomHandler(handler, MouseDownEvent.getType());
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		((Widget) group).fireEvent(event);
	}

	@Override
	public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
		return ((Widget) group).addDomHandler(handler, MouseUpEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return ((Widget) group).addDomHandler(handler, MouseOutEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		return ((Widget) group).addDomHandler(handler, MouseOverEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
		return ((Widget) group).addDomHandler(handler, MouseMoveEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
		return ((Widget) group).addDomHandler(handler, MouseWheelEvent.getType());
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return ((Widget) group).addDomHandler(handler, ClickEvent.getType());
	}

	@Override
	public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
		return ((Widget) group).addDomHandler(handler, DoubleClickEvent.getType());
	}

	@Override
	public Widget asWidget() {
		return (Widget) group;
	}

	/**
	 * Stops event propagation.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class StopPropagation implements MouseDownHandler, MouseUpHandler, MouseWheelHandler, MouseMoveHandler,
			MouseOutHandler, MouseOverHandler, ClickHandler, DoubleClickHandler {

		@Override
		public void onDoubleClick(DoubleClickEvent event) {
			checkPropagation(event);
		}

		@Override
		public void onClick(ClickEvent event) {
			checkPropagation(event);
		}

		@Override
		public void onMouseOver(MouseOverEvent event) {
			checkPropagation(event);
		}

		@Override
		public void onMouseOut(MouseOutEvent event) {
			checkPropagation(event);
		}

		@Override
		public void onMouseMove(MouseMoveEvent event) {
			checkPropagation(event);
		}

		@Override
		public void onMouseWheel(MouseWheelEvent event) {
			checkPropagation(event);
		}

		@Override
		public void onMouseUp(MouseUpEvent event) {
			checkPropagation(event);
		}

		@Override
		public void onMouseDown(MouseDownEvent event) {
			checkPropagation(event);
		}

		private void checkPropagation(MouseEvent<?> event) {
			if (stopPropagation) {
				event.stopPropagation();
			}
		}

	}

}
