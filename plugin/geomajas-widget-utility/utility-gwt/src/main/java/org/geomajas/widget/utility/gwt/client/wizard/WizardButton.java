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

import com.smartgwt.client.widgets.events.HasClickHandlers;

/**
 * Common interface to be implemented by all wizard buttons.
 * 
 * @param <DATA>
 *            wizard data

 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface WizardButton<DATA> extends HasClickHandlers {

	/**
	 * The 4 classical button types and the page type for direct page navigation.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	enum ButtonType {
		NEXT, PREVIOUS, CANCEL, FINISH, PAGE
	}

	/**
	 * Get the type of the button.
	 * 
	 * @return the type of the button.
	 */
	ButtonType getType();

	/**
	 * Enable/disable this button.
	 * 
	 * @param enabled
	 *            true if enabled, false otherwise
	 */
	void setEnabled(boolean enabled);

	/**
	 * Make this button active/inactive. Only relevant for page buttons, where it indicates whether the button's page is
	 * currently visible.
	 * 
	 * @param active
	 *            true if active, false otherwise
	 */
	void setActive(boolean active);

	/**
	 * Get the page to which this button refers. Only relevant for page buttons.
	 * 
	 * @return the referring page or null if no page button.
	 */
	WizardPage<DATA> getPage();

}
