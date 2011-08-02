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

import java.util.LinkedHashMap;
import java.util.Map;

import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.searchandfilter.client.SearchAndFilterMessages;
import org.geomajas.widget.searchandfilter.client.widget.multifeaturelistgrid.MultiFeatureListGrid;
import org.geomajas.widget.searchandfilter.client.widget.search.FavouritesController.FavouriteChangeHandler;
import org.geomajas.widget.searchandfilter.search.dto.AndCriterion;
import org.geomajas.widget.searchandfilter.search.dto.AttributeCriterion;
import org.geomajas.widget.searchandfilter.search.dto.Criterion;
import org.geomajas.widget.searchandfilter.search.dto.GeometryCriterion;
import org.geomajas.widget.searchandfilter.search.dto.OrCriterion;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.util.SC;

/**
 * Registry of {@link SearchWidget}s.
 * <p>
 * TODO explain architecture.
 * <p>
 * Caveat: Please keep in mind that search widgets always work on a specific map.
 * As there is only one SearchController, which also operates on a specific map, so
 * the search system can only work with one map.
 *
 * @author Kristof Heirwegh
 */
public final class SearchWidgetRegistry {

	private static final Map<String, SearchWidgetCreator> REGISTRY = new LinkedHashMap<String, SearchWidgetCreator>();

	private static final SearchAndFilterMessages MESSAGES = GWT.create(SearchAndFilterMessages.class);

	private static SearchController SEARCH_CONTROLLER;
	private static FavouritesController FAVOURITES_CONTROLLER;

	private static MapWidget MAP_WIDGET;

	private SearchWidgetRegistry() {
		// utility class, hide constructor
	}

	// TODO favoritecontroller

	// ----------------------------------------------------------

	public static void initialize(MapWidget mapWidget, SearchHandler searchResultGrid, boolean modalSearch) {
		MAP_WIDGET = mapWidget;
		SEARCH_CONTROLLER = new SearchController(mapWidget, modalSearch);
		FAVOURITES_CONTROLLER = new FavouritesController();
		if (searchResultGrid != null) {
			SEARCH_CONTROLLER.addSearchHandler(searchResultGrid);
		}
	}

	/**
	 * Initialize search registry.
	 *
	 * @param mapWidget map widget for registry
	 * @param searchResultGrid
	 *            can be null, add your own handler to be notified then
	 */
	public static void initialize(MapWidget mapWidget, MultiFeatureListGrid searchResultGrid) {
		initialize(mapWidget, searchResultGrid, true);
	}

	public static void addSearchHandler(SearchHandler handler) {
		if (checkState()) {
			SEARCH_CONTROLLER.addSearchHandler(handler);
		}
	}

	public static void removeSearchHandler(SearchHandler handler) {
		if (checkState()) {
			SEARCH_CONTROLLER.removeSearchHandler(handler);
		}
	}

	public static void addFavouriteChangeHandler(FavouriteChangeHandler handler) {
		if (checkState()) {
			FAVOURITES_CONTROLLER.addFavouriteChangeHandler(handler);
		}
	}

	public static void removeFavouriteChangeHandler(FavouriteChangeHandler handler) {
		if (checkState()) {
			FAVOURITES_CONTROLLER.removeFavouriteChangeHandler(handler);
		}
	}

	public static FavouritesController getFavouritesController() {
		if (checkState()) {
			return FAVOURITES_CONTROLLER;
		} else {
			return null;
		}
	}

	public static void put(SearchWidgetCreator widgetCreator) {
		if (checkState()) {
			if (null != widgetCreator) {
				REGISTRY.put(widgetCreator.getSearchWidgetId(), widgetCreator);
			}
		}
	}

	public static SearchWidget getSearchWidgetInstance(String searchWidgetId) {
		SearchWidget sw = REGISTRY.get(searchWidgetId).createInstance(MAP_WIDGET);
		sw.addSearchRequestHandler(SEARCH_CONTROLLER);
		sw.addFavouriteRequestHandler(FAVOURITES_CONTROLLER);
		return sw;
	}

	/**
	 * Get a list with all the ids + names of the search widgets in the registry.
	 *
	 * @return list of widget id/name pairs
	 */
	public static LinkedHashMap<String, String> getSearchWidgetMapping() {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		for (SearchWidgetCreator swc : REGISTRY.values()) {
			map.put(swc.getSearchWidgetId(), swc.getSearchWidgetName());
		}
		return map;
	}

	public static boolean isInitialized() {
		return (SEARCH_CONTROLLER != null);
	}

	private static boolean checkState() {
		if (isInitialized()) {
			return true;
		} else {
			String msg = "SearchWidgetRegistry has not been initialized. " +
					"Call initialize from your entry point on program startup.";
			SC.logWarn(msg);
			GWT.log(msg);
			return false;
		}
	}

	public static String getI18nTypeName(Criterion criterion) {
		if (criterion instanceof AndCriterion) {
			return MESSAGES.searchWidgetRegistryCriterionTypeAnd();
		} else if (criterion instanceof OrCriterion) {
			return MESSAGES.searchWidgetRegistryCriterionTypeOr();
		} else if (criterion instanceof AttributeCriterion) {
			return MESSAGES.searchWidgetRegistryCriterionTypeAttribute();
		} else if (criterion instanceof GeometryCriterion) {
			return MESSAGES.searchWidgetRegistryCriterionTypeGeometry();
		} else {
			return "??";
		}
	}
}
