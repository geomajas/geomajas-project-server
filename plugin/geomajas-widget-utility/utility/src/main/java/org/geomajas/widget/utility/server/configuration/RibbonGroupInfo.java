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
package org.geomajas.widget.utility.server.configuration;

import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.client.ClientWidgetInfo;

/**
 * Ribbon group configuration object. Determines the contents for a group within a ribbon bar.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class RibbonGroupInfo implements ClientWidgetInfo {

	private static final long serialVersionUID = 100L;

	private String title;

	private int internalMargin = -1;
	
	private List<RibbonColumnInfo> columns;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<RibbonColumnInfo> getColumns() {
		return columns;
	}

	public void setColumns(List<RibbonColumnInfo> columns) {
		this.columns = columns;
	}

	/**
	 * Allows to set the internal margin between components in this ribbongroup. 
	 * The default value is -1, meaning the size is determined by the parent RibbonBar.
	 * 
	 * @param internalMargin the internal margin to set
	 */
	public void setInternalMargin(int internalMargin) {
		this.internalMargin = internalMargin;
	}

	/**
	 * @return the internal margin between buttons.
	 */
	public int getInternalMargin() {
		return internalMargin;
	}
}