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
package org.geomajas.sld.editor.expert.client.view;

import org.geomajas.sld.editor.expert.client.SldEditorWindow;
import org.geomajas.sld.editor.expert.client.presenter.SldEditorExpertPresenter;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PopupViewImpl;

/**
 * View for SldEditorWindow (simple wrapper for decoupling).
 * 
 * @author Kristof Heirwegh
 */
public class SldEditorView extends PopupViewImpl implements SldEditorExpertPresenter.MyView {

	private SldEditorWindow w = new SldEditorWindow();
	
	@Inject
	public SldEditorView(EventBus eventBus) {
		super(eventBus);
	}

	@Override
	public Widget asWidget() {
		return w;
	}
}