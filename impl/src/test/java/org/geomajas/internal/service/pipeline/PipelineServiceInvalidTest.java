/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.internal.service.pipeline;

import junit.framework.Assert;

import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Test to verify correct handling of invalid pipeline definitions.
 *
 * @author Joachim Van der Auwera
 */
public class PipelineServiceInvalidTest {

	@Test
	public void testInvalidInterceptorDefinition() throws Exception {
		try {
			loadApplicationContext("/org/geomajas/internal/rendering/pipeline/pipelineOverlappingInterceptors.xml");
			Assert.fail("invalid pipeline declaration was allowed");
		} catch(GeomajasException ge) {
			Assert.assertEquals(ExceptionCode.PIPELINE_INTERCEPTOR_INVALID_NESTING, ge.getExceptionCode());
		}
	}

	@Test
	public void testInvalidPipelineAndDelegate() throws Exception {
		try {
			loadApplicationContext("/org/geomajas/internal/rendering/pipeline/pipelineAndDelegate.xml");
			Assert.fail("invalid pipeline declaration was allowed");
		} catch(GeomajasException ge) {
			Assert.assertEquals(ExceptionCode.PIPELINE_DEFINED_AND_DELEGATE, ge.getExceptionCode());
		}
	}

	@Test
	public void testInvalidPipelineWrongOrder() throws Exception {
		try {
			loadApplicationContext("/org/geomajas/internal/rendering/pipeline/pipelineWrongOrder.xml");
			Assert.fail("invalid pipeline declaration was allowed");
		} catch(GeomajasException ge) {
			Assert.assertEquals(ExceptionCode.PIPELINE_INTERCEPTOR_STEPS_ORDER, ge.getExceptionCode());
		}
	}

	@Test
	public void testInvalidPipelineWrongId() throws Exception {
		try {
			loadApplicationContext("/org/geomajas/internal/rendering/pipeline/pipelineWrongId.xml");
			Assert.fail("invalid pipeline declaration was allowed");
		} catch(GeomajasException ge) {
			Assert.assertEquals(ExceptionCode.PIPELINE_INTERCEPTOR_INVALID_STEP, ge.getExceptionCode());
		}
	}

	@Test
	public void testInvalidExtension() throws Exception {
		try {
			loadApplicationContext("/org/geomajas/internal/rendering/pipeline/pipelineInvalidExtension.xml");
			Assert.fail("invalid pipeline declaration was allowed");
		} catch(GeomajasException ge) {
			Assert.assertEquals(ExceptionCode.PIPELINE_UNSATISFIED_EXTENSION, ge.getExceptionCode());
		}
	}

	private ApplicationContext loadApplicationContext(String location) throws Exception {
		String[] locations = new String[2];
		locations[0] = "/org/geomajas/spring/geomajasContext.xml";
		locations[1] = location;
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
