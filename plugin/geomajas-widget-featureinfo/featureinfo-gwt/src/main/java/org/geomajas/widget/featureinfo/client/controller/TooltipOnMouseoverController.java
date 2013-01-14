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

package org.geomajas.widget.featureinfo.client.controller;

import org.geomajas.gwt.client.controller.listener.ListenerController;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.featureinfo.client.action.toolbar.TooltipOnMouseoverListener;

/**
 * 
 * @author Kristof Heirwegh
 * @author Oliver May
 * 
 */
public class TooltipOnMouseoverController extends ListenerController {

	public TooltipOnMouseoverController(MapWidget mapWidget) {
		super(mapWidget, new TooltipOnMouseoverListener(mapWidget));
	}
	
	public TooltipOnMouseoverController(MapWidget mapWidget, int pixelTolerance) {
		this(mapWidget);
		((TooltipOnMouseoverListener) getListener()).setPixelTolerance(pixelTolerance);
	}
}
