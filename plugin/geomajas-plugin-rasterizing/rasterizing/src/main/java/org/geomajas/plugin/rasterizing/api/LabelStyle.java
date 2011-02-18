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

package org.geomajas.plugin.rasterizing.api;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Stroke;

import org.geomajas.global.Api;
import org.geotools.renderer.style.Style2D;

/**
 * <p>
 * Extension of the Style2D class from the GeoTools library specific for labels.
 * </p>
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api(allMethods = true)
public class LabelStyle extends Style2D {

	private Composite fontComposite;
	private Composite backgroundComposite;
	private Composite strokeComposite;
	private Font font;
	private Color fontColor;
	private Color backgroundColor;
	private Color strokeColor;
	private float strokeWidth;
	private Stroke stroke;

	/**
	 * Get font opacity.
	 *
	 * @return font opacity
	 */
	public Composite getFontComposite() {
		return fontComposite;
	}

	/**
	 * Set font opacity.
	 *
	 * @param fontComposite font opacity
	 */
	public void setFontComposite(Composite fontComposite) {
		this.fontComposite = fontComposite;
	}

	/**
	 * Get background rectangle's opacity.
	 *
	 * @return background rectangle's opacity.
	 */
	public Composite getBackgroundComposite() {
		return backgroundComposite;
	}

	/**
	 * Set background rectangle's opacity.
	 *
	 * @param backgroundComposite background rectangle's opacity.
	 */
	public void setBackgroundComposite(Composite backgroundComposite) {
		this.backgroundComposite = backgroundComposite;
	}

	/**
	 * Get background stroke opacity.
	 *
	 * @return background stroke opacity.
	 */
	public Composite getStrokeComposite() {
		return strokeComposite;
	}

	/**
	 * Set background stroke opacity.
	 *
	 * @param strokeComposite background stroke opacity.
	 */
	public void setStrokeComposite(Composite strokeComposite) {
		this.strokeComposite = strokeComposite;
	}

	/**
	 * Get font.
	 *
	 * @return font
	 */
	public Font getFont() {
		return font;
	}

	/**
	 * Set font.
	 *
	 * @param font font
	 */
	public void setFont(Font font) {
		this.font = font;
	}

	/**
	 * Get font colour.
	 *
	 * @return font colour
	 */
	public Color getFontColor() {
		return fontColor;
	}

	/**
	 * Set font colour.
	 *
	 * @param fontColor font colour
	 */
	public void setFontColor(Color fontColor) {
		this.fontColor = fontColor;
	}

	/**
	 * Get background colour.
	 *
	 * @return background colour
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * Set background colour.
	 *
	 * @param backgroundColor background colour
	 */
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	/**
	 * Get stroke colour.
	 *
	 * @return stroke colour
	 */
	public Color getStrokeColor() {
		return strokeColor;
	}

	/**
	 * Set stroke colour.
	 *
	 * @param strokeColor stroke colour
	 */
	public void setStrokeColor(Color strokeColor) {
		this.strokeColor = strokeColor;
	}

	/**
	 * Get stroke width.
	 *
	 * @return stroke width
	 */
	public float getStrokeWidth() {
		return strokeWidth;
	}

	/**
	 * Set stroke width.
	 *
	 * @param strokeWidth stroke width
	 */
	public void setStrokeWidth(float strokeWidth) {
		this.strokeWidth = strokeWidth;
	}

	/**
	 * Get stroke style.
	 *
	 * @return stroke style
	 */
	public Stroke getStroke() {
		return stroke;
	}

	/**
	 * Set stroke style.
	 *
	 * @param stroke stroke style
	 */
	public void setStroke(Stroke stroke) {
		this.stroke = stroke;
	}
}