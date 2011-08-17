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
import org.geomajas.gwt.client.map.layer.VectorLayer;
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
	private IButton saveBtn;
	private AbstractSearchPanel searchPanel;
	private String widgetId;
	private String name;
	private Runnable closeAction;

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

		//setWidth100();
		//setHeight100();
		setMargin(10);

		SearchAndFilterMessages messages = GWT.create(SearchAndFilterMessages.class);

		IButton favouritesBtn = new IButton(messages.searchWidgetAddToFavourites());
		favouritesBtn.setIcon(BTN_FAVOURITES_IMG);
		favouritesBtn.setAutoFit(true);
		favouritesBtn.setShowDisabledIcon(false);
		favouritesBtn.addClickHandler(new ClickHandler() {

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
		saveBtn = new IButton(messages.searchWidgetSave());
		saveBtn.setIcon(BTN_SAVE_IMG);
		saveBtn.setAutoFit(true);
		saveBtn.setShowDisabledIcon(false);
		saveBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				onSave();
			}
		});
		saveBtn.setVisible(false);
		IButton cancelBtn = new IButton(messages.searchWidgetCancel());
		cancelBtn.setIcon(BTN_CANCEL_IMG);
		cancelBtn.setAutoFit(true);
		cancelBtn.setShowDisabledIcon(false);
		cancelBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				close();
			}
		});

		addMember(searchPanel);
		LayoutSpacer ls = new LayoutSpacer();
		ls.setWidth("*");

		HLayout searchButtonBar = new HLayout(10);
		searchButtonBar.setWidth100();
		if (searchPanel.canAddToFavourites()) {
			searchButtonBar.addMember(favouritesBtn);
		}
		searchButtonBar.addMember(ls);
		searchButtonBar.addMember(searchBtn);
		searchButtonBar.addMember(saveBtn);
		if (searchPanel.canBeReset()) {
			searchButtonBar.addMember(resetBtn);
		}
		addMember(searchButtonBar);
	}

	/**
	 * Method which is called to close/end the panel (after cancel or save). It reset the panel and calls the close
	 * action if present. If no close action is present, it hides the panel.
	 */
	protected void close() {
		reset();
		if (null == closeAction) {
			hide();
		} else {
			closeAction.run();
		}
	}

	/**
	 * Set action to be called when pressing the cancel button.
	 *
	 * @param closeAction cancel action to call
	 */
	public void setCloseAction(Runnable closeAction) {
		this.closeAction = closeAction;
	}

	// ----------------------------------------------------------

	/** {@inheritDoc} */
	public String getSearchWidgetId() {
		return widgetId;
	}

	/** {@inheritDoc} */
	public String getName() {
		return name;
	}

	/** {@inheritDoc} */
	public void showForSearch() {
		saveBtn.setVisible(false);
		searchBtn.setVisible(true);
		show();
		bringToFront();
	}

	/** {@inheritDoc} */
	public void showForSave(final SaveRequestHandler handler) {
		if (handler != null) {
			addSaveRequestHandler(new OneOffSaveRequestHandler(handler));
		}
		saveBtn.setVisible(true);
		searchBtn.setVisible(false);
		show();
		bringToFront();
	}

	/** {@inheritDoc} */
	public void initialize(Criterion settings) {
		searchPanel.initialize(settings);
	}

	/** {@inheritDoc} */
	public void reset() {
		searchHandlers.clear(); // need to remove showForSearch() handler
		saveHandlers.clear(); // need to remove showForSave() handler
		searchPanel.reset();
	}

	/** {@inheritDoc} */
	public void onSearchStart() {
		searchBtn.setIcon(BTN_PROCESSING);
		searchBtn.setDisabled(true);
	}

	/** {@inheritDoc} */
	public void onSearchEnd() {
		searchBtn.setIcon(BTN_SEARCH_IMG);
		searchBtn.setDisabled(false);
	}

	/** {@inheritDoc} */
	public void addSearchRequestHandler(SearchRequestHandler handler) {
		searchHandlers.add(handler);
	}

	/** {@inheritDoc} */
	public void removeSearchRequestHandler(SearchRequestHandler handler) {
		searchHandlers.remove(handler);
	}

	/** {@inheritDoc} */
	public void addSaveRequestHandler(SaveRequestHandler handler) {
		saveHandlers.add(handler);
	}

	/** {@inheritDoc} */
	public void removeSaveRequestHandler(SaveRequestHandler handler) {
		saveHandlers.remove(handler);
	}

	/** {@inheritDoc} */
	public void addFavouriteRequestHandler(FavouriteRequestHandler handler) {
		favouriteHandlers.add(handler);
	}

	/** {@inheritDoc} */
	public void removeFavouriteRequestHandler(FavouriteRequestHandler handler) {
		favouriteHandlers.remove(handler);
	}

	/** {@inheritDoc} */
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
			for (SearchRequestHandler handler : searchHandlers) {
				handler.onSearchRequested(sre);
			}
		}
	}

	private void onSave() {
		if (searchPanel.validate()) {
			Criterion critter = searchPanel.getFeatureSearchCriterion();
			for (SaveRequestHandler handler : saveHandlers) {
				handler.onSaveRequested(new SaveRequestEvent(this, critter));
			}
			close();
		}
	}

	private void onAddToFavourites() {
		if (searchPanel.validate()) {
			SearchFavourite fav = new SearchFavourite();
			fav.setCriterion(searchPanel.getFeatureSearchCriterion());
			for (FavouriteRequestHandler handler : favouriteHandlers) {
				handler.onAddRequested(new FavouriteEvent(null, fav, this));
			}
		}
	}

	private void setVectorLayerOnWhichSearchIsHappeningVisible() {
		VectorLayer vectorLayer = searchPanel.getFeatureSearchVectorLayer();
		if (null != vectorLayer && !vectorLayer.isVisible()) {
			vectorLayer.setVisible(true);
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
