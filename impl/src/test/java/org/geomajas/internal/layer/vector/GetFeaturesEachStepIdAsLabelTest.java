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
import org.geomajas.layer.VectorLayerService;
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
		"/org/geomajas/internal/layer/vector/getFeaturesEachStepIdAsLabel.xml" })
public class GetFeaturesEachStepIdAsLabelTest {

	// Small caveat, not everything is tested yet 
	// (for example filter from pipeline)

	@Autowired
	@Qualifier("beans")
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
	public void testIdAsLabel() throws Exception {
		GetFeaturesContainer result = new GetFeaturesContainer();
		gfes.execute(getPipelineContext(0, Integer.MAX_VALUE), result);

		Assert.assertEquals(2, result.getFeatures().size());
		Assert.assertEquals("1", result.getFeatures().get(0).getLabel());
		Assert.assertEquals("2", result.getFeatures().get(1).getLabel());
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
		pip.put(PipelineCode.FEATURE_INCLUDES_KEY, VectorLayerService.FEATURE_INCLUDE_LABEL);
		pip.put(PipelineCode.LAYER_ID_KEY, testLayer.getId());
		pip.put(PipelineCode.STYLE_KEY, testLayer.getLayerInfo().getNamedStyleInfos().get(0));

		return pip;
	}
	
	private PipelineContext getPipelineContext(int offset, int limit) {
		return getPipelineContext(offset, limit, null);
	}
}
