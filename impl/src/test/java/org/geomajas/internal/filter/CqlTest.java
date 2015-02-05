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

package org.geomajas.internal.filter;

import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.springframework.util.Assert;

/**
 * Verify CQL parser working.
 *
 * @author Pieter De Graef 
 */
public class CqlTest {

	@Test
	public void toFilter() {
		Filter f;
		try {
			f = CQL.toFilter("a.b < 5");
		} catch (CQLException e) {
			f = null;
		}
		Assert.notNull(f, "Filter should not be null");

//		try {
//	        f = CQL.toFilter("bookstore[1].book < 5");
//        } catch (CQLException e) {
//        	f = null;
//        }
//		Assert.notNull(f, "Filter should not be null");
	}
}
