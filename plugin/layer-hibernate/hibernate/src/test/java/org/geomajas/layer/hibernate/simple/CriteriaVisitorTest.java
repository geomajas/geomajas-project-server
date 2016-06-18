/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.layer.hibernate.simple;

import junit.framework.Assert;

import org.geomajas.layer.LayerException;
import org.geomajas.layer.hibernate.AbstractHibernateSimpleTest;
import org.geomajas.layer.hibernate.simple.pojo.SimpleFeature;
import org.geomajas.service.FilterService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Test cases that test the Hibernate criteria visitor. The idea is to see of OGC filters are correctly transformed into
 * Hibernate criteria objects.
 * 
 * @author Pieter De Graef
 */
public class CriteriaVisitorTest extends AbstractHibernateSimpleTest {

	@Autowired
	private FilterService filterService;

	@Before
	public void setUpTestDataWithinTransaction() throws LayerException {
		layer.create(SimpleFeature.getDefaultInstance1(null));
		layer.create(SimpleFeature.getDefaultInstance2(null));
		layer.create(SimpleFeature.getDefaultInstance3(null));
		layer.create(SimpleFeature.getDefaultInstance4(null));
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