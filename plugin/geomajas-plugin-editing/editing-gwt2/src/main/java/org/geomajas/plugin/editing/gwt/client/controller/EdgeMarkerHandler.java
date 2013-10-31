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

package org.geomajas.plugin.editing.gwt.client.controller;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.controller.MapEventParser;
import org.geomajas.gwt2.client.event.ViewPortChangedEvent;
import org.geomajas.gwt2.client.event.ViewPortChangedHandler;
import org.geomajas.gwt2.client.event.ViewPortScaledEvent;
import org.geomajas.gwt2.client.event.ViewPortTranslatedEvent;
import org.geomajas.gwt2.client.gfx.GfxUtil;
import org.geomajas.gwt2.client.gfx.VectorContainer;
import org.geomajas.gwt.client.handler.MapDownHandler;
import org.geomajas.gwt2.client.map.MapPresenter;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.plugin.editing.client.service.GeometryEditService;
import org.geomajas.plugin.editing.client.service.GeometryEditState;
import org.vaadin.gwtgraphics.client.shape.Path;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;

/**
 * ...
 * 
 * @author Pieter De Graef
 */
public class EdgeMarkerHandler implements MouseOutHandler, MouseOverHandler, MouseMoveHandler, MapDownHandler {

	private static final int MARKER_SIZE = 6;

	private FeatureStyleInfo style;

	private MapPresenter mapPresenter;

	private GeometryEditService service;

	private MapEventParser eventParser;

	private GfxUtil gfxUtil;

	private VectorContainer container;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	public EdgeMarkerHandler(MapPresenter mapPresenter, GeometryEditService service, MapEventParser eventParser,
			GfxUtil gfxUtil) {
		this.mapPresenter = mapPresenter;
		this.service = service;
		this.eventParser = eventParser;
		this.gfxUtil = gfxUtil;

		style = new FeatureStyleInfo();
		style.setFillColor("#444444");
		style.setFillOpacity(0f);
		style.setStrokeColor("#444444");
		style.setStrokeOpacity(0.8f);
		style.setStrokeWidth(1);

		mapPresenter.getEventBus().addViewPortChangedHandler(new ViewPortChangedHandler() {

			public void onViewPortTranslated(ViewPortTranslatedEvent event) {
				cleanup();
			}

			public void onViewPortScaled(ViewPortScaledEvent event) {
				cleanup();
			}

			public void onViewPortChanged(ViewPortChangedEvent event) {
				cleanup();
			}
		});
	}

	// ------------------------------------------------------------------------
	// MapEventParser implementation:
	// ------------------------------------------------------------------------

	public Coordinate getLocation(HumanInputEvent<?> event, RenderSpace renderSpace) {
		return eventParser.getLocation(event, renderSpace);
	}

	public Element getTarget(HumanInputEvent<?> event) {
		return eventParser.getTarget(event);
	}

	// ------------------------------------------------------------------------
	// Handler implementations:
	// ------------------------------------------------------------------------

	public void onDown(HumanInputEvent<?> event) {
		cleanup();
	}

	public void onMouseOut(MouseOutEvent event) {
		cleanup();
	}

	public void onMouseOver(MouseOverEvent event) {
		cleanup();
	}

	public void onMouseMove(MouseMoveEvent event) {
		drawEdgeHighlightMarker(eventParser.getLocation(event, RenderSpace.SCREEN));
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private void cleanup() {
		if (container != null) {
			mapPresenter.removeVectorContainer(container);
			container = null;
		}
	}

	private void drawEdgeHighlightMarker(Coordinate location) {
		if (service.getEditingState() == GeometryEditState.IDLE) {
			if (container == null) {
				container = mapPresenter.addScreenContainer();
			}
			container.clear();
			Coordinate tl = new Coordinate(location.getX() - MARKER_SIZE, location.getY() + MARKER_SIZE);
			Coordinate tr = new Coordinate(location.getX() + MARKER_SIZE, location.getY() + MARKER_SIZE);
			Coordinate bl = new Coordinate(location.getX() - MARKER_SIZE, location.getY() - MARKER_SIZE);
			Coordinate br = new Coordinate(location.getX() + MARKER_SIZE, location.getY() - MARKER_SIZE);

			// Top:
			Path top = new Path(tl.getX(), tl.getY());
			top.lineTo(tr.getX(), tr.getY());
			gfxUtil.applyStroke(top, style.getStrokeColor(), style.getStrokeOpacity(), style.getStrokeWidth(),
					style.getDashArray());
			container.add(top);

			Path right = new Path(tr.getX(), tr.getY());
			right.lineTo(br.getX(), br.getY());
			gfxUtil.applyStroke(right, style.getStrokeColor(), style.getStrokeOpacity(), style.getStrokeWidth(),
					style.getDashArray());
			container.add(right);

			Path bottom = new Path(br.getX(), br.getY());
			bottom.lineTo(bl.getX(), bl.getY());
			gfxUtil.applyStroke(bottom, style.getStrokeColor(), style.getStrokeOpacity(), style.getStrokeWidth(),
					style.getDashArray());
			container.add(bottom);

			Path left = new Path(bl.getX(), bl.getY());
			left.lineTo(tl.getX(), tl.getY());
			gfxUtil.applyStroke(left, style.getStrokeColor(), style.getStrokeOpacity(), style.getStrokeWidth(),
					style.getDashArray());
			container.add(left);
		}
	}
}