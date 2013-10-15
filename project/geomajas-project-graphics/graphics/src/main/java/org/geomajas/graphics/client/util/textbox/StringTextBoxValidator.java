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

import com.google.gwt.editor.ui.client.adapters.ValueBoxEditor;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;



/**
 * Extention of the {@link TextBoxEditorDecorator<T>} for textboxes displaying a string.
 * 
 * @author Jan Venstermans
 */
public abstract class StringTextBoxValidator extends TextBoxEditorDecorator<String> {
	
	private TextBox textBox;
	
	protected boolean valid;
	
	public StringTextBoxValidator() {
		super();
		textBox = new TextBox();
		setValueBox(textBox);
	}

	public StringTextBoxValidator(ValueBoxBase<String> widget,
			ValueBoxEditor<String> editor) {
		super(widget, editor);
		// TODO Auto-generated constructor stub
	}

	public String getLabel() {
		return textBox.getText();
	}

	public void setLabel(String label) {
		textBox.setText(label);
		validate();
	}

	protected abstract void validate();

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
	protected void evaluateErrorMessage(String errorMessage) {
		if (valid) {
			resetError();
		} else {
			showError(errorMessage);
		}
	}

}