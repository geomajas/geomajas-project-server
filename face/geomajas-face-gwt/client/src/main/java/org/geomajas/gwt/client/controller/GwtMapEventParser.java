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

package org.geomajas.gwt.client.controller;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.handler.MapEventParser;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.gwt.client.util.GwtEventUtil;
import org.geomajas.gwt.client.widget.MapWidget;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.TouchEvent;

/**
 * Implementation of the {@link MapEventParser} interface for the GWT face.
 * 
 * @author Pieter De Graef
 */
public class GwtMapEventParser implements MapEventParser {

	private MapWidget mapWidget;

	private int offsetX;

	private int offsetY;

	public GwtMapEventParser(MapWidget mapWidget, int offsetX, int offsetY) {
		this.mapWidget = mapWidget;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}

	public Coordinate getLocation(HumanInputEvent<?> event, RenderSpace renderSpace) {
		switch (renderSpace) {
			case WORLD:
				Coordinate screen = getLocation(event, RenderSpace.SCREEN);
				return mapWidget.getMapModel().getMapView().getWorldViewTransformer().viewToWorld(screen);
			case SCREEN:
			default:
				if (event instanceof MouseEvent<?>) {
					return GwtEventUtil.getPosition((MouseEvent<?>) event, offsetX, offsetY);
				} else if (event instanceof TouchEvent<?>) {
					Touch touch = ((TouchEvent<?>) event).getTouches().get(0);
					return new Coordinate(touch.getClientX(), touch.getClientY());
				}
				return new Coordinate(event.getNativeEvent().getClientX(), event.getNativeEvent().getClientY());
		}
	}

	public Element getTarget(DomEvent<?> event) {
		return GwtEventUtil.getTarget(event);
	}

	public String getTargetId(DomEvent<?> event) {
		return GwtEventUtil.getTargetId(event);
	}

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	public int getOffsetX() {
		return offsetX;
	}

	public void setOffsetX(int offsetX) {
		this.offsetX = offsetX;
	}

	public int getOffsetY() {
		return offsetY;
	}

	public void setOffsetY(int offsetY) {
		this.offsetY = offsetY;
	}
}