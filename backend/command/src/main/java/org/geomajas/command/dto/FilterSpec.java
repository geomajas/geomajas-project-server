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

package org.geomajas.command.dto;

import java.io.Serializable;

/**
 * Class which specifies a filter for a specific client layer together with the serverlayer id.
 *
 * @author An Buyle
 * @since 1.10.0
 */
public class FilterSpec implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String serverLayerId;
	private String filter;
	
	public FilterSpec() {
		super();
	}
	public FilterSpec(String serverLayerId, String filter) {
		super();
		this.serverLayerId = serverLayerId;
		this.filter = filter;
	}
	public void setServerLayerId(String serverLayerId) {
		this.serverLayerId = serverLayerId;
	}
	public String getServerLayerId() {
		return serverLayerId;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
	
	public String getFilter() {
		return filter;
	}
	
	
}
