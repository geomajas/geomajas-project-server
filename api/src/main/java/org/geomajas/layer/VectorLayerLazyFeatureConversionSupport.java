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
import org.geomajas.annotation.UserImplemented;

/**
 * Allow vector layers to indicate that they support lazy feature conversion.
 *
 * @author Joachim Van der Auwera
 * @since 1.9.0
 */
@Api(allMethods = true)
@UserImplemented
public interface VectorLayerLazyFeatureConversionSupport {

	/**
	 * Indicate whether lazy feature conversion should be used.
	 * <p/>
	 * This can be important for features where just getting an attribute can be an expensive operation.
	 *
	 * @return true when features should be converted lazily
	 */
	boolean useLazyFeatureConversion();
}
