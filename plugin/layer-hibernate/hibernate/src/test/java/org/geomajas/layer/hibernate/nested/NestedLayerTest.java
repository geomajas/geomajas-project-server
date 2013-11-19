package org.geomajas.layer.hibernate.nested;

import org.geomajas.layer.hibernate.AbstractHibernateNestedTest;
import org.geomajas.layer.hibernate.nested.pojo.NestedFeature;
import org.geomajas.layer.hibernate.nested.pojo.NestedOne;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class NestedLayerTest extends AbstractHibernateNestedTest {

	@Autowired
	SessionFactory sessionFactory;

	@Test
	public void testCreateFromPojo() throws Exception {
		NestedFeature feature = NestedFeature.getDefaultInstance1();
		Object created = layer.create(feature);
		sessionFactory.getCurrentSession().flush();
		Assert.assertNotNull(created);
		Assert.assertTrue(created instanceof NestedFeature);
		NestedFeature createdFeature = (NestedFeature) created;
		Assert.assertNotNull(createdFeature.getId());
	}


}
