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

package org.geomajas.plugin.editing.client.handler;

import java.util.Collections;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.gwt.client.handler.MapDownHandler;
import org.geomajas.plugin.editing.client.service.GeometryEditState;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexNotFoundException;
import org.geomajas.plugin.editing.client.service.GeometryIndexType;

import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;

/**
 * <p>
 * Recognizes a certain vertex during inserting for which a click/tap event ends the inserting state. For LineStrings
 * this would be the lastly inserted vertex, for LinearRings this would be the first vertex. When the inserting state is
 * ended, it is switched to <code>GeometryEditingState.IDLE</code>.
 * </p>
 * <p>
 * If the service is busy inserting (state = INSERTING), and a point is recognized for highlighting, this handler will
 * also snap to it.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class GeometryIndexStopInsertingHandler extends AbstractGeometryIndexMapHandler implements MapDownHandler,
		MouseOverHandler, MouseOutHandler, MouseMoveHandler {

	public void onDown(HumanInputEvent<?> event) {
		if (service.getEditingState() == GeometryEditState.INSERTING && isCorrectVertex()) {
			service.setEditingState(GeometryEditState.IDLE);
			service.getIndexStateService().highlightEnd(Collections.singletonList(index));
		}
	}

	public void onMouseOut(MouseOutEvent event) {
		if (service.getEditingState() == GeometryEditState.INSERTING && isCorrectVertex()) {
			service.getIndexStateService().highlightEnd(Collections.singletonList(index));
		}
	}

	public void onMouseMove(MouseMoveEvent event) {
		// Stop the propagation (if correct vertex), so the insert controller doesn't alter the TentativeMove location:
		if (service.getEditingState() == GeometryEditState.INSERTING && isCorrectVertex()) {
			event.stopPropagation();
		}
	}

	public void onMouseOver(MouseOverEvent event) {
		if (service.getEditingState() == GeometryEditState.INSERTING && isCorrectVertex()) {

			// Now snap the vertex to this location:
			if (service.getIndexService().getType(index) == GeometryIndexType.TYPE_VERTEX) {
				try {
					service.getIndexStateService().highlightBegin(Collections.singletonList(index));
					Coordinate location = getSnapLocation();
					service.setTentativeMoveLocation(location);
				} catch (GeometryIndexNotFoundException e) {
					throw new IllegalStateException(e);
				}
			}
		}
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private boolean isCorrectVertex() {
		try {
			String geomType = service.getIndexService().getGeometryType(service.getGeometry(), index);
			if (Geometry.LINE_STRING.equals(geomType)) {
				GeometryIndex temp = service.getIndexService().getPreviousVertex(service.getInsertIndex());
				return temp.equals(index);
			} else if (Geometry.LINEAR_RING.equals(geomType)) {
				return 0 == service.getIndexService().getValue(index);
			}
		} catch (GeometryIndexNotFoundException e) {
			throw new IllegalStateException(e);
		}
		return false;
	}

	private Coordinate getSnapLocation() throws GeometryIndexNotFoundException {
		String geomType = service.getIndexService().getGeometryType(service.getGeometry(), index);
		if (Geometry.LINE_STRING.equals(geomType)) {
			GeometryIndex temp = service.getIndexService().getPreviousVertex(service.getInsertIndex());
			return service.getIndexService().getVertex(service.getGeometry(), temp);
		} else if (Geometry.LINEAR_RING.equals(geomType)) {
			return service.getIndexService().getSiblingVertices(service.getGeometry(), index)[0];
		}
		return service.getIndexService().getVertex(service.getGeometry(), index);
	}
}