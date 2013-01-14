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


/**
 * TODO.
 * 
 * @author Jan De Moerloose
 * @deprecated this is magdageo specific.
 */
@Deprecated
public enum CategoryType {
	
	/**
	 * ...
	 */
	VO("Vlaamse Overheid"), 
	/**
	 * ...
	 */
	GEMEENTE("Gemeentelijke Overheden"), 
	/**
	 * ...
	 */
	PROVINCIE("Provinciale Overheden");

	private final String description;

	private CategoryType(String description) {
		this.description = description;
	}

	/**
	 * TODO.
	 * @return
	 */
	public String getDescription() {
		return description;
	}
}
