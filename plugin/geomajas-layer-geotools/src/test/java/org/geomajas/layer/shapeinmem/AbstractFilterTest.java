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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
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
