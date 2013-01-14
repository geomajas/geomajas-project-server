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

package org.geomajas.plugin.editing.gwt.example.client.split;

import org.geomajas.gwt.client.action.menu.ToggleSelectionAction;
import org.geomajas.gwt.client.controller.AbstractGraphicsController;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.gwt.client.widget.MapWidget;

import com.google.gwt.event.dom.client.HumanInputEvent;

/**
 * Controller that selects a feature through a mouse up event. Once selected, it is used to start the splitting
 * procedure.
 * 
 * @author Pieter De Graef
 */
public class SelectForSplitController extends AbstractGraphicsController {

	public SelectForSplitController(MapWidget mapWidget) {
		super(mapWidget);
	}

	public void onUp(HumanInputEvent<?> event) {
		ToggleSelectionAction action = new ToggleSelectionAction(mapWidget, false, 0);
		action.toggle(getLocation(event, RenderSpace.SCREEN), true);
	}
}