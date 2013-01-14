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

package org.geomajas.internal.configuration;

import java.util.Locale;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Verify that the check on layer tree nodes, layer needs to be part of the map.
 *
 * @author Joachim Van der Auwera
 */
public class ConfigurationDtoPostProcessorLayerTreeTest {
	
	@Before
	public void before() {
		Locale.setDefault(Locale.ENGLISH);
	}

	@Test
	public void testLayerTreeCheck() throws Exception {
		try {
			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
			context.setId("test");
			context.setDisplayName("test");
			context.setConfigLocation(
					"/org/geomajas/spring/geomajasContext.xml " +
					"/org/geomajas/testdata/beanContext.xml " +
					"/org/geomajas/testdata/layerBeans.xml " +
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
