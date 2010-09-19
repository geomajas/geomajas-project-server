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

import junit.framework.Assert;
import org.junit.Test;

/**
 * Test for DependencyCheckPostProcessor.
 *
 * @author Joachim Van der Auwera
 */
public class DependencyCheckPostProcessorTest {

	@Test
	public void testParseVersion() throws Exception {
		DependencyCheckPostProcessor.Version version;

		version = new DependencyCheckPostProcessor.Version("1.2.3");
		Assert.assertEquals(1, version.getMajor());
		Assert.assertEquals(2, version.getMinor());
		Assert.assertEquals(3, version.getRevision());
		Assert.assertEquals("", version.getQualifier());

		version = new DependencyCheckPostProcessor.Version("1.2");
		Assert.assertEquals(1, version.getMajor());
		Assert.assertEquals(2, version.getMinor());
		Assert.assertEquals(0, version.getRevision());
		Assert.assertEquals("", version.getQualifier());

		version = new DependencyCheckPostProcessor.Version("1");
		Assert.assertEquals(1, version.getMajor());
		Assert.assertEquals(0, version.getMinor());
		Assert.assertEquals(0, version.getRevision());
		Assert.assertEquals("", version.getQualifier());

		version = new DependencyCheckPostProcessor.Version("1.2.3-SNAPSHOT");
		Assert.assertEquals(1, version.getMajor());
		Assert.assertEquals(2, version.getMinor());
		Assert.assertEquals(3, version.getRevision());
		Assert.assertEquals("SNAPSHOT", version.getQualifier());

		version = new DependencyCheckPostProcessor.Version("1.2.3.SNAPSHOT");
		Assert.assertEquals(1, version.getMajor());
		Assert.assertEquals(2, version.getMinor());
		Assert.assertEquals(3, version.getRevision());
		Assert.assertEquals("SNAPSHOT", version.getQualifier());

		version = new DependencyCheckPostProcessor.Version("1.2-SNAPSHOT");
		Assert.assertEquals(1, version.getMajor());
		Assert.assertEquals(2, version.getMinor());
		Assert.assertEquals(0, version.getRevision());
		Assert.assertEquals("SNAPSHOT", version.getQualifier());

		version = new DependencyCheckPostProcessor.Version("1-SNAPSHOT");
		Assert.assertEquals(1, version.getMajor());
		Assert.assertEquals(0, version.getMinor());
		Assert.assertEquals(0, version.getRevision());
		Assert.assertEquals("SNAPSHOT", version.getQualifier());
	}

	@Test
	public void testVersionAfter() throws Exception {
		Assert.assertTrue(new DependencyCheckPostProcessor.Version("1.2.3").after(
				new DependencyCheckPostProcessor.Version("1.2.2")));
		Assert.assertTrue(new DependencyCheckPostProcessor.Version("2.0.0").after(
				new DependencyCheckPostProcessor.Version("1.2.3")));
		Assert.assertTrue(new DependencyCheckPostProcessor.Version("1.3.0").after(
				new DependencyCheckPostProcessor.Version("1.2.3")));
		Assert.assertTrue(new DependencyCheckPostProcessor.Version("1.2.3").after(
				new DependencyCheckPostProcessor.Version("1.2.3-SNAPSHOT")));

		Assert.assertFalse(new DependencyCheckPostProcessor.Version("1.2.3").after(
				new DependencyCheckPostProcessor.Version("1.2.3")));
		Assert.assertFalse(new DependencyCheckPostProcessor.Version("1.2.2").after(
				new DependencyCheckPostProcessor.Version("1.2.3")));
		Assert.assertFalse(new DependencyCheckPostProcessor.Version("1.1.10").after(
				new DependencyCheckPostProcessor.Version("1.2.3")));
		Assert.assertFalse(new DependencyCheckPostProcessor.Version("1.1.0").after(
				new DependencyCheckPostProcessor.Version("1.2.3")));
		Assert.assertFalse(new DependencyCheckPostProcessor.Version("1.2.3").after(
				new DependencyCheckPostProcessor.Version("2.3.4")));
		Assert.assertFalse(new DependencyCheckPostProcessor.Version("1.2.3-SNAPSHOT").after(
				new DependencyCheckPostProcessor.Version("1.2.3")));
	}
}
