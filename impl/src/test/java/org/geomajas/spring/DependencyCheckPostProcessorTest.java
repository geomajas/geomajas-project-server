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

package org.geomajas.spring;

import junit.framework.Assert;

import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.service.TestRecorder;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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

		version = new DependencyCheckPostProcessor.Version("1.2.3-M5");
		Assert.assertEquals(1, version.getMajor());
		Assert.assertEquals(2, version.getMinor());
		Assert.assertEquals(3, version.getRevision());
		Assert.assertEquals("M5", version.getQualifier());

		version = new DependencyCheckPostProcessor.Version("1.2.3.M5");
		Assert.assertEquals(1, version.getMajor());
		Assert.assertEquals(2, version.getMinor());
		Assert.assertEquals(3, version.getRevision());
		Assert.assertEquals("M5", version.getQualifier());

		version = new DependencyCheckPostProcessor.Version("1.2-M5");
		Assert.assertEquals(1, version.getMajor());
		Assert.assertEquals(2, version.getMinor());
		Assert.assertEquals(0, version.getRevision());
		Assert.assertEquals("M5", version.getQualifier());

		version = new DependencyCheckPostProcessor.Version("1-M5");
		Assert.assertEquals(1, version.getMajor());
		Assert.assertEquals(0, version.getMinor());
		Assert.assertEquals(0, version.getRevision());
		Assert.assertEquals("M5", version.getQualifier());
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
		Assert.assertTrue(new DependencyCheckPostProcessor.Version("1.2.3").after(
				new DependencyCheckPostProcessor.Version("1.2.3-M5")));
		Assert.assertTrue(new DependencyCheckPostProcessor.Version("1.2.3-SNAPSHOT").after(
				new DependencyCheckPostProcessor.Version("1.2.3-M5")));

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
		Assert.assertFalse(new DependencyCheckPostProcessor.Version("1.2.3-M5").after(
				new DependencyCheckPostProcessor.Version("1.2.3-SNAPSHOT")));
	}

	@Test
	public void testNormalGood() throws Exception {
		ApplicationContext ac = loadApplicationContext("/org/geomajas/spring/dependencyTestNormalGood.xml");
		TestRecorder recorder = ac.getBean(TestRecorder.class);
		Assert.assertEquals("", recorder.matches(DependencyCheckPostProcessor.GROUP,
				DependencyCheckPostProcessor.VALUE));
	}

	@Test
	public void testNormalBadBackend() throws Exception {
		try {
		loadApplicationContext("/org/geomajas/spring/dependencyTestNormalBadBackend.xml");
			Assert.fail("Dependency check did not function.");
		} catch (GeomajasException ge) {
	        Assert.assertEquals(ExceptionCode.DEPENDENCY_CHECK_FAILED, ge.getExceptionCode());
		}
	}

	@Test
	public void testNormalBadPlugin() throws Exception {
		try {
		loadApplicationContext("/org/geomajas/spring/dependencyTestNormalBadPlugin.xml");
			Assert.fail("Dependency check did not function.");
		} catch (GeomajasException ge) {
	        Assert.assertEquals(ExceptionCode.DEPENDENCY_CHECK_FAILED, ge.getExceptionCode());
		}
	}

	@Test
	public void testChecksInIdeBD() throws Exception {
		ApplicationContext ac = loadApplicationContext("/org/geomajas/spring/dependencyTestInIdeBackendDef.xml");
		TestRecorder recorder = ac.getBean(TestRecorder.class);
		Assert.assertEquals("", recorder.matches(DependencyCheckPostProcessor.GROUP,
				DependencyCheckPostProcessor.VALUE));
	}

	@Test
	public void testChecksInIdeBR() throws Exception {
		ApplicationContext ac = loadApplicationContext("/org/geomajas/spring/dependencyTestInIdeBackendRef.xml");
		TestRecorder recorder = ac.getBean(TestRecorder.class);
		Assert.assertEquals("", recorder.matches(DependencyCheckPostProcessor.GROUP,
				DependencyCheckPostProcessor.VALUE));
	}

	@Test
	public void testChecksInIdePD() throws Exception {
		ApplicationContext ac = loadApplicationContext("/org/geomajas/spring/dependencyTestInIdePluginDef.xml");
		TestRecorder recorder = ac.getBean(TestRecorder.class);
		Assert.assertEquals("", recorder.matches(DependencyCheckPostProcessor.GROUP,
				DependencyCheckPostProcessor.VALUE));
	}

	@Test
	public void testChecksInIdePR() throws Exception {
		ApplicationContext ac = loadApplicationContext("/org/geomajas/spring/dependencyTestInIdePluginRef.xml");
		TestRecorder recorder = ac.getBean(TestRecorder.class);
		Assert.assertEquals("", recorder.matches(DependencyCheckPostProcessor.GROUP,
				DependencyCheckPostProcessor.VALUE));
	}

	@Test
	public void testChecksDuplicatePluginGood() throws Exception {
		ApplicationContext ac = loadApplicationContext("/org/geomajas/spring/dependencyTestDuplicatePluginGood.xml");
		TestRecorder recorder = ac.getBean(TestRecorder.class);
		Assert.assertEquals("", recorder.matches(DependencyCheckPostProcessor.GROUP,
				DependencyCheckPostProcessor.VALUE));
	}

	@Test
	public void testChecksDuplicatePluginBad() throws Exception {
		try {
		loadApplicationContext("/org/geomajas/spring/dependencyTestDuplicatePluginBad.xml");
			Assert.fail("Dependency check did not function.");
		} catch (GeomajasException ge) {
	        Assert.assertEquals(ExceptionCode.DEPENDENCY_CHECK_INVALID_DUPLICATE, ge.getExceptionCode());
		}
	}

	@Test
	public void testChecksOptionalGood() throws Exception {
		ApplicationContext ac = loadApplicationContext("/org/geomajas/spring/dependencyTestOptionalGood.xml");
		TestRecorder recorder = ac.getBean(TestRecorder.class);
		Assert.assertEquals("", recorder.matches(DependencyCheckPostProcessor.GROUP,
				DependencyCheckPostProcessor.VALUE));
	}

	@Test
	public void testChecksOptionalMissing() throws Exception {
		ApplicationContext ac = loadApplicationContext("/org/geomajas/spring/dependencyTestOptionalMissing.xml");
		TestRecorder recorder = ac.getBean(TestRecorder.class);
		Assert.assertEquals("", recorder.matches(DependencyCheckPostProcessor.GROUP,
				DependencyCheckPostProcessor.VALUE));
	}

	@Test
	public void testChecksOptionalBad() throws Exception {
		try {
		loadApplicationContext("/org/geomajas/spring/dependencyTestOptionalBad.xml");
			Assert.fail("Dependency check did not function.");
		} catch (GeomajasException ge) {
	        Assert.assertEquals(ExceptionCode.DEPENDENCY_CHECK_FAILED, ge.getExceptionCode());
		}
	}

	@Test
	public void testChecksSourceFirst() throws Exception {
		ApplicationContext ac = loadApplicationContext("/org/geomajas/spring/dependencyTestSourceFirst.xml");
		TestRecorder recorder = ac.getBean(TestRecorder.class);
		Assert.assertEquals("", recorder.matches(DependencyCheckPostProcessor.GROUP,
				DependencyCheckPostProcessor.VALUE));
	}

	@Test
	public void testChecksSourceLast() throws Exception {
		ApplicationContext ac = loadApplicationContext("/org/geomajas/spring/dependencyTestSourceLast.xml");
		TestRecorder recorder = ac.getBean(TestRecorder.class);
		Assert.assertEquals("", recorder.matches(DependencyCheckPostProcessor.GROUP,
				DependencyCheckPostProcessor.VALUE));
	}

	private ApplicationContext loadApplicationContext(String... locations) throws Exception {
		try {
			return new ClassPathXmlApplicationContext(locations);
		} catch (Exception ex) {
			Throwable ge = ex;
			while (null != ge) {
				if (ge instanceof GeomajasException) {
					throw (GeomajasException)ge;
				}
				ge = ge.getCause();
			}
			throw ex;
		}
	}

}
