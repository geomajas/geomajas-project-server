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
 * Registry of SearchWidgets.
 * <p>
 * TODO explain architecture.
 * <p>
 * Caveat: Please keep in mind that searchwidgets always work on a specific map.
 * As there is only one SearchController, which also operates on a specific map, so
 * the search system can only work with one map.
 *
 * @author Kristof Heirwegh
 */
public final class SearchWidgetRegistry {

	private static final Map<String, SearchWidgetCreator> REGISTRY = new LinkedHashMap<String, SearchWidgetCreator>();

	private static final SearchAndFilterMessages MESSAGES = GWT.create(SearchAndFilterMessages.class);

	private static SearchController SEARCHCONTROLLER;
	private static FavouritesController FAVOURITESCONTROLLER;

	private static MapWidget MAPWIDGET;

	private SearchWidgetRegistry() {
		// utility class, hide constructor
	}

	// TODO favoritecontroller

	// ----------------------------------------------------------

	public static void initialize(MapWidget mapWidget, SearchHandler searchResultGrid, boolean modalSearch) {
		MAPWIDGET = mapWidget;
		SEARCHCONTROLLER = new SearchController(mapWidget, modalSearch);
		FAVOURITESCONTROLLER = new FavouritesController();
		if (searchResultGrid != null) {
			SEARCHCONTROLLER.addSearchHandler(searchResultGrid);
		}
	}

	/**
	 * @param searchResultGrid
	 *            can be null, add your own handler to be notified then
	 */
	public static void initialize(MapWidget mapWidget, MultiFeatureListGrid searchResultGrid) {
		initialize(mapWidget, searchResultGrid, true);
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

	public static void addFavouriteChangeHandler(FavouriteChangeHandler handler) {
		if (checkState()) {
			FAVOURITESCONTROLLER.addFavouriteChangeHandler(handler);
		}
	}

	public static void removeFavouriteChangeHandler(FavouriteChangeHandler handler) {
		if (checkState()) {
			FAVOURITESCONTROLLER.removeFavouriteChangeHandler(handler);
		}
	}

	public static FavouritesController getFavouritesController() {
		if (checkState()) {
			return FAVOURITESCONTROLLER;
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
		SearchWidget sw = REGISTRY.get(searchWidgetId).createInstance(MAPWIDGET);
		sw.addSearchRequestHandler(SEARCHCONTROLLER);
		sw.addFavouriteRequestHandler(FAVOURITESCONTROLLER);
		return sw;
	}

	/**
	 * Get a list with all the ids + names of the searchwidgets in the repo.
	 * @return
	 */
	public static LinkedHashMap<String, String> getSearchWidgetMapping() {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		for (SearchWidgetCreator swc : REGISTRY.values()) {
			map.put(swc.getSearchWidgetId(), swc.getSearchWidgetName());
		}
		return map;
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
