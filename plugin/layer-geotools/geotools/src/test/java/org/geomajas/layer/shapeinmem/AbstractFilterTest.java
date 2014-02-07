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
package org.geomajas.layer.shapeinmem;

import org.geomajas.layer.LayerException;
import org.geomajas.service.FilterService;
import org.junit.After;
import org.junit.runner.RunWith;
import org.opengis.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test for shape-in-memory layer.
 *
 * @author Pieter De Graef
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml", "/org/geomajas/layer/transaction.xml",
		"/org/geomajas/testdata/layerCountries.xml", "/org/geomajas/testdata/simplevectorsContext.xml",
		"/org/geomajas/layer/shapeinmem/test.xml" })
public abstract class AbstractFilterTest {

	public static final String PARAM_TEXT_ATTR = "TEXTATTR";

	public static final String PARAM_INT_ATTR = "INTATTR";

	public static final String PARAM_FLOAT_ATTR = "FLOATATTR";

	public static final String PARAM_DOUBLE_ATTR = "DOUBLEATTR";

	public static final String PARAM_BOOLEAN_ATTR = "BOOLEANATT";

	public static final String PARAM_DATE_ATTR = "DATEATTR";

	public static final String PARAM_GEOMETRY_ATTR = "the_geom";

	
	@Autowired
	protected FilterService filterService;

	@Autowired()
	@Qualifier("filterTest")
	protected ShapeInMemLayer layer;

	protected Filter filter;

	@After
	public void refreshContext() throws LayerException {
		layer.initFeatures();
	}
}
