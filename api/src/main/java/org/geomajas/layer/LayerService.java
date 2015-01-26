/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.layer;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Crs;

/**
 * Service for accessing layers in ways which can apply to all types of layers.
 *
 * @author Joachim Van der Auwera
 * @since 1.9.0
 */
@Api(allMethods = true)
public interface LayerService {

	/**
	 * The the {@link org.geomajas.geometry.Crs} for a layer.
	 * <p/>
	 * This can be used as alternative to the deprecated {@link Layer#getCrs()} method.
	 *
	 * @param layer layer to get Crs for
	 * @return crs for layer
	 * @throws LayerException the declared CRS from the layer info does not exist
	 */
	Crs getCrs(Layer layer) throws LayerException;

}
