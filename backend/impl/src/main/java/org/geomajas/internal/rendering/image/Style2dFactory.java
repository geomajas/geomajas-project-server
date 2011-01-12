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

package org.geomajas.internal.rendering.image;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.layer.LayerType;
import org.geotools.renderer.style.LineStyle2D;
import org.geotools.renderer.style.PolygonStyle2D;
import org.geotools.renderer.style.Style2D;
import org.geotools.renderer.style.TextStyle2D;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.util.StringTokenizer;

/**
 * <p>
 * Factory that creates <code>Style2D</code> objects from a configuration
 * <code>StyleInfo</code> object.
 * </p>
 *
 * @author Pieter De Graef
 */
public final class Style2dFactory {

	private Style2dFactory() {
	}

	/**
	 * Create a Style2D object, given an XML StyleInfo object and the layer
	 * type. Depending on the layer type, a <code>LineStyle2D</code> or
	 * <code>PolygonStyle2D</code> will be returned.
	 *
	 * @param styleInfo The style configuration object.
	 * @param layerType The layer type.
	 * @return Returns a GeoTools style object.
	 */
	public static Style2D createStyle(FeatureStyleInfo styleInfo, LayerType layerType) {
		if (layerType == LayerType.LINESTRING || layerType == LayerType.MULTILINESTRING) {
			return createLineStyle(styleInfo);
		} else if (layerType == LayerType.POLYGON || layerType == LayerType.MULTIPOLYGON) {
			return createPolygonStyle(styleInfo);
		} else if (layerType == LayerType.POINT || layerType == LayerType.MULTIPOINT) {
			return createPolygonStyle(styleInfo);
		}
		return null;
	}

	/**
	 * Create a TextStyle2D object that can be used in labeling.
	 *
	 * @param styleInfo
	 *            The style configuration object.
	 * @param label
	 *            The text to style.
	 * @param font
	 *            The font to use.
	 * @return Returns a GeoTools style object.
	 */
	public static TextStyle2D createStyle(FeatureStyleInfo styleInfo, String label, Font font) {
		TextStyle2D style = new TextStyle2D();
		float opacity = styleInfo.getFillOpacity();
		AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity);
		style.setComposite(composite);
		Color fillColor = createColorFromHTMLCode(styleInfo.getFillColor());
		style.setFill(fillColor);
		style.setFont(font);
		style.setLabel(label);

		return style;
	}

	/**
	 * Create a Color object from a HTML color code.
	 *
	 * @param htmlCode
	 *            The HTML color code. In the form of "#RRGGBB".
	 * @return Returns the correct color. In case anything went wrong, this
	 *         function will automatically return white.
	 */
	public static Color createColorFromHTMLCode(String htmlCode) {
		if (htmlCode == null) {
			return Color.WHITE;
		}
		try {
			String colourCode = htmlCode;
			if (colourCode.charAt(0) == '#') {
				colourCode = colourCode.substring(1);
			}
			int r = Integer.parseInt(colourCode.substring(0, 2), 16);
			int g = Integer.parseInt(colourCode.substring(2, 4), 16);
			int b = Integer.parseInt(colourCode.substring(4, 6), 16);

			return new Color(r, g, b);
		} catch (Exception e) {
			return Color.WHITE;
		}
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	private static LineStyle2D createLineStyle(FeatureStyleInfo styleInfo) {
		LineStyle2D style = new LineStyle2D();

		// Stroke color:
		Color c = createColorFromHTMLCode(styleInfo.getStrokeColor());
		style.setContour(c);

		// Stroke width and dash array:
		BasicStroke stroke;
		if (styleInfo.getDashArray() != null && styleInfo.getDashArray().length() > 0) {
			StringTokenizer tokenizer = new StringTokenizer(styleInfo.getDashArray(), ",");
			float[] dash = new float[tokenizer.countTokens()];
			int count = 0;
			while (tokenizer.hasMoreElements()) {
				String token = tokenizer.nextElement().toString().trim();
				try {
					float f = Float.parseFloat(token);
					dash[count++] = f;
				} catch (NumberFormatException e) {
					throw new IllegalStateException("Invalid dash array value " + token);
				}
			}
			stroke = new BasicStroke(styleInfo.getStrokeWidth(), BasicStroke.CAP_ROUND,
					BasicStroke.JOIN_ROUND, 1.0f, dash, 0);
		} else {
			stroke = new BasicStroke(styleInfo.getStrokeWidth());
		}
		style.setStroke(stroke);

		// Stroke opacity:
		float opacity = styleInfo.getStrokeOpacity();
		AlphaComposite strokeComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity);
		style.setContourComposite(strokeComposite);

		return style;
	}

	private static PolygonStyle2D createPolygonStyle(FeatureStyleInfo styleInfo) {
		PolygonStyle2D style = new PolygonStyle2D();

		// Stroke color:
		Color strokeColor = createColorFromHTMLCode(styleInfo.getStrokeColor());
		style.setContour(strokeColor);

		// Stroke width and dash array:
		BasicStroke stroke;
		if (styleInfo.getDashArray() != null && styleInfo.getDashArray().length() > 0) {
			StringTokenizer tokenizer = new StringTokenizer(styleInfo.getDashArray(), ",");
			float[] dash = new float[tokenizer.countTokens()];
			int count = 0;
			boolean errors = false;
			while (tokenizer.hasMoreElements()) {
				try {
					float f = Float.parseFloat(tokenizer.nextElement().toString());
					dash[count++] = f;
				} catch (Exception e) {
					errors = true;
					break;
				}
			}
			if (errors) {
				stroke = new BasicStroke(styleInfo.getStrokeWidth());
			} else {
				stroke = new BasicStroke(styleInfo.getStrokeWidth(), BasicStroke.CAP_ROUND,
						BasicStroke.JOIN_ROUND, 1.0f, dash, 0);
			}
		} else {
			stroke = new BasicStroke(styleInfo.getStrokeWidth());
		}
		style.setStroke(stroke);

		// Fill color:
		Color fillColor = createColorFromHTMLCode(styleInfo.getFillColor());
		style.setFill(fillColor);

		// Stroke opacity:
		AlphaComposite strokeComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				styleInfo.getStrokeOpacity());
		style.setContourComposite(strokeComposite);

		// Fill opacity:
		AlphaComposite fillComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, styleInfo.getFillOpacity());
		style.setFillComposite(fillComposite);

		return style;
	}
}