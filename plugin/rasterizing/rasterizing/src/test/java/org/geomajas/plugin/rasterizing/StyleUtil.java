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
package org.geomajas.plugin.rasterizing;

import java.util.List;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.SymbolInfo;
import org.geomajas.layer.LayerType;
import org.geomajas.sld.CssParameterInfo;
import org.geomajas.sld.ExternalGraphicInfo;
import org.geomajas.sld.FeatureTypeStyleInfo;
import org.geomajas.sld.FillInfo;
import org.geomajas.sld.FormatInfo;
import org.geomajas.sld.GraphicInfo;
import org.geomajas.sld.GraphicInfo.ChoiceInfo;
import org.geomajas.sld.LineSymbolizerInfo;
import org.geomajas.sld.MarkInfo;
import org.geomajas.sld.OnlineResourceInfo;
import org.geomajas.sld.PointSymbolizerInfo;
import org.geomajas.sld.PolygonSymbolizerInfo;
import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.SizeInfo;
import org.geomajas.sld.StrokeInfo;
import org.geomajas.sld.SymbolizerTypeInfo;
import org.geomajas.sld.UserStyleInfo;
import org.geomajas.sld.WellKnownNameInfo;
import org.geomajas.sld.xlink.SimpleLinkInfo.HrefInfo;
import org.springframework.util.StringUtils;

/**
 * Utility for creating styles.
 * 
 * @author Jan De Moerloose
 * 
 */
public final class StyleUtil {

	public static final String WKN_SQUARE = "square";

	public static final String WKN_CIRCLE = "circle";

	private StyleUtil() {
		// do not allow instantiation.
	}

	/**
	 * Create a style with a single rule.
	 * 
	 * @param rule
	 * @return the style
	 */
	public static UserStyleInfo createStyle(RuleInfo rule) {
		UserStyleInfo userStyleInfo = new UserStyleInfo();
		FeatureTypeStyleInfo fts = new FeatureTypeStyleInfo();
		fts.getRuleList().add(rule);
		userStyleInfo.getFeatureTypeStyleList().add(fts);
		return userStyleInfo;
	}

	public static RuleInfo createRule(LayerType type, FeatureStyleInfo featureStyle) {
		SymbolInfo symbol = featureStyle.getSymbol();
		RuleInfo rule = null;
		StrokeInfo stroke = StyleUtil.createStroke(featureStyle.getStrokeColor(), (int) featureStyle.getStrokeWidth(),
				featureStyle.getStrokeOpacity(), null);
		FillInfo fill = StyleUtil.createFill(featureStyle.getFillColor(), featureStyle.getFillOpacity());
		switch (type) {
			case GEOMETRY:
				break;
			case LINESTRING:
			case MULTILINESTRING:
				LineSymbolizerInfo line = StyleUtil.createLineSymbolizer(stroke);
				rule = StyleUtil.createRule(featureStyle.getName(), featureStyle.getName(), line);
				break;
			case MULTIPOINT:
			case POINT:
				GraphicInfo graphic = null;
				if (symbol.getCircle() != null) {
					MarkInfo circle = StyleUtil.createMark(WKN_CIRCLE, fill, stroke);
					graphic = StyleUtil.createGraphic(circle, (int) (2 * symbol.getCircle().getR()));
				} else if (symbol.getRect() != null) {
					MarkInfo rect = StyleUtil.createMark(WKN_SQUARE, fill, stroke);
					graphic = StyleUtil.createGraphic(rect, (int) symbol.getRect().getH());
				} else {
					ExternalGraphicInfo image = StyleUtil.createExternalGraphic(symbol.getImage().getHref());
					graphic = StyleUtil.createGraphic(image, symbol.getImage().getHeight());
				}
				PointSymbolizerInfo point = StyleUtil.createPointSymbolizer(graphic);
				rule = StyleUtil.createRule(featureStyle.getName(), featureStyle.getName(), point);
				break;
			case POLYGON:
			case MULTIPOLYGON:
			default:
				PolygonSymbolizerInfo polygon = StyleUtil.createPolygonSymbolizer(fill, stroke);
				rule = StyleUtil.createRule(featureStyle.getName(), featureStyle.getName(), polygon);
				break;
		}
		return rule;
	}

	/**
	 * Create a non-filtered rule with the specified title, name and symbolizer.
	 * 
	 * @param title the title
	 * @param name the name
	 * @param symbolizer the symbolizer
	 * @return the rule
	 */
	public static RuleInfo createRule(String title, String name, SymbolizerTypeInfo symbolizer) {
		RuleInfo rule = new RuleInfo();
		rule.setTitle(title);
		rule.setName(name);
		rule.getSymbolizerList().add(symbolizer);
		return rule;
	}

	/**
	 * Creates a point symbolizer with the specified graphic.
	 * 
	 * @param graphicInfo the graphic
	 * @return the symbolizer
	 */
	public static PointSymbolizerInfo createPointSymbolizer(GraphicInfo graphicInfo) {
		PointSymbolizerInfo symbolizerInfo = new PointSymbolizerInfo();
		symbolizerInfo.setGraphic(graphicInfo);
		return symbolizerInfo;
	}

	/**
	 * Creates a line symbolizer with the specified stroke.
	 * 
	 * @param strokeInfo the stroke
	 * @return the symbolizer
	 */
	public static LineSymbolizerInfo createLineSymbolizer(StrokeInfo strokeInfo) {
		LineSymbolizerInfo symbolizerInfo = new LineSymbolizerInfo();
		symbolizerInfo.setStroke(strokeInfo);
		return symbolizerInfo;
	}

	/**
	 * Creates a polygon symbolizer with the specified fill and stroke.
	 * 
	 * @param fillInfo the fill
	 * @param strokeInfo the stroke
	 * @return the symbolizer
	 */
	public static PolygonSymbolizerInfo createPolygonSymbolizer(FillInfo fillInfo, StrokeInfo strokeInfo) {
		PolygonSymbolizerInfo symbolizerInfo = new PolygonSymbolizerInfo();
		symbolizerInfo.setFill(fillInfo);
		symbolizerInfo.setStroke(strokeInfo);
		return symbolizerInfo;
	}

	/**
	 * Creates a default stroke.
	 * 
	 * @return the stroke
	 */
	public static StrokeInfo createStroke() {
		return createStroke("#000000", 1, 1, null);
	}

	/**
	 * Creates a stroke with the specified CSS parameters.
	 * 
	 * @param parameters the CSS parameters
	 * @return the stroke
	 */
	public static StrokeInfo createStroke(List<CssParameterInfo> parameters) {
		StrokeInfo strokeInfo = new StrokeInfo();
		for (CssParameterInfo param : parameters) {
			strokeInfo.getCssParameterList().add(new CssParameterInfo(param.getName(), param.getValue()));
		}
		return strokeInfo;
	}

	/**
	 * Creates a stroke with the specified CSS parameters.
	 * 
	 * @param color the color
	 * @param width the width
	 * @param opacity the opacity
	 * @param dashArray the dash array
	 * @return the stroke
	 */
	public static StrokeInfo createStroke(String color, int width, float opacity, String dashArray) {
		StrokeInfo strokeInfo = new StrokeInfo();
		if (color != null) {
			strokeInfo.getCssParameterList().add(createCssParameter("stroke", color));
		}
		strokeInfo.getCssParameterList().add(createCssParameter("stroke-width", width));
		if (dashArray != null) {
			strokeInfo.getCssParameterList().add(createCssParameter("stroke-dasharray", dashArray));
		}
		strokeInfo.getCssParameterList().add(createCssParameter("stroke-opacity", opacity));
		return strokeInfo;
	}

	/**
	 * Creates a default fill.
	 * 
	 * @return the fill
	 */
	public static FillInfo createFill() {
		return createFill("#ffffff", 0.5f);
	}

	/**
	 * Creates a fill with the specified CSS parameters.
	 * 
	 * @param parameters the CSS parameters
	 * @return the fill
	 */
	public static FillInfo createFill(List<CssParameterInfo> parameters) {
		FillInfo fillInfo = new FillInfo();
		for (CssParameterInfo param : parameters) {
			fillInfo.getCssParameterList().add(new CssParameterInfo(param.getName(), param.getValue()));
		}
		return fillInfo;
	}

	/**
	 * Creates a fill with the specified CSS parameters.
	 * 
	 * @param color the color
	 * @param opacity the opacity
	 * @return the fill
	 */
	public static FillInfo createFill(String color, float opacity) {
		FillInfo fillInfo = new FillInfo();
		if (color != null) {
			fillInfo.getCssParameterList().add(createCssParameter("fill", color));
		}
		fillInfo.getCssParameterList().add(createCssParameter("fill-opacity", opacity));
		return fillInfo;
	}

	/**
	 * Creates a default mark.
	 * 
	 * @return the mark
	 */
	public static MarkInfo createMark() {
		return createMark(WKN_CIRCLE, createFill(), createStroke());
	}

	/**
	 * Creates a mark with the specified parameters.
	 * 
	 * @param wellKnownName the well known name
	 * @param fill the fill
	 * @param stroke the stroke
	 * @return the mark
	 */
	public static MarkInfo createMark(String wellKnownName, FillInfo fill, StrokeInfo stroke) {
		MarkInfo mark = new MarkInfo();
		WellKnownNameInfo wellKnownNameInfo = new WellKnownNameInfo();
		wellKnownNameInfo.setWellKnownName(wellKnownName);
		mark.setWellKnownName(wellKnownNameInfo);
		mark.setFill(fill);
		mark.setStroke(stroke);
		return mark;
	}

	/**
	 * Creates an external graphic for the specified href.
	 * 
	 * @param href the href
	 * @return the graphic
	 */
	public static ExternalGraphicInfo createExternalGraphic(String href) {
		ExternalGraphicInfo externalGraphic = new ExternalGraphicInfo();
		FormatInfo format = new FormatInfo();
		format.setFormat("image/" + StringUtils.getFilenameExtension(href));
		externalGraphic.setFormat(format);
		OnlineResourceInfo onlineResource = new OnlineResourceInfo();
		onlineResource.setType("simple");
		HrefInfo hrefInfo = new HrefInfo();
		hrefInfo.setHref(href);
		onlineResource.setHref(hrefInfo);
		externalGraphic.setOnlineResource(onlineResource);
		return externalGraphic;
	}

	/**
	 * Creates a default graphic.
	 * 
	 * @return the graphic
	 */
	public static GraphicInfo createGraphic() {
		return createGraphic(createMark(), 20);
	}

	/**
	 * Creates a graphic with the specified mark and size.
	 * 
	 * @param mark the mark
	 * @param size the size
	 * @return the graphic
	 */
	public static GraphicInfo createGraphic(MarkInfo mark, int size) {
		GraphicInfo graphicInfo = new GraphicInfo();
		ChoiceInfo choice = new ChoiceInfo();
		choice.setMark(mark);
		graphicInfo.getChoiceList().add(choice);
		SizeInfo sizeInfo = new SizeInfo();
		sizeInfo.setValue(Integer.toString(size));
		graphicInfo.setSize(sizeInfo);
		return graphicInfo;
	}

	/**
	 * Creates a graphic with the specified external graphic and size.
	 * 
	 * @param graphic the graphic
	 * @param size the size
	 * @return the graphic
	 */
	public static GraphicInfo createGraphic(ExternalGraphicInfo graphic, int size) {
		GraphicInfo graphicInfo = new GraphicInfo();
		ChoiceInfo choice = new ChoiceInfo();
		choice.setExternalGraphic(graphic);
		graphicInfo.getChoiceList().add(choice);
		SizeInfo sizeInfo = new SizeInfo();
		sizeInfo.setValue(Integer.toString(size));
		graphicInfo.setSize(sizeInfo);
		return graphicInfo;
	}

	/**
	 * Creates a CSS parameter with specified name and value.
	 * 
	 * @param name the name
	 * @param value the value
	 * @return the parameter
	 */
	public static CssParameterInfo createCssParameter(String name, Object value) {
		CssParameterInfo css = new CssParameterInfo();
		css.setName(name);
		css.setValue(value.toString());
		return css;
	}

}
