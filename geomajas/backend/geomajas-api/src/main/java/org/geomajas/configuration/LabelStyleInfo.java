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
package org.geomajas.configuration;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

/**
 * Information about how to access and how to render the label attribute.

 * @author Joachim Van der Auwera
 */
public class LabelStyleInfo implements Serializable {

	private static final long serialVersionUID = 151L;
	@NotNull
	private String labelAttributeName;
	private FeatureStyleInfo fontStyle;
	private FeatureStyleInfo backgroundStyle;

	/**
	 * Get label attribute name.
	 *
	 * @return label attribute name
	 */
	public String getLabelAttributeName() {
		return labelAttributeName;
	}

	/**
	 * Set label attribute name.
	 *
	 * @param labelAttributeName label attribute name
	 */
	public void setLabelAttributeName(String labelAttributeName) {
		this.labelAttributeName = labelAttributeName;
	}

	/**
	 * Get font style for label.
	 *
	 * @return font style
	 */
	public FeatureStyleInfo getFontStyle() {
		return fontStyle;
	}

	/**
	 * Set font style for label.
	 *
	 * @param fontStyle font style
	 */
	public void setFontStyle(FeatureStyleInfo fontStyle) {
		this.fontStyle = fontStyle;
	}

	/**
	 * Get the background style for the labels.
	 *
	 * @return background style for label
	 */
	public FeatureStyleInfo getBackgroundStyle() {
		return backgroundStyle;
	}

	/**
	 * Set background style for labels.
	 *
	 * @param backgroundStyle background style for label
	 */
	public void setBackgroundStyle(FeatureStyleInfo backgroundStyle) {
		this.backgroundStyle = backgroundStyle;
	}
}
