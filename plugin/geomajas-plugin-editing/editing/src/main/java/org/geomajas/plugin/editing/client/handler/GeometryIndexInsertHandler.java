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

package org.geomajas.plugin.editing.client.handler;

import java.util.Collections;
import java.util.List;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.handler.MapDownHandler;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.plugin.editing.client.operation.GeometryOperationFailedException;
import org.geomajas.plugin.editing.client.service.GeometryEditState;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexType;

import com.google.gwt.event.dom.client.HumanInputEvent;

/**
 * ...
 * 
 * @author Pieter De Graef
 */
public class GeometryIndexInsertHandler extends AbstractGeometryIndexMapHandler implements MapDownHandler {

	public void onDown(HumanInputEvent<?> event) {
		// This handler should only have been applied onto edges. Can't hurt to check again:
		if (service.getEditingState() == GeometryEditState.IDLE
				&& service.getIndexService().getType(index) == GeometryIndexType.TYPE_EDGE) {
			List<GeometryIndex> indices = Collections.singletonList(index);
			try {
				// If this edge is highlighted, end it:
				if (service.getIndexStateService().isHightlighted(index)) {
					service.getIndexStateService().highlightEnd(indices);
				}

				// First insert the point into the geometry:
				Coordinate location = getLocation(event, RenderSpace.WORLD);
				service.insert(indices, Collections.singletonList(Collections.singletonList(location)));

				// Then change the selection to the newly inserted point:
				service.getIndexStateService().deselectAll();
				service.getIndexStateService().select(
						Collections.singletonList(service.getIndexService().getNextVertex(index)));

				// Set status to dragging:
				service.setEditingState(GeometryEditState.DRAGGING);
			} catch (GeometryOperationFailedException e) {
			}
		}
	}
}