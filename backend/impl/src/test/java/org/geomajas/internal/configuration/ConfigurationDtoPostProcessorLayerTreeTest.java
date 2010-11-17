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

package org.geomajas.internal.configuration;

import junit.framework.Assert;
import org.junit.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Verify that the check on layer tree nodes, layer needs to be part of the map.
 *
 * @author Joachim Van der Auwera
 */
public class ConfigurationDtoPostProcessorLayerTreeTest {

	@Test
	public void testLayerTreeCheck() throws Exception {
		try {
			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
			context.setId("test");
			context.setDisplayName("test");
			context.setConfigLocation(
					"/org/geomajas/spring/geomajasContext.xml " +
					"/org/geomajas/layer/bean/beanContext.xml " +
					"/org/geomajas/layer/bean/layerBeans.xml " +
					"/org/geomajas/internal/configuration/layerTreeInvalid.xml " +
					"");
			context.refresh();
			Assert.fail("Context initialization should have failed.");
		} catch (BeanCreationException bce) {
			Assert.assertTrue(bce.getCause().getMessage().startsWith(
					"A LayerTreeNodeInfo object can only reference layers which are part of the map, layer "));
		}
	}
}
