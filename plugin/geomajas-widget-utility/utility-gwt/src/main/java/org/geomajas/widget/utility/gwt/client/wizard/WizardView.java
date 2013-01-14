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
package org.geomajas.widget.utility.gwt.client.wizard;

import java.util.List;

import org.geomajas.annotation.Api;

import com.google.gwt.user.client.ui.Widget;

/**
 * View part of the {@link Wizard}.
 *
 * @param <DATA> data type
 *
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface WizardView<DATA> {

	/**
	 * Add a page to the view.
	 * 
	 * @param page
	 *            the page to be added
	 */
	void addPageToView(WizardPage<DATA> page);

	/**
	 * Called when the visible page of this view changes. Views can implement this method to perform custom actions
	 * (e.g. setting a title).
	 * 
	 * @param currentPage
	 *            the current page.
	 */
	void setCurrentPage(WizardPage<DATA> currentPage);

	/**
	 * Return a list of all buttons of this view.
	 * 
	 * @return list with all buttons
	 */
	List<WizardButton<DATA>> getButtons();

	/**
	 * Return the actual widget implementation of this view.
	 * 
	 * @return the widget
	 */
	Widget asWidget();

	/**
	 * Set an indicator to show that this page is doing some loading.
	 * 
	 * @param loading
	 *            true to set indicator, false to remove indicator
	 */
	void setLoading(boolean loading);

}
