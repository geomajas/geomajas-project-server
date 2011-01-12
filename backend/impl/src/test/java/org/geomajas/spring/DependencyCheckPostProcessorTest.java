/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
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
