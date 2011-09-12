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
 * Ribbon configuration information object. This object represents a single ribbon bar. Can be used within a tabbed
 * ribbon, or as a stand-alone ribbon bar.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class RibbonBarInfo implements ClientWidgetInfo {

	private static final long serialVersionUID = 100L;

	private int internalMargin = 2;
	
	private int internalRibbonGroupMargin = 10;
	
	private String title;

	private List<RibbonGroupInfo> groups;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<RibbonGroupInfo> getGroups() {
		return groups;
	}

	public void setGroups(List<RibbonGroupInfo> groups) {
		this.groups = groups;
	}

	/**
	 * Set the internal margin between ribbon groups. Defaults to 2.
	 * 
	 * @param internalMargin the internalMargin to set
	 */
	public void setInternalMargin(int internalMargin) {
		this.internalMargin = internalMargin;
	}

	/**
	 * @return the internalMargin
	 */
	public int getInternalMargin() {
		return internalMargin;
	}

	/**
	 * Set the internal margin between components within ribon groups. Defaults to 10. 
	 * Note that this can be ovverridden by {@link RibbonGroupInfo}.setInternalMargin(int margin).
	 * 
	 * @param internalRibbonGroupMargin the internalRibbonGroupMargin to set
	 */
	public void setInternalRibbonGroupMargin(int internalRibbonGroupMargin) {
		this.internalRibbonGroupMargin = internalRibbonGroupMargin;
	}

	/**
	 * @return the internalRibbonGroupMargin
	 */
	public int getInternalRibbonGroupMargin() {
		return internalRibbonGroupMargin;
	}
}