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
package org.geomajas.spring;

import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test to see if the parsing of resolutions, using the (1:x) format works.
 * 
 * @author Pieter De Graef
 */
public class ResolutionFormatterTest {

	@Test
	public void testParse() {
		try {
			double resolution = ResolutionFormatterFactory.parseResolution("1:2000");
			Assert.assertEquals(5.0E-4, resolution, 0.001);
			resolution = ResolutionFormatterFactory.parseResolution(" 1:2000 ");
			Assert.assertEquals(5.0E-4, resolution, 0.001);
			resolution = ResolutionFormatterFactory.parseResolution("1 : 2000");
			Assert.assertEquals(5.0E-4, resolution, 0.001);
			resolution = ResolutionFormatterFactory.parseResolution("1:2,000");
			Assert.assertEquals(5.0E-4, resolution, 0.001);
			resolution = ResolutionFormatterFactory.parseResolution("1:2 000");
			Assert.assertEquals(5.0E-4, resolution, 0.001);
			resolution = ResolutionFormatterFactory.parseResolution(" 1 : 2,000 ");
			Assert.assertEquals(5.0E-4, resolution, 0.001);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
