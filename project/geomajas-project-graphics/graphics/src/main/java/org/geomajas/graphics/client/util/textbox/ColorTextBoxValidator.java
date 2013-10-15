/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.graphics.client.util.textbox;

import java.util.Arrays;



/**
 * Extention of the {@link TextBoxEditorDecorator<T>} for textboxes displaying a string.
 * 
 * @author Jan Venstermans
 */
public class ColorTextBoxValidator extends StringTextBoxValidator {
	
	private static final String HEX_FORM = "[#][0-9A-Fa-f]{6}";
	
	@Override
	public boolean isValid() {
		validate();
		evaluateErrorMessage("Color must have form #xxxxxx or be a standard value (e.g. red).");
		return super.isValid();
	}
	
	@Override
	protected void validate() {
		setValid(validateCssColor(getLabel()));
	}
	
	
	private boolean validateCssColor(String color) {
		// hexadecimal form
		if (color.matches(HEX_FORM)) {
			return true;
		} 
		
		// 17 standard colors; see http://www.w3schools.com/cssref/css_colornames.asp
		String[] standardColors = { "aqua", "black",
				"blue", "fuchsia", "gray", "green", "lime", "maroon", "navy", "olive",
				"orange", "purple", "red", "silver", "teal", "white", "yellow"};
		return Arrays.asList(standardColors).contains(color.toLowerCase()) ;
	}
	

}