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

package org.geomajas.puregwt.client.controller;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.controller.AbstractController;
import org.geomajas.gwt.client.controller.MapEventParser;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.puregwt.client.map.MapPresenter;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.TouchEvent;

/**
 * <p>
 * Extension of the generic {@link AbstractController} that's specific for the pure GWT face. It adds activation and
 * deactivation methods, and applies a {@link MapEventParser} at construction time.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
public abstract class AbstractMapController extends AbstractController implements MapController {

	protected MapPresenter mapPresenter;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	protected AbstractMapController(boolean dragging) {
		super(dragging);
		setMapEventParser(new PureGwtEventParser());
	}

	protected AbstractMapController(MapEventParser eventParser, boolean dragging) {
		super(eventParser, dragging);
	}

	// -------------------------------------------------------------------------
	// MapController implementation:
	// -------------------------------------------------------------------------

	/** {@inheritDoc} */
	public void onActivate(MapPresenter mapPresenter) {
		this.mapPresenter = mapPresenter;
	}

	/** {@inheritDoc} */
	public void onDeactivate(MapPresenter mapPresenter) {
	}

	// -------------------------------------------------------------------------
	// Private classes:
	// -------------------------------------------------------------------------

	/**
	 * Default implementation of a {@link MapEventParser} for the Pure GWT face.
	 * 
	 * @author Pieter De Graef
	 */
	private class PureGwtEventParser implements MapEventParser {

		/** {@inheritDoc} */
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

		/** {@inheritDoc} */
		public Element getTarget(HumanInputEvent<?> event) {
			EventTarget target = event.getNativeEvent().getEventTarget();
			if (Element.is(target)) {
				return Element.as(target);
			}
			return null;
		}

	}
}