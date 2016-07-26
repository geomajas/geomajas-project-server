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

package org.geomajas.internal.layer.feature;

import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.spring.ThreadScopeContextHolder;
import org.geomajas.testdata.Country;
import org.geomajas.testdata.ReloadContext;
import org.geomajas.testdata.ReloadContextTestExecutionListener;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Test for AttributeService.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/beanContext.xml", "/org/geomajas/testdata/layerBeans.xml",
		"/org/geomajas/internal/service/countriesNotAllEditable.xml"})
@TestExecutionListeners(listeners = {ReloadContextTestExecutionListener.class,
		DependencyInjectionTestExecutionListener.class})
@ReloadContext
public class AttributeServiceEditableCapabilityTest {

	private static final long TEST_ID = 17;
	private static final String TEST_COUNTRY = "BE";
	private static final String TEST_NAME = "Belgium";

	private Country bean;

	@Autowired
	@Qualifier("layerCountriesNotAllEditable")
	private VectorLayer layer;

	@Autowired
	private AttributeService attributeService;

	@Autowired
	private org.geomajas.security.SecurityManager securityManager;

	@Before
	public void init() {
		bean = new Country();
		bean.setId(TEST_ID);
		bean.setCountry(TEST_COUNTRY);
		bean.setName(TEST_NAME);
		securityManager.createSecurityContext(""); // log in
	}

	@After
	public void fixSideEffects() {
		ThreadScopeContextHolder.clear();
	}

	@Test
	public void testEditableState() throws Exception {
		InternalFeature feature = new InternalFeatureImpl();
		Assert.assertNotNull(attributeService.getAttributes(layer, feature, bean));
		Attribute attribute;
		Attribute linked;
		attribute = feature.getAttributes().get("country");
		assertThat(attribute.getValue()).isEqualTo(TEST_COUNTRY);
		assertThat(attribute.isEditable()).isFalse(); // AllowAll + editable==false -> not editable
		attribute = feature.getAttributes().get("name");
		assertThat(attribute.getValue()).isEqualTo(TEST_NAME);
		assertThat(attribute.isEditable()).isTrue(); // AllowAll + editable=true -> editable
	}

}

