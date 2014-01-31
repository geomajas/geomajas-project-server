/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.layer.feature;

import org.geomajas.configuration.SyntheticAttributeInfo;

/**
 * Service class for building synthetic attribute values.
 *
 * @param <VALUE_TYPE> attribute value type
 *
 * @author Joachim Van der Auwera
 */
public interface SyntheticAttributeBuilder<VALUE_TYPE> {

	/**
	 * Get the attribute to be built for the (partial) feature.
	 *
	 * @param info attribute definition
	 * @param feature feature for which the attribute should be built
	 * @return the new attribute
	 */
	Attribute<VALUE_TYPE> getAttribute(SyntheticAttributeInfo info, InternalFeature feature);

}
