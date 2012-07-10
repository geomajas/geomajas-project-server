package org.geomajas.internal.service;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import junit.framework.Assert;

import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.layer.LayerException;
import org.geomajas.service.StyleConverterService;
import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.StyledLayerDescriptorInfo;
import org.geomajas.sld.UserStyleInfo;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.filter.spatial.Equals;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Overlaps;
import org.opengis.filter.spatial.Touches;
import org.opengis.filter.spatial.Within;
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

	@Autowired
	@Qualifier("layerBeansMixedGeometryStyleInfoSld")
	private NamedStyleInfo layerBeansMixedGeometryStyleInfoSld;

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
	public void testStyleWithAttributeLabel() throws JiBXException, LayerException {
		IBindingFactory bfact = BindingDirectory.getFactory(StyledLayerDescriptorInfo.class);
		IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
		Object object = uctx.unmarshalDocument(
				getClass().getResourceAsStream("/org/geomajas/testdata/sld/line_labelfollowingline.sld"),
						null);
		StyledLayerDescriptorInfo sld = (StyledLayerDescriptorInfo) object;
		NamedStyleInfo info = styleConverterService.convert(sld.getChoiceList().get(0).getNamedLayer().getChoiceList()
				.get(0).getUserStyle(), featureInfo);
		Assert.assertNotNull(info);
		
		Assert.assertEquals("name", info.getLabelStyle().getLabelAttributeName());

	}

	
	@Test
	public void testStyleWithLiteralLabel() throws JiBXException, LayerException {
		IBindingFactory bfact = BindingDirectory.getFactory(StyledLayerDescriptorInfo.class);
		IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
		Object object = uctx.unmarshalDocument(
				getClass().getResourceAsStream("/org/geomajas/testdata/sld/line_literallabelfollowingline.sld"),
						null);
		StyledLayerDescriptorInfo sld = (StyledLayerDescriptorInfo) object;
		NamedStyleInfo info = styleConverterService.convert(sld.getChoiceList().get(0).getNamedLayer().getChoiceList()
				.get(0).getUserStyle(), featureInfo);
		Assert.assertNotNull(info);
		
		Assert.assertEquals("'\u2192'", info.getLabelStyle().getLabelValueExpression());

	}

	@Test
	public void testStyleWithLiteralCssParameter() throws JiBXException, LayerException {
		IBindingFactory bfact = BindingDirectory.getFactory(StyledLayerDescriptorInfo.class);
		IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
		Object object = uctx.unmarshalDocument(
				getClass().getResourceAsStream("/org/geomajas/testdata/sld/polygon_literalcssparameter.sld"),
						null);
		StyledLayerDescriptorInfo sld = (StyledLayerDescriptorInfo) object;
		NamedStyleInfo info = styleConverterService.convert(sld.getChoiceList().get(0).getNamedLayer().getChoiceList()
				.get(0).getUserStyle(), featureInfo);
		Assert.assertNotNull(info);
	}

	@Test
	public void testMixedGeometryStyle() throws LayerException {
		UserStyleInfo style = styleConverterService.convert(layerBeansMixedGeometryStyleInfo, "geometry");
		List<RuleInfo> rules = style.getFeatureTypeStyleList().get(0).getRuleList();
		Assert.assertEquals(3, rules.size());
	}

	@Test
	public void testFilters() throws LayerException {
		Style style = styleConverterService.convert(layerBeansMixedGeometryStyleInfoSld.getUserStyle());
		List<Rule> rules = style.featureTypeStyles().get(0).rules();
		assertThat(rules.get(0).getFilter()).isInstanceOf(BBOX.class);
		assertThat(rules.get(1).getFilter()).isInstanceOf(Contains.class);
		assertThat(rules.get(2).getFilter()).isInstanceOf(Crosses.class);
		assertThat(rules.get(3).getFilter()).isInstanceOf(Disjoint.class);
		assertThat(rules.get(4).getFilter()).isInstanceOf(Equals.class);
		assertThat(rules.get(5).getFilter()).isInstanceOf(Intersects.class);
		assertThat(rules.get(6).getFilter()).isInstanceOf(Overlaps.class);
		assertThat(rules.get(7).getFilter()).isInstanceOf(Touches.class);
		assertThat(rules.get(8).getFilter()).isInstanceOf(Within.class);
		NamedStyleInfo namedStyleInfo = styleConverterService.convert(
				layerBeansMixedGeometryStyleInfoSld.getUserStyle(), featureInfo);
		Assert.assertEquals(9, namedStyleInfo.getFeatureStyles().size());
	}

}
