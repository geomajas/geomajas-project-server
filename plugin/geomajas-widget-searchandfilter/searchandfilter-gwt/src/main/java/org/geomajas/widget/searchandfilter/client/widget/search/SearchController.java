/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
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

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.Geomajas;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.featureinfo.client.widget.DockableWindow;
import org.geomajas.widget.searchandfilter.client.SearchAndFilterMessages;
import org.geomajas.widget.searchandfilter.client.util.DataCallback;
import org.geomajas.widget.searchandfilter.client.util.SearchCommService;
import org.geomajas.widget.searchandfilter.client.widget.search.SearchWidget.SearchRequestEvent;
import org.geomajas.widget.searchandfilter.client.widget.search.SearchWidget.SearchRequestHandler;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Controller to handle the searches.
 * 
 * @see SearchWidgetRegistry
 * @author Kristof Heirwegh
 * @since 1.0.0
 */
@Api
public class SearchController implements SearchRequestHandler {

	private final SearchAndFilterMessages messages = GWT.create(SearchAndFilterMessages.class);

	private final Set<SearchHandler> searchHandlers = new HashSet<SearchHandler>();
	private MapWidget mapWidget;
	private boolean modalSearch;
	private Window window;

	/**
	 * Construct search controller.
	 *
	 * @param mapWidget map widget
	 * @param modalSearch should result be shown in a modal window
	 */
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
				}, new Runnable() {
					public void run() {
						fireSearchEndEvent(searchEvent);
						destroyWindow();
						SC.say(messages.searchControllerFailureMessage());
					}
				});
	}

	public void onCancelRequested(SearchRequestEvent event) {
	}

	/**
	 * Add a search handler, for example an instance of
	 * {@link org.geomajas.widget.searchandfilter.client.widget.multifeaturelistgrid.MultiFeatureListGrid}.
	 * 
	 * @param handler handler to process the search results
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
		showWindow();
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
		hideWindow();
	}

	private void showWindow() {
		if (window == null) {
			window = createWindow();
			if (isModalSearch()) {
				window.setIsModal(true);
				window.setShowModalMask(true);
			}
		}
		window.show();
	}

	private void hideWindow() {
		if (window != null) {
			window.hide();
		}
	}

	private void destroyWindow() {
		if (window != null) {
			window.destroy();
			window = null;
		}
	}

	private Window createWindow() {
		VLayout layout = new VLayout(5);
		layout.setLayoutAlign(Alignment.CENTER);
		layout.setAlign(Alignment.CENTER);
		layout.setWidth100();
		Label label = new Label(messages.searchControllerSearchingMessage());
		label.setWidth100();
		label.setHeight(30);
		label.setAlign(Alignment.CENTER);
		layout.addMember(label);

		HTMLPane img = new HTMLPane();
		img.setLayoutAlign(Alignment.CENTER);
		img.setAlign(Alignment.CENTER);
		img.setWidth(20);
		img.setHeight(20);
		img.setContents("<img src=\"" + Geomajas.getIsomorphicDir()
				+ "/geomajas/ajax-loader.gif\" width=\"18\" height=\"18\" />");
		layout.addMember(img);
		Window w = new DockableWindow();
		w.setTitle(messages.searchControllerSearchingTitle());
		w.setAlign(Alignment.CENTER);
		w.setPadding(5);
		w.setHeight(100);
		w.setWidth(300);
		w.addItem(layout);
		w.setShowMinimizeButton(false);
		w.setShowCloseButton(false);
		w.setKeepInParentRect(true);
		w.setAutoCenter(true);
		return w;
	}
}
