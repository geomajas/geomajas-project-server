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

import org.geomajas.gwt.client.handler.MapOutHandler;
import org.geomajas.gwt.client.handler.MapOverHandler;
import org.geomajas.plugin.editing.client.service.GeometryEditingState;

import com.google.gwt.event.dom.client.HumanInputEvent;

/**
 * ...
 * 
 * @author Pieter De Graef
 */
public class GeometryIndexHighlightHandler extends GeometryIndexMapHandler implements MapOverHandler, MapOutHandler {

	public void onOver(HumanInputEvent<?> event) {
		if (service.getEditingState() != GeometryEditingState.DRAGGING) {
			service.highlightBegin(Collections.singletonList(index));
		}
	}

	public void onOut(HumanInputEvent<?> event) {
		service.highlightEnd(Collections.singletonList(index));
	}
}