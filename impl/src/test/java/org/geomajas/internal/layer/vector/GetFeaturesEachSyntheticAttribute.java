package org.geomajas.internal.layer.vector;

import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.internal.layer.feature.InternalFeatureImpl;
import org.geomajas.internal.service.pipeline.PipelineContextImpl;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.bean.BeanLayer;
import org.geomajas.layer.feature.InternalFeature;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml","/org/geomajas/testdata/allowAll.xml","/org/geomajas/internal/layer/vector/getFeatureEachStepSynthLabel.xml",
		"/org/geomajas/internal/layer/vector/getFeaturesEachStep.xml" })
public class GetFeaturesEachSyntheticAttribute {

	private static final String TEST_NAME = "Doe";

	private static final String TEST_SURNAME = "John";

	private static final String TEST_FULL_NAME = TEST_SURNAME + " " + TEST_NAME;

	private static final String TEST_NAME_DATE = TEST_NAME + " 1984-12-25";

	@Autowired
	@Qualifier("beans")
	private BeanLayer testLayer;

	@Autowired
	@Qualifier("beansStyleInfo1")
	private NamedStyleInfo style1;

	@Autowired
	@Qualifier("beansStyleInfo2")
	private NamedStyleInfo style2;

	@Autowired
	@Qualifier("beansStyleInfo3")
	private NamedStyleInfo style3;

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
	public void testLabel1() throws Exception {
		GetFeaturesContainer result = new GetFeaturesContainer();
		gfes.execute(getPipelineContext(0, Integer.MAX_VALUE, style1), result);
		Assert.assertEquals(1, result.getFeatures().size());
		Assert.assertEquals(TEST_SURNAME, result.getFeatures().get(0).getLabel());

	}

	@Test
	public void testLabel2() throws Exception {
		GetFeaturesContainer result = new GetFeaturesContainer();
		gfes.execute(getPipelineContext(0, Integer.MAX_VALUE, style2), result);
		Assert.assertEquals(1, result.getFeatures().size());
		Assert.assertEquals(TEST_FULL_NAME, result.getFeatures().get(0).getLabel());

	}

	@Test
	public void testLabel3() throws Exception {
		GetFeaturesContainer result = new GetFeaturesContainer();
		gfes.execute(getPipelineContext(0, Integer.MAX_VALUE, style3), result);
		Assert.assertEquals(1, result.getFeatures().size());
		Assert.assertEquals(TEST_NAME_DATE, result.getFeatures().get(0).getLabel());
	}

	// ----------------------------------------------------------

	private PipelineContext getPipelineContext(int offset, int limit, Boolean forcePaging, NamedStyleInfo style) {
		PipelineContext pip = new PipelineContextImpl();
		pip.put(PipelineCode.LAYER_KEY, testLayer);
		pip.put(PipelineCode.OFFSET_KEY, offset);
		pip.put(PipelineCode.MAX_RESULT_SIZE_KEY, limit);
		if (forcePaging != null) {
			pip.put(PipelineCode.FORCE_PAGING_KEY, forcePaging);
		}

		pip.put(PipelineCode.FILTER_KEY, Filter.INCLUDE);
		pip.put(PipelineCode.FEATURE_INCLUDES_KEY, VectorLayerService.FEATURE_INCLUDE_LABEL);
		pip.put(PipelineCode.LAYER_ID_KEY, testLayer.getId());
		pip.put(PipelineCode.STYLE_KEY, style);

		return pip;
	}

	private PipelineContext getPipelineContext(int offset, int limit, NamedStyleInfo style) {
		return getPipelineContext(offset, limit, null, style);
	}

}
