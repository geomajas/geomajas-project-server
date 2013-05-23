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
package org.geomajas.widget.searchandfilter.client.widget.search;

import org.geomajas.annotation.Api;
import org.geomajas.widget.featureinfo.client.widget.DockableWindow;
import org.geomajas.widget.searchandfilter.client.util.GsfLayout;
import org.geomajas.widget.searchandfilter.search.dto.Criterion;

import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClickEvent;

/**
 * A generic {@link SearchWidget} implemented as dockable window.
 * <p>
 * To build a search widget combine this search widget with a {@link AbstractSearchPanel}.
 * 
 * @see SearchWidgetRegistry
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
	 *        needed to find the widget in the SearchWidgetRegistry.
	 * @param name
	 *        name of the widget to show in window title and on buttons.
	 * @param searchPanel
	 *        your specific implementation of a search
	 */
	public DockableWindowSearchWidget(String widgetId, String name, final AbstractSearchPanel searchPanel) {
		if (widgetId == null || searchPanel == null) {
			throw new IllegalArgumentException("All parameters are required");
		}

		this.searchPanel = searchPanel;
		this.widgetId = widgetId;
		this.name = name;
		this.setTitle(name);
		this.setKeepInParentRect(true);

		addCloseClickHandler(new CloseClickHandler() {

			public void onCloseClick(CloseClickEvent event) {
				searchPanel.hide();
				hide();
				destroy();
			}
		});

		panelSearchWidget = new PanelSearchWidget(widgetId + "Item", name, searchPanel);
		panelSearchWidget.setCloseAction(new Runnable() {
			public void run() {
				hide();
				destroy();
			}
		});
		this.addItem(panelSearchWidget);
		this.setAutoSize(true);

		positionWindow();
	}

	// ----------------------------------------------------------

	@Override
	public void hideSearchButtons() {
		panelSearchWidget.hide();
	}

	@Override
	public void showSearchButtons() {
		panelSearchWidget.hide();
	}

	@Override
	public String getSearchWidgetId() {
		return widgetId;
	}

	@Override
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

	@Override
	public void showForSearch() {
		show();
		bringToFront();
		panelSearchWidget.showForSearch();
	}

	@Override
	public void showForSave(final SaveRequestHandler handler) {
		show();
		bringToFront();
		panelSearchWidget.showForSave(handler);
	}

	@Override
	public void initialize(Criterion settings) {
		searchPanel.initialize(settings);
	}

	@Override
	public void reset() {
		searchPanel.reset();
	}

	@Override
	public void onSearchStart() {
		panelSearchWidget.onSearchStart();
	}

	@Override
	public void onSearchEnd() {
		panelSearchWidget.onSearchEnd();
	}

	@Override
	public void addSearchRequestHandler(SearchRequestHandler handler) {
		panelSearchWidget.addSearchRequestHandler(handler);
	}

	@Override
	public void removeSearchRequestHandler(SearchRequestHandler handler) {
		panelSearchWidget.removeSearchRequestHandler(handler);
	}

	@Override
	public void addSaveRequestHandler(SaveRequestHandler handler) {
		panelSearchWidget.addSaveRequestHandler(handler);
	}

	@Override
	public void removeSaveRequestHandler(SaveRequestHandler handler) {
		panelSearchWidget.removeSaveRequestHandler(handler);
	}

	@Override
	public void addFavouriteRequestHandler(FavouriteRequestHandler handler) {
		panelSearchWidget.addFavouriteRequestHandler(handler);
	}

	@Override
	public void removeFavouriteRequestHandler(FavouriteRequestHandler handler) {
		panelSearchWidget.removeFavouriteRequestHandler(handler);
	}

	@Override
	public void startSearch() {
		onSearch();
	}

	/**
	 * LayoutTypes for Searchwindow.
	 */
	public static enum SearchWindowPositionType {
		CENTERED, 
		ABSOLUTE, 
		/** See SmartGwt's snapto functions for properties. */
		SNAPTO;
	}

	// ----------------------------------------------------------
	// -- buttonActions --
	// ----------------------------------------------------------

	private void onSearch() {
		panelSearchWidget.onSearch();
	}

	// -------------------------------------------------

	private void setVectorLayerOnWhichSearchIsHappeningVisible() {
		if (searchPanel.getFeatureSearchVectorLayer() != null
				&& !searchPanel.getFeatureSearchVectorLayer().isVisible()) {
			searchPanel.getFeatureSearchVectorLayer().setVisible(true);
		}
	}

	private void positionWindow() {
		if (GsfLayout.searchWindowPositionType != null) {
			switch (GsfLayout.searchWindowPositionType) {
				case CENTERED:
					this.setAutoCenter(true);
					break;
				case ABSOLUTE:
					this.moveTo(GsfLayout.searchWindowPosLeft, GsfLayout.searchWindowPosTop);
					break;
				case SNAPTO:
					this.setParentElement(GsfLayout.searchWindowParentElement);
					this.setSnapTo(GsfLayout.searchWindowPosSnapTo);
					this.setSnapOffsetLeft(GsfLayout.searchWindowPosLeft);
					this.setSnapOffsetTop(GsfLayout.searchWindowPosTop);
			}
		}
	}
}
