package org.geomajas.internal.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import junit.framework.Assert;

import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.layer.LayerException;
import org.geomajas.service.StyleConverterService;
import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.StyledLayerDescriptorInfo;
import org.geomajas.sld.UserStyleInfo;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * <p>
 * Test class for {@link org.geomajas.service.StyleConverterService} service.
 * </p>
 * 
 * @author Jan De Moerloose
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/layerBeans.xml", "/org/geomajas/testdata/layerBeansMixedGeometry.xml" })
public class StyleConverterServiceTest {

	@Autowired
	private StyleConverterService styleConverterService;

	@Autowired
	@Qualifier("beansFeatureInfo")
	private FeatureInfo featureInfo;

	@Autowired
	@Qualifier("layerBeansMixedGeometryStyleInfo")
	private NamedStyleInfo layerBeansMixedGeometryStyleInfo;

	@Test
	public void testSingleStyle() throws JiBXException, LayerException {
		IBindingFactory bfact = BindingDirectory.getFactory(StyledLayerDescriptorInfo.class);
		IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
		Object object = uctx.unmarshalDocument(
				getClass().getResourceAsStream("/org/geomajas/testdata/sld/single_layer_no_stylename.sld"), null);
		StyledLayerDescriptorInfo sld = (StyledLayerDescriptorInfo) object;
		NamedStyleInfo info = styleConverterService.convert(sld.getChoiceList().get(0).getNamedLayer().getChoiceList()
				.get(0).getUserStyle(), featureInfo);
		Assert.assertNotNull(info);
		Assert.assertEquals("Some title", info.getName());
	}

	@Test
	public void testMixedGeometryStyle() throws LayerException {
		UserStyleInfo style = styleConverterService.convert(layerBeansMixedGeometryStyleInfo, "geometry");
		List<RuleInfo> rules = style.getFeatureTypeStyleList().get(0).getRuleList();
		Assert.assertEquals(3, rules.size());
	}
	
}
