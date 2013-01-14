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

package org.geomajas.sld.editor.common.client.presenter;

import java.util.logging.Logger;

import org.geomajas.sld.editor.common.client.model.RuleGroup;
import org.geomajas.sld.editor.common.client.model.SldModel;
import org.geomajas.sld.editor.common.client.model.event.RuleSelectedEvent.HasRuleSelectedHandlers;
import org.geomajas.sld.editor.common.client.model.event.SldSelectedEvent;
import org.geomajas.sld.editor.common.client.model.event.SldSelectedEvent.SldSelectedHandler;
import org.geomajas.sld.editor.common.client.presenter.event.InitSldLayoutEvent;
import org.geomajas.sld.editor.common.client.presenter.event.InitSldLayoutEvent.InitSldLayoutHandler;
import org.geomajas.sld.editor.common.client.presenter.event.SldContentChangedEvent.HasSldContentChangedHandlers;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

/**
 * @author An Buyle
 */

public class RuleSelectorPresenter extends Presenter<RuleSelectorPresenter.MyView, RuleSelectorPresenter.MyProxy>
		implements InitSldLayoutHandler, SldSelectedHandler {

	@Inject
	public RuleSelectorPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	private RuleGroup currentModel;

	private Logger logger = Logger.getLogger("RuleSelectorPresenter");

	/**
	 * {@link RuleSelectorPresenter}'s proxy.
	 */
	@ProxyStandard
	public interface MyProxy extends Proxy<RuleSelectorPresenter> {
	}

	/**
	 * {@link RuleSelectorPresenter}'s view.
	 */
	public interface MyView extends View, HasRuleSelectedHandlers, HasSldContentChangedHandlers {

		void copyToView(RuleGroup model);

		void clear();

		void reset();

		void focus();

		void setError(String errorText);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, StyledLayerDescriptorLayoutPresenter.TYPE_RULES_CONTENT, this);
	}

	@ProxyEvent
	public void onInitSldLayout(InitSldLayoutEvent event) {
		forceReveal();
	}

	/**
	 * Handler, called when change of selected SLD event is received.
	 * 
	 * @param event
	 */
	
	@ProxyEvent
	public void onSldSelected(SldSelectedEvent event) {
		SldModel model = event.getSld();
		if (null == model) {
			clear();
		} else {
			if (model.isSupported()) {
				currentModel = model.getRuleGroup();
				getView().copyToView(currentModel);
			} else {
				currentModel = null;
				getView().setError(model.getSupportedWarning());
				clear();
			}
		}
		forceReveal(); //TODO: needed???
	}

	private void clear() {
		currentModel = null;
		getView().clear();
	}

}
