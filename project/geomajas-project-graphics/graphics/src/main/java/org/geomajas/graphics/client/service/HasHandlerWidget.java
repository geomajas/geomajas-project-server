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
 * {@link Widget} wrapper that can stop event propagation and act as handler registrar.
 * 
 * @author Jan De Moerloose
 * 
 */
public class HasHandlerWidget implements HasAllMouseAndClickHandlers, IsWidget {

	private Widget widget;

	private boolean stopPropagation;

	public HasHandlerWidget(Widget widget, boolean stopPropagation) {
		this.widget = widget;
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
		return widget.addDomHandler(handler, type);
	}

	public boolean isStopPropagation() {
		return stopPropagation;
	}

	public void setStopPropagation(boolean stopPropagation) {
		this.stopPropagation = stopPropagation;
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
		return widget.addDomHandler(handler, MouseDownEvent.getType());
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		widget.fireEvent(event);
	}

	@Override
	public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
		return widget.addDomHandler(handler, MouseUpEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return widget.addDomHandler(handler, MouseOutEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		return widget.addDomHandler(handler, MouseOverEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
		return widget.addDomHandler(handler, MouseMoveEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
		return widget.addDomHandler(handler, MouseWheelEvent.getType());
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return widget.addDomHandler(handler, ClickEvent.getType());
	}

	@Override
	public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
		return widget.addDomHandler(handler, DoubleClickEvent.getType());
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
