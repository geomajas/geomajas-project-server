/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.sld.editor.common.client.presenter;

import org.geomajas.sld.editor.common.client.model.RuleModel;
import org.geomajas.sld.editor.common.client.model.SldManager;
import org.geomajas.sld.editor.common.client.model.event.RuleChangedEvent;
import org.geomajas.sld.editor.common.client.model.event.RuleSelectedEvent;
import org.geomajas.sld.editor.common.client.model.event.RuleSelectedEvent.RuleSelectedHandler;
import org.geomajas.sld.editor.common.client.model.event.SldChangedEvent;
import org.geomajas.sld.editor.common.client.model.event.SldChangedEvent.SldChangedHandler;
import org.geomajas.sld.editor.common.client.presenter.event.InitSldLayoutEvent;
import org.geomajas.sld.editor.common.client.presenter.event.InitSldLayoutEvent.InitSldLayoutHandler;

import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

/**
 * MVP Presenter class for {@link RuleModel}.
 * 
 * @author Jan De Moerloose
 * 
 */
public class RulePresenter extends Presenter<RulePresenter.MyView, RulePresenter.MyProxy> implements
		RuleSelectedHandler, InitSldLayoutHandler, SldChangedHandler {

	/**
	 * Use this in leaf presenters, inside their {@link #revealInParent} method.
	 */
	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_SYMBOL_CONTENT = new Type<RevealContentHandler<?>>();

	/**
	 * Use this in leaf presenters, inside their {@link #revealInParent} method.
	 */
	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_FILTER_CONTENT = new Type<RevealContentHandler<?>>();

	private final SldManager manager;

	@Inject
	public RulePresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final SldManager manager) {
		super(eventBus, view, proxy);
		this.manager = manager;
	}

	private RuleModel currentModel;

	/**
	 * {@link RulePresenter}'s proxy.
	 */
	@ProxyStandard
	public interface MyProxy extends Proxy<RulePresenter> {
	}

	/**
	 * {@link RulePresenter}'s view.
	 */
	public interface MyView extends View {

		void reset();
	}

	@Override
	protected void onBind() {
		super.onBind();
		//addRegisteredHandler(RuleSelectedEvent.getType(), this);
		addRegisteredHandler(SldChangedEvent.getType(), this);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, StyledLayerDescriptorLayoutPresenter.TYPE_RULE_CONTENT, this);
	}

	@ProxyEvent
	public void onRuleSelected(RuleSelectedEvent event) {
		if (event.isClearAll()) {
			getView().reset();
			currentModel = null;
		} else {
			currentModel = event.getRuleModel();
		}
	}

	@ProxyEvent
	public void onInitSldLayout(InitSldLayoutEvent event) {
		forceReveal();
	}

	public void onChanged(SldChangedEvent event) {
		// resets the model after changes of the SLD (may have completely changed !)
		currentModel = manager.getCurrentSld().getRuleGroup().findByReference(currentModel.getReference());
		// notify all rule-specific presenters
		RuleChangedEvent.fire(this, currentModel);
	}
}
