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
package org.geomajas.widget.utility.gwt.client;

import java.util.List;

import org.geomajas.configuration.client.ClientToolInfo;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.utility.common.client.ribbon.RibbonColumn;
import org.geomajas.widget.utility.gwt.client.ribbon.RibbonColumnRegistry;
import org.geomajas.widget.utility.gwt.client.ribbon.map.DisplayCoordinatesRibbonColumn;

import com.google.gwt.core.client.EntryPoint;


/**
 * Entry point for the widget utility plugin, registers the widgets on loading.
 * 
 * @author Oliver May
 *
 */
public class GwtUtilityWidgetsEntryPoint implements EntryPoint {

	@Override
	public void onModuleLoad() {
		RibbonColumnRegistry.put(DisplayCoordinatesRibbonColumn.IDENTIFIER, 
				new RibbonColumnRegistry.RibbonColumnCreator() {

			public RibbonColumn create(List<ClientToolInfo> tools, MapWidget mapWidget) {
				return new DisplayCoordinatesRibbonColumn(mapWidget);
			}
		});
	}

}
