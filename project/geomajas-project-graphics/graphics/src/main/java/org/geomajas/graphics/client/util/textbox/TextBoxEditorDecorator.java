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

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.HasEditorErrors;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.ui.client.adapters.ValueBoxEditor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;

/**
 * Validation decorator for a {@link ValueBoxEditor<T>}. Use this in your UIBinder templates when using validation.
 * 
 * @author Jan De Moerloose
 * @param <T> the type of value to be edited
 */
public class TextBoxEditorDecorator<T> extends Composite implements HasEditorErrors<String>,
		IsEditor<ValueBoxEditor<T>> {

	/**
	 * UIBinder for this decorator.
	 * 
	 * @author Emiel Ackermann
	 */
	interface TextBoxEditorDecoratorBinder extends UiBinder<Widget, TextBoxEditorDecorator<?>> {

		TextBoxEditorDecoratorBinder BINDER = GWT.create(TextBoxEditorDecoratorBinder.class);
	}

	@UiField
	protected SimplePanel contents;

	@UiField
	protected DivElement errorLabel;

	@UiField
	protected DivElement warningLabel;

	private ValueBoxEditor<T> editor;

	/**
	 * Constructs a ValueBoxEditorDecorator.
	 */
	@UiConstructor
	public TextBoxEditorDecorator() {
		initWidget(TextBoxEditorDecoratorBinder.BINDER.createAndBindUi(this));
	}

	/**
	 * Constructs a TextBoxEditorDecorator using a {@link TextBox} widget and a {@link ValueBoxEditor} editor.
	 * 
	 * @param widget the widget
	 * @param editor the editor
	 */
	public TextBoxEditorDecorator(ValueBoxBase<T> widget, ValueBoxEditor<T> editor) {
		this();
		contents.add(widget);
		this.editor = editor;
	}

	/**
	 * Returns the associated {@link ValueBoxEditor}.
	 * 
	 * @return a {@link ValueBoxEditor} instance
	 * @see #setEditor(ValueBoxEditor)
	 */
	public ValueBoxEditor<T> asEditor() {
		return editor;
	}

	/**
	 * Sets the associated {@link ValueBoxEditor}.
	 * 
	 * @param editor a {@link ValueBoxEditor} instance
	 * @see #asEditor()
	 */
	public void setEditor(ValueBoxEditor<T> editor) {
		this.editor = editor;
	}

	/**
	 * Set the widget that the EditorPanel will display. This method will automatically call {@link #setEditor}.
	 * 
	 * @param widget a {@link ValueBoxBase} widget
	 */
	@UiChild(limit = 1, tagname = "valuebox")
	public void setValueBox(ValueBoxBase<T> widget) {
		contents.add(widget);
		setEditor(widget.asEditor());
	}

	public void showError(String error) {
		errorLabel.setInnerText(error);
		errorLabel.getStyle().setDisplay(Display.INLINE_BLOCK);
	}

	public void resetError() {
		errorLabel.setInnerText(null);
		errorLabel.getStyle().setDisplay(Display.NONE);
	}

	public void showWarning(String warning) {
		warningLabel.setInnerText(warning);
		warningLabel.getStyle().setDisplay(Display.INLINE_BLOCK);
	}

	public void resetWarning() {
		warningLabel.setInnerText(null);
		warningLabel.getStyle().setDisplay(Display.NONE);
	}

	@Override
	public void showErrors(List<EditorError> errors) {
		// Not used
	}
}