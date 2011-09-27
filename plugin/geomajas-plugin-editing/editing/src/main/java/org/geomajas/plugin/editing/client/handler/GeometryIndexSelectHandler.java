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

import org.geomajas.gwt.client.handler.MapDownHandler;

import com.google.gwt.event.dom.client.HumanInputEvent;

/**
 * ...
 * 
 * @author Pieter De Graef
 */
public class GeometryIndexSelectHandler extends AbstractGeometryIndexMapHandler implements MapDownHandler {

	public void onDown(HumanInputEvent<?> event) {
		if (event.isShiftKeyDown()) {
			// Add to or remove from selection:
			if (service.isSelected(index)) {
				service.deselect(Collections.singletonList(index));
			} else {
				service.select(Collections.singletonList(index));
			}
			event.stopPropagation();
		} else {
			// Deselect all and select only this index:
			service.deselectAll();
			service.select(Collections.singletonList(index));
		}
	}
}