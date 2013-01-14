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
package org.geomajas.plugin.printing.client;

import org.geomajas.gwt.client.action.ToolCreator;
import org.geomajas.gwt.client.action.ToolbarBaseAction;
import org.geomajas.gwt.client.action.toolbar.ToolbarRegistry;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.printing.client.action.toolbar.PrintingAction;

import com.google.gwt.core.client.EntryPoint;

/**
 * Initializes the printing plugin.
 * 
 * @author Jan De Moerloose
 * 
 */
public class Printing implements EntryPoint {

	public static final String SHOW_DEFAULT_PRINT_KEY = "ShowDefaultPrint";

	public void onModuleLoad() {
		ToolbarRegistry.put(SHOW_DEFAULT_PRINT_KEY, new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new PrintingAction(mapWidget);
			}
		});
	}

}
