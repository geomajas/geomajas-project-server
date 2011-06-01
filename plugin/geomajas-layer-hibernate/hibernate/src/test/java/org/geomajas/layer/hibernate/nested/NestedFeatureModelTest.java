package org.geomajas.layer.hibernate.nested;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import org.geomajas.layer.LayerException;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.feature.attribute.AssociationValue;
import org.geomajas.layer.feature.attribute.LongAttribute;
import org.geomajas.layer.feature.attribute.ManyToOneAttribute;
import org.geomajas.layer.feature.attribute.OneToManyAttribute;
import org.geomajas.layer.feature.attribute.StringAttribute;
import org.geomajas.layer.hibernate.AbstractHibernateNestedTest;
import org.geomajas.layer.hibernate.nested.pojo.NestedFeature;
import org.geomajas.layer.hibernate.nested.pojo.NestedOne;
import org.geomajas.layer.hibernate.nested.pojo.NestedOneInMany;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opengis.filter.Filter;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class NestedFeatureModelTest extends AbstractHibernateNestedTest {

	private FeatureModel featureModel;

	private NestedFeature feature1;

	private WKTReader wktReader;

	@Before
	public void setUpTestDataWithinTransaction() throws LayerException, ParseException {
		wktReader = new WKTReader(new GeometryFactory(new PrecisionModel(), 4326));
		featureModel = layer.getFeatureModel();
		feature1 = NestedFeature.getDefaultInstance1();
		feature1.setGeometry(wktReader.read("POINT(10 20)"));
		layer.create(feature1);
	}

	@Test
	public void testGetAttributes() throws LayerException {
		Iterator it = layer.getElements(Filter.INCLUDE, 0, 1);
		Assert.assertTrue(it.hasNext());
		Object f1 = it.next();
		Assert.assertFalse(it.hasNext());
		Attribute many = featureModel.getAttribute(f1, "many");
		Assert.assertTrue(many instanceof OneToManyAttribute);
		Assert.assertNotNull(many.getValue());
		Iterator<AssociationValue> it2 = ((OneToManyAttribute) many).getValue().iterator();
		Assert.assertTrue(it2.hasNext());
		AssociationValue v = it2.next();
		Attribute<?> manyInMany = v.getAllAttributes().get("manyInMany");
		Assert.assertNotNull(manyInMany);
		Assert.assertTrue(manyInMany instanceof OneToManyAttribute);
		Attribute<?> oneInMany = v.getAllAttributes().get("oneInMany");
		Assert.assertNotNull(oneInMany);
		Assert.assertTrue(oneInMany instanceof ManyToOneAttribute);
		Attribute one = featureModel.getAttribute(f1, "one");
		Assert.assertTrue(one instanceof ManyToOneAttribute);
		Assert.assertNotNull(one.getValue());
		AssociationValue v2 = ((ManyToOneAttribute) one).getValue();
		Attribute<?> oneInOne = v2.getAllAttributes().get("oneInOne");
		Assert.assertNotNull(oneInOne);
		Assert.assertTrue(oneInOne instanceof ManyToOneAttribute);
		Attribute<?> manyInOne = v2.getAllAttributes().get("manyInOne");
		Assert.assertNotNull(manyInOne);
		Assert.assertTrue(manyInOne instanceof OneToManyAttribute);
	}

	@Test
	public void testCreate() throws LayerException {
		Object o = featureModel.newInstance();
		OneToManyAttribute many = new OneToManyAttribute(new ArrayList<AssociationValue>());
		ManyToOneAttribute one = new ManyToOneAttribute(new AssociationValue(null, new HashMap<String, Attribute<?>>(),
				false));

		// create 2 manyInMany
		OneToManyAttribute manyInMany = new OneToManyAttribute(new ArrayList<AssociationValue>());
		AssociationValue manyInMany1 = new AssociationValue(null, new HashMap<String, Attribute<?>>(), false);
		manyInMany1.getAllAttributes().put("textAttr", new StringAttribute("manyInMany1"));
		manyInMany.getValue().add(manyInMany1);
		AssociationValue manyInMany2 = new AssociationValue(null, new HashMap<String, Attribute<?>>(), false);
		manyInMany2.getAllAttributes().put("textAttr", new StringAttribute("manyInMany2"));
		manyInMany.getValue().add(manyInMany2);

		// create oneInMany
		ManyToOneAttribute oneInMany = new ManyToOneAttribute();
		AssociationValue oneInManyValue = new AssociationValue(null, new HashMap<String, Attribute<?>>(), false);
		oneInManyValue.getAllAttributes().put("textAttr", new StringAttribute("oneInMany"));
		oneInMany.setValue(oneInManyValue);

		// create 2 manyInOne
		OneToManyAttribute manyInOne = new OneToManyAttribute(new ArrayList<AssociationValue>());
		AssociationValue manyInOne1 = new AssociationValue(null, new HashMap<String, Attribute<?>>(), false);
		manyInOne1.getAllAttributes().put("textAttr", new StringAttribute("manyInOne1"));
		manyInOne.getValue().add(manyInOne1);
		AssociationValue manyInOne2 = new AssociationValue(null, new HashMap<String, Attribute<?>>(), false);
		manyInOne2.getAllAttributes().put("textAttr", new StringAttribute("manyInOne2"));
		manyInOne.getValue().add(manyInOne2);

		// create oneInOne
		ManyToOneAttribute oneInOne = new ManyToOneAttribute();
		AssociationValue oneInOneValue = new AssociationValue(null, new HashMap<String, Attribute<?>>(), false);
		oneInOneValue.getAllAttributes().put("textAttr", new StringAttribute("oneInOne"));
		oneInOne.setValue(oneInOneValue);

		// create 2 many
		AssociationValue many1 = new AssociationValue(null, new HashMap<String, Attribute<?>>(), false);
		AssociationValue many2 = new AssociationValue(null, new HashMap<String, Attribute<?>>(), false);
		// add manyInMany to many1
		many1.getAllAttributes().put("manyInMany", manyInMany);
		// add oneInMany to many2
		many2.getAllAttributes().put("oneInMany", oneInMany);
		// add to many
		many.getValue().add(many1);
		many.getValue().add(many2);

		// add manyInOne to one
		one.getValue().getAllAttributes().put("manyInOne", manyInOne);
		// add oneInOne to one
		one.getValue().getAllAttributes().put("oneInOne", oneInOne);
		Map<String, Attribute> attributes = new HashMap<String, Attribute>();
		attributes.put("many", many);
		attributes.put("one", one);
		featureModel.setAttributes(o, attributes);
		Object feature = layer.create(o);
		Assert.assertNotNull(feature);
	}

	@Test
	public void testExistingOneInMany() throws Exception {
		NestedOneInMany oneInMany1 = new NestedOneInMany();
		oneInMany1.setTextAttr("existing1");
		factory.getCurrentSession().save(oneInMany1);
		factory.getCurrentSession().flush();
		
		Object o = featureModel.newInstance();
		Map<String, Attribute> attributes = new HashMap<String, Attribute>();
		// create many attribute
		OneToManyAttribute many = new OneToManyAttribute(new ArrayList<AssociationValue>());
		// create empty and add
		AssociationValue many1 = new AssociationValue(null, new HashMap<String, Attribute<?>>(), false);
		many.getValue().add(many1);
		// create an existing one-in-many and add
		ManyToOneAttribute oneInMany2 = new ManyToOneAttribute();
		AssociationValue oneInManyValue = new AssociationValue(new LongAttribute(oneInMany1.getId()), new HashMap<String, Attribute<?>>(), false);
		oneInMany2.setValue(oneInManyValue);
		AssociationValue many2 = new AssociationValue(null, new HashMap<String, Attribute<?>>(), false);
		many2.getAllAttributes().put("oneInMany", oneInMany2);
		many.getValue().add(many2);
		// add many
		attributes.put("many", many);
		featureModel.setAttributes(o, attributes);
		
		// check text attr
		Object feature = layer.create(o);
		OneToManyAttribute attr = (OneToManyAttribute)featureModel.getAttribute(feature, "many");
		AssociationValue val = attr.getValue().get(1);
		ManyToOneAttribute one = (ManyToOneAttribute)val.getAllAttributes().get("oneInMany");
		Assert.assertEquals("existing1", one.getValue().getAttributeValue("textAttr"));

	}

}
