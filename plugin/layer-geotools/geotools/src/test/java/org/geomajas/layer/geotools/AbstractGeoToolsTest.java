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
package org.geomajas.layer.geotools;

import org.geomajas.service.DtoConverterService;
import org.geomajas.service.FilterService;
import org.geomajas.spring.ThreadScopeContextHolder;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Base for GeoTools tests.
 * 
 * @author Jan De Moerloose
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/layerCountries.xml",
		"/org/geomajas/layer/geotools/layerPopulatedPlaces110m.xml",
		"/org/geomajas/testdata/simplevectorsContext.xml",
		"/org/geomajas/layer/geotools/test.xml" })
@Transactional(rollbackFor = { Throwable.class })
public abstract class AbstractGeoToolsTest {

	protected static final String SHAPE_FILE = 
		"org/geomajas/testdata/shapes/natural_earth/110m_populated_places_simple.shp";

	protected static final String LAYER_NAME = "110m_populated_places_simple";
	
	protected static final String ATTRIBUTE_NAME = "NAME";

	protected static final String ATTRIBUTE_POPULATION = "POP_OTHER";

	@Autowired
	protected ApplicationContext applicationContext;

	@Autowired
	protected FilterService filterCreator;

	@Autowired
	protected DtoConverterService converterService;
	
	@Before
	public void before() {
		ThreadScopeContextHolder.clear();
	}
}
