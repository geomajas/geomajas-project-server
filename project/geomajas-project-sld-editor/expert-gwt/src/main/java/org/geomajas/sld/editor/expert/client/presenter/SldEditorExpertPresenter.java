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
package org.geomajas.sld.editor.expert.client.presenter;

import java.util.List;

import org.geomajas.sld.editor.expert.client.domain.RawSld;
import org.geomajas.sld.editor.expert.client.domain.SldInfo;
import org.geomajas.sld.editor.expert.client.model.SldManager;
import org.geomajas.sld.editor.expert.client.presenter.event.TemplateNamesLoadedEvent;
import org.geomajas.sld.editor.expert.client.presenter.event.TemplateNamesLoadedEvent.HasTemplateNamesLoadedHandlers;
import org.geomajas.sld.editor.expert.client.presenter.event.TemplateNamesLoadedEvent.TemplateNamesLoadedHandler;
import org.geomajas.sld.editor.expert.client.presenter.event.TemplateLoadedEvent;
import org.geomajas.sld.editor.expert.client.presenter.event.TemplateLoadedEvent.HasTemplateLoadedHandlers;
import org.geomajas.sld.editor.expert.client.presenter.event.TemplateLoadedEvent.TemplateLoadedHandler;
import org.geomajas.sld.editor.expert.client.presenter.event.TemplateSelectEvent;
import org.geomajas.sld.editor.expert.client.presenter.event.TemplateSelectEvent.HasTemplateSelectHandlers;
import org.geomajas.sld.editor.expert.client.presenter.event.TemplateSelectEvent.TemplateSelectHandler;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealRootContentEvent;
import com.smartgwt.client.util.SC;

/**
 * Presenter for Expert SldEditorWindow.
 * 
 * @author Kristof Heirwegh
 */
public class SldEditorExpertPresenter extends
		Presenter<SldEditorExpertPresenter.MyView, SldEditorExpertPresenter.MyProxy> {

	public static final String NAMETOKEN = "sld-editor-expert";

	private SldManager manager;

	/**
	 * {@link SldEditorExpertPresenter}'s view.
	 */
	public interface MyView extends View, HasTemplateSelectHandlers, HasTemplateNamesLoadedHandlers, HasTemplateLoadedHandlers { 
		void setTemplates(List<SldInfo> templates);
		void setData(RawSld raw);
		void clear();
	}

	/**
	 * {@link SldEditorExpertPresenter}'s proxy.
	 */
	@ProxyStandard
	@NameToken(NAMETOKEN)
	public interface MyProxy extends ProxyPlace<SldEditorExpertPresenter> {	}

	@Inject
	public SldEditorExpertPresenter(EventBus eventBus, MyView view,	MyProxy proxy, final SldManager manager) {
		super(eventBus, view, proxy);
		this.manager = manager;
	}

	protected void revealInParent() {
		RevealRootContentEvent.fire(this, this);
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		manager.fetchTemplateNames();
	}

	@Override
	protected void onBind() {
		super.onBind();
		registerHandler(getView().addTemplateLoadedHandler(new TemplateLoadedHandler() {
			public void onTemplateLoaded(TemplateLoadedEvent event) {
				getView().setData(manager.getTemplate());
			}
		}));
		
		registerHandler(getView().addTemplateNamesLoadedHandler(new TemplateNamesLoadedHandler() {
			public void onTemplateNamesLoaded(TemplateNamesLoadedEvent event) {
				getView().setTemplates(manager.getTemplateNames());
			}
		}));
		
		registerHandler(getView().addTemplateSelectHandler(new TemplateSelectHandler() {
			public void onTemplateSelect(TemplateSelectEvent event) {
				manager.fetchTemplate(event.getTemplateName());
// TODO check
//				if (manager.getCurrentSld() != null && manager.getCurrentSld().isDirty()) {
//					viewUtil.showYesNoMessage(messages.confirmSavingChangesBeforeUnloadingSld(), new YesNoCallback() {
//
//						public void onYes() {
//							manager.saveAndSelect(event.getName());
//						}
//
//						public void onNo() {
//							manager.select(event.getName());
//						}
//
//						public void onCancel() {
//						}
//					});
//				} else {
//					manager.select(event.getName());
//				}
			}
		}));
		
		
		// addRegisteredHandler(SldAddedEvent.getType(), this);
	}
}
