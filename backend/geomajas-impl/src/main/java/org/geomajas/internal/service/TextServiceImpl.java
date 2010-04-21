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
package org.geomajas.internal.service;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import org.geomajas.configuration.FontStyleInfo;
import org.geomajas.service.TextService;
import org.springframework.stereotype.Component;

/**
 * AWT-based text service implementation. This should work in a headless server environment.
 * 
 * @author Jan De Moerloose
 * 
 */
@Component
public class TextServiceImpl implements TextService {

	public Rectangle2D getStringBounds(String text, FontStyleInfo fontStyle) {
		Font font = getFont(fontStyle);
		return font.getStringBounds(text, 0, text.length(), new FontRenderContext(new AffineTransform(), true, true));
	}

	private Font getFont(FontStyleInfo fontStyle) {
		int style = Font.PLAIN;
		String styleStr = fontStyle.getStyle();
		if (styleStr != null) {
			if (styleStr.trim().equalsIgnoreCase("normal")) {
				style = Font.PLAIN;
			} else if (styleStr.trim().equalsIgnoreCase("bold")) {
				style = Font.BOLD;
			} else if (styleStr.trim().equalsIgnoreCase("italic")) {
				style = Font.ITALIC;
			}
		}
		String family = "Verdana";
		if (fontStyle.getFamily() != null) {
			family = fontStyle.getFamily();
		}
		int size = 12;
		if (fontStyle.getSize() != -1) {
			size = fontStyle.getSize();
		}
		return new Font(family, style, size);
	}

}
