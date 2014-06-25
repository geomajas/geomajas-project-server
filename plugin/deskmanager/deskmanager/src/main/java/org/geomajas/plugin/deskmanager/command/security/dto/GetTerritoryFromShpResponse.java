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
package org.geomajas.plugin.deskmanager.command.security.dto;

import org.geomajas.command.CommandResponse;
import org.geomajas.geometry.Geometry;

/**
 * Response for {@link org.geomajas.plugin.deskmanager.command.security.GetTerritoryFromShpCommand}.
 * 
 * @author Jan Venstermans
 * 
 */
public class GetTerritoryFromShpResponse extends CommandResponse {

	private static final long serialVersionUID = 115L;

	private Geometry geometry;

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}
}
