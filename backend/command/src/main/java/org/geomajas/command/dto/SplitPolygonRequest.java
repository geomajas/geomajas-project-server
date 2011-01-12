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

import org.geomajas.command.CommandRequest;
import org.geomajas.geometry.Geometry;

/**
 * Request object for {@link org.geomajas.command.geometry.SplitPolygonCommand}.
 * 
 * @author Joachim Van der Auwera
 */
public class SplitPolygonRequest implements CommandRequest {

	private static final long serialVersionUID = 151L;

	private Geometry geometry;

	private Geometry splitter;

	public SplitPolygonRequest() {
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	public Geometry getSplitter() {
		return splitter;
	}

	public void setSplitter(Geometry splitter) {
		this.splitter = splitter;
	}
}
