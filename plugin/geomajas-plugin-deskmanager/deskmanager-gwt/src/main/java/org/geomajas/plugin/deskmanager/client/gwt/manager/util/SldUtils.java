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
package org.geomajas.plugin.deskmanager.client.gwt.manager.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.layer.LayerType;
import org.geomajas.plugin.deskmanager.command.manager.dto.DynamicVectorLayerConfiguration;
import org.geomajas.sld.CssParameterInfo;
import org.geomajas.sld.FeatureTypeStyleInfo;
import org.geomajas.sld.FillInfo;
import org.geomajas.sld.GraphicInfo;
import org.geomajas.sld.GraphicInfo.ChoiceInfo;
import org.geomajas.sld.LabelInfo;
import org.geomajas.sld.LineSymbolizerInfo;
import org.geomajas.sld.MarkInfo;
import org.geomajas.sld.PointSymbolizerInfo;
import org.geomajas.sld.PolygonSymbolizerInfo;
import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.SizeInfo;
import org.geomajas.sld.StrokeInfo;
import org.geomajas.sld.SymbolizerTypeInfo;
import org.geomajas.sld.TextSymbolizerInfo;
import org.geomajas.sld.UserStyleInfo;
import org.geomajas.sld.WellKnownNameInfo;
import org.geomajas.sld.expression.PropertyNameInfo;

import com.google.gwt.core.client.GWT;

/**
 * Some sld utilities.
 * 
 * @author Kristof Heirwegh
 */
public final class SldUtils {

	public static final String FILLCOLOR = "fill";
	public static final String FILLOPACITY = "fill-opacity";
	public static final String STROKECOLOR = "stroke";
	public static final String STROKEOPACITY = "stroke-opacity";
	public static final String STROKEWIDTH = "stroke-width";
	public static final String SIZE = "size";
	public static final String WELLKNOWNNAME = "well-known-name"; // MARK
	public static final String STYLENAME = "style-name"; // default = namedStyleInfo.getName();
	public static final String LABELFEATURENAME = "label-field-name";

	public static final String DEFAULT_FILLCOLOR = "#CCCCCC";
	public static final Float DEFAULT_FILLOPACITY = 0.5f;
	public static final String DEFAULT_STROKECOLOR = "#000000";
	public static final Float DEFAULT_STROKEOPACITY = 1f;
	public static final Float DEFAULT_STROKEWIDTH = 1f;
	public static final String DEFAULT_SIZE = "6"; // don't ask
	public static final String DEFAULT_WELLKNOWNNAME = "circle"; // MARK

	private SldUtils() {
	}

	public static UserStyleInfo createSimpleSldStyle(DynamicVectorLayerConfiguration dvc, 
			Map<String, Object> properties) {
		UserStyleInfo usi = new UserStyleInfo();
		usi.setTitle(getPropValue(STYLENAME, properties, dvc.getClientVectorLayerInfo().getNamedStyleInfo().getName()));
		usi.setName(usi.getTitle());
		FeatureTypeStyleInfo fsi = new FeatureTypeStyleInfo();
		fsi.setName(usi.getTitle());
		usi.getFeatureTypeStyleList().add(fsi);

		RuleInfo ri = createRule(dvc.getVectorLayerInfo().getLayerType(), properties);
		fsi.getRuleList().add(ri);

		return usi;
	}

	/**
	 * Leave null what you don't need/want defaults will be used.
	 * 
	 * @param type
	 * @param fillColor
	 * @param fillOpacity
	 * @param strokeColor
	 * @param strokeOpacity
	 * @param strokeWidth
	 * @return
	 */
	public static RuleInfo createRule(LayerType type, Map<String, Object> properties) {
		RuleInfo rule = new RuleInfo();
		rule.setName(getPropValue(STYLENAME, properties, "default"));
		List<SymbolizerTypeInfo> symbolizerList = new ArrayList<SymbolizerTypeInfo>();
		SymbolizerTypeInfo symbolizer = null;

		switch (type) {
		case POINT:
		case MULTIPOINT:
			symbolizer = new PointSymbolizerInfo();
			GraphicInfo gi = new GraphicInfo();
			((PointSymbolizerInfo) symbolizer).setGraphic(gi);
			gi.setSize(createSize(properties));
			List<GraphicInfo.ChoiceInfo> list = new ArrayList<GraphicInfo.ChoiceInfo>();
			GraphicInfo.ChoiceInfo choiceInfoGraphic = new GraphicInfo.ChoiceInfo();
			list.add(choiceInfoGraphic);
			((PointSymbolizerInfo) symbolizer).getGraphic().setChoiceList(list);
			choiceInfoGraphic.setMark(createMark(properties));

			break;
		case LINESTRING:
		case MULTILINESTRING:
			symbolizer = new LineSymbolizerInfo();
			((LineSymbolizerInfo) symbolizer).setStroke(createStroke(properties));

			break;
		case POLYGON:
		case MULTIPOLYGON:
			symbolizer = new PolygonSymbolizerInfo();
			((PolygonSymbolizerInfo) symbolizer).setFill(createFill(properties));
			((PolygonSymbolizerInfo) symbolizer).setStroke(createStroke(properties));

			break;
		default:
			GWT.log("unsupported geometrytype");
		}

		symbolizerList.add(symbolizer);
		rule.setSymbolizerList(symbolizerList);

		// -- set a textsymbolizer
		if (properties.containsKey(LABELFEATURENAME)) {
			TextSymbolizerInfo tsi = new TextSymbolizerInfo();
			tsi.setLabel(new LabelInfo());
			PropertyNameInfo pni = new PropertyNameInfo();
			pni.setValue(properties.get(LABELFEATURENAME).toString());
			tsi.getLabel().getExpressionList().add(pni);
			tsi.getLabel().setValue("\n  ");
			symbolizerList.add(tsi);
		}

		return rule;
	}

	public static Map<String, Object> getProperties(ClientVectorLayerInfo cvli) {
		return getProperties(cvli.getNamedStyleInfo().getUserStyle());
	}

	/**
	 * This will extract the properties from the sld (first occurence / best effort (not every posibility is checked)).
	 * 
	 * @return map of properties
	 */
	public static Map<String, Object> getProperties(UserStyleInfo usi) {
		Map<String, Object> props = new HashMap<String, Object>();
		if (usi != null) {
			if (usi.getTitle() != null) {
				props.put(STYLENAME, usi.getTitle());
			}
			SymbolizerTypeInfo symbolizer = extractSymbolizer(usi);
			if (symbolizer != null) {
				if (symbolizer instanceof PointSymbolizerInfo) {
					extractProperties((PointSymbolizerInfo) symbolizer, props);
				} else if (symbolizer instanceof LineSymbolizerInfo) {
					extractProperties((LineSymbolizerInfo) symbolizer, props);
				} else if (symbolizer instanceof PolygonSymbolizerInfo) {
					extractProperties((PolygonSymbolizerInfo) symbolizer, props);
				}
			}
			TextSymbolizerInfo textSym = extractTextSymbolizer(usi);
			if (textSym != null && textSym.getLabel() != null && textSym.getLabel().getExpressionList().size() > 0) {
				if (textSym.getLabel().getExpressionList().get(0) instanceof PropertyNameInfo) {
					props.put(LABELFEATURENAME, textSym.getLabel().getExpressionList().get(0).getValue());
				}
			}
		}
		return props;
	}

	// ---------------------------------------------------------------

	public static String getPropValue(String propName, Map<String, Object> properties, String defaultValue) {
		if (properties.containsKey(propName)) {
			return (String) properties.get(propName);
		} else {
			return defaultValue;
		}
	}

	public static Float getPropValue(String propName, Map<String, Object> properties, Float defaultValue) {
		if (properties.containsKey(propName)) {
			return (Float) properties.get(propName);
		} else {
			return defaultValue;
		}
	}

	// ---------------------------------------------------------------

	private static void extractProperties(PointSymbolizerInfo psi, Map<String, Object> properties) {
		if (psi.getGraphic() != null && psi.getGraphic() != null) {
			if (psi.getGraphic().getSize() != null && psi.getGraphic().getSize().getValue() != null) {
				properties.put(SIZE, Float.valueOf(psi.getGraphic().getSize().getValue()));
			}
			if (psi.getGraphic().getChoiceList() != null && psi.getGraphic().getChoiceList().size() > 0) {
				ChoiceInfo ci = psi.getGraphic().getChoiceList().get(0);
				if (ci.getMark() != null) {
					if (ci.getMark().getStroke() != null && ci.getMark().getStroke().getCssParameterList() != null) {
						List<CssParameterInfo> plist = ci.getMark().getStroke().getCssParameterList();
						properties.put(STROKECOLOR, extractCssPropertyValue(STROKECOLOR, plist, DEFAULT_STROKECOLOR));
						properties.put(STROKEOPACITY,
								extractCssPropertyValue(STROKEOPACITY, plist, DEFAULT_STROKEOPACITY));
						properties.put(STROKEWIDTH, extractCssPropertyValue(STROKEWIDTH, plist, DEFAULT_STROKEWIDTH));
					}
					if (ci.getMark().getFill() != null && ci.getMark().getFill().getCssParameterList() != null) {
						List<CssParameterInfo> plist = ci.getMark().getFill().getCssParameterList();
						properties.put(FILLCOLOR, extractCssPropertyValue(FILLCOLOR, plist, DEFAULT_FILLCOLOR));
						properties.put(FILLOPACITY, extractCssPropertyValue(FILLOPACITY, plist, DEFAULT_FILLOPACITY));
					}
					if (ci.getMark().getWellKnownName() != null
							&& ci.getMark().getWellKnownName().getWellKnownName() != null) {
						properties.put(WELLKNOWNNAME, ci.getMark().getWellKnownName().getWellKnownName());
					}
				}
			}
		}
	}

	private static void extractProperties(LineSymbolizerInfo lsi, Map<String, Object> properties) {
		if (lsi.getStroke() != null && lsi.getStroke().getCssParameterList() != null) {
			List<CssParameterInfo> plist = lsi.getStroke().getCssParameterList();
			properties.put(STROKECOLOR, extractCssPropertyValue(STROKECOLOR, plist, DEFAULT_STROKECOLOR));
			properties.put(STROKEOPACITY, extractCssPropertyValue(STROKEOPACITY, plist, DEFAULT_STROKEOPACITY));
			properties.put(STROKEWIDTH, extractCssPropertyValue(STROKEWIDTH, plist, DEFAULT_STROKEWIDTH));
		}
	}

	private static void extractProperties(PolygonSymbolizerInfo psi, Map<String, Object> properties) {
		if (psi.getStroke() != null && psi.getStroke().getCssParameterList() != null) {
			List<CssParameterInfo> plist = psi.getStroke().getCssParameterList();
			properties.put(STROKECOLOR, extractCssPropertyValue(STROKECOLOR, plist, DEFAULT_STROKECOLOR));
			properties.put(STROKEOPACITY, extractCssPropertyValue(STROKEOPACITY, plist, DEFAULT_STROKEOPACITY));
			properties.put(STROKEWIDTH, extractCssPropertyValue(STROKEWIDTH, plist, DEFAULT_STROKEWIDTH));
		}
		if (psi.getFill() != null && psi.getFill().getCssParameterList() != null) {
			List<CssParameterInfo> plist = psi.getFill().getCssParameterList();
			properties.put(FILLCOLOR, extractCssPropertyValue(FILLCOLOR, plist, DEFAULT_FILLCOLOR));
			properties.put(FILLOPACITY, extractCssPropertyValue(FILLOPACITY, plist, DEFAULT_FILLOPACITY));
		}
	}

	private static Object extractCssPropertyValue(String key, List<CssParameterInfo> cssParameterList,
			Object defaultValue) {
		for (CssParameterInfo cpi : cssParameterList) {
			if (cpi.getName().equals(key)) {
				if (defaultValue == null) {
					return null;
				} else if (defaultValue instanceof Float) {
					return Float.valueOf(cpi.getValue());
				} else if (defaultValue instanceof String) {
					return cpi.getValue().toString();
				} else {
					return cpi.getValue();
				}
			}
		}
		return defaultValue;
	}

	private static SymbolizerTypeInfo extractSymbolizer(UserStyleInfo usi) {
		if (usi.getFeatureTypeStyleList() != null && usi.getFeatureTypeStyleList().size() > 0) {
			FeatureTypeStyleInfo ftsi = usi.getFeatureTypeStyleList().get(0);
			if (ftsi.getRuleList() != null && ftsi.getRuleList().size() > 0) {
				RuleInfo ri = ftsi.getRuleList().get(0);
				if (ri.getSymbolizerList() != null && ri.getSymbolizerList().size() > 0) {
					for (SymbolizerTypeInfo sti : ri.getSymbolizerList()) {
						if (!(sti instanceof TextSymbolizerInfo)) {
							return sti;
						}
					}
				}
			}
		}
		return null;
	}

	private static TextSymbolizerInfo extractTextSymbolizer(UserStyleInfo usi) {
		if (usi.getFeatureTypeStyleList() != null && usi.getFeatureTypeStyleList().size() > 0) {
			FeatureTypeStyleInfo ftsi = usi.getFeatureTypeStyleList().get(0);
			if (ftsi.getRuleList() != null && ftsi.getRuleList().size() > 0) {
				RuleInfo ri = ftsi.getRuleList().get(0);
				if (ri.getSymbolizerList() != null && ri.getSymbolizerList().size() > 0) {
					for (SymbolizerTypeInfo sti : ri.getSymbolizerList()) {
						if (sti instanceof TextSymbolizerInfo) {
							return (TextSymbolizerInfo) sti;
						}
					}
				}
			}
		}
		return null;
	}

	private static MarkInfo createMark(Map<String, Object> properties) {
		MarkInfo mi = new MarkInfo();
		mi.setWellKnownName(new WellKnownNameInfo());
		mi.getWellKnownName().setWellKnownName(getPropValue(WELLKNOWNNAME, properties, DEFAULT_WELLKNOWNNAME));
		mi.setFill(createFill(properties));
		mi.setStroke(createStroke(properties));
		return mi;
	}

	private static StrokeInfo createStroke(Map<String, Object> properties) {
		StrokeInfo s = new StrokeInfo();
		s.setStrokeColor(getPropValue(STROKECOLOR, properties, DEFAULT_STROKECOLOR));
		s.setStrokeWidth(getPropValue(STROKEWIDTH, properties, DEFAULT_STROKEWIDTH));
		s.setStrokeOpacity(getPropValue(STROKEOPACITY, properties, DEFAULT_STROKEOPACITY));
		return s;
	}

	private static FillInfo createFill(Map<String, Object> properties) {
		FillInfo f = new FillInfo();
		f.setFillColor(getPropValue(FILLCOLOR, properties, DEFAULT_FILLCOLOR));
		f.setFillOpacity(getPropValue(FILLOPACITY, properties, DEFAULT_FILLOPACITY));
		return f;
	}

	private static SizeInfo createSize(Map<String, Object> properties) {
		SizeInfo si = new SizeInfo();
		si.setValue(getPropValue(SIZE, properties, DEFAULT_SIZE));
		return si;
	}
}
