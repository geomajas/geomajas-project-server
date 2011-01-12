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

package org.geomajas.plugin.rasterizing;

import org.geomajas.global.Api;
import org.geomajas.plugin.caching.step.CacheContainer;

/**
 * Container for result of a rasterize request in {@link RasterizingController}.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api(allMethods = true)
public class RasterizingContainer extends CacheContainer {

	private byte[] image;

	/**
	 * Get the image.
	 *
	 * @return image data
	 */
	public byte[] getImage() {
		return image;
	}

	/**
	 * Set the image data.
	 *
	 * @param image data
	 */
	public void setImage(byte[] image) {
		this.image = image;
	}
}
