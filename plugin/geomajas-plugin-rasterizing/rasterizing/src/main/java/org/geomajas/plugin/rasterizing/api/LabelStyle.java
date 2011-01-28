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
	 * Text font.
	 */
	private Font font;

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

	/**
	 * Stroke
	 */
	private Stroke stroke;

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

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
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