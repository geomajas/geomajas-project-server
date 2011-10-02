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
package org.geomajas.widget.utility.configuration;

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
}