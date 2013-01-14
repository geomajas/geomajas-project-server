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
package org.geomajas.plugin.deskmanager.domain.security.dto;

import java.io.Serializable;

/**
 * @author Oliver May
 * @author Kristof Heirwegh
 */
public class CategoryDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	private String description;

	private String name;

	// ----------------------------------------------------------

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategoryType() {
		return name;
	}

	public void setCategoryType(String name) {
		this.name = name;
	}
}
