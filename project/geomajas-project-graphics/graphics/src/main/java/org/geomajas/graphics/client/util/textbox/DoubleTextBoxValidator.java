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

/**
 * Extention of the {@link StringTextBoxValidator} for textBoxes where the String content must be an integer.
 * 
 * @author Jan Venstermans
 */
public class DoubleTextBoxValidator extends StringTextBoxValidator {
	
	@Override
	public boolean isValid() {
		validate();
		evaluateErrorMessage("Value must be a double.");
		return super.isValid();
	}
	
	@Override
	protected void validate() {
		setValid(validateInteger(getLabel()));
	}
	
	private boolean validateInteger(String label) {
		try {
			Double.parseDouble(label);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public double getDouble() {
		validate();
		if (super.isValid()) {
			return Double.parseDouble(getLabel());
		} else {
			return 0;
		}
	}
}