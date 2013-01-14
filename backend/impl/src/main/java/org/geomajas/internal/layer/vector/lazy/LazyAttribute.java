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

package org.geomajas.internal.layer.vector.lazy;

import org.geomajas.layer.feature.Attribute;

/**
 * Marker interface to indicate that a {@link Attribute} implementations is lazy.
 *
 * @param <VALUE_TYPE> type of the attribute value
 *
 * @author Joachim Van der Auwera
 */
public interface LazyAttribute<VALUE_TYPE> extends Attribute<VALUE_TYPE> {

	/**
	 * Convert this object to the non-lazy/instantiated variant.
	 *
	 * @return non-lazy attribute
	 */
	Attribute<VALUE_TYPE> instantiate();

}
