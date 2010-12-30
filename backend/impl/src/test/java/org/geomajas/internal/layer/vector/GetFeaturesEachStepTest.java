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

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;

import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.internal.service.pipeline.PipelineContextImpl;
import org.geomajas.layer.bean.BeanLayer;
import org.geomajas.layer.bean.FeatureBean;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.pipeline.GetFeaturesContainer;
import org.geomajas.security.Authentication;
import org.geomajas.security.SecurityContext;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.filter.Filter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Test cases for {@link GetFeaturesEachStep}.
 * 
 * @author Kristof Heirwegh
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/layer/bean/beanContext.xml", "/org/geomajas/layer/bean/layerBeans.xml" })
public class GetFeaturesEachStepTest implements InitializingBean {

	// Small caveat, not everything is tested yet 
	// (for example filter from pipeline)

	@Autowired
	private BeanLayer testLayer;

	@Override
	public void afterPropertiesSet() throws Exception {
		// -- clear layer, then add some features
		FeatureBean fb;
		for (Object o : testLayer.getFeatures().toArray()) {
			testLayer.delete("" + ((FeatureBean) o).getId());
		}
		for (long i = 1l; i < 21l; i++) {
			fb = new FeatureBean();
			fb.setId(i);
			testLayer.create(fb);
		}
		Assert.assertTrue("There should be 20 features", testLayer.getFeatures().size() == 20);
	}

	@Test
	public void testNoSecurityNorOffsetNorLimit() throws Exception {
		GetFeaturesEachStep gfes = getGetFeaturesEachStep(false);
		GetFeaturesContainer result = new GetFeaturesContainer();
		gfes.execute(getPipelineContext(0, Integer.MAX_VALUE), result);

		Assert.assertTrue(result.getFeatures().size() == 20);
		Assert.assertEquals("1", result.getFeatures().get(0).getId());
		Assert.assertEquals("20", result.getFeatures().get(19).getId());
	}

	@Test
	public void testSecurity() throws Exception {
		GetFeaturesEachStep gfes = getGetFeaturesEachStep(true);
		GetFeaturesContainer result = new GetFeaturesContainer();
		gfes.execute(getPipelineContext(0, Integer.MAX_VALUE), result);

		Assert.assertTrue(result.getFeatures().size() == 10);
		Assert.assertEquals("2", result.getFeatures().get(0).getId());
		Assert.assertEquals("20", result.getFeatures().get(9).getId());
	}

	@Test
	public void testOffset() throws Exception {
		GetFeaturesEachStep gfes = getGetFeaturesEachStep(false);
		GetFeaturesContainer result = new GetFeaturesContainer();
		gfes.execute(getPipelineContext(5, Integer.MAX_VALUE), result);

		Assert.assertTrue(result.getFeatures().size() == 15);
		Assert.assertEquals("6", result.getFeatures().get(0).getId());
		Assert.assertEquals("20", result.getFeatures().get(14).getId());
	}

	@Test
	public void testLimit() throws Exception {
		GetFeaturesEachStep gfes = getGetFeaturesEachStep(false);
		GetFeaturesContainer result = new GetFeaturesContainer();
		gfes.execute(getPipelineContext(0, 10), result);

		Assert.assertTrue(result.getFeatures().size() == 10);
		Assert.assertEquals("1", result.getFeatures().get(0).getId());
		Assert.assertEquals("10", result.getFeatures().get(9).getId());
	}

	@Test
	public void testOffsetAndLimit() throws Exception {
		GetFeaturesEachStep gfes = getGetFeaturesEachStep(false);
		GetFeaturesContainer result = new GetFeaturesContainer();
		gfes.execute(getPipelineContext(5, 10), result);

		Assert.assertTrue(result.getFeatures().size() == 10);
		Assert.assertEquals("6", result.getFeatures().get(0).getId());
		Assert.assertEquals("15", result.getFeatures().get(9).getId());
	}

	@Test
	public void testSecurityAndOffsetAndLimit() throws Exception {
		GetFeaturesEachStep gfes = getGetFeaturesEachStep(true);
		GetFeaturesContainer result = new GetFeaturesContainer();
		gfes.execute(getPipelineContext(5, 3), result);

		Assert.assertTrue(result.getFeatures().size() == 3);
		Assert.assertEquals("12", result.getFeatures().get(0).getId());
		Assert.assertEquals("16", result.getFeatures().get(2).getId());
	}

	// ----------------------------------------------------------

	private GetFeaturesEachStep getGetFeaturesEachStep(boolean security) throws Exception {
		GetFeaturesEachStep gfes = new GetFeaturesEachStep();
		inoculate(gfes, security);
		return gfes;
	}

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

	private void inoculate(GetFeaturesEachStep gfes, final boolean filter) throws Exception {
		Field f = GetFeaturesEachStep.class.getDeclaredField("securityContext");
		f.setAccessible(true);
		f.set(gfes, new SecurityContext() {
			@Override
			public String getUserOrganization() {
				return null;
			}

			@Override
			public String getUserName() {
				return null;
			}

			@Override
			public Locale getUserLocale() {
				return null;
			}

			@Override
			public String getUserId() {
				return null;
			}

			@Override
			public String getUserDivision() {
				return null;
			}

			@Override
			public boolean isAttributeWritable(String layerId, InternalFeature feature, String attributeName) {
				return false;
			}

			@Override
			public boolean isAttributeReadable(String layerId, InternalFeature feature, String attributeName) {
				return true;
			}

			@Override
			public boolean isFeatureVisible(String layerId, InternalFeature feature) {
				if (filter)
					return (Integer.parseInt(feature.getId()) % 2 == 0); // filter
																			// odd
				else
					return true;
			}

			@Override
			public boolean isFeatureUpdateAuthorized(String layerId, InternalFeature orgFeature,
					InternalFeature newFeature) {
				return false;
			}

			@Override
			public boolean isFeatureUpdateAuthorized(String layerId, InternalFeature feature) {
				return false;
			}

			@Override
			public boolean isFeatureDeleteAuthorized(String layerId, InternalFeature feature) {
				return false;
			}

			@Override
			public boolean isFeatureCreateAuthorized(String layerId, InternalFeature feature) {
				return false;
			}

			@Override
			public boolean isPartlyVisibleSufficient(String layerId) {
				return false;
			}

			@Override
			public boolean isPartlyUpdateAuthorizedSufficient(String layerId) {
				return false;
			}

			@Override
			public boolean isPartlyDeleteAuthorizedSufficient(String layerId) {
				return false;
			}

			@Override
			public boolean isPartlyCreateAuthorizedSufficient(String layerId) {
				return false;
			}

			@Override
			public Geometry getVisibleArea(String layerId) {
				return null;
			}

			@Override
			public Geometry getUpdateAuthorizedArea(String layerId) {
				return null;
			}

			@Override
			public Geometry getDeleteAuthorizedArea(String layerId) {
				return null;
			}

			@Override
			public Geometry getCreateAuthorizedArea(String layerId) {
				return null;
			}

			@Override
			public Filter getFeatureFilter(String layerId) {
				return null;
			}

			@Override
			public boolean isToolAuthorized(String toolId) {
				return false;
			}

			@Override
			public boolean isLayerVisible(String layerId) {
				return false;
			}

			@Override
			public boolean isLayerUpdateAuthorized(String layerId) {
				return false;
			}

			@Override
			public boolean isLayerDeleteAuthorized(String layerId) {
				return false;
			}

			@Override
			public boolean isLayerCreateAuthorized(String layerId) {
				return false;
			}

			@Override
			public boolean isCommandAuthorized(String commandName) {
				return false;
			}

			@Override
			public String getId() {
				return null;
			}

			@Override
			public String getToken() {
				return null;
			}

			@Override
			public List<Authentication> getSecurityServiceResults() {
				return null;
			}
		});
	}
}
