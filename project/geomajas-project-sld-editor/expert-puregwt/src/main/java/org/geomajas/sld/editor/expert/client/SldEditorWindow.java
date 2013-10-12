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
package org.geomajas.sld.editor.expert.client;

import org.geomajas.gwt2.widget.client.dialog.CloseableDialogBox;
import org.geomajas.sld.editor.expert.client.i18n.SldEditorExpertMessages;
import org.geomajas.sld.editor.expert.client.presenter.SldEditorExpertPresenter;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

/**
 * Wrapper for an SldEditorPanel, showing it as a window.
 * 
 * @author Kristof Heirwegh
 */
public class SldEditorWindow implements IsWidget {

	private static final SldEditorExpertMessages MSG = GWT.create(SldEditorExpertMessages.class);

	private CloseableDialogBox dialog;
	private SldEditorPanel editor;
	
	public SldEditorWindow(EventBus eventBus, SldEditorExpertPresenter.MyView view) {
		editor = new SldEditorPanel(eventBus, view);
		int height = Window.getClientHeight() > 500 ? 500 : Math.round(Window.getClientHeight() * 0.8f);
		int width = Window.getClientWidth() > 800 ? 800 : Math.round(Window.getClientWidth() * 0.8f);
		editor.setSize(width + "px", height + "px");
		dialog = new CloseableDialogBox();
		dialog.setWidget(editor);
		dialog.setAnimationEnabled(true);
		dialog.setGlassEnabled(true);
		dialog.setModal(true);
		dialog.setHTML(MSG.windowTitle());
	}

	public void center() {
		dialog.center();
	}

	public void hide() {
		dialog.hide();
	}

	public void show() {
		dialog.show();
	}
	
	@Override
	public Widget asWidget() {
		return dialog;
	}
	
	public SldEditorPanel getEditor() {
		return editor;
	}
}
