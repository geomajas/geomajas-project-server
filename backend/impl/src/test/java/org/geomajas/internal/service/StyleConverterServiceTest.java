package org.geomajas.internal.service;

import junit.framework.Assert;

import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.layer.LayerException;
import org.geomajas.service.StyleConverterService;
import org.geomajas.sld.StyledLayerDescriptorInfo;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.SLDParser;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
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
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml", "/org/geomajas/testdata/layerBeans.xml" })
public class StyleConverterServiceTest {

	@Autowired
	private StyleConverterService styleConverterService;
	
	@Autowired
	@Qualifier("beansFeatureInfo")
	private FeatureInfo featureInfo;

	@Test
	public void testSingleStyle() throws JiBXException, LayerException {
		IBindingFactory bfact = BindingDirectory.getFactory(StyledLayerDescriptorInfo.class);
		IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
		Object object = uctx.unmarshalDocument(getClass().getResourceAsStream("/org/geomajas/testdata/sld/single_layer_no_stylename.sld"), null);
		StyledLayerDescriptorInfo sld = (StyledLayerDescriptorInfo) object;
		NamedStyleInfo info = styleConverterService.convert(sld, featureInfo, "layer", "style");
		Assert.assertNotNull(info);
		Assert.assertEquals("style", info.getName());
	}
	
	@Test
	public void testPickStyle() throws JiBXException, LayerException {
		IBindingFactory bfact = BindingDirectory.getFactory(StyledLayerDescriptorInfo.class);
		IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
		Object object = uctx.unmarshalDocument(getClass().getResourceAsStream("/org/geomajas/testdata/sld/multiple_layer_stylename.sld"), null);
		StyledLayerDescriptorInfo sld = (StyledLayerDescriptorInfo) object;
		NamedStyleInfo info1 = styleConverterService.convert(sld, featureInfo, "Layer1", "Style1");
		Assert.assertNotNull(info1);
		Assert.assertEquals("Style1", info1.getName());
		Assert.assertEquals(1, info1.getFeatureStyles().size());
		
		NamedStyleInfo info2 = styleConverterService.convert(sld, featureInfo, "Layer2", "Style2");
		Assert.assertNotNull(info2);
		Assert.assertEquals("Style2", info2.getName());
		Assert.assertEquals(2, info2.getFeatureStyles().size());
	}
	
}
