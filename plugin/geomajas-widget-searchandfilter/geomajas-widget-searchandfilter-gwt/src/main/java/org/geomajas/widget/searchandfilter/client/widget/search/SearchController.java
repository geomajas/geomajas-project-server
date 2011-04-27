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
import org.geomajas.widget.searchandfilter.client.SearchAndFilterMessages;
import org.geomajas.widget.searchandfilter.client.util.SearchCommService;
import org.geomajas.widget.searchandfilter.client.util.DataCallback;
import org.geomajas.widget.searchandfilter.client.widget.search.SearchWidget.SearchRequestEvent;
import org.geomajas.widget.searchandfilter.client.widget.search.SearchWidget.SearchRequestHandler;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.layout.HLayout;

/**
 * Controller to handle the searches.
 * 
 * @see SearchWidgetRegistry.
 * @author Kristof Heirwegh
 */
public class SearchController implements SearchRequestHandler {

	private final SearchAndFilterMessages messages = GWT.create(SearchAndFilterMessages.class);

	private final Set<SearchHandler> searchHandlers = new HashSet<SearchHandler>();
	private MapWidget mapWidget;
	private boolean modalSearch;
	private Window modalWindow;

	public SearchController(MapWidget mapWidget, boolean modalSearch) {
		this.mapWidget = mapWidget;
		this.modalSearch = modalSearch;
	}

	// ----------------------------------------------------------

	public void onSearchRequested(SearchRequestEvent event) {
		final SearchEvent searchEvent = new SearchEvent();
		searchEvent.setCriterion(event.getCriterion());
		fireSearchStartEvent(searchEvent);
		SearchCommService.searchByCriterion(event.getCriterion(), mapWidget,
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
	 *
	 * @param handler
	 */
	public void addSearchHandler(SearchHandler handler) {
		searchHandlers.add(handler);
	}

	public void removeSearchHandler(SearchHandler handler) {
		searchHandlers.remove(handler);
	}

	public boolean isModalSearch() {
		return modalSearch;
	}

	public void setModalSearch(boolean modalSearch) {
		this.modalSearch = modalSearch;
	}

	// ----------------------------------------------------------

	private void fireSearchStartEvent(SearchEvent event) {
		showModalWindow();
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
		hideModalWindow();
	}

	private void showModalWindow() {
		if (isModalSearch()) {
			if (modalWindow == null) {
				HLayout layout = new HLayout();
				layout.setHeight(20);
				layout.setWidth100();
				layout.addMember(new Img("[ISOMORPHIC]/geomajas/ajax-loader.gif", 18, 18));
				layout.addMember(new Label(messages.searchControllerSearchingMessage()));
				modalWindow = new Window();
				modalWindow.setTitle(messages.searchControllerSearchingTitle());
				modalWindow.setAlign(Alignment.CENTER);
				modalWindow.setPadding(20);
				modalWindow.setHeight(100);
				modalWindow.setWidth(300);
				modalWindow.addItem(layout);
				modalWindow.setAutoCenter(true);
				modalWindow.setShowMinimizeButton(false);
				modalWindow.setShowCloseButton(false);
				modalWindow.setIsModal(true);
				modalWindow.setShowModalMask(true);
			}
			modalWindow.show();
		}
	}

	private void hideModalWindow() {
		if (modalWindow != null) {
			modalWindow.hide();
		}
	}
}
