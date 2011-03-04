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

package org.geomajas.puregwt.client.map;

import org.vaadin.gwtgraphics.client.Group;

/**
 * Default implementation of the WorldContainer interface. It represents a vector group element in world space.
 * 
 * @author Pieter De Graef
 */
public class WorldGroup extends Group implements WorldContainer {

	private String id;

	public WorldGroup(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}