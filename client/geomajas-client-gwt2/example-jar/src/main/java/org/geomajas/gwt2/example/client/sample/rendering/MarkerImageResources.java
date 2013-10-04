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

package org.geomajas.gwt2.example.client.sample.rendering;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Images for marker panel.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface MarkerImageResources extends ClientBundle {

	/**
	 * Image sprite.
	 * 
	 * @return
	 */
	@Source("images/marker1.png")
	ImageResource marker1();

}
