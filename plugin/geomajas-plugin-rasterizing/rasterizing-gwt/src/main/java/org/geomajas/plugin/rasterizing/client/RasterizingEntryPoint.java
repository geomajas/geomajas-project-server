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
package org.geomajas.plugin.rasterizing.client;

import org.geomajas.gwt.client.action.ToolCreator;
import org.geomajas.gwt.client.action.ToolbarBaseAction;
import org.geomajas.gwt.client.action.toolbar.ToolbarRegistry;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.rasterizing.client.action.toolbar.GetLegendImageAction;
import org.geomajas.plugin.rasterizing.client.action.toolbar.GetMapImageAction;

import com.google.gwt.core.client.EntryPoint;

/**
 * {@link EntryPoint} for rasterizing GWT module.
 * 
 * @author Jan De Moerloose
 * 
 */
public class RasterizingEntryPoint implements EntryPoint {

	/**
	 * Registers toolbar actions.
	 */
	public void onModuleLoad() {
		ToolbarRegistry.put(RasterizingToolId.GET_LEGEND_IMAGE, new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new GetLegendImageAction(mapWidget);
			}
		});
		ToolbarRegistry.put(RasterizingToolId.GET_MAP_IMAGE, new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new GetMapImageAction(mapWidget);
			}
		});
	}

}
