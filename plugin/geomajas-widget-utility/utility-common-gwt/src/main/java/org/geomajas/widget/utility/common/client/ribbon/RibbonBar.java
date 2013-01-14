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
 * <p>
 * Definition of a ribbon bar widget. Such ribbon bars can contain a list of ribbon groups that in turn contain ribbon
 * group columns. Within those columns buttons and other widgets are displayed.
 * </p>
 * <p>
 * The idea is to replace the default toolbar with this widget, as it is more user-friendly (displaying titles, etc).
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface RibbonBar {

	/**
	 * Get the actual widget the represents this ribbon bar.
	 * 
	 * @return The actual widget the represents this ribbon bar.
	 */
	Widget asWidget();

	/**
	 * Determines whether or not the group titles should be displayed.
	 * 
	 * @param showGroupTitles
	 *            The new value - true or false.
	 */
	void setShowGroupTitles(boolean showGroupTitles);

	/**
	 * Are the group titles currently displayed or not?
	 * 
	 * @return Are the group titles currently displayed or not?
	 */
	boolean isShowGroupTitles();

	/**
	 * Get the ribbon group that is associated with the given index.
	 * 
	 * @param index
	 *            The index at which to search a ribbon group.
	 * @return The ribbon group that is associated with the given index.
	 */
	RibbonGroup getGroup(int index);

	/**
	 * Add a new group to the ribbon bar. New groups are added at the end (right).
	 * 
	 * @param ribbonGroup
	 *            The group to add to the ribbon bar.
	 */
	void addGroup(RibbonGroup ribbonGroup);

	/**
	 * Insert a new group into the ribbon bar.
	 * 
	 * @param ribbonGroup
	 *            The group to add to the ribbon bar.
	 * @param index
	 *            The position at which to insert the ribbon group.
	 */
	void addGroup(RibbonGroup ribbonGroup, int index);

	/**
	 * Remove a ribbon group from this bar.
	 * 
	 * @param ribbonGroup
	 *            The ribbon group to remove.
	 */
	void removeGroup(RibbonGroup ribbonGroup);

	/**
	 * Remove the ribbon group at the given index from this bar.
	 * 
	 * @param index
	 *            The index at which to delete a ribbon group.
	 */
	void removeGroup(int index);
}