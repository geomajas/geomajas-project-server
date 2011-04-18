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
package org.geomajas.widget.searchandfilter.client.widget.search;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.searchandfilter.client.widget.multifeaturelistgrid.MultiFeatureListGrid;

import com.smartgwt.client.util.SC;

/**
 * Registry of SearchWidgets.
 * <p>
 * TODO explain architecture.
 * <p>
 * Caveat: Please keep in mind that searchwidgets always work on a specific map.
 * As there is only one SearchController, which also operates on a specific map
 * the search system can only work with one map.
 *
 * @author Kristof Heirwegh
 */
public final class SearchWidgetRegistry {

	private static final Map<String, SearchWidget> REGISTRY = new LinkedHashMap<String, SearchWidget>();

	private static SearchController SEARCHCONTROLLER;

	private SearchWidgetRegistry() {
		// utility class, hide constructor
	}

	// TODO favoritecontroller

	// ----------------------------------------------------------

	/**
	 * @param searchResultGrid
	 *            can be null, add your own handler to be notified then
	 */
	public static void initialize(MapWidget mapWidget, MultiFeatureListGrid searchResultGrid) {
		SEARCHCONTROLLER = new SearchController(mapWidget);
		if (searchResultGrid != null) {
			SEARCHCONTROLLER.addSearchHandler(searchResultGrid);
		}
	}

	public static void addSearchHandler(SearchHandler handler) {
		if (checkState()) {
			SEARCHCONTROLLER.addSearchHandler(handler);
		}
	}

	public static void removeSearchHandler(SearchHandler handler) {
		if (checkState()) {
			SEARCHCONTROLLER.removeSearchHandler(handler);
		}
	}

	public static void put(SearchWidget widget) {
		if (checkState()) {
			if (null != widget) {
				REGISTRY.put(widget.getSearchWidgetId(), widget);
				widget.addSearchRequestHandler(SEARCHCONTROLLER);
			}
		}
	}

	/**
	 * Will be wrapped in a BasicSearchWidget.
	 * 
	 * @param panel
	 */
	public static void put(SearchPanel panel, String widgetId) {
		if (checkState()) {
			if (null != panel) {
				SearchWidget widget = new BasicSearchWidget(widgetId, panel);
				REGISTRY.put(widgetId, widget);
				widget.addSearchRequestHandler(SEARCHCONTROLLER);
			}
		}
	}

	public static SearchWidget getSearchWidget(String searchWidgetId) {
		return REGISTRY.get(searchWidgetId);
	}

	public static Collection<SearchWidget> getSearchWidgets() {
		return REGISTRY.values();
	}

	public static boolean isInitialized() {
		return (SEARCHCONTROLLER != null);
	}

	private static boolean checkState() {
		if (isInitialized()) {
			return true;
		} else {
			SC.logWarn("SearchWidgetRegistry has not been initialized. call initialize from your entrypoint on program "
					+ "startup.");
			return false;
		}
	}
}
