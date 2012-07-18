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
package org.geomajas.plugin.runtimeconfig.gwt.example.client.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.FontStyleInfo;
import org.geomajas.configuration.IsInfo;
import org.geomajas.configuration.LabelStyleInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.gwt.client.action.ToolbarAction;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.runtimeconfig.command.dto.SaveOrUpdateParameterBeanRequest;
import org.geomajas.plugin.runtimeconfig.command.dto.SaveOrUpdateParameterBeanResponse;
import org.geomajas.plugin.runtimeconfig.service.factory.ClientVectorLayerBeanFactory;
import org.geomajas.plugin.runtimeconfig.service.factory.GeoToolsLayerBeanFactory;

import com.smartgwt.client.widgets.events.ClickEvent;

/**
 * Adds a layer.
 * 
 * @author Jan De Moerloose
 * 
 */
public class AddLayerShapeAction extends ToolbarAction {

	private Random rand = new Random();

	public static final String TOOL = "AddLayerShape";

	private MapWidget map;

	public AddLayerShapeAction(MapWidget map) {
		super("[ISOMORPHIC]/geomajas/osgeo/layer-vector-add.png", "Add shape layer", "Add shape layer");
		this.map = map;
	}

	public void onClick(ClickEvent event) {
		SaveOrUpdateParameterBeanRequest request = new SaveOrUpdateParameterBeanRequest();
		request.addStringParameter(GeoToolsLayerBeanFactory.CLASS_NAME, "org.geomajas.layer.geotools.GeoToolsLayer");
		request.addStringParameter(GeoToolsLayerBeanFactory.BEAN_NAME, "adminCountries");
		NamedStyleInfo style1 = createRandomPolygonStyle("style1");
		NamedStyleInfo style2 = createRandomPolygonStyle("style2");
		List<IsInfo> styles = new ArrayList<IsInfo>();
		styles.add(style1);
		styles.add(style2);
		request.addListParameter(GeoToolsLayerBeanFactory.STYLE_INFO, styles);
		request.addStringParameter("location", "adminCountries.shp");
		GwtCommand command = new GwtCommand(SaveOrUpdateParameterBeanRequest.COMMAND);
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command,
				new AbstractCommandCallback<SaveOrUpdateParameterBeanResponse>() {

					public void execute(SaveOrUpdateParameterBeanResponse response) {
						SaveOrUpdateParameterBeanRequest request = new SaveOrUpdateParameterBeanRequest();
						request.addStringParameter(ClientVectorLayerBeanFactory.CLASS_NAME,
								"org.geomajas.configuration.client.ClientVectorLayerInfo");
						request.addStringParameter(ClientVectorLayerBeanFactory.BEAN_NAME,
								"clientRuntimeConfigCountries");
						request.addStringParameter(ClientVectorLayerBeanFactory.LABEL, "Countries");
						request.addStringParameter(ClientVectorLayerBeanFactory.SERVER_LAYER_ID, "adminCountries");
						request.addStringParameter(ClientVectorLayerBeanFactory.MAP_ID, "mapRuntimeConfig");
						GwtCommand command = new GwtCommand(SaveOrUpdateParameterBeanRequest.COMMAND);
						command.setCommandRequest(request);
						GwtCommandDispatcher.getInstance().execute(command,
								new AbstractCommandCallback<SaveOrUpdateParameterBeanResponse>() {

									public void execute(SaveOrUpdateParameterBeanResponse response) {
										map.getMapModel().refresh();
									}

								});
					}

				});
	}

	private NamedStyleInfo createRandomPolygonStyle(String name) {
		NamedStyleInfo styleInfo = new NamedStyleInfo();
		styleInfo.setName(name);
		FeatureStyleInfo featureStyle = createRandomPolygonStyle();
		styleInfo.getFeatureStyles().add(featureStyle);
		// first identifying is label
		styleInfo.setLabelStyle(createRandomLabelStyle("NAME"));
		return styleInfo;
	}

	private FeatureStyleInfo createRandomPolygonStyle() {
		FeatureStyleInfo featureStyle = new FeatureStyleInfo();
		Color strokeColor = createRandomColor();
		featureStyle.setFillColor(strokeColor.brighter().toCss());
		featureStyle.setFillOpacity(0.5F);
		featureStyle.setIndex(0);
		featureStyle.setStrokeColor(strokeColor.toCss());
		featureStyle.setStrokeOpacity(0.5F);
		featureStyle.setStrokeWidth(1);
		featureStyle.setName("default");
		return featureStyle;
	}

	private Color createRandomColor() {
		return new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
	}

	private LabelStyleInfo createRandomLabelStyle(String attributeName) {
		LabelStyleInfo style = new LabelStyleInfo();
		style.setBackgroundStyle(createRandomPolygonStyle());
		FontStyleInfo fontStyle = new FontStyleInfo();
		fontStyle.setColor(style.getBackgroundStyle().getStrokeColor());
		fontStyle.setFamily("Verdana");
		fontStyle.setOpacity(1F);
		fontStyle.setSize(8);
		fontStyle.setWeight("normal");
		fontStyle.setStyle("normal");
		style.setFontStyle(fontStyle);
		style.setLabelAttributeName(attributeName);
		return style;
	}
	
	/**
	 * Minimal color class.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	class Color {

		private static final float FACTOR = 0.7f;

		private int r;

		private int g;

		private int b;

		public Color(int r, int g, int b) {
			this.r = r;
			this.g = g;
			this.b = b;
		}

		public Color brighter() {
			return new Color(Math.min((int) (r / FACTOR), 255), Math.min((int) (r / FACTOR), 255), Math.min(
					(int) (r / FACTOR), 255));
		}

		public String toCss() {
			return "#" + pad(Integer.toHexString(r)) + pad(Integer.toHexString(g)) + pad(Integer.toHexString(b));
		}

		private String pad(String in) {
			if (in.length() == 0) {
				return "00";
			}
			if (in.length() == 1) {
				return "0" + in;
			}
			return in;
		}

	}


}
