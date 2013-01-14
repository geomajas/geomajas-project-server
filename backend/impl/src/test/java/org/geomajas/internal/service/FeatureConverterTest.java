/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.internal.service;

import junit.framework.Assert;

import org.geomajas.global.GeomajasException;
import org.geomajas.internal.layer.feature.InternalFeatureImpl;
import org.geomajas.layer.feature.Feature;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.service.DtoConverterService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests for the DtoConverterService, specifically testing the feature conversions.
 * 
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/spring/moreContext.xml" })
public class FeatureConverterTest {

	@Autowired
	private DtoConverterService converterService;

	@Test
	public void toInternal() throws GeomajasException {
		Feature feature = new Feature();
		Assert.assertNotNull(converterService.toInternal(feature));

		feature.setId("id");
		feature.setLabel("label");
		feature.setDeletable(true);
		feature.setUpdatable(true);

		InternalFeature internalFeature = converterService.toInternal(feature);
		Assert.assertEquals("id", internalFeature.getId());
		Assert.assertEquals("label", internalFeature.getLabel());
		Assert.assertTrue(internalFeature.isDeletable());
		Assert.assertTrue(internalFeature.isEditable());
	}

	@Test
	public void toDto() throws GeomajasException {
		InternalFeature internalFeature = new InternalFeatureImpl();
		Assert.assertNotNull(converterService.toDto(internalFeature));

		internalFeature.setId("id");
		internalFeature.setLabel("label");
		internalFeature.setDeletable(true);
		internalFeature.setEditable(true);

		Feature feature = converterService.toDto(internalFeature);
		Assert.assertEquals("id", feature.getId());
		Assert.assertEquals("label", feature.getLabel());
		Assert.assertTrue(feature.isDeletable());
		Assert.assertTrue(feature.isUpdatable());
	}
}
