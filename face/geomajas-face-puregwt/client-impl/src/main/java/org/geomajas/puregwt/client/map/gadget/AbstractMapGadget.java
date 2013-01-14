/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.puregwt.client.map.gadget;

import org.geomajas.puregwt.client.map.MapGadget;
import org.geomajas.puregwt.client.map.MapPresenter;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.layout.client.Layout.Alignment;

/**
 * Simple base implementation of a {@link MapGadget}. This can be used as a base for your gadgets.
 * 
 * @author Pieter De Graef
 */
public abstract class AbstractMapGadget implements MapGadget {

	protected MapPresenter mapPresenter;

	protected int horizontalMargin;

	protected int verticalMargin;

	protected Alignment horizontalAlignment = Alignment.BEGIN;

	protected Alignment verticalAlignment = Alignment.BEGIN;

	public void beforeDraw(MapPresenter mapPresenter) {
		this.mapPresenter = mapPresenter;
	}

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	public int getHorizontalMargin() {
		return horizontalMargin;
	}

	public void setHorizontalMargin(int horizontalMargin) {
		this.horizontalMargin = horizontalMargin;
	}

	public int getVerticalMargin() {
		return verticalMargin;
	}

	public void setVerticalMargin(int verticalMargin) {
		this.verticalMargin = verticalMargin;
	}

	public Alignment getHorizontalAlignment() {
		return horizontalAlignment;
	}

	public void setHorizontalAlignment(Alignment horizontalAlignment) {
		this.horizontalAlignment = horizontalAlignment;
	}

	public Alignment getVerticalAlignment() {
		return verticalAlignment;
	}

	public void setVerticalAlignment(Alignment verticalAlignment) {
		this.verticalAlignment = verticalAlignment;
	}

	public int getWidth() {
		return asWidget().getOffsetWidth();
	}

	public int getHeight() {
		return asWidget().getOffsetHeight();
	}

	public void setWidth(int width) {
		asWidget().setWidth(width + "px");
	}

	public void setHeight(int height) {
		asWidget().setHeight(height + "px");
	}

	public void setTop(int top) {
		asWidget().getElement().getStyle().setTop(top, Unit.PX);
	}

	public void setLeft(int left) {
		asWidget().getElement().getStyle().setLeft(left, Unit.PX);
	}

	public void addResizeHandler(ResizeHandler resizeHandler) {
		asWidget().addHandler(resizeHandler, ResizeEvent.getType());
	}

	/**
	 * Combination of different handlers with a single goal: stop all the events from propagating to the map. This is
	 * meant to be used for clickable widgets.
	 * 
	 * @author Pieter De Graef
	 */
	public class StopPropagationHandler implements MouseDownHandler, MouseUpHandler, ClickHandler, DoubleClickHandler {

		public void onDoubleClick(DoubleClickEvent event) {
			event.stopPropagation();
		}

		public void onClick(ClickEvent event) {
			event.stopPropagation();
		}

		public void onMouseDown(MouseDownEvent event) {
			event.stopPropagation();
			event.preventDefault();
		}

		public void onMouseUp(MouseUpEvent event) {
			event.stopPropagation();
		}
	}
}