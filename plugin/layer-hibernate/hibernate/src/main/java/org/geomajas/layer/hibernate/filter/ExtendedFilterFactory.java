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

package org.geomajas.layer.hibernate.filter;

import org.geotools.filter.FilterFactoryImpl;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.expression.Expression;

/**
 * ???
 * 
 * @deprecated We should switch this class to opengis filters.
 * @author Pieter De Graef
 */
@Deprecated
public class ExtendedFilterFactory extends FilterFactoryImpl {

	public PropertyIsLike like(Expression expr, String pattern, String wildcard, String singleChar,
			String escape) {

		ExtendedLikeFilterImpl filter = new ExtendedLikeFilterImpl();
		filter.setExpression(expr);
		filter.setPattern(pattern, wildcard, singleChar, escape);

		return filter;
	}
}