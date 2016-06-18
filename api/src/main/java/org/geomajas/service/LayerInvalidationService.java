/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.service;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.ExpectAlternatives;
import org.geomajas.annotation.UserImplemented;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.Layer;

/**
 * Service for invalidating layers. This should be called whenever significant changes to the layer configuration have
 * occurred. These include deleting the layer, changes in the style, data structure, security, authorizations,...
 *
 * @author Joachim Van der Auwera
 * @since 1.8.0
 */
@Api(allMethods = true)
@UserImplemented
@ExpectAlternatives
public interface LayerInvalidationService {

	/**
	 * Invalidate the layer. This should be called whenever significant changes to the layer have occurred. These
	 * include deleting the layer, changes in the style, data structure, security, authorizations,...
	 *
	 * @param layer layer which needs to be invalidated (can be null indicating the across-layer cache)
	 * @throws GeomajasException oops (should not happen, steps should just log and not (re)throw exceptions)
	 */
	void invalidateLayer(Layer layer) throws GeomajasException;

}
