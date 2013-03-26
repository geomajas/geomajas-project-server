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

import java.util.List;

import org.geomajas.sld.editor.common.client.view.ViewUtil;
import org.geomajas.sld.editor.expert.client.SldEditorWindow;
import org.geomajas.sld.editor.expert.client.domain.RawSld;
import org.geomajas.sld.editor.expert.client.domain.SldInfo;
import org.geomajas.sld.editor.expert.client.presenter.SldEditorExpertPresenter;
import org.geomajas.sld.editor.expert.client.presenter.event.TemplateLoadedEvent;
import org.geomajas.sld.editor.expert.client.presenter.event.TemplateLoadedEvent.TemplateLoadedHandler;
import org.geomajas.sld.editor.expert.client.presenter.event.TemplateNamesLoadedEvent;
import org.geomajas.sld.editor.expert.client.presenter.event.TemplateNamesLoadedEvent.TemplateNamesLoadedHandler;
import org.geomajas.sld.editor.expert.client.presenter.event.TemplateSelectEvent;
import org.geomajas.sld.editor.expert.client.presenter.event.TemplateSelectEvent.TemplateSelectHandler;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.gwtplatform.mvp.client.PopupViewImpl;

/**
 * View for SldEditorWindow (simple wrapper for decoupling).
 * 
 * @author Kristof Heirwegh
 */
public class SldEditorView extends PopupViewImpl implements SldEditorExpertPresenter.MyView {

	private SldEditorWindow w;
	private final EventBus eventBus;
	private final ViewUtil viewUtil;
	
	@Inject
	public SldEditorView(EventBus eventBus, ViewUtil viewUtil) {
		super(eventBus);
		this.viewUtil = viewUtil;
		this.eventBus = eventBus;
	}

	// ---------------------------------------------------------------

	public Widget asWidget() {
		if (w == null) {
			w = new SldEditorWindow(eventBus, viewUtil, this);
		}
		return w;
	}

	public void setTemplates(List<SldInfo> templates) {
		w.getEditor().setTemplates(templates);
	}

	public void setData(RawSld raw) {
		w.getEditor().setData(raw);
	}

	public void clear() {
		w.getEditor().clearValues();
	}

	// ---------------------------------------------------------------

	public void fireEvent(GwtEvent<?> event) {
		eventBus.fireEvent(event);
	}

	public HandlerRegistration addTemplateSelectHandler(TemplateSelectHandler handler) {
		return eventBus.addHandler(TemplateSelectEvent.getType(), handler);
	}

	public HandlerRegistration addTemplateNamesLoadedHandler(TemplateNamesLoadedHandler handler) {
		return eventBus.addHandler(TemplateNamesLoadedEvent.getType(), handler);
	}

	public HandlerRegistration addTemplateLoadedHandler(TemplateLoadedHandler handler) {
		return eventBus.addHandler(TemplateLoadedEvent.getType(), handler);
	}
}