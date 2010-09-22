/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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

//	public PropertyIsEqualTo equal(Expression expr1, Expression expr2, boolean matchCase) {
//		return new ExtendedIsEqualsToImpl(this, expr1, expr2, matchCase);
//	}

	public PropertyIsLike like(Expression expr, String pattern, String wildcard, String singleChar,
			String escape) {

		ExtendedLikeFilterImpl filter = new ExtendedLikeFilterImpl();
		filter.setExpression(expr);
		filter.setPattern(pattern, wildcard, singleChar, escape);

		return filter;
	}
}