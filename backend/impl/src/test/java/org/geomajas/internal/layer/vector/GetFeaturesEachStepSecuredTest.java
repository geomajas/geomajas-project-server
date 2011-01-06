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

package org.geomajas.internal.layer.vector;

import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.internal.service.pipeline.PipelineContextImpl;
import org.geomajas.layer.bean.BeanLayer;
import org.geomajas.layer.pipeline.GetFeaturesContainer;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test cases for {@link org.geomajas.internal.layer.vector.GetFeaturesEachStep}.
 *
 * @author Kristof Heirwegh
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/layer/bean/beanContext.xml", "/org/geomajas/layer/bean/layerBeans.xml",
		"/org/geomajas/internal/layer/vector/getFeaturesEachStep.xml",
		"/org/geomajas/internal/layer/vector/oddFeatureSecurity.xml" })
public class GetFeaturesEachStepSecuredTest {

	// Small caveat, not everything is tested yet 
	// (for example filter from pipeline)

	@Autowired
	@Qualifier("lotsObeans")
	private BeanLayer testLayer;

	@Autowired
	private GetFeaturesEachStep gfes;

	@Autowired
	private org.geomajas.security.SecurityManager securityManager;

	@Before
	public void login() {
		// assure security context is set
		securityManager.createSecurityContext(null);
	}

	@Test
	public void testSecurity() throws Exception {
		GetFeaturesContainer result = new GetFeaturesContainer();
		gfes.execute(getPipelineContext(0, Integer.MAX_VALUE), result);

		Assert.assertEquals(10, result.getFeatures().size());
		Assert.assertEquals("2", result.getFeatures().get(0).getId());
		Assert.assertEquals("20", result.getFeatures().get(9).getId());
	}

	@Test
	public void testSecurityAndOffsetAndLimit() throws Exception {
		GetFeaturesContainer result = new GetFeaturesContainer();
		gfes.execute(getPipelineContext(5, 3), result);

		Assert.assertEquals(3, result.getFeatures().size());
		Assert.assertEquals("12", result.getFeatures().get(0).getId());
		Assert.assertEquals("16", result.getFeatures().get(2).getId());
	}

	// ----------------------------------------------------------

	private PipelineContext getPipelineContext(int offset, int limit) {
		PipelineContext pip = new PipelineContextImpl();
		pip.put(PipelineCode.LAYER_KEY, testLayer);
		pip.put(PipelineCode.OFFSET_KEY, offset);
		pip.put(PipelineCode.MAX_RESULT_SIZE_KEY, limit);

		pip.put(PipelineCode.FILTER_KEY, Filter.INCLUDE);
		pip.put(PipelineCode.FEATURE_INCLUDES_KEY, 0);
		pip.put(PipelineCode.LAYER_ID_KEY, testLayer.getId());
		pip.put(PipelineCode.STYLE_KEY, new NamedStyleInfo());

		return pip;
	}
}
