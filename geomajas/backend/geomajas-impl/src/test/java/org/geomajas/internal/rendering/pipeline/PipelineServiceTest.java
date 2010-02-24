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

package org.geomajas.internal.rendering.pipeline;

import junit.framework.Assert;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.feature.attribute.StringAttribute;
import org.geomajas.rendering.pipeline.PipelineInfo;
import org.geomajas.rendering.pipeline.PipelineService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test for the correct functioning of {@link PipelineService}.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml", 
		"/org/geomajas/internal/rendering/pipeline/pipelineContext.xml" })
public class PipelineServiceTest {

	@Autowired
	private PipelineService pipelineService;

	@Test
	public void testPipeline() throws Exception {
		StringAttribute response = new StringAttribute("bla");

		pipelineService.execute(pipelineService.getPipeline("pipelineTest", null), "start", response);
		Assert.assertEquals("starts1s2s3", response.getValue());

		pipelineService.execute(pipelineService.getPipeline("pipelineTest", "aLayer"), "bla-", response);
		Assert.assertEquals("bla-step-1step-3", response.getValue());

		pipelineService.execute(pipelineService.getPipeline("pipelineTest", "stop"), "stop-", response);
		Assert.assertEquals("stop-s1-STOP", response.getValue());
	}

	@Test
	public void testGetPipeline() throws Exception {
		PipelineInfo<String, StringAttribute> pipelineInfo;

		pipelineInfo = pipelineService.getPipeline("pipelineTest", "aLayer");
		Assert.assertNotNull(pipelineInfo);
		Assert.assertEquals("step-1", pipelineInfo.getPipeline().get(0).getId());

		pipelineInfo = pipelineService.getPipeline("pipelineTest", "bla");
		Assert.assertNotNull(pipelineInfo);
		Assert.assertEquals("s1", pipelineInfo.getPipeline().get(0).getId());

		pipelineInfo = pipelineService.getPipeline("pipelineTest", null);
		Assert.assertNotNull(pipelineInfo);
		Assert.assertEquals("s1", pipelineInfo.getPipeline().get(0).getId());

		try {
			pipelineInfo = pipelineService.getPipeline("bla", null);
			Assert.fail("exception should be thrown");
		} catch (GeomajasException ge) {
			Assert.assertEquals(ExceptionCode.PIPELINE_UNKNOWN, ge.getExceptionCode());
		}
	}
}
