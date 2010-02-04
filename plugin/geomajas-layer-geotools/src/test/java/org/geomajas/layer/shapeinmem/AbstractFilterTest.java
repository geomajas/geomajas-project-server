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

import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.GeometricAttributeInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.PrimitiveType;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.layer.shapeinmem.ShapeInMemLayer;
import org.geomajas.service.FilterService;
import org.junit.Before;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFilterTest {

	private static final String SHAPE_FILE = "classpath:org/geomajas/testdata/shapes/filtertest/inmemfiltertest.shp";

	protected static final String PARAM_TEXT_ATTR = "TEXTATTR";

	protected static final String PARAM_INT_ATTR = "INTATTR";

	protected static final String PARAM_FLOAT_ATTR = "FLOATATTR";

	protected static final String PARAM_DOUBLE_ATTR = "DOUBLEATTR";

	protected static final String PARAM_BOOLEAN_ATTR = "BOOLEANATT";

	protected static final String PARAM_DATE_ATTR = "DATEATTR";

	protected static final String PARAM_GEOMETRY_ATTR = "the_geom";

	protected ShapeInMemLayer layer;

	protected FilterService filterCreator;

	@Before
	public void setUp() throws Exception {
		FeatureInfo ft = new FeatureInfo();
		ft.setDataSourceName("inmemfiltertest");

		PrimitiveAttributeInfo ia = new PrimitiveAttributeInfo();
		ia.setLabel("id");
		ia.setName("Id");
		ia.setType(PrimitiveType.STRING);
		ft.setIdentifier(ia);

		GeometricAttributeInfo ga = new GeometricAttributeInfo();
		ga.setName("the_geom");
		ga.setEditable(false);
		ft.setGeometryType(ga);

		List<AttributeInfo> attr = new ArrayList<AttributeInfo>();

		PrimitiveAttributeInfo pa = new PrimitiveAttributeInfo();
		pa.setLabel(PARAM_TEXT_ATTR);
		pa.setName(PARAM_TEXT_ATTR);
		pa.setEditable(false);
		pa.setIdentifying(true);
		pa.setType(PrimitiveType.STRING);
		attr.add(pa);

		PrimitiveAttributeInfo pa2 = new PrimitiveAttributeInfo();
		pa2.setLabel(PARAM_INT_ATTR);
		pa2.setName(PARAM_INT_ATTR);
		pa2.setEditable(false);
		pa2.setIdentifying(true);
		pa2.setType(PrimitiveType.INTEGER);
		attr.add(pa2);

		PrimitiveAttributeInfo pa3 = new PrimitiveAttributeInfo();
		pa3.setLabel(PARAM_FLOAT_ATTR);
		pa3.setName(PARAM_FLOAT_ATTR);
		pa3.setEditable(false);
		pa3.setIdentifying(true);
		pa3.setType(PrimitiveType.FLOAT);
		attr.add(pa3);

		PrimitiveAttributeInfo pa4 = new PrimitiveAttributeInfo();
		pa4.setLabel(PARAM_DOUBLE_ATTR);
		pa4.setName(PARAM_DOUBLE_ATTR);
		pa4.setEditable(false);
		pa4.setIdentifying(true);
		pa4.setType(PrimitiveType.DOUBLE);
		attr.add(pa4);

		PrimitiveAttributeInfo pa5 = new PrimitiveAttributeInfo();
		pa5.setLabel(PARAM_BOOLEAN_ATTR);
		pa5.setName(PARAM_BOOLEAN_ATTR);
		pa5.setEditable(false);
		pa5.setIdentifying(true);
		pa5.setType(PrimitiveType.BOOLEAN);
		attr.add(pa5);

		PrimitiveAttributeInfo pa6 = new PrimitiveAttributeInfo();
		pa6.setLabel(PARAM_DATE_ATTR);
		pa6.setName(PARAM_DATE_ATTR);
		pa6.setEditable(false);
		pa6.setIdentifying(true);
		pa6.setType(PrimitiveType.DATE);
		attr.add(pa6);

		ft.setAttributes(attr);

		VectorLayerInfo layerInfo = new VectorLayerInfo();
		layerInfo.setFeatureInfo(ft);
		layerInfo.setCrs("EPSG:4326");

		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				new String[] {"org/geomajas/spring/geomajasContext.xml",
						"org/geomajas/testdata/layerCountries.xml",
						"org/geomajas/testdata/simplevectorsContext.xml",
						"org/geomajas/layer/shapeinmem/test.xml"});
		filterCreator = applicationContext.getBean("service.FilterService", FilterService.class);
		layer = applicationContext.getBean("filterTest", ShapeInMemLayer.class);
		layer.setUrl(SHAPE_FILE);

		layer.setLayerInfo(layerInfo);
	}
}
