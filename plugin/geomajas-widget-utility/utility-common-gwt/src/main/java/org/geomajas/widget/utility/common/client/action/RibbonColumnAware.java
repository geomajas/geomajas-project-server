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
package org.geomajas.widget.utility.common.client.action;

import org.geomajas.widget.utility.common.client.ribbon.RibbonColumn;

/**
 * Implement this interface if you need to know the RibbonColumn associated with your action.
 * 
 * @author Kristof Heirwegh
 * 
 * @deprecated Use ButtonAction.
 */
@Deprecated
public interface RibbonColumnAware {

	/**
	 * This method will be called at creation time with the RibbonColumn associated with this action.
	 * 
	 * @param column
	 */
	void setRibbonColumn(RibbonColumn column);

	/**
	 * Get the RibbonColumn associated with this action.
	 * 
	 * @return The RibbonColumn
	 */
	RibbonColumn getRibbonColumn();

}
