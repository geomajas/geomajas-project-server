package org.geomajas.gwt.client.widget.attribute;

import javax.annotation.PostConstruct;

import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.gwt.junit.GWTMockUtilities;
import com.google.gwt.junit.client.GWTTestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/gwt/client/mapBeans.xml", "/org/geomajas/gwt/client/layerBeans.xml",
		"/org/geomajas/gwt/client/applicationContext.xml" })
public class AttributeFormTest extends GWTTestCase {

	@Autowired
	private ClientMapInfo mapInfo;

	private MapModel mapModel;

	private VectorLayer layer;

//	public AttributeFormTest() {
//		//GWTMockUtilities.disarm();
//	}

	public String getModuleName() {
		return "org.geomajas.gwt.client.widget.attribute.AttributeFormTest";
	}

	@PostConstruct
	public void initialize() {
		mapModel = new MapModel("mapBeans");
		mapModel.initialize(mapInfo);
		layer = mapModel.getVectorLayer("beansLayer");
	}

	@Test
	public void testSmt() {
		//AttributeFormFactory factory = new DefaultAttributeFormFactory();
		//AttributeForm form = factory.createAttributeForm(layer);
		//Assert.assertNotNull(form);
	}
}