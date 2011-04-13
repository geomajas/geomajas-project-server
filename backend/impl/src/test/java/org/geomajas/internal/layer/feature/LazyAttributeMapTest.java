package org.geomajas.internal.layer.feature;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import junit.framework.Assert;

import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.bean.BeanFeatureModel;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.FilterService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vividsolutions.jts.geom.Geometry;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/layer/bean/beanContext.xml", "/org/geomajas/layer/bean/layerBeans.xml" })
public class LazyAttributeMapTest {

	public class MockFeature {

	}

	@Autowired
	@Qualifier("beans")
	private VectorLayer layerBeans;

	@Autowired
	private FilterService filterService;

	@Autowired
	private DtoConverterService converterService;

	@Test
	public void testAttributeAccess() throws LayerException {
		FeatureInfo featureInfo = layerBeans.getLayerInfo().getFeatureInfo();
		Iterator it = layerBeans.getElements(filterService.createFidFilter(new String[] { "1" }), 0, 1);
		MockFeatureModel model = new MockFeatureModel();
		LazyAttributeMap map = new LazyAttributeMap(model, featureInfo, it.next());
		Assert.assertEquals(featureInfo.getAttributes().size(), map.keySet().size());
		//check all attributes
		for (AttributeInfo info : featureInfo.getAttributes()) {
			Assert.assertTrue(map.containsKey(info.getName()));
			Attribute attr = map.get(info.getName());
			//check not editable
			Assert.assertFalse(attr.isEditable());
		}
		//check that all attributes have been accessed
		for (AttributeInfo info : featureInfo.getAttributes()) {
			Assert.assertTrue(model.isAttributeAccessed(info.getName()));
		}
	}

	@Test
	public void testLazyAttributeAccess() throws LayerException {
		FeatureInfo featureInfo = layerBeans.getLayerInfo().getFeatureInfo();
		Iterator it = layerBeans.getElements(filterService.createFidFilter(new String[] { "1" }), 0, 1);
		MockFeatureModel model = new MockFeatureModel();
		LazyAttributeMap map = new LazyAttributeMap(model, featureInfo, it.next());
		// access primitive attributes only
		for (AttributeInfo info : featureInfo.getAttributes()) {
			if(info instanceof PrimitiveAttributeInfo){
				Attribute attr = map.get(info.getName());
			}
		}
		//check that association attributes are not accessed
		for (AttributeInfo info : featureInfo.getAttributes()) {
			if(info instanceof PrimitiveAttributeInfo){
				Assert.assertTrue(model.isAttributeAccessed(info.getName()));
			} else {
				Assert.assertFalse(model.isAttributeAccessed(info.getName()));
			}
		}
		
	}
	
	@Test
	public void testAttributeFiltering() throws LayerException {
		FeatureInfo featureInfo = layerBeans.getLayerInfo().getFeatureInfo();
		Iterator it = layerBeans.getElements(filterService.createFidFilter(new String[] { "1" }), 0, 1);
		LazyAttributeMap map = new LazyAttributeMap(new MockFeatureModel(), featureInfo, it.next());
		// test making attribute editable
		Assert.assertFalse(map.get("booleanAttr").isEditable());
		map.setAttributeEditable("booleanAttr", true);
		Assert.assertTrue(map.get("booleanAttr").isEditable());
		// test removing attribute
		map.removeAttribute("booleanAttr");
		Assert.assertFalse(map.containsKey("booleanAttr"));
	}
	

	public class MockFeatureModel implements FeatureModel {

		private BeanFeatureModel model;
		
		private Set<String> accessed = new HashSet<String>();

		public MockFeatureModel() throws LayerException {
			model = new BeanFeatureModel(layerBeans.getLayerInfo(), 4326, converterService);
		}
		
		public boolean isAttributeAccessed(String name){
			return accessed.contains(name);
		}

		public void setLayerInfo(VectorLayerInfo vectorLayerInfo) throws LayerException {
		}

		public Attribute getAttribute(Object feature, String name) throws LayerException {
			accessed.add(name);
			return model.getAttribute(feature, name);
		}

		public Map<String, Attribute> getAttributes(Object feature) throws LayerException {
			Map<String, Attribute> attribs =  model.getAttributes(feature);
			accessed.addAll(attribs.keySet());
			return attribs;
		}

		public String getId(Object feature) throws LayerException {
			return model.getId(feature);
		}

		public Geometry getGeometry(Object feature) throws LayerException {
			return model.getGeometry(feature);
		}

		public void setAttributes(Object feature, Map<String, Attribute> attributes) throws LayerException {
		}

		public void setGeometry(Object feature, Geometry geometry) throws LayerException {
		}

		public Object newInstance() throws LayerException {
			return null;
		}

		public Object newInstance(String id) throws LayerException {
			return null;
		}

		public int getSrid() throws LayerException {
			return model.getSrid();
		}

		public String getGeometryAttributeName() throws LayerException {
			return model.getGeometryAttributeName();
		}

		public boolean canHandle(Object feature) {
			return true;
		}

	}

}
