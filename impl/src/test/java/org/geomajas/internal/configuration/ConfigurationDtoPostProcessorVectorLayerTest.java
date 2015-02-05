/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.internal.configuration;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;
import java.util.Locale;

import junit.framework.Assert;

import org.geomajas.configuration.LabelStyleInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.layer.VectorLayer;
import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.UserStyleInfo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Verify that the check on layer tree nodes, layer needs to be part of the map.
 *
 * @author Joachim Van der Auwera
 */
public class ConfigurationDtoPostProcessorVectorLayerTest {

	@Before
	public void before() {
		Locale.setDefault(Locale.ENGLISH);
	}
	
	@Test
	public void testAttributeInvalidNameCheck() {
		try {
			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
			context.setId("test");
			context.setDisplayName("test");
			context.setConfigLocation(
					"/org/geomajas/spring/geomajasContext.xml " +
					"/org/geomajas/testdata/beanContext.xml " +
					"/org/geomajas/testdata/layerBeans.xml " +
					"/org/geomajas/internal/configuration/layerBeansInvalid.xml " +
					"");
			context.refresh();
			Assert.fail("Context initialization should have failed.");
		} catch (BeanCreationException bce) {
			assertThat(bce.getCause().getCause().getMessage()).startsWith(
					"Invalid attribute name manyToOne.stringAttr in layer beans.");
		}
	}

	@Test
	public void testDuplicateAttribute() {
		try {
			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
			context.setId("test");
			context.setDisplayName("test");
			context.setConfigLocation(
					"/org/geomajas/spring/geomajasContext.xml " +
					"/org/geomajas/internal/configuration/layerBeansDuplicateAttribute.xml " +
					"");
			context.refresh();
			Assert.fail("Context initialization should have failed.");
		} catch (BeanCreationException bce) {
			assertThat(bce.getCause().getCause().getMessage()).startsWith(
					"Duplicate attribute name stringAttr in layer beans, path .");
		}
	}

	@Test
	public void testDuplicateAttributeInOneToMany() {
		try {
			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
			context.setId("test");
			context.setDisplayName("test");
			context.setConfigLocation(
					"/org/geomajas/spring/geomajasContext.xml " +
					"/org/geomajas/internal/configuration/layerBeansDuplicateAttrOneToMany.xml " +
					"");
			context.refresh();
			Assert.fail("Context initialization should have failed.");
		} catch (BeanCreationException bce) {
			assertThat(bce.getCause().getCause().getMessage()).startsWith(
					"Duplicate attribute name stringAttr in layer beans, path /oneToManyAttr.");
		}
	}

	@Test
	public void testDuplicateAttributeInManyToOne() {
		try {
			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
			context.setId("test");
			context.setDisplayName("test");
			context.setConfigLocation(
					"/org/geomajas/spring/geomajasContext.xml " +
					"/org/geomajas/internal/configuration/layerBeansDuplicateAttrManyToOne.xml " +
					"");
			context.refresh();
			Assert.fail("Context initialization should have failed.");
		} catch (BeanCreationException bce) {
			assertThat(bce.getCause().getCause().getMessage()).startsWith(
					"Duplicate attribute name stringAttr in layer beans, path /manyToOneAttr.");
		}
	}

	@Test
	public void testDefaultStyle() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
		context.setId("test");
		context.setDisplayName("test");
		context.setConfigLocation(
				"/org/geomajas/spring/geomajasContext.xml "+
				"/org/geomajas/internal/configuration/layerDefaultStyle.xml ");
		context.refresh();
		VectorLayer layerDefaultStyle = (VectorLayer)context.getBean("layerDefaultStyle");
		List<NamedStyleInfo> styles = layerDefaultStyle.getLayerInfo().getNamedStyleInfos();
		Assert.assertEquals(1, styles.size());
		NamedStyleInfo defaultStyle = styles.get(0);
		Assert.assertEquals(NamedStyleInfo.DEFAULT_NAME, defaultStyle.getName());
		Assert.assertEquals(LabelStyleInfo.ATTRIBUTE_NAME_ID, defaultStyle.getLabelStyle().getLabelAttributeName());
	}

	@Test
	public void testUserStyle() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
		context.setId("test");
		context.setDisplayName("test");
		context.setConfigLocation(
				"/org/geomajas/spring/geomajasContext.xml "+
				"/org/geomajas/internal/configuration/layerDefaultStyle.xml ");
		context.refresh();
		VectorLayer layerDefaultStyle = (VectorLayer)context.getBean("layerDefaultStyle");
		List<NamedStyleInfo> styles = layerDefaultStyle.getLayerInfo().getNamedStyleInfos();
		Assert.assertEquals(1, styles.size());
		NamedStyleInfo defaultStyle = styles.get(0);
		UserStyleInfo userStyle = defaultStyle.getUserStyle();
		List<RuleInfo> rules = userStyle.getFeatureTypeStyleList().get(0).getRuleList();
		Assert.assertEquals(1, rules.size());
		Assert.assertEquals(null, rules.get(0).getName());
	}
}
