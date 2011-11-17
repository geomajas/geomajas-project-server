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

import org.geomajas.geometry.Geometry;
import org.geomajas.gwt.client.handler.MapDownHandler;
import org.geomajas.plugin.editing.client.service.GeometryEditingState;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.geomajas.plugin.editing.client.service.GeometryIndexNotFoundException;

import com.google.gwt.event.dom.client.HumanInputEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;

/**
 * Recognizes a certain vertex during inserting for which a click/tap event ends the inserting state. For LineStrings
 * this would be the lastly inserted vertex, for LinearRings this would be the first vertex. When the inserting state is
 * ended, it is switched to <code>GeometryEditingState.IDLE</code>.
 * 
 * @author Pieter De Graef
 */
public class GeometryIndexStopInsertingHandler extends AbstractGeometryIndexMapHandler implements MapDownHandler,
		MouseOverHandler, MouseOutHandler {

	public void onDown(HumanInputEvent<?> event) {
		if (service.getEditingState() == GeometryEditingState.INSERTING && isCorrectVertex()) {
			service.setEditingState(GeometryEditingState.IDLE);
		}
	}

	public void onMouseOut(MouseOutEvent event) {
		if (service.getEditingState() == GeometryEditingState.INSERTING && isCorrectVertex()) {
			service.getIndexStateService().highlightEnd(Collections.singletonList(index));
		}
	}

	public void onMouseOver(MouseOverEvent event) {
		if (service.getEditingState() == GeometryEditingState.INSERTING && isCorrectVertex()) {
			service.getIndexStateService().highlightBegin(Collections.singletonList(index));
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
				return 0 == getLastValue(index);
			}
		} catch (GeometryIndexNotFoundException e) {
		}
		return false;
	}

	private int getLastValue(GeometryIndex index) {
		if (index.hasChild()) {
			return getLastValue(index.getChild());
		}
		return index.getValue();
	}
}