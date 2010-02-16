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
package org.geomajas.layer.geotools;

import org.geomajas.service.DtoConverterService;
import org.geomajas.service.FilterService;
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
		"/org/geomajas/testdata/layerCountries.xml", "/org/geomajas/testdata/layerCities.xml",
		"/org/geomajas/testdata/simplevectorsContext.xml", "/org/geomajas/layer/geotools/test.xml" })
@Transactional(rollbackFor = {org.geomajas.global.GeomajasException.class})
public abstract class AbstractGeoToolsTest {
	@Autowired 
	protected ApplicationContext applicationContext;
	
	@Autowired
	protected FilterService filterCreator;

	@Autowired
	protected DtoConverterService converterService;
}
