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

package org.geomajas.internal.service.pipeline;

import junit.framework.Assert;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.service.pipeline.PipelineContext;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
		Assert.assertEquals(0, ((List)context.get("list")).size());

		Assert.assertEquals("SomeText", context.get("text", String.class));
		Assert.assertEquals(17, (int)context.get("int", Integer.class));
		Assert.assertEquals(0, context.get("list", List.class).size());
		Assert.assertEquals(0, context.get("list", ArrayList.class).size());

		Assert.assertEquals("SomeText", context.put("text", "someOtherText"));
		Assert.assertEquals("someOtherText", context.get("text"));
	}
}
