package org.geomajas.plugin.runtimeconfig.command.runtimeconfig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.CommandResponse;
import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.FontStyleInfo;
import org.geomajas.configuration.IsInfo;
import org.geomajas.configuration.LabelStyleInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.plugin.runtimeconfig.command.dto.SaveOrUpdateParameterBeanRequest;
import org.geomajas.plugin.runtimeconfig.command.dto.SaveOrUpdateParameterBeanResponse;
import org.geomajas.plugin.runtimeconfig.service.factory.ClientVectorLayerBeanFactory;
import org.geomajas.plugin.runtimeconfig.service.factory.GeoToolsLayerBeanFactory;
import org.geomajas.security.SecurityManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/runtimeconfig/command/runtimeconfig/runtimeConfig.xml",
		"/org/geomajas/plugin/runtimeconfig/command/runtimeconfig/appRuntimeConfig.xml",
		"/org/geomajas/plugin/runtimeconfig/command/runtimeconfig/mapRuntimeConfig.xml" })
public class IssueRTC2Test {

	@Autowired
	private CommandDispatcher commandDispatcher;

	@Autowired
	private SecurityManager manager;

	@Autowired
	private ApplicationContext applicationContext;

	@Before
	public void login() {
		manager.createSecurityContext(null);
	}

	@After
	public void logout() {
		manager.clearSecurityContext();
	}

	@Test
	public void testSetFormulaMethod() {
		GeometryWrapper wrapper = new GeometryWrapper("archive/shapes/multipolygons.shp", Arrays.asList(1.0,2.0), Arrays.asList(100.0,200.0));
		SaveOrUpdateParameterBeanRequest request = new SaveOrUpdateParameterBeanRequest();
		request.addStringParameter(GeoToolsLayerBeanFactory.CLASS_NAME, "org.geomajas.layer.geotools.GeoToolsLayer");
		request.addStringParameter(GeoToolsLayerBeanFactory.BEAN_NAME, "adminCountries");
		NamedStyleInfo style1 = createRandomPolygonStyle("style1", wrapper);
		NamedStyleInfo style2 = createRandomPolygonStyle("style2", wrapper);
		List<IsInfo> styles = new ArrayList<IsInfo>();
		styles.add(style1);
		styles.add(style2);
		request.addListParameter(GeoToolsLayerBeanFactory.STYLE_INFO, styles);
		request.addStringParameter("location", "adminCountries.shp");
		CommandResponse response = commandDispatcher.execute(SaveOrUpdateParameterBeanRequest.COMMAND, request, null,
				null);
		if (response.isError()) {
			for (Throwable throwable : response.getErrors()) {
				throwable.printStackTrace();
			}
		}

		request = new SaveOrUpdateParameterBeanRequest();
		request.addStringParameter(ClientVectorLayerBeanFactory.CLASS_NAME,
				"org.geomajas.configuration.client.ClientVectorLayerInfo");
		request.addStringParameter(ClientVectorLayerBeanFactory.BEAN_NAME, "clientRuntimeConfigCountries");
		request.addStringParameter(ClientVectorLayerBeanFactory.LABEL, "Countries");
		request.addStringParameter(ClientVectorLayerBeanFactory.SERVER_LAYER_ID, "adminCountries");
		request.addStringParameter(ClientVectorLayerBeanFactory.MAP_ID, "mapRuntimeConfig");
		response = commandDispatcher.execute(SaveOrUpdateParameterBeanRequest.COMMAND, request, null, null);
		if (response.isError()) {
			for (Throwable throwable : response.getErrors()) {
				throwable.printStackTrace();
			}
		}
	}

	private NamedStyleInfo createRandomPolygonStyle(String name, GeometryWrapper wrapper) {
		NamedStyleInfo styleInfo = new NamedStyleInfo();
		styleInfo.setName(name);
		styleInfo.setSldStyleName(name);
		FeatureStyleInfo featureStyle = createRandomPointStyle(wrapper.getMax(0), wrapper.getMin(0));
		styleInfo.getFeatureStyles().add(featureStyle);
		styleInfo.setLabelStyle(createRandomLabelStyle("minInfRed"));
		return styleInfo;
	}

	private FeatureStyleInfo createRandomPointStyle(double max, double min) {
		FeatureStyleInfo featureStyle = new FeatureStyleInfo();
		featureStyle.setName("Points: [" + min + "; " + max + "]");
		featureStyle.setFormula("(minInfRed > " + min + " )");
		featureStyle.setFillColor("#1A9641");
		featureStyle.setFillOpacity(0.5F);
		featureStyle.setStrokeColor("#1A9641");
		featureStyle.setStrokeOpacity(0.5F);
		featureStyle.setStrokeWidth(1);
		return featureStyle;
	}

	private LabelStyleInfo createRandomLabelStyle(String attributeName) {
		LabelStyleInfo labelStyle = new LabelStyleInfo();
		labelStyle.setLabelAttributeName(attributeName);
		FontStyleInfo fontStyle = new FontStyleInfo();
		fontStyle.setColor("#000000");
		fontStyle.setFamily("Verdana");
		fontStyle.setOpacity(1F);
		fontStyle.setSize(8);
		labelStyle.setFontStyle(fontStyle);

		FeatureStyleInfo backgroundStyle = new FeatureStyleInfo();
		backgroundStyle.setFillColor("#FFFFFF");
		backgroundStyle.setFillOpacity(0.7F);
		backgroundStyle.setStrokeColor("#000099");
		backgroundStyle.setStrokeOpacity(1F);
		backgroundStyle.setStrokeWidth(1);
		labelStyle.setBackgroundStyle(backgroundStyle);

		return labelStyle;
	}

	class GeometryWrapper implements Serializable {

		private String shapefilePath;

		private List<Double> max;

		private List<Double> min;

		public GeometryWrapper() {
		}

		public GeometryWrapper(String shapefilePath, List<Double> max, List<Double> min) {
			this.shapefilePath = shapefilePath;
			this.max = max;
			this.min = min;
		}

		public String getShapefilePath() {
			return shapefilePath;
		}

		public double getMax(int slot) {
			return max.get(slot);
		}

		public int size() {
			return max.size() == min.size() ? max.size() : -1;
		}

		public double getMin(int slot) {
			return min.get(slot);
		}

		public void setShapefilePath(String shapefilePath) {
			this.shapefilePath = shapefilePath;
		}

		public void setMax(List<Double> max) {
			this.max = max;
		}

		public void setMin(List<Double> min) {
			this.min = min;
		}

		public List<Double> getMax() {
			return max;
		}

		public List<Double> getMin() {
			return min;
		}

		@Override
		public String toString() {
			String result = "";
			result += "Path: " + shapefilePath + ";\n";
			result += "Max: " + max + ";\n";
			result += "Min: " + min + ";\n";

			return result;
		}
	}

}
