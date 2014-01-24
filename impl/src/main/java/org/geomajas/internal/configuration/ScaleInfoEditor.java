/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.internal.configuration;

import java.beans.PropertyEditorSupport;

import org.geomajas.configuration.client.ScaleInfo;

/**
 * Custom bean editor for ScaleInfo class. Supports 1 : x and x : y notation.
 * 
 * @author Jan De Moerloose
 * @see ScaleInfo
 * 
 */
public class ScaleInfoEditor extends PropertyEditorSupport {

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		// Prepare the string; remove all spaces and all comma's:
		text = text.replaceAll(" ", "");
		text = text.replaceAll(",", "");

		int pos = text.indexOf(':');
		if (pos > 0) {
			try {
				double numerator = Double.parseDouble(text.substring(0, pos));
				double denominator = Double.parseDouble(text.substring(pos + 1));
				setValue(new ScaleInfo(numerator, denominator));
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Scale " + text
						+ " could not be parsed. The following format was expected:" + " (x : y).", e);
			}
		} else {
			try {
				// Not recommended....
				setValue(new ScaleInfo(Double.parseDouble(text)));
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Scale " + text
						+ " could not be parsed. The following format was expected:" + " (x : y).", e);
			}
		}
	}

}
