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
package org.geomajas.graphics.client.controller;

import org.geomajas.graphics.client.controller.PopupMenuControllerFactory.EditorAction.PopupMenuEditorDialogHandler;
import org.geomajas.graphics.client.editor.Editor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Dialog for editors spawned by popup menu.
 * 
 * @author Jan De Moerloose
 * 
 */
public class PopupMenuEditorDialog implements IsWidget {

	private static final Binder UIBINDER = GWT.create(Binder.class);

	/**
	 * UI binder.
	 * 
	 */
	interface Binder extends UiBinder<DialogBox, PopupMenuEditorDialog> {

	}

	private DialogBox dialog;

	@UiField
	protected SimplePanel editorPanel;
	
	@UiField
	protected Button undoButton;

	private Editor editor;
	
	private PopupMenuEditorDialogHandler handler;

	/**
	 * Creates a dialog for an editor.
	 * @param editor
	 */
	public PopupMenuEditorDialog(Editor editor) {
		dialog = UIBINDER.createAndBindUi(this);
		editorPanel.setWidget(editor);
		dialog.setText(editor.getLabel());
		undoButton.setEnabled(false);
		this.editor = editor;
	}
	
	public void addHandler(PopupMenuEditorDialogHandler handler) {
		this.handler = handler;
	}

	public void center() {
		dialog.center();
	}

	public void hide() {
		dialog.hide();
	}

	@UiHandler("okButton")
	public void confirm(ClickEvent event) {
		handler.onOk();
	}

	@UiHandler("applyButton")
	public void apply(ClickEvent event) {
		undoButton.setEnabled(true);
		handler.onApply();
	}
	
	@UiHandler("undoButton")
	public void undo(ClickEvent event) {
		undoButton.setEnabled(false);
		handler.onUndo();
	}
	
	@UiHandler("cancelButton")
	public void cancel(ClickEvent event) {
		handler.onCancel();
	}
	

	@Override
	public Widget asWidget() {
		return dialog;
	}

}
