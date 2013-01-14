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
package org.geomajas.command.dto;

import org.geomajas.command.CommandRequest;
import org.geomajas.geometry.Geometry;

/**
 * Request object for {@link org.geomajas.command.geometry.SplitPolygonCommand}.
 * 
 * @author Joachim Van der Auwera
 * @deprecated use {@link org.geomajas.command.geometry.GeometrySplitCommand}
 */
@Deprecated
public class SplitPolygonRequest implements CommandRequest {

	private static final long serialVersionUID = 151L;

	/**
	 * Command name for this request.
	 *
	 * @since 1.9.0
	 * */
	public static final String COMMAND = "command.geometry.SplitPolygon";

	private Geometry geometry;

	private Geometry splitter;

	/**
	 * Get the geometry which needs to be split. This has to be a polygon.
	 *
	 * @return polygon to split
	 */
	public Geometry getGeometry() {
		return geometry;
	}

	/**
	 * Set the geometry which needs to be split. This has to be a polygon.
	 *
	 * @param geometry polygon to split
	 */
	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	/**
	 * Get the linestring to use as splitter.
	 *
	 * @return linestring to use as splitter
	 */
	public Geometry getSplitter() {
		return splitter;
	}

	/**
	 * Set the linestring to use as splitter.
	 *
	 * @param splitter linestring to use as splitter
	 */
	public void setSplitter(Geometry splitter) {
		this.splitter = splitter;
	}

	@Override
	public String toString() {
		return "SplitPolygonRequest{" +
				"geometry=" + geometry +
				", splitter=" + splitter +
				'}';
	}
}
