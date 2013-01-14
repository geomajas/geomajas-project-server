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

package org.geomajas.plugin.geocoder.client;

import com.google.gwt.core.client.EntryPoint;
import org.geomajas.gwt.client.action.ToolCreator;
import org.geomajas.gwt.client.action.ToolbarBaseAction;
import org.geomajas.gwt.client.action.toolbar.ToolbarRegistry;
import org.geomajas.gwt.client.widget.MapWidget;

/**
 * {@link EntryPoint} for the geocoder plug-in.
 *
 * @author Joachim Van der Auwera
 */
public class GeocoderEntryPoint implements EntryPoint {

	public void onModuleLoad() {
		ToolbarRegistry.put(GeocoderToolId.TOOL_GEOCODER, new ToolCreator() {
			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new GeocoderAction(mapWidget);
			}
		});
	}

}
