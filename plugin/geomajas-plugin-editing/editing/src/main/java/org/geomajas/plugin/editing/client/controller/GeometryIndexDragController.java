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
import org.geomajas.gwt.client.controller.AbstractController;
import org.geomajas.gwt.client.controller.MapEventParser;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.plugin.editing.client.service.GeometryEditingService;
import org.geomajas.plugin.editing.client.service.GeometryEditingState;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexNotFoundException;

import com.google.gwt.event.dom.client.HumanInputEvent;

/**
 * Default controller to be used when in dragging mode during editing. It will allow the user to drag the selected
 * indices about.
 * 
 * @author Pieter De Graef
 */
public class GeometryIndexDragController extends AbstractController {

	private GeometryEditingService service;

	private Coordinate previous;

	public GeometryIndexDragController(GeometryEditingService service, MapEventParser mapEventParser) {
		super(mapEventParser, service.getEditingState() == GeometryEditingState.DRAGGING);
		this.service = service;
	}

	public void onDown(HumanInputEvent<?> event) {
		previous = getLocation(event, RenderSpace.WORLD);
	}

	public void onDrag(HumanInputEvent<?> event) {
		// First calculate moving delta:
		Coordinate worldPos = getLocation(event, RenderSpace.WORLD);
		double deltaX = worldPos.getX() - previous.getX();
		double deltaY = worldPos.getY() - previous.getY();

		try {
			// Go over all selected indices and find their new positions:
			List<List<Coordinate>> coordinateList = new ArrayList<List<Coordinate>>();
			for (GeometryIndex selected : service.getSelection()) {
				switch (service.getIndexService().getType(selected)) {
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

	public void onUp(HumanInputEvent<?> event) {
		previous = null;
		service.setEditingState(GeometryEditingState.IDLE);
	}
}