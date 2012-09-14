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

import org.geomajas.command.CommandRequest;
import org.geomajas.geometry.Geometry;

/**
 * TODO.
 * 
 * @author Jan De Moerloose
 *
 */
public class BufferGeometriesRequest implements CommandRequest {

	private static final long serialVersionUID = 1L;

	public static final String COMMAND = "command.deskmanager.BufferGeometries";

	private Geometry[] geometries;

	private double buffer = 5.0d;

	public Geometry[] getGeometries() {
		return geometries;
	}

	public void setGeometries(Geometry[] geometries) {
		this.geometries = geometries.clone();
	}

	public double getBuffer() {
		return buffer;
	}

	public void setBuffer(double buffer) {
		this.buffer = buffer;
	}
}
