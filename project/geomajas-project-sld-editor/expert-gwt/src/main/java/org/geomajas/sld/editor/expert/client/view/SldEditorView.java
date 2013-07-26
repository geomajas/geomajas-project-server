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

import org.geomajas.sld.editor.expert.client.presenter.event.SldCloseEvent;
import org.geomajas.sld.editor.expert.client.presenter.event.SldCloseEvent.SldCloseHandler;
import org.geomajas.sld.editor.expert.client.presenter.event.SldSaveEvent;
import org.geomajas.sld.editor.expert.client.presenter.event.SldSaveEvent.SldSaveHandler;
import org.geomajas.sld.editor.expert.client.SldEditorWindow;
import org.geomajas.sld.editor.expert.client.model.SldModel;
import org.geomajas.sld.editor.expert.client.presenter.SldEditorExpertPresenter;
import org.geomajas.sld.editor.expert.client.presenter.event.SldCancelEvent;
import org.geomajas.sld.editor.expert.client.presenter.event.SldCancelEvent.SldCancelHandler;
import org.geomajas.sld.editor.expert.client.presenter.event.SldCancelledEvent;
import org.geomajas.sld.editor.expert.client.presenter.event.SldCancelledEvent.SldCancelledHandler;
import org.geomajas.sld.editor.expert.client.presenter.event.SldLoadedEvent;
import org.geomajas.sld.editor.expert.client.presenter.event.SldLoadedEvent.SldLoadedHandler;
import org.geomajas.sld.editor.expert.client.presenter.event.SldValidateEvent;
import org.geomajas.sld.editor.expert.client.presenter.event.SldValidateEvent.SldValidateHandler;
import org.geomajas.sld.editor.expert.client.presenter.event.SldValidatedEvent;
import org.geomajas.sld.editor.expert.client.presenter.event.SldValidatedEvent.SldValidatedHandler;
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
	
	@Inject
	public SldEditorView(EventBus eventBus) {
		super(eventBus);
		this.eventBus = eventBus;
	}

	// ---------------------------------------------------------------

	public Widget asWidget() {
		if (w == null) {
			w = new SldEditorWindow(eventBus, this);
		}
		return w;
	}

	public void setTemplates(SldModel model) {
		w.getEditor().setTemplates(model.getTemplateNames());
	}

	public void clearData() {
		w.getEditor().clearValues();
	}

	public void modelToView(SldModel model, boolean keepDirty) {
		if (w == null) { asWidget(); }
		w.getEditor().setData(model.getRawSld().getXml());
		w.getEditor().setDataDirty(keepDirty); // tricky ;-)
	}

	public void viewToModel(SldModel model) {
		model.getRawSld().setXml(w.getEditor().getData());
		model.setDirty(w.getEditor().isDataDirty());
	}

	public void selectTemplateCancelled() {
		w.getEditor().selectTemplateCancelled();
	}
	
	@Override
	public void hide() {
		w.hide();
		w.destroy();
		w = null;
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

	public HandlerRegistration addSldLoadedHandler(SldLoadedHandler handler) {
		return eventBus.addHandler(SldLoadedEvent.getType(), handler);
	}

	public HandlerRegistration addSldSaveHandler(SldSaveHandler handler) {
		return eventBus.addHandler(SldSaveEvent.getType(), handler);
	}

	public HandlerRegistration addSldCloseHandler(SldCloseHandler handler) {
		return eventBus.addHandler(SldCloseEvent.getType(), handler);
	}

	public HandlerRegistration addSldValidateHandler(SldValidateHandler handler) {
		return eventBus.addHandler(SldValidateEvent.getType(), handler);
	}

	public HandlerRegistration addSldCancelHandler(SldCancelHandler handler) {
		return eventBus.addHandler(SldCancelEvent.getType(), handler);
	}

	public HandlerRegistration addSldCancelledHandler(SldCancelledHandler handler) {
		return eventBus.addHandler(SldCancelledEvent.getType(), handler);
	}

	public HandlerRegistration addSldValidatedHandler(SldValidatedHandler handler) {
		return eventBus.addHandler(SldValidatedEvent.getType(), handler);
	}
}