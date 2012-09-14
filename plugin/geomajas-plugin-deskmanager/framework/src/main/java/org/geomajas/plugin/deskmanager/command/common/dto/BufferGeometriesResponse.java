/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.command.common.dto;

import org.geomajas.command.CommandResponse;
import org.geomajas.geometry.Geometry;

/**
 * TODO.
 * 
 * @author Jan De Moerloose
 *
 */
public class BufferGeometriesResponse extends CommandResponse {

	private static final long serialVersionUID = 1L;

	private Geometry geometryBuffer;

	private Geometry geometryCenter;

	public Geometry getGeometryBuffer() {
		return geometryBuffer;
	}

	public void setGeometryBuffer(Geometry geometryBuffer) {
		this.geometryBuffer = geometryBuffer;
	}

	public Geometry getGeometryCenter() {
		return geometryCenter;
	}

	public void setGeometryCenter(Geometry geometryCenter) {
		this.geometryCenter = geometryCenter;
	}

}
