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

package org.geomajas.internal.service;

import junit.framework.Assert;

import org.geomajas.geometry.Bbox;
import org.geomajas.service.DtoConverterService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Tests for the DtoConverterService, specifically testing the bbox conversions. 
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/spring/moreContext.xml"})
public class BboxConverterTest {

	private static final double ALLOWANCE = .000001;

	@Autowired
	private DtoConverterService converterService;

	@Test
	public void testToInternal() {
		Bbox bbox = new Bbox(10, 20, 30, 40);
		Envelope envelope = converterService.toInternal(bbox);
		Assert.assertEquals(10, envelope.getMinX(), ALLOWANCE);
		Assert.assertEquals(20, envelope.getMinY(), ALLOWANCE);
		Assert.assertEquals(40, envelope.getMaxX(), ALLOWANCE);
		Assert.assertEquals(60, envelope.getMaxY(), ALLOWANCE);
	}

	@Test
	public void testToDto() {
		Envelope envelope = new Envelope(10, 20, 30, 40);
		Bbox bbox = converterService.toDto(envelope);
		Assert.assertEquals(10, bbox.getX(), ALLOWANCE);
		Assert.assertEquals(30, bbox.getY(), ALLOWANCE);
		Assert.assertEquals(20, bbox.getMaxX(), ALLOWANCE);
		Assert.assertEquals(40, bbox.getMaxY(), ALLOWANCE);
	}
}
