/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.internal.service;

import junit.framework.TestCase;
import org.geomajas.geometry.Bbox;
import org.geomajas.service.BboxService;

/**
 * Tests for the BboxService class
 *
 * @author Joachim Van der Auwera
 */
public class BboxServiceTest extends TestCase {

	private BboxService bboxService = new BboxServiceImpl();

	public void testIsNull() {
		assertTrue(bboxService.isNull(new Bbox(10, 10, 0, 10)));
		assertTrue(bboxService.isNull(new Bbox(10, 10, 10, 0)));
		assertFalse(bboxService.isNull(new Bbox(0, 10, 10, 10)));
		assertFalse(bboxService.isNull(new Bbox(10, 0, 10, 10)));
	}

	public void testIntersection() {
		Bbox bbox1 = new Bbox(10, 10, 10, 10);
		Bbox bbox2 = new Bbox(15, 5, 7, 8);
		Bbox res = bboxService.intersection(bbox1, bbox2);
		assertEquals(15, res.getX(), 0.0001);
		assertEquals(10, res.getY(), 0.0001);
		assertEquals(5, res.getWidth(), 0.0001);
		assertEquals(3, res.getHeight(), 0.0001);
	}

	public void testExpandToInclude() {
		Bbox bbox = new Bbox(10, 10, 10, 10);
		Bbox other = new Bbox(15, 5, 7, 8);
		bboxService.expandToInclude(bbox, other);
		assertEquals(10, bbox.getX(), 0.0001);
		assertEquals(5, bbox.getY(), 0.0001);
		assertEquals(12, bbox.getWidth(), 0.0001);
		assertEquals(15, bbox.getHeight(), 0.0001);
	}
}
