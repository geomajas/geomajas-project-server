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

package org.geomajas.internal.layer.vector;

import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.internal.service.pipeline.PipelineContextImpl;
import org.geomajas.layer.bean.BeanLayer;
import org.geomajas.layer.pipeline.GetFeaturesContainer;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.junit.After;
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
 * Test cases for {@link GetFeaturesEachStep}.
 * 
 * @author Kristof Heirwegh
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/beanContext.xml", "/org/geomajas/testdata/layerBeans.xml",
		"/org/geomajas/internal/layer/vector/getFeaturesEachStep.xml" })
public class GetFeaturesEachStepTest {

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

	@After
	public void clearSecurityContext() {
		securityManager.clearSecurityContext();
	}

	@Test
	public void testNoSecurityNorOffsetNorLimit() throws Exception {
		GetFeaturesContainer result = new GetFeaturesContainer();
		gfes.execute(getPipelineContext(0, Integer.MAX_VALUE), result);

		Assert.assertEquals(20, result.getFeatures().size());
		Assert.assertEquals("1", result.getFeatures().get(0).getId());
		Assert.assertEquals("20", result.getFeatures().get(19).getId());
	}

	@Test
	public void testNorOffsetNorLimitNorForcePaging() throws Exception {
		GetFeaturesContainer result = new GetFeaturesContainer();
		gfes.execute(getPipelineContext(0, Integer.MAX_VALUE, false), result);

		Assert.assertEquals(20, result.getFeatures().size());
		Assert.assertEquals("1", result.getFeatures().get(0).getId());
		Assert.assertEquals("20", result.getFeatures().get(19).getId());
	}

	@Test
	public void testOffset() throws Exception {
		GetFeaturesContainer result = new GetFeaturesContainer();
		gfes.execute(getPipelineContext(5, Integer.MAX_VALUE), result);

		Assert.assertEquals(15, result.getFeatures().size());
		Assert.assertEquals("6", result.getFeatures().get(0).getId());
		Assert.assertEquals("20", result.getFeatures().get(14).getId());
	}

	@Test
	public void testOffsetAndNoForcePaging() throws Exception {
		GetFeaturesContainer result = new GetFeaturesContainer();
		gfes.execute(getPipelineContext(5, Integer.MAX_VALUE, false), result);

		Assert.assertEquals(15, result.getFeatures().size());
		Assert.assertEquals("6", result.getFeatures().get(0).getId());
		Assert.assertEquals("20", result.getFeatures().get(14).getId());
	}

	@Test
	public void testLimit() throws Exception {
		GetFeaturesContainer result = new GetFeaturesContainer();
		gfes.execute(getPipelineContext(0, 10), result);

		Assert.assertEquals(10, result.getFeatures().size());
		Assert.assertEquals("1", result.getFeatures().get(0).getId());
		Assert.assertEquals("10", result.getFeatures().get(9).getId());
	}

	@Test
	public void testLimitAndNoForcePaging() throws Exception {
		GetFeaturesContainer result = new GetFeaturesContainer();
		gfes.execute(getPipelineContext(0, 10, false), result);

		Assert.assertEquals(10, result.getFeatures().size());
		Assert.assertEquals("1", result.getFeatures().get(0).getId());
		Assert.assertEquals("10", result.getFeatures().get(9).getId());
	}

	@Test
	public void testOffsetAndLimit() throws Exception {
		GetFeaturesContainer result = new GetFeaturesContainer();
		gfes.execute(getPipelineContext(5, 10), result);

		Assert.assertEquals(10, result.getFeatures().size());
		Assert.assertEquals("6", result.getFeatures().get(0).getId());
		Assert.assertEquals("15", result.getFeatures().get(9).getId());
	}

	@Test
	public void testOffsetAndLimitAndNoForcePaging() throws Exception {
		GetFeaturesContainer result = new GetFeaturesContainer();
		gfes.execute(getPipelineContext(5, 10, false), result);

		Assert.assertEquals(10, result.getFeatures().size());
		Assert.assertEquals("6", result.getFeatures().get(0).getId());
		Assert.assertEquals("15", result.getFeatures().get(9).getId());
	}

	@Test
	public void testOffsetAndLimitAndForcePaging() throws Exception {
		GetFeaturesContainer result = new GetFeaturesContainer();
		gfes.execute(getPipelineContext(5, 10, true), result);

		Assert.assertEquals(10, result.getFeatures().size());
		Assert.assertEquals("6", result.getFeatures().get(0).getId());
		Assert.assertEquals("15", result.getFeatures().get(9).getId());
	}

	// ----------------------------------------------------------
	
	private PipelineContext getPipelineContext(int offset, int limit, Boolean forcePaging) {
		PipelineContext pip = new PipelineContextImpl();
		pip.put(PipelineCode.LAYER_KEY, testLayer);
		pip.put(PipelineCode.OFFSET_KEY, offset);
		pip.put(PipelineCode.MAX_RESULT_SIZE_KEY, limit);
		if(forcePaging != null) {
			pip.put(PipelineCode.FORCE_PAGING_KEY, forcePaging);
		}

		pip.put(PipelineCode.FILTER_KEY, Filter.INCLUDE);
		pip.put(PipelineCode.FEATURE_INCLUDES_KEY, 0);
		pip.put(PipelineCode.LAYER_ID_KEY, testLayer.getId());
		pip.put(PipelineCode.STYLE_KEY, new NamedStyleInfo());

		return pip;
	}
	
	private PipelineContext getPipelineContext(int offset, int limit) {
		return getPipelineContext(offset, limit, null);
	}
}
