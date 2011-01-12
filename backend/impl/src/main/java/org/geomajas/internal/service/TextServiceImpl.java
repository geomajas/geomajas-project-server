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
