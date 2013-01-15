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

package org.geomajas.widget.searchandfilter.client;

import org.geomajas.gwt.client.action.ToolCreator;
import org.geomajas.gwt.client.action.ToolbarBaseAction;
import org.geomajas.gwt.client.action.toolbar.ToolbarRegistry;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.searchandfilter.client.action.CombinedSearchAction;
import org.geomajas.widget.searchandfilter.client.action.FavouritesSearchAction;
import org.geomajas.widget.searchandfilter.client.action.FreeSearchAction;
import org.geomajas.widget.searchandfilter.client.action.SpatialSearchAction;

import com.google.gwt.core.client.EntryPoint;

/**
 * Initializes the SearchAndFilter plugin.
 *
 * @author Kristof Heirwegh
 */
public class SearchAndFilter implements EntryPoint {

	public void onModuleLoad() {
		ToolbarRegistry.put(FavouritesSearchAction.IDENTIFIER, new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new FavouritesSearchAction();
			}
		});

		ToolbarRegistry.put(CombinedSearchAction.IDENTIFIER, new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new CombinedSearchAction();
			}
		});

		ToolbarRegistry.put(SpatialSearchAction.IDENTIFIER, new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new SpatialSearchAction();
			}
		});

		ToolbarRegistry.put(FreeSearchAction.IDENTIFIER, new ToolCreator() {

			public ToolbarBaseAction createTool(MapWidget mapWidget) {
				return new FreeSearchAction();
			}
		});
	}

}
