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
 * Definition of a group of actions on a ribbon bar. It groups together a few ribbon columns and displays a group title.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface RibbonGroup {

	/**
	 * Get the actual widget the represents this ribbon group.
	 * 
	 * @return The actual widget the represents this ribbon group.
	 */
	Widget asWidget();

	/**
	 * Get the title for this ribbon group.
	 * 
	 * @return The title for this ribbon group.
	 */
	String getTitle();

	/**
	 * Determine whether or not the title for this group should be displayed.
	 * 
	 * @param showTitle
	 *            Display the title? true or false.
	 */
	void setShowTitle(boolean showTitle);

	/**
	 * See if the title for this group is currently shown or not?
	 * 
	 * @return Is the title displayed?
	 */
	boolean isShowTitle();

	/**
	 * Get the ribbon column that is associated with the given index.
	 * 
	 * @param index
	 *            The index at which to search a ribbon column.
	 * @return The ribbon column that is associated with the given index.
	 */
	RibbonColumn getColumn(int index);

	/**
	 * Add a new column to the ribbon group. New columns are added at the end (right).
	 * 
	 * @param ribbonColumn
	 *            The column to add to the ribbon group.
	 */
	void addColumn(RibbonColumn ribbonColumn);

	/**
	 * Insert a new column into the ribbon group.
	 * 
	 * @param ribbonColumn
	 *            The column to add to the ribbon group.
	 * @param index
	 *            The position at which to insert the ribbon column.
	 */
	void addColumn(RibbonColumn ribbonColumn, int index);

	/**
	 * Remove a ribbon column from this group.
	 * 
	 * @param ribbonColumn
	 *            The ribbon column to remove.
	 */
	void removeColumn(RibbonColumn ribbonColumn);

	/**
	 * Remove the ribbon column at the given index from this group.
	 * 
	 * @param index
	 *            The index at which to delete a ribbon column.
	 */
	void removeColumn(int index);
}