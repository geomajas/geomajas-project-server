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
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.spring.ThreadScopeContextHolder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test for AttributeService.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/beanContext.xml", "/org/geomajas/internal/layer/feature/syntheticAttributeContext.xml"})
public class AttributeServiceSynthTest {

	private static final long TEST_ID = 1;
	private static final String TEST_NAME = "Doe";
	private static final String TEST_SURNAME = "John";
	private static final String TEST_FULL_NAME = TEST_SURNAME + " " + TEST_NAME;
	private static final String TEST_NAME_DATE = TEST_NAME + " 1984-12-25";

	private NameBean nameBean;

	@Autowired
	@Qualifier("beans")
	private VectorLayer layerBeans;

	@Autowired
	private AttributeService attributeService;

	@Autowired
	private org.geomajas.security.SecurityManager securityManager;

	@Before
	public void init() {
		nameBean = new NameBean();
		nameBean.setId(TEST_ID);
		nameBean.setName(TEST_NAME);
		nameBean.setSurname(TEST_SURNAME);

		securityManager.createSecurityContext(""); // log in
	}

	@After
	public void fixSideEffects() {
		ThreadScopeContextHolder.clear();
	}

	@Test
	public void testSyntheticFields() throws Exception {
		InternalFeature feature = new InternalFeatureImpl();
		feature.setLayer(layerBeans);
		Assert.assertNotNull(attributeService.getAttributes(layerBeans, feature, nameBean));
		Assert.assertEquals(TEST_NAME, feature.getAttributes().get("name").getValue());
		Assert.assertEquals(TEST_SURNAME, feature.getAttributes().get("surname").getValue());
		Assert.assertEquals(TEST_FULL_NAME, feature.getAttributes().get("fullName").getValue());
		Assert.assertEquals(TEST_NAME_DATE, feature.getAttributes().get("nameDate").getValue());
	}

}

