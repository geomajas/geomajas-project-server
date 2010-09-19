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
