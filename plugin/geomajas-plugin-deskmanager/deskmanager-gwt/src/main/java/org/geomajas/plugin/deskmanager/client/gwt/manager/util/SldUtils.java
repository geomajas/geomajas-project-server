package org.geomajas.plugin.deskmanager.client.gwt.manager.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.layer.LayerType;
import org.geomajas.sld.FeatureTypeStyleInfo;
import org.geomajas.sld.FillInfo;
import org.geomajas.sld.GraphicInfo;
import org.geomajas.sld.LineSymbolizerInfo;
import org.geomajas.sld.MarkInfo;
import org.geomajas.sld.PointSymbolizerInfo;
import org.geomajas.sld.PolygonSymbolizerInfo;
import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.SizeInfo;
import org.geomajas.sld.StrokeInfo;
import org.geomajas.sld.SymbolizerTypeInfo;
import org.geomajas.sld.UserStyleInfo;
import org.geomajas.sld.WellKnownNameInfo;

import com.google.gwt.core.client.GWT;

/**
 * Some sld utilities.
 * 
 * @author Kristof Heirwegh
 */
public class SldUtils {
	
	public static final String FILLCOLOR = "fillColor";
	public static final String FILLOPACITY = "fillOpacity";
	public static final String STROKECOLOR = "strokeColor";
	public static final String STROKEOPACITY = "strokeOpacity";
	public static final String STROKEWIDTH = "strokeWidth";
	public static final String SIZE = "size";
	public static final String WELLKNOWNNAME = "wellKnownName"; // MARK
	public static final String STYLENAME = "styleName"; // default = namedStyleInfo.getName();
	
	public static final String DEFAULT_FILLCOLOR = "#CCCCCC";
	public static final Float DEFAULT_FILLOPACITY = 0.5f;
	public static final String DEFAULT_STROKECOLOR = "#000000";
	public static final Float DEFAULT_STROKEOPACITY = 1f;
	public static final Float DEFAULT_STROKEWIDTH = 1f;
	public static final String DEFAULT_SIZE = "6"; // don't ask
	public static final String DEFAULT_WELLKNOWNNAME = "circle"; // MARK
	
	private SldUtils() {
	}

	public static UserStyleInfo createSimpleSldStyle(ClientVectorLayerInfo cvli, Map<String, Object> properties) {
		UserStyleInfo usi = new UserStyleInfo();
		usi.setTitle(getPropValue(STYLENAME, properties, cvli.getNamedStyleInfo().getName()));
		FeatureTypeStyleInfo fsi = new FeatureTypeStyleInfo();
		usi.getFeatureTypeStyleList().add(fsi);

		RuleInfo ri = createRule(cvli.getLayerType(), properties);
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
					
				} else if (symbolizer instanceof LineSymbolizerInfo) {
					
				} else if (symbolizer instanceof PolygonSymbolizerInfo) {
					
				}
			}
		}
		return props;
	}
	
	// ---------------------------------------------------------------

	private static SymbolizerTypeInfo extractSymbolizer(UserStyleInfo usi) {
		if (usi.getFeatureTypeStyleList() != null && usi.getFeatureTypeStyleList().size() > 0) {
			FeatureTypeStyleInfo ftsi = usi.getFeatureTypeStyleList().get(0);
			if (ftsi.getRuleList() != null && ftsi.getRuleList().size() > 0) {
				RuleInfo ri = ftsi.getRuleList().get(0);
				if (ri.getSymbolizerList() != null && ri.getSymbolizerList().size() > 0) {
					return ri.getSymbolizerList().get(0);
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
	
	// ---------------------------------------------------------------

	private static String getPropValue(String propName, Map<String, Object> properties, String defaultValue) {
		if (properties.containsKey(propName)) {
			return (String) properties.get(propName);
		} else {
			return defaultValue;
		}
	}

	private static Float getPropValue(String propName, Map<String, Object> properties, Float defaultValue) {
		if (properties.containsKey(propName)) {
			return (Float) properties.get(propName);
		} else {
			return defaultValue;
		}
	}
}
