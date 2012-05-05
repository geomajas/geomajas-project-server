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

package org.geomajas.command.dto;

import org.geomajas.annotation.Api;
import org.geomajas.command.LayerIdCommandRequest;
import org.geomajas.geometry.Bbox;

/**
 * Request object for {@link org.geomajas.command.render.GetRasterTilesCommand}.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class GetRasterTilesRequest extends LayerIdCommandRequest {

	private static final long serialVersionUID = 151L;

	/**
	 * Command name for this request.
	 *
	 * @since 1.9.0
	 * */
	public static final String COMMAND = "command.render.GetRasterTiles";

	private String crs;

	private Bbox bbox;

	private double scale;

	/**
	 * Crs which is used for the bounding box coordinates.
	 *
	 * @return crs
	 */
	public String getCrs() {
		return crs;
	}

	/**
	 * Set the crs which should be used for the bounding box coordinates.
	 *
	 * @param crs crs
	 */
	public void setCrs(String crs) {
		this.crs = crs;
	}

	/**
	 * Bounding box for which you need the raster data.
	 *
	 * @return bounding box
	 */
	public Bbox getBbox() {
		return bbox;
	}

	/**
	 * Set bounding box for which you need raster data.
	 *
	 * @param bbox bounding box
	 */
	public void setBbox(Bbox bbox) {
		this.bbox = bbox;
	}

	/**
	 * The scale of the view in pixel/unit of coordinate system.
	 *
	 * @return scale
	 */
	public double getScale() {
		return scale;
	}

	/**
	 * Set the view scale in pixels/unit of coordinate system.
	 *
	 * @param scale scale
	 */
	public void setScale(double scale) {
		this.scale = scale;
	}

	@Override
	public String toString() {
		return "GetRasterTilesRequest{" +
				"crs='" + crs + '\'' +
				", bbox=" + bbox +
				", scale=" + scale +
				'}';
	}
}
