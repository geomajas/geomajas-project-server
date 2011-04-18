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

import org.geomajas.widget.searchandfilter.search.dto.Criterion;

/**
 * Interface with common methods needed to build a SearchWidget.
 * 
 * @see SearchWidgetRegistry.
 * @author Kristof Heirwegh
 */
public interface SearchWidget {

	String getSearchWidgetId();

	/**
	 * Name of the searchwidget (generally I18N name).
	 * 
	 * @return name of search (to be used on buttons and such).
	 */
	String getName();

	/**
	 * Show your widget for userinteraction, with a searchbutton.
	 */
	void showForSearch();

	/**
	 * Show your widget for userinteraction, with a savebutton.
	 */
	void showForSave();

	void addSearchRequestHandler(SearchRequestHandler handler);

	void removeSearchRequestHandler(SearchRequestHandler handler);

	void addSaveRequestHandler(SaveRequestHandler handler);

	void removeSaveRequestHandler(SaveRequestHandler handler);

	/**
	 * Conveniencemethod so you don't have to add an eventlistener to the searchsystem.
	 * <p>
	 * Called when search starts.
	 * <p>
	 * This allows you to show some interaction in the gui. (for example show a
	 * spinner animation)
	 */
	void onSearchStart();

	/**
	 * Conveniencemethod so you don't have to add an eventlistener to the searchsystem.
	 * <p>
	 * Called when search finished and result was processed.
	 * <p>
	 * This allows you to show some interaction in the gui.
	 */
	void onSearchEnd();

	/**
	 * Configure the widget with previously saved settings.
	 *
	 * @param settings
	 */
	void initialize(Criterion settings);

	/**
	 * clean up the widget to its initial state.
	 */
	void reset();

	// ----------------------------------------------------------

	/**
	 * Handler to be notified when the user presses the save button.
	 * 
	 * @author Kristof Heirwegh
	 */
	public interface SaveRequestHandler {
		void onSaveRequested(SaveRequestEvent event);
	}

	/**
	 * Handler to be notified when the user presses the search button.
	 * 
	 * @author Kristof Heirwegh
	 */
	public interface SearchRequestHandler {
		void onSearchRequested(SearchRequestEvent event);
	}

	/**
	 * Event used in SearchWidgetHandlers.
	 * 
	 * @author Kristof Heirwegh
	 */
	public static class SearchWidgetEvent {
		private final SearchWidget source;
		private final Criterion criterion;

		public SearchWidgetEvent(SearchWidget source, Criterion criterion) {
			this.source = source;
			this.criterion = criterion;
		}

		public SearchWidget getSource() {
			return source;
		}

		public Criterion getCriterion() {
			return criterion;
		}
	}

	/**
	 * Event used in SearchWidgetHandlers.
	 * 
	 * @author Kristof Heirwegh
	 */
	public static class SaveRequestEvent extends SearchWidgetEvent {
		public SaveRequestEvent(SearchWidget source, Criterion featureSearchCriterion) {
			super(source, featureSearchCriterion);
		}
	}

	/**
	 * Event used in SearchWidgetHandlers.
	 * 
	 * @author Kristof Heirwegh
	 */
	public static class SearchRequestEvent extends SearchWidgetEvent {
		public SearchRequestEvent(SearchWidget source, Criterion criterion) {
			super(source, criterion);
		}
	}

}
