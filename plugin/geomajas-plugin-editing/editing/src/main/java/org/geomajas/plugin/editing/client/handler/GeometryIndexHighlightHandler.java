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

import org.geomajas.plugin.editing.client.service.GeometryEditState;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;

/**
 * ...
 * 
 * @author Pieter De Graef
 */
public class GeometryIndexHighlightHandler extends AbstractGeometryIndexMapHandler implements MouseOverHandler,
		MouseOutHandler {

	public void onMouseOver(MouseOverEvent event) {
		if (service.getEditingState() == GeometryEditState.IDLE) {
			service.getIndexStateService().highlightEndAll();
			service.getIndexStateService().highlightBegin(Collections.singletonList(index));
		}
	}

	public void onMouseOut(MouseOutEvent event) {
		service.getIndexStateService().highlightEnd(Collections.singletonList(index));
	}
}