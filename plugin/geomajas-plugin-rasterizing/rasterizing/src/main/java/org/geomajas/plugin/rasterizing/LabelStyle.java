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

package org.geomajas.plugin.rasterizing;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.FontStyleInfo;
import org.geomajas.configuration.LabelStyleInfo;
import org.geotools.renderer.style.Style2D;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Stroke;

/**
 * <p>
 * Extension of the Style2D class from the GeoTools library specific for labels.
 * </p>
 *
 * @author Pieter De Graef
 */
public class LabelStyle extends Style2D {

	/**
	 * Font opacity.
	 */
	private Composite fontComposite;

	/**
	 * Background rectangle's opacity.
	 */
	private Composite backgroundComposite;

	/**
	 * Background stroke opacity.
	 */
	private Composite strokeComposite;

	/**
	 * Font color.
	 */
	private Color fontColor;

	/**
	 * Background color.
	 */
	private Color backgroundColor;

	/**
	 * Background stroke color.
	 */
	private Color strokeColor;

	/**
	 * Background stroke width.
	 */
	private float strokeWidth;

	private Stroke stroke;

	// -------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------

	public LabelStyle(LabelStyleInfo info) {
		this(info.getFontStyle(), info.getBackgroundStyle());
	}
	/**
	 * Initialize the LabelStyle using style info objects from the layer's XML label configuration.
	 *
	 * @param fontInfo
	 *            Determines the font's style.
	 * @param backgroundInfo
	 *            Determines the background style.
	 */
	public LabelStyle(FontStyleInfo fontInfo, FeatureStyleInfo backgroundInfo) {
		fontComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fontInfo.getOpacity());
		backgroundComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, backgroundInfo.getFillOpacity());
		strokeComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, backgroundInfo.getStrokeOpacity());

		if (fontInfo.getColor() != null) {
			fontColor = Style2dFactory.createColorFromHTMLCode(fontInfo.getColor());
		} else {
			fontColor = Color.BLACK;
		}

		if (backgroundInfo.getFillColor() != null) {
			backgroundColor = Style2dFactory.createColorFromHTMLCode(backgroundInfo.getFillColor());
		} else {
			backgroundColor = Color.WHITE;
		}

		if (backgroundInfo.getStrokeColor() != null) {
			strokeColor = Style2dFactory.createColorFromHTMLCode(backgroundInfo.getStrokeColor());
		} else {
			strokeColor = Color.WHITE;
		}

		stroke = new BasicStroke(backgroundInfo.getStrokeWidth());
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	public Composite getFontComposite() {
		return fontComposite;
	}

	public void setFontComposite(Composite fontComposite) {
		this.fontComposite = fontComposite;
	}

	public Composite getBackgroundComposite() {
		return backgroundComposite;
	}

	public void setBackgroundComposite(Composite backgroundComposite) {
		this.backgroundComposite = backgroundComposite;
	}

	public Composite getStrokeComposite() {
		return strokeComposite;
	}

	public void setStrokeComposite(Composite strokeComposite) {
		this.strokeComposite = strokeComposite;
	}

	public Color getFontColor() {
		return fontColor;
	}

	public void setFontColor(Color fontColor) {
		this.fontColor = fontColor;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public Color getStrokeColor() {
		return strokeColor;
	}

	public void setStrokeColor(Color strokeColor) {
		this.strokeColor = strokeColor;
	}

	public float getStrokeWidth() {
		return strokeWidth;
	}

	public void setStrokeWidth(float strokeWidth) {
		this.strokeWidth = strokeWidth;
	}

	public Stroke getStroke() {
		return stroke;
	}

	public void setStroke(Stroke stroke) {
		this.stroke = stroke;
	}
}