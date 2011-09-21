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

package org.geomajas.plugin.editing.client.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.handler.MapDragHandler;
import org.geomajas.gwt.client.handler.MapEventParser;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.editing.client.service.GeometryEditingService;
import org.geomajas.plugin.editing.client.service.GeometryEditingState;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexNotFoundException;

import com.google.gwt.event.dom.client.HumanInputEvent;

/**
 * ...
 * 
 * @author Pieter De Graef
 */
public class MoveGeometrySelectionHandler implements MapDragHandler {

	private GeometryEditingService service;

	private MapWidget mapWidget;

	private Coordinate previous;

	private MapEventParser eventParser;

	public MoveGeometrySelectionHandler(MapWidget mapWidget, GeometryEditingService service,
			MapEventParser mapEventParser) {
		this.mapWidget = mapWidget;
		this.service = service;
		eventParser = mapEventParser;
	}

	public MapEventParser getEventParser() {
		return eventParser;
	}

	public void onDragStart(HumanInputEvent<?> event, Coordinate position) {
		previous = mapWidget.getMapModel().getMapView().getWorldViewTransformer().viewToWorld(position);
	}

	public void onDragMove(HumanInputEvent<?> event, Coordinate position) {
		// First calculate moving delta:
		Coordinate worldPos = mapWidget.getMapModel().getMapView().getWorldViewTransformer().viewToWorld(position);
		double deltaX = worldPos.getX() - previous.getX();
		double deltaY = worldPos.getY() - previous.getY();

		try {
			// Go over all selected indices and find their new positions:
			List<List<Coordinate>> coordinateList = new ArrayList<List<Coordinate>>();
			for (GeometryIndex selected : service.getSelection()) {
				switch (selected.getType()) {
					case TYPE_VERTEX:
						Coordinate from = service.getIndexService().getVertex(service.getGeometry(), selected);
						Coordinate to = new Coordinate(from.getX() + deltaX, from.getY() + deltaY);
						coordinateList.add(Collections.singletonList(to));
						break;
					case TYPE_EDGE:
					default:
						throw new RuntimeException("Not really a RTE. Just not implemented ;-)");
				}
			}

			// Execute the move operation:
			service.move(service.getSelection(), coordinateList);
		} catch (GeometryIndexNotFoundException e) {
			// Something went wrong;
		}

		previous = worldPos;
	}

	public void onDragEnd(HumanInputEvent<?> event, Coordinate position) {
		previous = null;
		service.setEditingState(GeometryEditingState.IDLE);
	}
}