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

import java.util.ArrayList;
import java.util.List;

import org.geomajas.widget.searchandfilter.client.SearchAndFilterMessages;
import org.geomajas.widget.searchandfilter.search.dto.Criterion;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * A generic Searchwidget with common functionality.
 * <p>
 * To build a Searchwidget combine this Searchwidget with a SearchPanel.
 * 
 * @see SearchWidgetRegistry.
 * @author Kristof Heirwegh
 */
public class BasicSearchWidget extends Window implements SearchWidget {

	private static final String BTN_FAVOURITES_IMG = "[ISOMORPHIC]/geomajas/osgeo/bookmark_new.png";
	private static final String BTN_SAVE_IMG = "[ISOMORPHIC]/geomajas/osgeo/save1.png";
	private static final String BTN_CANCEL_IMG = "[ISOMORPHIC]/geomajas/osgeo/undo.png";
	private static final String BTN_SEARCH_IMG = "[ISOMORPHIC]/geomajas/silk/find.png";
	private static final String BTN_RESET_IMG = "[ISOMORPHIC]/geomajas/osgeo/undo.png";
	private static final String BTN_PROCESSING = "[ISOMORPHIC]/geomajas/ajax-loader.gif";

	private final SearchAndFilterMessages messages = GWT.create(SearchAndFilterMessages.class);
	private final List<SearchRequestHandler> searchHandlers = new ArrayList<SearchWidget.SearchRequestHandler>();
	private final List<SaveRequestHandler> saveHandlers = new ArrayList<SearchWidget.SaveRequestHandler>();

	private IButton favouritesSBtn;
	private IButton favouritesRBtn;
	private IButton searchBtn;
	private IButton resetBtn;
	private IButton saveBtn;
	private IButton cancelBtn;
	private HLayout searchButtonBar;
	private HLayout saveButtonBar;
	private SearchPanel searchPanel;
	private String widgetId;
	private String name;

	private boolean hideAfterSearch;

	/**
	 * @param mapWidget
	 * @param searchWidgetId
	 *            needed to find the widget in the SearchWidgetRegistry.
	 * @param searchWidgetName
	 *            name of the widget to show in window title and on buttons.
	 * @param searchPanel
	 *            your specific implementation of a search
	 */
	public BasicSearchWidget(String widgetId, String name, final SearchPanel searchPanel) {
		if (widgetId == null || searchPanel == null) {
			throw new IllegalArgumentException("All parameters are required");
		}

		this.searchPanel = searchPanel;
		this.widgetId = widgetId;
		this.name = name;
		this.setTitle(name);
		this.setAutoCenter(true);

		VLayout layout = new VLayout(10);
		layout.setWidth100();
		layout.setHeight100();
		layout.setMargin(10);

		searchButtonBar = new HLayout(10);
		saveButtonBar = new HLayout(10);
		saveButtonBar.setVisible(false);

		favouritesSBtn = new IButton(messages.searchWidgetAddToFavourites());
		favouritesSBtn.setIcon(BTN_FAVOURITES_IMG);
		favouritesSBtn.setAutoFit(true);
		favouritesSBtn.setShowDisabledIcon(false);
		favouritesSBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				onAddToFavourites();
			}
		});
		favouritesRBtn = new IButton(messages.searchWidgetAddToFavourites());
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
		resetBtn = new IButton(messages.searchWidgetReset());
		resetBtn.setIcon(BTN_RESET_IMG);
		resetBtn.setAutoFit(true);
		resetBtn.setShowDisabledIcon(false);
		resetBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				searchPanel.reset();
			}
		});
		saveBtn = new IButton(messages.searchWidgetSave());
		saveBtn.setIcon(BTN_SAVE_IMG);
		saveBtn.setAutoFit(true);
		saveBtn.setShowDisabledIcon(false);
		saveBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				onSave();
			}
		});
		cancelBtn = new IButton(messages.searchWidgetCancel());
		cancelBtn.setIcon(BTN_CANCEL_IMG);
		cancelBtn.setAutoFit(true);
		cancelBtn.setShowDisabledIcon(false);
		cancelBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				hide();
				destroy();
			}
		});
		
		addCloseClickHandler(new CloseClickHandler() {
			public void onCloseClick(CloseClientEvent event) {
				hide();
				destroy();
			}
		});

		// ----------------------------------------------------------

		layout.addMember(searchPanel);
		LayoutSpacer lss = new LayoutSpacer();
		LayoutSpacer lsr = new LayoutSpacer();
		lss.setWidth("*");
		lsr.setWidth("*");

		searchButtonBar.setWidth(searchPanel.getWidthAsString());
		searchButtonBar.addMember(favouritesRBtn);
		searchButtonBar.addMember(lsr);
		searchButtonBar.addMember(searchBtn);
		searchButtonBar.addMember(resetBtn);
		layout.addMember(searchButtonBar);

		saveButtonBar.setWidth(searchPanel.getWidthAsString());
		saveButtonBar.addMember(favouritesSBtn);
		saveButtonBar.addMember(lss);
		saveButtonBar.addMember(saveBtn);
		saveButtonBar.addMember(cancelBtn);
		layout.addMember(saveButtonBar);

		this.addItem(layout);
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
	 * @param hideAfterSearch
	 */
	public void setHideAfterSearch(boolean hideAfterSearch) {
		this.hideAfterSearch = hideAfterSearch;
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

	// ----------------------------------------------------------
	// -- buttonActions --
	// ----------------------------------------------------------

	private void onSearch() {
		if (searchPanel.validate()) {
			Criterion critter = searchPanel.getFeatureSearchCriterion();
			SearchRequestEvent sre = new SearchRequestEvent(this, critter);
			for (SearchRequestHandler h : searchHandlers) {
				h.onSearchRequested(sre);
			}
			if (hideAfterSearch) {
				hide();
				destroy();
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
		// TODO
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
