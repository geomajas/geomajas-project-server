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

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.global.Api;
import org.geomajas.widget.searchandfilter.client.SearchAndFilterMessages;
import org.geomajas.widget.searchandfilter.client.widget.search.FavouritesController.FavouriteEvent;
import org.geomajas.widget.searchandfilter.search.dto.Criterion;
import org.geomajas.widget.searchandfilter.search.dto.SearchFavourite;

import java.util.ArrayList;
import java.util.List;

/**
 * A generic {@link SearchWidget} implemented as a panel which can be embedded anywhere.
 * <p>
 * To build a search widget combine this search widget with a {@link AbstractSearchPanel}.
 *
 * @see {@link org.geomajas.widget.searchandfilter.client.widget.search.SearchWidgetRegistry}.
 * @author Kristof Heirwegh
 * @since 1.0.0
 */
@Api
public class PanelSearchWidget extends VLayout implements SearchWidget {

	private static final String BTN_FAVOURITES_IMG = "[ISOMORPHIC]/geomajas/osgeo/bookmark_new.png";
	private static final String BTN_SAVE_IMG = "[ISOMORPHIC]/geomajas/osgeo/save1.png";
	private static final String BTN_CANCEL_IMG = "[ISOMORPHIC]/geomajas/osgeo/undo.png";
	private static final String BTN_SEARCH_IMG = "[ISOMORPHIC]/geomajas/silk/find.png";
	private static final String BTN_RESET_IMG = "[ISOMORPHIC]/geomajas/osgeo/undo.png";
	private static final String BTN_PROCESSING = "[ISOMORPHIC]/geomajas/ajax-loader.gif";

	private final List<SearchRequestHandler> searchHandlers = new ArrayList<SearchRequestHandler>();
	private final List<SaveRequestHandler> saveHandlers = new ArrayList<SaveRequestHandler>();
	private final List<FavouriteRequestHandler> favouriteHandlers =
		new ArrayList<FavouriteRequestHandler>();

	private IButton searchBtn;
	private HLayout searchButtonBar;
	private HLayout saveButtonBar;
	private AbstractSearchPanel searchPanel;
	private String widgetId;
	private String name;

	/**
	 * @param widgetId
	 *            needed to find the widget in the SearchWidgetRegistry.
	 * @param name
	 *            name of the widget to show in window title and on buttons.
	 * @param searchPanel
	 *            your specific implementation of a search
	 */
	public PanelSearchWidget(String widgetId, String name, final AbstractSearchPanel searchPanel) {
		super(10);
		if (widgetId == null || searchPanel == null) {
			throw new IllegalArgumentException("All parameters are required");
		}

		this.searchPanel = searchPanel;
		this.widgetId = widgetId;
		this.name = name;

		setWidth100();
		setHeight100();
		setMargin(10);

		searchButtonBar = new HLayout(10);
		saveButtonBar = new HLayout(10);
		saveButtonBar.setVisible(false);

		SearchAndFilterMessages messages = GWT.create(SearchAndFilterMessages.class);

		IButton favouritesSBtn = new IButton(messages.searchWidgetAddToFavourites());
		favouritesSBtn.setIcon(BTN_FAVOURITES_IMG);
		favouritesSBtn.setAutoFit(true);
		favouritesSBtn.setShowDisabledIcon(false);
		favouritesSBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				onAddToFavourites();
			}
		});
		IButton favouritesRBtn = new IButton(messages.searchWidgetAddToFavourites());
		favouritesRBtn.setIcon(BTN_FAVOURITES_IMG);
		favouritesRBtn.setAutoFit(true);
		favouritesRBtn.setShowDisabledIcon(false);
		favouritesRBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				onAddToFavourites();
			}
		});
		searchBtn = new IButton(messages.searchWidgetSearch());
		searchBtn.setIcon(BTN_SEARCH_IMG);
		searchBtn.setAutoFit(true);
		searchBtn.setShowDisabledIcon(false);
		searchBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				onSearch();
			}
		});
		IButton resetBtn = new IButton(messages.searchWidgetReset());
		resetBtn.setIcon(BTN_RESET_IMG);
		resetBtn.setAutoFit(true);
		resetBtn.setShowDisabledIcon(false);
		resetBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				searchPanel.reset();
			}
		});
		IButton saveBtn = new IButton(messages.searchWidgetSave());
		saveBtn.setIcon(BTN_SAVE_IMG);
		saveBtn.setAutoFit(true);
		saveBtn.setShowDisabledIcon(false);
		saveBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				onSave();
			}
		});
		IButton cancelBtn = new IButton(messages.searchWidgetCancel());
		cancelBtn.setIcon(BTN_CANCEL_IMG);
		cancelBtn.setAutoFit(true);
		cancelBtn.setShowDisabledIcon(false);
		cancelBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				hide();
				destroy();
			}
		});

		addMember(searchPanel);
		LayoutSpacer lss = new LayoutSpacer();
		LayoutSpacer lsr = new LayoutSpacer();
		lss.setWidth("*");
		lsr.setWidth("*");

		searchButtonBar.setWidth(searchPanel.getWidthAsString());
		if (searchPanel.canAddToFavourites()) {
			searchButtonBar.addMember(favouritesRBtn);
		}
		searchButtonBar.addMember(lsr);
		searchButtonBar.addMember(searchBtn);
		if (searchPanel.canBeReset()) {
			searchButtonBar.addMember(resetBtn);
		}
		addMember(searchButtonBar);

		saveButtonBar.setWidth(searchPanel.getWidthAsString());
		if (searchPanel.canAddToFavourites()) {
			saveButtonBar.addMember(favouritesSBtn);
		}
		saveButtonBar.addMember(lss);
		saveButtonBar.addMember(saveBtn);
		saveButtonBar.addMember(cancelBtn);
		addMember(saveButtonBar);
	}

	// ----------------------------------------------------------

	public String getSearchWidgetId() {
		return widgetId;
	}

	public String getName() {
		return name;
	}

	public void showForSearch() {
		saveButtonBar.setVisible(false);
		searchButtonBar.setVisible(true);
		show();
		bringToFront();
	}

	public void showForSave(final SaveRequestHandler handler) {
		if (handler != null) {
			addSaveRequestHandler(new OneOffSaveRequestHandler(handler));
		}
		saveButtonBar.setVisible(true);
		searchButtonBar.setVisible(false);
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
		searchBtn.setIcon(BTN_PROCESSING);
		searchBtn.setDisabled(true);
	}

	public void onSearchEnd() {
		searchBtn.setIcon(BTN_SEARCH_IMG);
		searchBtn.setDisabled(false);
	}

	public void addSearchRequestHandler(SearchRequestHandler handler) {
		searchHandlers.add(handler);
	}

	public void removeSearchRequestHandler(SearchRequestHandler handler) {
		searchHandlers.remove(handler);
	}

	public void addSaveRequestHandler(SaveRequestHandler handler) {
		saveHandlers.add(handler);
	}

	public void removeSaveRequestHandler(SaveRequestHandler handler) {
		saveHandlers.remove(handler);
	}

	public void addFavouriteRequestHandler(FavouriteRequestHandler handler) {
		favouriteHandlers.add(handler);
	}

	public void removeFavouriteRequestHandler(FavouriteRequestHandler handler) {
		favouriteHandlers.remove(handler);
	}

	public void startSearch() {
		onSearch();
	}

	// ----------------------------------------------------------
	// -- buttonActions --
	// ----------------------------------------------------------

	void onSearch() {
		if (searchPanel.validate()) {
			setVectorLayerOnWhichSearchIsHappeningVisible();
			Criterion critter = searchPanel.getFeatureSearchCriterion();
			SearchRequestEvent sre = new SearchRequestEvent(this, critter);
			for (SearchRequestHandler h : searchHandlers) {
				h.onSearchRequested(sre);
			}
		}
	}

	private void onSave() {
		if (searchPanel.validate()) {
			Criterion critter = searchPanel.getFeatureSearchCriterion();
			for (SaveRequestHandler h : saveHandlers) {
				h.onSaveRequested(new SaveRequestEvent(this, critter));
			}
			hide();
			destroy();
		}
	}

	private void onAddToFavourites() {
		if (searchPanel.validate()) {
			SearchFavourite fav = new SearchFavourite();
			fav.setCriterion(searchPanel.getFeatureSearchCriterion());
			for (FavouriteRequestHandler h : favouriteHandlers) {
				h.onAddRequested(new FavouriteEvent(null, fav, this));
			}
		}
	}

	private void setVectorLayerOnWhichSearchIsHappeningVisible() {
		if (searchPanel.getFeatureSearchVectorLayer() != null 
				&& !searchPanel.getFeatureSearchVectorLayer().isVisible()) {
			searchPanel.getFeatureSearchVectorLayer().setVisible(true);
		}
	}

	// ----------------------------------------------------------

	/**
	 * @author Kristof Heirwegh
	 */
	private class OneOffSaveRequestHandler implements SaveRequestHandler {

		private final SaveRequestHandler oneOffHandler;

		public OneOffSaveRequestHandler(SaveRequestHandler handler) {
			this.oneOffHandler = handler;
		}

		public void onSaveRequested(SaveRequestEvent event) {
			oneOffHandler.onSaveRequested(event);
			GWT.runAsync(new RunAsyncCallback() {

				public void onSuccess() {
					removeSaveRequestHandler(oneOffHandler);
				}

				public void onFailure(Throwable reason) {
				}
			});
		}
	}
}
