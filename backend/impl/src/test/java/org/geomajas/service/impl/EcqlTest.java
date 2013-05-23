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

package org.geomajas.service.impl;

import org.geotools.filter.text.ecql.ECQL;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.filter.Filter;

/**
 * Test for the Geotools ECQL library.
 * 
 * @author Kristof Heirwegh
 */
public class EcqlTest {

	private static final String FILTER_SIMPLE_LARGE =                "POPULATION >= 1000000000";
	private static final String FOR_SOME_REASON_THIS_DOES_NOT_WORK = "POPULATION >= 100000000";
	private static final String FILTER_SIMPLE_SMALL =                "POPULATION >= 10000000";

	@Test
	public void testParseFilter() throws Exception {
		Filter res = ECQL.toFilter(FILTER_SIMPLE_LARGE);
		Assert.assertNotNull(res);
		res = ECQL.toFilter(FILTER_SIMPLE_SMALL);
		Assert.assertNotNull(res);
	}

	/**
	 * Unignore this once it is fixed in Geotools: GEOT-4211.
	 * TODO FIXME (in Geotools CQL).
	 * 
	 * @throws Exception
	 */
	@Ignore
	@Test
	public void testParseFilterBad() throws Exception {
		Filter res = ECQL.toFilter(FOR_SOME_REASON_THIS_DOES_NOT_WORK);
		Assert.assertNotNull(res);
	}
}
