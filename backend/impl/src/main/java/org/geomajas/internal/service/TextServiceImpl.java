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
	
	private static final String DEFAULT_FONT = "Verdana";
	private static final int DEFAULT_FONT_SIZE = 12;
	
	private static final String STYLE_BOLD = "bold";
	private static final String STYLE_ITALIC = "italic";

	/** {@inheritDoc} */
	public Rectangle2D getStringBounds(String text, FontStyleInfo fontStyle) {
		Font font = getFont(fontStyle);
		return font.getStringBounds(text, 0, text.length(), new FontRenderContext(new AffineTransform(), true, true));
	}

	/** {@inheritDoc} */
	public Font getFont(FontStyleInfo fontStyle) {
		int style = Font.PLAIN;
		String styleStr = fontStyle.getStyle();
		if (styleStr != null) {
			styleStr = styleStr.trim();
			if (STYLE_BOLD.equalsIgnoreCase(styleStr)) {
				style = Font.BOLD;
			} else if (STYLE_ITALIC.equalsIgnoreCase(styleStr)) {
				style = Font.ITALIC;
			}
		}
		String family = DEFAULT_FONT;
		if (fontStyle.getFamily() != null) {
			family = fontStyle.getFamily();
		}
		int size = DEFAULT_FONT_SIZE;
		if (fontStyle.getSize() != -1) {
			size = fontStyle.getSize();
		}
		return new Font(family, style, size);
	}

}
