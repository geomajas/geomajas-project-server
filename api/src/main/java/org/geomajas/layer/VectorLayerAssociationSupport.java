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

package org.geomajas.layer;

import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;
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
	 * Return the list of possible object values for the specified attribute path. An attribute path is a recursive path
	 * to a (possibly nested) attribute name, using '.' as a separator.
	 * 
	 * @param attributePath attribute path to get objects for
	 * @param filter filter to be applied
	 * @return possible object values
	 * @throws LayerException oops
	 */
	List<Attribute<?>> getAttributes(String attributePath, Filter filter) throws LayerException;
}
