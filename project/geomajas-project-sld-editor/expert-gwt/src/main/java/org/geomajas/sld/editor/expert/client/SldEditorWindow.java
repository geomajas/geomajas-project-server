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

import org.geomajas.sld.editor.expert.client.i18n.SldEditorExpertMessages;
import org.geomajas.sld.editor.expert.client.presenter.SldEditorExpertPresenter;

import com.google.gwt.core.shared.GWT;
import com.google.web.bindery.event.shared.EventBus;
import com.smartgwt.client.widgets.Window;

/**
 * Wrapper for an SldEditorPanel, showing it as a window.
 * 
 * @author Kristof Heirwegh
 */
public class SldEditorWindow extends Window {

	private static final SldEditorExpertMessages EXP_MSG = GWT.create(SldEditorExpertMessages.class);

	private SldEditorPanel editor;
	
	public SldEditorWindow(EventBus eventBus, SldEditorExpertPresenter.MyView view) {
		setAutoSize(true);
		setTitle(EXP_MSG.windowTitle());
		setAutoCenter(true);
		setCanDragReposition(true);
		setCanDragResize(false);
		setShowMinimizeButton(false);
		setShowCloseButton(false);
		setIsModal(true);

		editor = new SldEditorPanel(eventBus, view);
		addItem(editor);
	}

	// ---------------------------------------------------------------

	public SldEditorPanel getEditor() {
		return editor;
	}

}
