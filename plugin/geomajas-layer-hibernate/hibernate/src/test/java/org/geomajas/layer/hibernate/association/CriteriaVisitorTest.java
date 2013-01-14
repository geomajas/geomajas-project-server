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

package org.geomajas.layer.hibernate.association;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.geomajas.layer.LayerException;
import org.geomajas.layer.hibernate.AbstractHibernateAssociationTest;
import org.geomajas.layer.hibernate.association.pojo.AssociationFeature;
import org.geomajas.layer.hibernate.association.pojo.OneToManyProperty;
import org.geomajas.service.FilterService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Test cases that test the criteria visitor when using associations in the OGC filter statements. Test to see if they
 * are correctly parsed to Hibernate criteria and if the correct aliases are created along the way.
 * 
 * @author Pieter De Graef
 */
public class CriteriaVisitorTest extends AbstractHibernateAssociationTest {

	@Autowired
	private FilterService filterService;

	@Before
	public void setUpTestDataWithinTransaction() throws LayerException {
		AssociationFeature f1 = AssociationFeature.getDefaultInstance1(null);
		AssociationFeature f2 = AssociationFeature.getDefaultInstance2(null);
		AssociationFeature f3 = AssociationFeature.getDefaultInstance3(null);
		AssociationFeature f4 = AssociationFeature.getDefaultInstance4(null);

		Set<OneToManyProperty> otm1 = new HashSet<OneToManyProperty>();
		otm1.add(OneToManyProperty.getDefaultInstance1(null, f1));
		otm1.add(OneToManyProperty.getDefaultInstance2(null, f1));
		f1.setOneToMany(otm1);

		Set<OneToManyProperty> otm2 = new HashSet<OneToManyProperty>();
		otm2.add(OneToManyProperty.getDefaultInstance3(null, f2));
		otm2.add(OneToManyProperty.getDefaultInstance4(null, f2));
		f2.setOneToMany(otm2);

		Set<OneToManyProperty> otm3 = new HashSet<OneToManyProperty>();
		otm3.add(OneToManyProperty.getDefaultInstance1(null, f3));
		otm3.add(OneToManyProperty.getDefaultInstance3(null, f3));
		f3.setOneToMany(otm3);

		Set<OneToManyProperty> otm4 = new HashSet<OneToManyProperty>();
		otm4.add(OneToManyProperty.getDefaultInstance1(null, f4));
		otm4.add(OneToManyProperty.getDefaultInstance2(null, f4));
		otm4.add(OneToManyProperty.getDefaultInstance3(null, f4));
		otm4.add(OneToManyProperty.getDefaultInstance4(null, f4));
		f4.setOneToMany(otm4);

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