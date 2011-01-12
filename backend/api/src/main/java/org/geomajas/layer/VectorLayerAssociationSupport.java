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

package org.geomajas.layer;

import java.util.List;

import org.geomajas.global.Api;
import org.geomajas.global.UserImplemented;
import org.geomajas.layer.feature.Attribute;
import org.opengis.filter.Filter;

/**
 * Extension for vector layers which support associations.
 * 
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
@UserImplemented
public interface VectorLayerAssociationSupport {

	/**
	 * Return the list of possible object values.
	 * 
	 * @param attributeName
	 *            attribute to get objects for
	 * @param filter
	 *            filter to be applied
	 * @return possible object values
	 * @throws LayerException
	 *             oops
	 */
	List<Attribute<?>> getAttributes(String attributeName, Filter filter) throws LayerException;
}
