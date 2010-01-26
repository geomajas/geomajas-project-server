/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.internal.rendering.image;

import org.geomajas.configuration.StyleInfo;
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

	private Stroke stroke;

	// -------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------

	/**
	 * Initialize the LabelStyle using style info objects from the layer's XML
	 * label configuration.
	 *
	 * @param fontInfo
	 *            Determines the font's style.
	 * @param backgroundInfo
	 *            Determines the background style.
	 */
	public LabelStyle(StyleInfo fontInfo, StyleInfo backgroundInfo) {
		float opacity = 1;

		fontComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fontInfo.getFillOpacity());

		backgroundComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, backgroundInfo.getFillOpacity());

		strokeComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, backgroundInfo.getStrokeOpacity());

		if (fontInfo.getFillColor() != null) {
			fontColor = Style2dFactory.createColorFromHTMLCode(fontInfo.getFillColor());
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

	public Stroke getStroke() {
		return stroke;
	}

	public void setStroke(Stroke stroke) {
		this.stroke = stroke;
	}
}