package org.geomajas.layer.hibernate;

import java.text.DateFormat;
import java.util.Date;

import junit.framework.Assert;

import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.hibernate.pojo.HibernateTestFeature;
import org.geomajas.layer.hibernate.pojo.HibernateTestOneToMany;
import org.geomajas.service.FilterService;
import org.hibernate.criterion.Criterion;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Before;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.temporal.After;
import org.opengis.filter.temporal.During;
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

	@Test
	public void testVisitAfter() throws GeomajasException {
		Filter f = filterService.parseFilter("myDate AFTER 2006-11-30T01:30:00Z");
		Criterion c = (Criterion) (new CriteriaVisitor((HibernateFeatureModel) layer.getFeatureModel(),
				DateFormat.getDateTimeInstance()).visit((After) f, null));
		Date date = ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2006-11-30T01:30:00Z").toDate();
		Assert.assertEquals("myDate>" + date, c.toString());
	}

	@Test
	public void testVisitBefore() throws GeomajasException {
		Filter f = filterService.parseFilter("myDate BEFORE 2006-11-30T01:30:00Z");
		Criterion c = (Criterion) (new CriteriaVisitor((HibernateFeatureModel) layer.getFeatureModel(),
				DateFormat.getDateTimeInstance()).visit((org.opengis.filter.temporal.Before) f, null));
		Date date = ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2006-11-30T01:30:00Z").toDate();
		Assert.assertEquals("myDate<" + date, c.toString());
	}

	@Test
	public void testVisitDuring() throws GeomajasException {
		Filter f = filterService.parseFilter("myDate DURING 2006-11-30T00:30:00Z/2006-11-30T01:30:00Z");
		Criterion c = (Criterion) (new CriteriaVisitor((HibernateFeatureModel) layer.getFeatureModel(),
				DateFormat.getDateTimeInstance()).visit((During) f, null));
		Date from = ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2006-11-30T00:30:00Z").toDate();
		Date to = ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2006-11-30T01:30:00Z").toDate();
		Assert.assertEquals("myDate between " + from + " and " + to, c.toString());
	}

}
