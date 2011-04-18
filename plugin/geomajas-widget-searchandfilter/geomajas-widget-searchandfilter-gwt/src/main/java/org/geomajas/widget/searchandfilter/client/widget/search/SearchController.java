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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.searchandfilter.client.util.CommService;
import org.geomajas.widget.searchandfilter.client.util.DataCallback;
import org.geomajas.widget.searchandfilter.client.widget.search.SearchWidget.SearchRequestEvent;
import org.geomajas.widget.searchandfilter.client.widget.search.SearchWidget.SearchRequestHandler;

/**
 * Controller to handle the searches.
 * 
 * @see SearchWidgetRegistry.
 * @author Kristof Heirwegh
 */
public class SearchController implements SearchRequestHandler {

	private final Set<SearchHandler> searchHandlers = new HashSet<SearchHandler>();
	private MapWidget mapWidget;

	public SearchController(MapWidget mapWidget) {
		this.mapWidget = mapWidget;
	}

	// ----------------------------------------------------------

	public void onSearchRequested(SearchRequestEvent event) {
		final SearchEvent searchEvent = new SearchEvent();
		searchEvent.setCriterion(event.getCriterion());
		fireSearchStartEvent(searchEvent);
		CommService.searchByCriterion(event.getCriterion(), mapWidget,
				new DataCallback<Map<VectorLayer, List<Feature>>>() {
					public void execute(Map<VectorLayer, List<Feature>> result) {
						searchEvent.setResult(result);
						fireSearchDoneEvent(searchEvent);
						fireSearchEndEvent(searchEvent);
					}
				}, new ErrorHandler() {
					public void execute() {
						fireSearchEndEvent(searchEvent);
					}
				});
	}

	/**
	 * For instance {@link MultiFeatureListGrid}.
	 * @param handler
	 */
	public void addSearchHandler(SearchHandler handler) {
		searchHandlers.add(handler);
	}

	public void removeSearchHandler(SearchHandler handler) {
		searchHandlers.remove(handler);
	}

	// ----------------------------------------------------------

	private void fireSearchStartEvent(SearchEvent event) {
		for (SearchHandler handler : searchHandlers) {
			handler.onSearchStart(event);
		}
	}

	private void fireSearchDoneEvent(SearchEvent event) {
		for (SearchHandler handler : searchHandlers) {
			handler.onSearchDone(event);
		}
	}

	private void fireSearchEndEvent(SearchEvent event) {
		for (SearchHandler handler : searchHandlers) {
			handler.onSearchEnd(event);
		}
	}
}
