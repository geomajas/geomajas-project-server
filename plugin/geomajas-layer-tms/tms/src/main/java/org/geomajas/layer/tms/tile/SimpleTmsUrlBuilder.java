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

package org.geomajas.layer.tms.tile;

import org.geomajas.layer.tile.TileCode;

/**
 * Tile URL builder that builds URLs based upon a base TMS URL and an extension. All tiles will look like this:<br/>
 * <code>baseTmsUrl + "/" + tileLevel + "/" + X + "/" + Y.extension</code>
 * 
 * @author Pieter De Graef
 */
public class SimpleTmsUrlBuilder implements TileUrlBuilder {

	private final String extension;

	public SimpleTmsUrlBuilder(String extension) {
		if (extension == null) {
			throw new IllegalStateException("Values may not be null.");
		}
		if (extension.charAt(0) != '.') {
			this.extension = "." + extension;
		} else {
			this.extension = extension;
		}
	}

	/** {@inheritDoc} */
	public String buildUrl(TileCode tileCode, String baseTmsUrl) {
		StringBuilder builder = new StringBuilder(baseTmsUrl);
		if (!baseTmsUrl.endsWith("/")) {
			builder.append("/");
		}
		builder.append(tileCode.getTileLevel());
		builder.append("/");
		builder.append(tileCode.getX());
		builder.append("/");
		builder.append(tileCode.getY());
		builder.append(extension);
		return builder.toString();
	}
	
}