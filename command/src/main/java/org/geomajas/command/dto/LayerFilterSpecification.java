/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.command.dto;

import java.io.Serializable;

import org.geomajas.annotation.Api;

/**
 * Class which specifies a layer filter (e.g. for a specific client layer) together with the server layer id.
 * It is used by {@link org.geomajas.command.dto.SearchByLocationRequest}.
 * 
 * @author An Buyle
 * @since 1.10.0
 */
@Api(allMethods = true)
public class LayerFilterSpecification  implements Serializable {

	private static final long serialVersionUID = 1100L; /* 1.10.0 */

	private String serverLayerId;
	private String filter; // filter expression, if null, a true layer filter has to be applied

	/**
	 * Default constructor (no info).
	 */
	public LayerFilterSpecification() {
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param serverLayerId server layer id
	 * @param filter optional filter expression which should be applied on the specified layer, 
	 * 					if null, a true filter will be applied
	 */
	public LayerFilterSpecification(String serverLayerId, String filter) {
		this();
		this.serverLayerId = serverLayerId;
		this.filter = filter;
	}

	/**
	 * Get the server layer id.
	 *
	 * @param serverLayerId server layer id
	 */
	public void setServerLayerId(String serverLayerId) {
		this.serverLayerId = serverLayerId;
	}

	/**
	 * Set the server layer id.
	 *
	 * @return serverLayerId server layer id
	 */
	public String getServerLayerId() {
		return serverLayerId;
	}

	/**
	 * Get the additional filter which needs to be applied on the layer.
	 *
	 * @param filter optional filter expression which should be applied on the specified layer, 
	 * 					if null, a true filter will be applied 
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}

	/**
	 * Set the additional filter which needs to be applied on the layer.
	 *
	 * @return layer filter expression (null means true filter)
	 */
	public String getFilter() {
		return filter;
	}

	@Override
	public String toString() {
		return "{serverLayerId='" + this.serverLayerId + "'; filter='" +  filter + "'}";
	}
}
