/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.domain.security;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Category of a territory, used for grouping in the management interface.
 * 
 * @author Oliver May
 * @author Kristof Heirwegh
 */
@Entity
@Table(name = "gdm_territory_category")
public class TerritoryCategory implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "category_type", nullable = false)
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
