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

package org.geomajas.internal.service.pipeline;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.service.pipeline.PipelineContext;
import org.junit.Test;

/**
 * Test for {@link PipelineContextImpl}.
 *
 * @author Joachim Van der Auwera
 */
public class PipelineContextTest {

	@Test
	public void testPutGet() throws Exception {
		PipelineContext context = new PipelineContextImpl();
		context.put("text", "SomeText");
		context.put("int", 17);
		context.put("list", new ArrayList<String>());

		Assert.assertNull(context.getOptional("unknown"));
		try {
			context.get("unknown");
			Assert.fail("should have thrown an exception");
		} catch (GeomajasException ge) {
			Assert.assertEquals(ExceptionCode.PIPELINE_CONTEXT_MISSING, ge.getExceptionCode());
		}
		Assert.assertEquals("SomeText", context.get("text"));
		Assert.assertEquals(17, context.get("int"));
		Assert.assertEquals(0, ((List<?>)context.get("list")).size());

		Assert.assertEquals("SomeText", context.get("text", String.class));
		Assert.assertEquals(17, (int)context.get("int", Integer.class));
		Assert.assertEquals(0, context.get("list", List.class).size());
		Assert.assertEquals(0, context.get("list", ArrayList.class).size());

		Assert.assertEquals("SomeText", context.put("text", "someOtherText"));
		Assert.assertEquals("someOtherText", context.get("text"));
	}
	
	@Test
	public void testOptional() throws Exception {
		PipelineContext context = new PipelineContextImpl();
		Assert.assertNull(context.getOptional("text"));
		Assert.assertEquals("someDefault", context.getOptional("text", String.class, "someDefault"));
		context.put("text", "SomeText");
		Assert.assertEquals("SomeText", context.getOptional("text", String.class, "someDefault"));
	}
	
	@Test
	public void testcontainsKey() throws Exception {
		PipelineContext context = new PipelineContextImpl();
		context.put("text", "SomeText");
		context.put("int", null);
		Assert.assertTrue(context.containsKey("text"));
		Assert.assertTrue(context.containsKey("int"));
		Assert.assertFalse(context.containsKey("other"));
	}
}
