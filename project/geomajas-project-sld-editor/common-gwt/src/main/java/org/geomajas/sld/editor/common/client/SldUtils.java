/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.sld.editor.common.client;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.sld.FeatureTypeStyleInfo;
import org.geomajas.sld.GraphicInfo;
import org.geomajas.sld.LineSymbolizerInfo;
import org.geomajas.sld.PointSymbolizerInfo;
import org.geomajas.sld.PolygonSymbolizerInfo;
import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.SymbolizerTypeInfo;

import com.google.gwt.core.client.GWT;

/**
 * SLD utilities.
 * 
 * @author An Buyle
 * 
 */
public final class SldUtils {

	public static final String DEFAULT_STYLE_NAME = "Standaard stijl"; // TODO i18n

	private SldUtils() {
	}

	public static RuleInfo createDefaultRule(GeometryType geomType) {
		RuleInfo defaultRule = new RuleInfo();

		defaultRule.setTitle(DEFAULT_STYLE_NAME);

		List<SymbolizerTypeInfo> symbolizerList = new ArrayList<SymbolizerTypeInfo>();
		SymbolizerTypeInfo symbolizer = null;

		switch (geomType) {
			case POINT:
				symbolizer = new PointSymbolizerInfo();

				((PointSymbolizerInfo) symbolizer).setGraphic(new GraphicInfo());
				List<GraphicInfo.ChoiceInfo> list = new ArrayList<GraphicInfo.ChoiceInfo>();

				GraphicInfo.ChoiceInfo choiceInfoGraphic = new GraphicInfo.ChoiceInfo();
				list.add(choiceInfoGraphic);
				((PointSymbolizerInfo) symbolizer).getGraphic().setChoiceList(list);

				break;
			case LINE:
				symbolizer = new LineSymbolizerInfo();
				break;
			case POLYGON:
				symbolizer = new PolygonSymbolizerInfo();
				break;

			default:
				GWT.log("createDefaultRule: unsupported geometrie type " + geomType.toString()); // TODO
		}

		symbolizerList.add(symbolizer);

		defaultRule.setSymbolizerList(symbolizerList);
		return defaultRule;
	}

	public static GeometryType getGeometryType(RuleInfo rule) {
		GeometryType geomType = GeometryType.UNSPECIFIED;

		if (null == rule || null == rule.getSymbolizerList() || 0 == rule.getSymbolizerList().size()) {
			GWT.log("getGeometryType: rule must specify a <xxxSymbolizer> element to retrieve the geometry type.");
			return GeometryType.UNSPECIFIED;
		}
		Object symbolizerInfo = rule.getSymbolizerList().get(0); // retrieve the first symbolizer specification

		if (symbolizerInfo.getClass().equals(PointSymbolizerInfo.class)) {
			geomType = GeometryType.POINT;
		} else if (symbolizerInfo.getClass().equals(LineSymbolizerInfo.class)) {
			geomType = GeometryType.LINE;
		} else if (symbolizerInfo.getClass().equals(PolygonSymbolizerInfo.class)) {
			geomType = GeometryType.POLYGON;
		}
		return geomType;

	}

	public static GeometryType getGeometryType(List<FeatureTypeStyleInfo> featureTypeStyleList) {

		GeometryType geometryType = GeometryType.UNSPECIFIED;

		if (null == featureTypeStyleList || featureTypeStyleList.size() == 0) {
			return GeometryType.UNSPECIFIED; // ABORT
		}

		FeatureTypeStyleInfo featureTypeStyle = featureTypeStyleList.iterator().next(); // retrieve the first
		// <FeatureTypeStyle> element (which contains 1 list of rules)

		if (null != featureTypeStyle && featureTypeStyle.getRuleList().size() >= 1) {
			RuleInfo rule = featureTypeStyle.getRuleList().iterator().next(); // retrieve the first rule
			geometryType = getGeometryType(rule);
		}
		return geometryType;
	}
}
