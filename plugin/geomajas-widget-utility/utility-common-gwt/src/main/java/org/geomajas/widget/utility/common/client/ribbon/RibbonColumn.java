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

package org.geomajas.widget.utility.common.client.ribbon;

import org.geomajas.annotation.Api;

import com.google.gwt.user.client.ui.Widget;

/**
 * Definition of an individual column within a ribbon group. Examples can be big buttons, or vertical columns with a
 * list of buttons, or custom widgets.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface RibbonColumn {

	/**
	 * Enumeration determining possible alignments for titles of the widgets within ribbon columns.
	 * 
	 * @author Pieter De Graef
	 * @since 1.0.0
	 */
	enum TitleAlignment {
		TOP, BOTTOM, RIGHT
	};

	/**
	 * Get the actual widget the represents this ribbon column.
	 * 
	 * @return The actual widget the represents this ribbon column.
	 */
	Widget asWidget();

	/**
	 * Determine whether or not the titles for this column should be displayed.
	 * 
	 * @param showTitles
	 *            Display the titles? true or false.
	 */
	void setShowTitles(boolean showTitles);

	/**
	 * Are the titles of the widget(s) within this columns currently visible?
	 * 
	 * @return See if the titles of the widget(s) within this columns currently visible?
	 */
	boolean isShowTitles();

	/**
	 * Determine title alignment (BOTTOM, RIGHT) for title within this column.
	 *
	 * @param titleAlignment The new value. Applying this new value will immediately trigger the GUI to redraw.
	 */
	void setTitleAlignment(TitleAlignment titleAlignment);

	/**
	 * Get the alignment for titles within this column (BOTTOM, RIGHT).
	 * 
	 * @return The alignment for titles within this column (BOTTOM, RIGHT).
	 */
	TitleAlignment getTitleAlignment();

	/**
	 * Set the base CSS class to be used for buttons within this column.
	 * 
	 * @param buttonBaseStyle
	 *            The button base CSS class.
	 */
	void setButtonBaseStyle(String buttonBaseStyle);

	/**
	 * Add configuration key/value pair.
	 * 
	 * @param key
	 *            parameter key
	 * @param value
	 *            parameter value
	 */
	void configure(String key, String value);

	/**
	 * Is the ribbonColumn enabled?
	 * 
	 * @return true if column is enabled
	 */
	boolean isEnabled();

	/**
	 * Set the enabled state of the RibbonColumn.
	 * 
	 * @param enabled
	 *            The enabled state
	 */
	void setEnabled(boolean enabled);
}