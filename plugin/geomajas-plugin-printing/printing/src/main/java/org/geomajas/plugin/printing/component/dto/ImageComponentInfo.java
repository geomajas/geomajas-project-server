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
package org.geomajas.plugin.printing.component.dto;

import org.geomajas.annotation.Api;

/**
 * DTO object for ImageComponent.
 * 
 * @author Jan De Moerloose
 * @see org.geomajas.plugin.printing.component.impl.ImageComponentImpl
 * @since 2.0.0
 */
@Api(allMethods = true)
public class ImageComponentInfo extends PrintComponentInfo {

	private static final long serialVersionUID = 200L;

	private String imagePath;

	/**
	 * Get the image path.
	 *
	 * @return image path
	 */
	public String getImagePath() {
		return imagePath;
	}

	/**
	 * Set the image path.
	 *
	 * @param imagePath image path
	 */
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

}
