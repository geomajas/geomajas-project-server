/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.internal.service;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.FontStyleInfo;
import org.geomajas.configuration.LabelStyleInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.global.GeomajasException;
import org.geomajas.service.StyleConverterService;
import org.geomajas.sld.FeatureTypeStyleInfo;
import org.geomajas.sld.FillInfo;
import org.geomajas.sld.FontInfo;
import org.geomajas.sld.LineSymbolizerInfo;
import org.geomajas.sld.NamedLayerInfo;
import org.geomajas.sld.PointSymbolizerInfo;
import org.geomajas.sld.PolygonSymbolizerInfo;
import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.StrokeInfo;
import org.geomajas.sld.StyledLayerDescriptorInfo;
import org.geomajas.sld.SymbolizerTypeInfo;
import org.geomajas.sld.TextSymbolizerInfo;
import org.geomajas.sld.UserStyleInfo;
import org.geomajas.sld.expression.ExpressionInfo;
import org.geomajas.sld.filter.BinaryComparisonOpTypeInfo;
import org.geomajas.sld.filter.ComparisonOpsTypeInfo;
import org.geomajas.sld.filter.FilterTypeInfo;
import org.geomajas.sld.filter.PropertyIsEqualToInfo;
import org.geomajas.sld.filter.PropertyIsGreaterThanInfo;
import org.geomajas.sld.filter.PropertyIsGreaterThanOrEqualToInfo;
import org.geomajas.sld.filter.PropertyIsLessThanInfo;
import org.geomajas.sld.filter.PropertyIsLessThanOrEqualToInfo;
import org.geomajas.sld.filter.PropertyIsNotEqualToInfo;
import org.springframework.stereotype.Component;

/**
 * Default implementation of {@link StyleConverterService}. Supports named layers and user styles only.
 * 
 * @author Jan De Moerloose
 * 
 */
@Component
public class StyleConverterServiceImpl implements StyleConverterService {

	public List<NamedStyleInfo> extractLayerStyle(StyledLayerDescriptorInfo styledLayerDescriptorInfo, String layerName)
			throws GeomajasException {
		NamedLayerInfo namedLayerInfo = null;
		List<NamedStyleInfo> namedStyleInfos = new ArrayList<NamedStyleInfo>();
		// find first named layer or find by name
		for (StyledLayerDescriptorInfo.ChoiceInfo choice : styledLayerDescriptorInfo.getChoiceList()) {
			// we only support named layers
			if (choice.ifNamedLayer()) {
				if (layerName != null) {
					if (choice.getNamedLayer().getName().getName().equals(layerName)) {
						namedLayerInfo = choice.getNamedLayer();
						break;
					}
				} else {
					namedLayerInfo = choice.getNamedLayer();
					break;
				}
			}
		}
		for (NamedLayerInfo.ChoiceInfo choice : namedLayerInfo.getChoiceList()) {
			// we only support user styles
			if (choice.ifUserStyle()) {
				UserStyleInfo userStyle = choice.getUserStyle();
				NamedStyleInfo namedStyleInfo = new NamedStyleInfo();
				namedStyleInfo.setName(userStyle.getName().getName());
				LabelStyleInfo labelStyleInfo = new LabelStyleInfo();
				List<FeatureStyleInfo> featureStyleInfos = new ArrayList<FeatureStyleInfo>();
				for (FeatureTypeStyleInfo featureTypeStyleInfo : userStyle.getFeatureTypeStyleList()) {
					for (RuleInfo ruleInfo : featureTypeStyleInfo.getRuleList()) {
						FeatureStyleInfo featureStyleInfo = new FeatureStyleInfo();
						if (ruleInfo.getChoice().ifFilter()) {
							featureStyleInfo.setFormula(convertFormula(ruleInfo.getChoice().getFilter()));
						}
						for (SymbolizerTypeInfo symbolizerTypeInfo : ruleInfo.getSymbolizerList()) {
							if (symbolizerTypeInfo instanceof PointSymbolizerInfo) {
								PointSymbolizerInfo pointInfo = (PointSymbolizerInfo) symbolizerTypeInfo;
							} else if (symbolizerTypeInfo instanceof LineSymbolizerInfo) {
								LineSymbolizerInfo lineInfo = (LineSymbolizerInfo) symbolizerTypeInfo;
								convertStroke(featureStyleInfo, lineInfo.getStroke());
							} else if (symbolizerTypeInfo instanceof PolygonSymbolizerInfo) {
								PolygonSymbolizerInfo polygonInfo = (PolygonSymbolizerInfo) symbolizerTypeInfo;
								convertFill(featureStyleInfo, polygonInfo.getFill());
								convertStroke(featureStyleInfo, polygonInfo.getStroke());
							} else if (symbolizerTypeInfo instanceof TextSymbolizerInfo) {
								TextSymbolizerInfo textInfo = (TextSymbolizerInfo) symbolizerTypeInfo;
								labelStyleInfo.setFontStyle(convertFont(textInfo.getFont()));
								for (ExpressionInfo expr : textInfo.getLabel().getExpressionList()) {
									labelStyleInfo.setLabelAttributeName(expr.getValue());
								}
								FeatureStyleInfo background = new FeatureStyleInfo();
								convertFill(background, textInfo.getHalo().getFill());
								labelStyleInfo.setBackgroundStyle(background);
							}
						}
						featureStyleInfos.add(featureStyleInfo);
					}
				}
				namedStyleInfo.setFeatureStyles(featureStyleInfos);
				namedStyleInfo.setLabelStyle(labelStyleInfo);
				namedStyleInfos.add(namedStyleInfo);
			}
		}
		return namedStyleInfos;

	}

	private FontStyleInfo convertFont(FontInfo font) {
		// TODO Auto-generated method stub
		return null;
	}

	private void convertFill(FeatureStyleInfo featureStyleInfo, FillInfo fill) {
		// TODO Auto-generated method stub

	}

	private void convertStroke(FeatureStyleInfo featureStyleInfo, StrokeInfo stroke) {
		// TODO Auto-generated method stub

	}

	private String convertFormula(FilterTypeInfo filter) {
		if (filter.ifComparisonOps()) {
			ComparisonOpsTypeInfo coOps = filter.getComparisonOps();
			if (coOps instanceof BinaryComparisonOpTypeInfo) {
				BinaryComparisonOpTypeInfo binary = (BinaryComparisonOpTypeInfo) coOps;
				String propertyName = binary.getExpressionList().get(0).getValue();
				String propertyValue = binary.getExpressionList().get(1).getValue();
				if (binary instanceof PropertyIsEqualToInfo) {
					PropertyIsEqualToInfo eq = (PropertyIsEqualToInfo) coOps;
					return propertyName + " = " + propertyValue;
				} else if (binary instanceof PropertyIsGreaterThanInfo) {

				} else if (binary instanceof PropertyIsGreaterThanOrEqualToInfo) {

				} else if (binary instanceof PropertyIsLessThanInfo) {

				} else if (binary instanceof PropertyIsLessThanOrEqualToInfo) {

				} else if (binary instanceof PropertyIsNotEqualToInfo) {

				}
			}
		} else if (filter.ifFeatureIdList()) {

		} else if (filter.ifLogicOps()) {

		} else if (filter.ifSpatialOps()) {

		}
		return null;
	}

}
