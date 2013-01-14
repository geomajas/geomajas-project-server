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

package org.geomajas.layer.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.geomajas.layer.LayerException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.attribute.AssociationValue;
import org.geomajas.layer.feature.attribute.LongAttribute;
import org.geomajas.layer.feature.attribute.ManyToOneAttribute;
import org.geomajas.layer.feature.attribute.OneToManyAttribute;
import org.geomajas.layer.feature.attribute.StringAttribute;
import org.geomajas.service.FilterService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/layer/bean/layerEditingBeans.xml" })
public class BeanLayerEditingTest {

	@Autowired
	@Qualifier("layerEditingBeans")
	private VectorLayer layer;

	@Autowired
	private FilterService filterService;

	@Test
	public void testCreateNested() throws LayerException {
		// retrieve test feature
		Object test = layer.getElements(filterService.createFidFilter(new String[] { "1" }), 0, 1).next();
		// retrieve the feature with modified primitive attrs
		FeatureBean prim = (FeatureBean)layer.getElements(filterService.createFidFilter(new String[] { "2" }), 0, 1).next();
		// assemble the attributes
		Map<String, Attribute> attrs = new HashMap<String, Attribute>();
		attrs.put("urlAttr", layer.getFeatureModel().getAttribute(prim, "urlAttr"));
		attrs.put("stringAttr", layer.getFeatureModel().getAttribute(prim, "stringAttr"));
		attrs.put("doubleAttr", layer.getFeatureModel().getAttribute(prim, "doubleAttr"));
		attrs.put("manyToOneAttr", layer.getFeatureModel().getAttribute(prim, "manyToOneAttr"));
		attrs.put("oneToManyAttr", layer.getFeatureModel().getAttribute(prim, "oneToManyAttr"));
		// apply to the test feature
		layer.getFeatureModel().setAttributes(test, attrs);
		// test and modified should be the same after setting id to 1L
		prim.setId(1L);
		Assert.assertEquals(prim, test);
		
		
	}

	@Test
	public void testUpdatePrimitives() throws LayerException {
		Object tranzient = createNested();
		Object persistent = layer.create(tranzient);
		String id = layer.getFeatureModel().getId(persistent);
		// retrieve feature
		Object read = layer.getElements(filterService.createFidFilter(new String[] { id }), 0, 1).next();
		// read primitives
		ManyToOneAttribute manyToOne = (ManyToOneAttribute) layer.getFeatureModel().getAttribute(read, "manyToOneAttr");
		StringAttribute s = (StringAttribute) manyToOne.getValue().getAllAttributes().get("stringAttr");
		Assert.assertEquals("one", s.getValue());
		// update primitives
		Map<String, Attribute> attribs = new HashMap<String, Attribute>();
		attribs.put("stringAttr", new StringAttribute("bean2"));
		ManyToOneAttribute manyToOne2 = (ManyToOneAttribute) manyToOne.clone();
		manyToOne2.getValue().getAllAttributes().put("stringAttr", new StringAttribute("one2"));
		attribs.put("manyToOneAttr", manyToOne2);
		layer.getFeatureModel().setAttributes(read, attribs);
		layer.saveOrUpdate(read);
		// retrieve feature again
		Object read2 = layer.getElements(filterService.createFidFilter(new String[] { id }), 0, 1).next();
		// check attributes
		Attribute stringAttr = layer.getFeatureModel().getAttribute(read2, "stringAttr");
		Assert.assertNotNull(stringAttr);
		Assert.assertTrue(stringAttr.isPrimitive());
		Assert.assertEquals("bean2", stringAttr.getValue());
		ManyToOneAttribute manyToOne3 = (ManyToOneAttribute) layer.getFeatureModel()
				.getAttribute(read, "manyToOneAttr");
		Assert.assertEquals("one2", manyToOne3.getValue().getAttributeValue("stringAttr"));

	}

	@Test
	public void testUpdateManyToOneWithExisting() throws LayerException {
		Object tranzient = createExistingManyToOne();
		Object persistent = layer.create(tranzient);
		Attribute stringAttr = layer.getFeatureModel().getAttribute(persistent, "manyToOneAttr.stringAttr");
		Assert.assertEquals("ManyToOne - 1", stringAttr.getValue());
	}

	public void testUpdateManyToOneWithNew() throws LayerException {

	}

	public void testUpdateAssociationsUsingNew() {

	}

	public void testDeleteAssociations() {

	}

	public Object createNested() throws LayerException {
		Object o = layer.getFeatureModel().newInstance();
		OneToManyAttribute many = new OneToManyAttribute(new ArrayList<AssociationValue>());
		ManyToOneAttribute one = new ManyToOneAttribute(new AssociationValue(null, new HashMap<String, Attribute<?>>(),
				false));
		one.getValue().getAllAttributes().put("stringAttr", new StringAttribute("one"));

		// create 2 manyInMany
		OneToManyAttribute manyInMany = new OneToManyAttribute(new ArrayList<AssociationValue>());
		AssociationValue manyInMany1 = new AssociationValue(null, new HashMap<String, Attribute<?>>(), false);
		manyInMany1.getAllAttributes().put("stringAttr", new StringAttribute("manyInMany1"));
		manyInMany.getValue().add(manyInMany1);
		AssociationValue manyInMany2 = new AssociationValue(null, new HashMap<String, Attribute<?>>(), false);
		manyInMany2.getAllAttributes().put("stringAttr", new StringAttribute("manyInMany2"));
		manyInMany.getValue().add(manyInMany2);

		// create oneInMany
		ManyToOneAttribute oneInMany = new ManyToOneAttribute();
		AssociationValue oneInManyValue = new AssociationValue(null, new HashMap<String, Attribute<?>>(), false);
		oneInManyValue.getAllAttributes().put("stringAttr", new StringAttribute("oneInMany"));
		oneInMany.setValue(oneInManyValue);

		// create 2 manyInOne
		OneToManyAttribute manyInOne = new OneToManyAttribute(new ArrayList<AssociationValue>());
		AssociationValue manyInOne1 = new AssociationValue(null, new HashMap<String, Attribute<?>>(), false);
		manyInOne1.getAllAttributes().put("stringAttr", new StringAttribute("manyInOne1"));
		manyInOne.getValue().add(manyInOne1);
		AssociationValue manyInOne2 = new AssociationValue(null, new HashMap<String, Attribute<?>>(), false);
		manyInOne2.getAllAttributes().put("stringAttr", new StringAttribute("manyInOne2"));
		manyInOne.getValue().add(manyInOne2);

		// create oneInOne
		ManyToOneAttribute oneInOne = new ManyToOneAttribute();
		AssociationValue oneInOneValue = new AssociationValue(null, new HashMap<String, Attribute<?>>(), false);
		oneInOneValue.getAllAttributes().put("stringAttr", new StringAttribute("oneInOne"));
		oneInOne.setValue(oneInOneValue);

		// create 2 many
		AssociationValue many1 = new AssociationValue(null, new HashMap<String, Attribute<?>>(), false);
		AssociationValue many2 = new AssociationValue(null, new HashMap<String, Attribute<?>>(), false);
		// add manyInMany to many1
		many1.getAllAttributes().put("oneToManyAttr", manyInMany);
		// add oneInMany to many2
		many2.getAllAttributes().put("manyToOneAttr", oneInMany);
		// add to many
		many.getValue().add(many1);
		many.getValue().add(many2);

		// add manyInOne to one
		one.getValue().getAllAttributes().put("oneToManyAttr", manyInOne);
		// add oneInOne to one
		one.getValue().getAllAttributes().put("manyToOneAttr", oneInOne);
		Map<String, Attribute> attributes = new HashMap<String, Attribute>();
		attributes.put("oneToManyAttr", many);
		attributes.put("manyToOneAttr", one);
		attributes.put("stringAttr", new StringAttribute("bean1"));
		layer.getFeatureModel().setAttributes(o, attributes);
		return o;
	}

	public Object createExistingManyToOne() throws LayerException {
		Object o = layer.getFeatureModel().newInstance();
		ManyToOneAttribute one = new ManyToOneAttribute(new AssociationValue(new LongAttribute(1L),
				new HashMap<String, Attribute<?>>(), false));
		Map<String, Attribute> attributes = new HashMap<String, Attribute>();
		attributes.put("manyToOneAttr", one);
		layer.getFeatureModel().setAttributes(o, attributes);
		return o;
	}

}
