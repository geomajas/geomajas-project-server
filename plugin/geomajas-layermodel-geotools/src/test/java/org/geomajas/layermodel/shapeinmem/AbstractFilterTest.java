package org.geomajas.layermodel.shapeinmem;

import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.GeometricAttributeInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.PrimitiveType;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.service.FilterCreator;
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

	protected ShapeInMemLayerModel layerModel;

	protected FilterCreator filterCreator;

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
						"org/geomajas/testdata/simplevectorsContext.xml"});
		filterCreator = applicationContext.getBean("service.FilterCreator", FilterCreator.class);
		layerModel = applicationContext.getBean(
				"layermodel.shapeinmem.ShapeInMemLayerModel", ShapeInMemLayerModel.class);
		layerModel.setUrl(SHAPE_FILE);

		layerModel.setLayerInfo(layerInfo);
	}
}
