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
package org.geomajas.widget.utility.smartgwt.client.wizard;

import org.geomajas.annotation.FutureApi;

import com.google.gwt.user.client.ui.Widget;

/**
 * Abstract class to be extended by all wizard pages. The following abstract methods have to be implemented:
 * <ul>
 * <li>getTitle() : return the title of the page</li>
 * <li>getExplanation() : return an explanatory text for the page</li>
 * <li>doValidate() : validate the page (e.g. validate the form on the page)</li>
 * <li>asWidget() : return the actual view component</li>
 * <li>clear() : clear the page (e.g. clearing the form on the page)</li>
 * <li>show() : prepare the page for showing (e.g. transfer wizard data to the form)</li>
 * </ul>
 *
 * @param <DATA> data type
 * 
 * @author Jan De Moerloose
 */
@FutureApi
public abstract class WizardPage<DATA> {

	private DATA wizardData;

	private WizardPage<DATA> backPage;

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
	public WizardPage<DATA> getBackPage() {
		return backPage;
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
		WizardPage<DATA> back = getBackPage();
		while (back != null && back.isValid()) {
			back = back.getBackPage();
		}
		return back == null;
	}

	protected void setValid(boolean valid) {
		this.valid = valid;
	}

	protected abstract boolean doValidate();

	protected abstract Widget asWidget();

	protected abstract void clear();

	protected abstract void show();

	protected void setWizardData(DATA wizardData) {
		this.wizardData = wizardData;
	}

	protected void setBackPage(WizardPage<DATA> backPage) {
		this.backPage = backPage;
	}

	protected void setNextPage(WizardPage<DATA> nextPage) {
		this.nextPage = nextPage;
	}

}
