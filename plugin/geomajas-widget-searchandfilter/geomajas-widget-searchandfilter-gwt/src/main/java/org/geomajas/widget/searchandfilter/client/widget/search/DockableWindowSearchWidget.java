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

import org.geomajas.global.Api;
import org.geomajas.widget.featureinfo.client.widget.DockableWindow;
import org.geomajas.widget.searchandfilter.search.dto.Criterion;

import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;

/**
 * A generic {@link SearchWidget} implemented as dockable window.
 * <p>
 * To build a search widget combine this search widget with a {@link AbstractSearchPanel}.
 * 
 * @see {@link SearchWidgetRegistry}.
 * @author Kristof Heirwegh
 * @since 1.0.0
 */
@Api
public class DockableWindowSearchWidget extends DockableWindow implements SearchWidget {

	private PanelSearchWidget panelSearchWidget;
	private AbstractSearchPanel searchPanel;
	private String widgetId;
	private String name;

	private boolean hideAfterSearch;

	/**
	 * @param widgetId
	 *            needed to find the widget in the SearchWidgetRegistry.
	 * @param name
	 *            name of the widget to show in window title and on buttons.
	 * @param searchPanel
	 *            your specific implementation of a search
	 */
	public DockableWindowSearchWidget(String widgetId, String name, final AbstractSearchPanel searchPanel) {
		if (widgetId == null || searchPanel == null) {
			throw new IllegalArgumentException("All parameters are required");
		}

		this.searchPanel = searchPanel;
		this.widgetId = widgetId;
		this.name = name;
		this.setTitle(name);
		this.setAutoCenter(true);
		this.setKeepInParentRect(true);

		addCloseClickHandler(new CloseClickHandler() {

			public void onCloseClick(CloseClientEvent event) {
				hide();
				destroy();
			}
		});

		panelSearchWidget = new PanelSearchWidget(widgetId + "Item", name, searchPanel);
		this.addItem(panelSearchWidget);
		this.setAutoSize(true);
	}

	// ----------------------------------------------------------

	public String getSearchWidgetId() {
		return widgetId;
	}

	public String getName() {
		return name;
	}

	public boolean isHideAfterSearch() {
		return hideAfterSearch;
	}

	/**
	 * Should the widget be hidden after a search or stay open for a new search?
	 * 
	 * @param hideAfterSearch hide window after search?
	 */
	public void setHideAfterSearch(boolean hideAfterSearch) {
		this.hideAfterSearch = hideAfterSearch;
	}

	public void showForSearch() {
		panelSearchWidget.showForSearch();
		show();
		bringToFront();
	}

	public void showForSave(final SaveRequestHandler handler) {
		panelSearchWidget.showForSave(handler);
		show();
		bringToFront();
	}

	public void initialize(Criterion settings) {
		searchPanel.initialize(settings);
	}

	public void reset() {
		searchPanel.reset();
	}

	public void hide() {
		searchPanel.hide();
		super.hide();
	}

	public void onSearchStart() {
		panelSearchWidget.onSearchStart();
	}

	public void onSearchEnd() {
		panelSearchWidget.onSearchEnd();
	}

	public void addSearchRequestHandler(SearchRequestHandler handler) {
		panelSearchWidget.addSearchRequestHandler(handler);
	}

	public void removeSearchRequestHandler(SearchRequestHandler handler) {
		panelSearchWidget.removeSearchRequestHandler(handler);
	}

	public void addSaveRequestHandler(SaveRequestHandler handler) {
		panelSearchWidget.addSaveRequestHandler(handler);
	}

	public void removeSaveRequestHandler(SaveRequestHandler handler) {
		panelSearchWidget.removeSaveRequestHandler(handler);
	}

	public void addFavouriteRequestHandler(FavouriteRequestHandler handler) {
		panelSearchWidget.addFavouriteRequestHandler(handler);
	}

	public void removeFavouriteRequestHandler(FavouriteRequestHandler handler) {
		panelSearchWidget.removeFavouriteRequestHandler(handler);
	}

	public void startSearch() {
		onSearch();
	}

	// ----------------------------------------------------------
	// -- buttonActions --
	// ----------------------------------------------------------

	private void onSearch() {
		panelSearchWidget.onSearch();
	}

	private void setVectorLayerOnWhichSearchIsHappeningVisible() {
		if (searchPanel.getFeatureSearchVectorLayer() != null 
				&& !searchPanel.getFeatureSearchVectorLayer().isVisible()) {
			searchPanel.getFeatureSearchVectorLayer().setVisible(true);
		}
	}
}
