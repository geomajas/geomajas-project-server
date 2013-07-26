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
package org.geomajas.gwt.client.controller;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.map.MapPresenter;
import org.geomajas.gwt.client.map.RenderSpace;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.TouchEvent;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Utility methods for acquiring information out of events that come from the map.
 * 
 * @author Pieter De Graef
 */
public class MapEventParserImpl implements MapEventParser {

	private final MapPresenter mapPresenter;

	/**
	 * This object must be initialized with the map it's supposed to interpret the events from.
	 * 
	 * @param mapPresenter
	 */
	@Inject
	public MapEventParserImpl(@Assisted MapPresenter mapPresenter) {
		this.mapPresenter = mapPresenter;
	}

	@Override
	public Coordinate getLocation(HumanInputEvent<?> event, RenderSpace renderSpace) {
		switch (renderSpace) {
			case WORLD:
				Coordinate screen = getLocation(event, RenderSpace.SCREEN);
				return mapPresenter.getViewPort().transform(screen, RenderSpace.SCREEN, RenderSpace.WORLD);
			case SCREEN:
			default:
				if (event instanceof MouseEvent<?>) {
					Element element = mapPresenter.asWidget().getElement();
					double offsetX = ((MouseEvent<?>) event).getRelativeX(element);
					double offsetY = ((MouseEvent<?>) event).getRelativeY(element);
					return new Coordinate(offsetX, offsetY);
				} else if (event instanceof TouchEvent<?>) {
					Touch touch = ((TouchEvent<?>) event).getTouches().get(0);
					return new Coordinate(touch.getClientX(), touch.getClientY());
				}
				return new Coordinate(event.getNativeEvent().getClientX(), event.getNativeEvent().getClientY());
		}
	}

	@Override
	public Element getTarget(HumanInputEvent<?> event) {
		EventTarget target = event.getNativeEvent().getEventTarget();
		if (Element.is(target)) {
			return Element.as(target);
		}
		return null;
	}
}