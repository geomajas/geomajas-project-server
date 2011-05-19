package org.geomajas.layer.hibernate;

import junit.framework.Assert;

import org.geomajas.layer.LayerException;
import org.geomajas.layer.hibernate.pojo.HibernateTestFeature;
import org.geomajas.layer.hibernate.pojo.HibernateTestOneToMany;
import org.geomajas.service.FilterService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CriteriaVisitorTest extends AbstractHibernateLayerModelTest {

	@Autowired
	private FilterService filterService;
	
	@Before
	public void setUpTestDataWithinTransaction() throws LayerException {
		HibernateTestFeature f1 = HibernateTestFeature.getDefaultInstance1(null);
		HibernateTestFeature f2 = HibernateTestFeature.getDefaultInstance2(null);
		HibernateTestFeature f3 = HibernateTestFeature.getDefaultInstance3(null);
		HibernateTestFeature f4 = HibernateTestFeature.getDefaultInstance4(null);

		f1.addOneToMany(HibernateTestOneToMany.getDefaultInstance1(null));
		f1.addOneToMany(HibernateTestOneToMany.getDefaultInstance2(null));

		f2.addOneToMany(HibernateTestOneToMany.getDefaultInstance3(null));
		f2.addOneToMany(HibernateTestOneToMany.getDefaultInstance4(null));

		f3.addOneToMany(HibernateTestOneToMany.getDefaultInstance1(null));
		f3.addOneToMany(HibernateTestOneToMany.getDefaultInstance3(null));

		f4.addOneToMany(HibernateTestOneToMany.getDefaultInstance1(null));
		f4.addOneToMany(HibernateTestOneToMany.getDefaultInstance2(null));
		f4.addOneToMany(HibernateTestOneToMany.getDefaultInstance3(null));
		f4.addOneToMany(HibernateTestOneToMany.getDefaultInstance4(null));

		layer.create(f1);
		layer.create(f2);
		layer.create(f3);
		layer.create(f4);
	}


	@Test
	public void testExclude() throws LayerException {
		Assert.assertFalse(layer.getElements(filterService.createFalseFilter(), 0, 1).hasNext());
	}
	
	@Test
	public void testInclude() throws LayerException {
		Assert.assertTrue(layer.getElements(filterService.createTrueFilter(), 0, 1).hasNext());
	}
	
	
}
