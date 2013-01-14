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

import org.geomajas.annotation.Api;

import com.google.gwt.user.client.ui.Widget;

/**
 * Abstract class to be extended by all wizard pages. The following abstract methods have to be implemented:
 * <ul>
 * <li>getTitle() : return the title of the page</li>
 * <li>getExplanation() : return an explanatory text for the page</li>
 * <li>doValidate() : validate the page (e.g. validate the form on the page)</li>
 * <li>savePage() : save the page</li>
 * <li>asWidget() : return the actual view component</li>
 * <li>clear() : clear the page (e.g. clearing the form on the page)</li>
 * <li>show() : prepare the page for showing (e.g. transfer wizard data to the form)</li>
 * </ul>
 *
 * @param <DATA> data type
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api
public abstract class WizardPage<DATA> {

	private DATA wizardData;

	private WizardPage<DATA> previousPage;

	private WizardPage<DATA> nextPage;

	private boolean valid;

	/**
	 * Get the wizard data (same for all pages).
	 * 
	 * @return the data
	 */
	public DATA getWizardData() {
		return wizardData;
	}

	/**
	 * Get the previous page.
	 *
	 * @return the previous page
	 */
	public WizardPage<DATA> getPreviousPage() {
		return previousPage;
	}

	/**
	 * Set the previous page.
	 *
	 * @param previousPage previous page
	 */
	public void setPreviousPage(WizardPage<DATA> previousPage) {
		this.previousPage = previousPage;
	}

	/**
	 * Get the next page.
	 *
	 * @return the next page
	 */
	public WizardPage<DATA> getNextPage() {
		return nextPage;
	}

	/**
	 * Set the next page.
	 *
	 * @param nextPage next page
	 */
	public void setNextPage(WizardPage<DATA> nextPage) {
		this.nextPage = nextPage;
	}

	/**
	 * Return whether this page is valid (last validation status).
	 * 
	 * @return true if valid
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * Validate this page.
	 * 
	 * @return true if valid
	 */
	public boolean validate() {
		setValid(doValidate());
		return isValid();
	}

	/**
	 * Save the page data. This can communicate with the server if needed. The return is handled by calling either the
	 * success or failure callback.
	 *
	 * @param view wizard view
	 * @param successCallback what to do on success
	 * @param failureCallback what to do on failure
	 */
	public void savePage(WizardView view, Runnable successCallback, Runnable failureCallback) {
		if (null != successCallback) {
			successCallback.run();
		}
	}

	/**
	 * Get a title for this page. Will appear in button.
	 * 
	 * @return title
	 */
	public abstract String getTitle();

	/**
	 * Get an explanatory text for this page. Will appear on top of page.
	 * 
	 * @return explanation
	 */
	public abstract String getExplanation();

	/**
	 * Returns whether this page can already be shown to the user. Default implementation checks whether all previous
	 * pages have been validated.
	 * 
	 * @return true if page can be shown
	 */
	public boolean canShow() {
		WizardPage<DATA> back = getPreviousPage();
		while (back != null && back.isValid()) {
			back = back.getPreviousPage();
		}
		return back == null;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	/**
	 * Method to overwrite which does the actual page validation.
	 *
	 * @return true when page validation is successful
	 */
	public abstract boolean doValidate();

	/**
	 * Method to overwrite. Get the the widget for the wizard.
	 *
	 * @return widget
	 */
	public abstract Widget asWidget();

	/**
	 * Clear the page content.
	 */
	public abstract void clear();

	/**
	 * Show the page.
	 */
	public abstract void show();

	/**
	 * Set the wizard data for the page.
	 *
	 * @param wizardData wizard data
	 */
	public void setWizardData(DATA wizardData) {
		this.wizardData = wizardData;
	}
}
