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

import org.geomajas.gwt.client.handler.MapDownHandler;
import org.geomajas.plugin.editing.client.operation.GeometryOperationFailedException;
import org.geomajas.plugin.editing.client.service.GeometryEditState;

import com.google.gwt.event.dom.client.HumanInputEvent;

/**
 * Prepares dragging on mouse down or on touch start.
 * 
 * @author Pieter De Graef
 */
public class GeometryIndexDragSelectionHandler extends AbstractGeometryIndexMapHandler implements MapDownHandler {

	public void onDown(HumanInputEvent<?> event) {
		if (service.getIndexStateService().isSelected(index)) {
			service.setEditingState(GeometryEditState.DRAGGING);

			if (service.isOperationSequenceActive()) {
				service.stopOperationSequence();
			}
			try {
				service.startOperationSequence();
			} catch (GeometryOperationFailedException e) {
				throw new IllegalStateException(e);
			}
		}
	}
}