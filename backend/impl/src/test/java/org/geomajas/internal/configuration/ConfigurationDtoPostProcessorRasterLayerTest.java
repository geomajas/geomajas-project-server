/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.internal.configuration;

import junit.framework.Assert;
import org.geomajas.configuration.LabelStyleInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.layer.VectorLayer;
import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.UserStyleInfo;
import org.junit.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Verify the checks on {@link org.geomajas.configuration.RasterLayerInfo}.
 *
 * @author Joachim Van der Auwera
 */
public class ConfigurationDtoPostProcessorRasterLayerTest {

	@Test
	public void testNullTileWidth() throws Exception {
		try {
			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
			context.setId("test");
			context.setDisplayName("test");
			context.setConfigLocation(
					"/org/geomajas/spring/geomajasContext.xml " +
					"/org/geomajas/internal/configuration/RasterLayerZeroTileWidth.xml " +
					"");
			context.refresh();
			Assert.fail("Context initialization should have failed.");
		} catch (BeanCreationException bce) {
			assertThat(bce.getCause().getCause().getMessage()).startsWith(
					"Layer layerOsm is not correctly configured: tileWidth should not be zero.");
		}
	}
	
	@Test
	public void testNullTileHeight() throws Exception {
		try {
			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
			context.setId("test");
			context.setDisplayName("test");
			context.setConfigLocation(
					"/org/geomajas/spring/geomajasContext.xml " +
					"/org/geomajas/internal/configuration/RasterLayerZeroTileHeight.xml " +
					"");
			context.refresh();
			Assert.fail("Context initialization should have failed.");
		} catch (BeanCreationException bce) {
			assertThat(bce.getCause().getCause().getMessage()).startsWith(
					"Layer layerOsm is not correctly configured: tileHeight should not be zero.");
		}
	}

}
