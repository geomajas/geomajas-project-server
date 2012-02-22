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
package org.geomajas.sld.client.presenter;

import org.geomajas.sld.client.model.FilterModel;
import org.geomajas.sld.client.model.FilterModel.FilterModelState;
import org.geomajas.sld.client.model.RuleModel;
import org.geomajas.sld.client.model.event.RuleChangedEvent;
import org.geomajas.sld.client.model.event.RuleChangedEvent.RuleChangedHandler;
import org.geomajas.sld.client.model.event.RuleSelectedEvent;
import org.geomajas.sld.client.model.event.RuleSelectedEvent.RuleSelectedHandler;
import org.geomajas.sld.client.presenter.event.InitSldLayoutEvent;
import org.geomajas.sld.client.presenter.event.InitSldLayoutEvent.InitSldLayoutHandler;
import org.geomajas.sld.client.presenter.event.SldContentChangedEvent.HasSldContentChangedHandlers;
import org.geomajas.sld.client.view.ViewUtil;
import org.geomajas.sld.editor.client.GeometryType;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

/**
 * MVP Presenter class for {@link FilterModel}.
 * 
 * @author Jan De Moerloose
 * 
 */
public class FilterPresenter extends Presenter<FilterPresenter.MyView, FilterPresenter.MyProxy> implements
		RuleSelectedHandler, InitSldLayoutHandler, RuleChangedHandler {

	private final ViewUtil viewUtil;

	private FilterModel currentModel;

	@Inject
	public FilterPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final ViewUtil viewUtil) {
		super(eventBus, view, proxy);
		this.viewUtil = viewUtil;
	}

	/**
	 * {@link FilterPresenter}'s proxy.
	 */
	@ProxyStandard
	public interface MyProxy extends Proxy<FilterPresenter> {
	}

	/**
	 * {@link FilterPresenter}'s view.
	 */
	public interface MyView extends View, HasSldContentChangedHandlers {

		void modelToView(FilterModel filterModel);

		void clear();

		void hide();

	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(RuleSelectedEvent.getType(), this);
		addRegisteredHandler(RuleChangedEvent.getType(), this);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, RulePresenter.TYPE_FILTER_CONTENT, this);
	}

	@Override
	protected void onReveal() {
		super.onReveal();
	}

	public void onRuleSelected(RuleSelectedEvent event) {
		if (event.isClearAll()) {
			clearModelAndView();
		} else {
			setRule(event.getRuleModel(), true);
		}
	}

	public void onChanged(RuleChangedEvent event) {
		if (event.getRuleModel() == null) {
			clearModelAndView();
		} else {
			setRule(event.getRuleModel(), false);
		}
	}

	public void setRule(RuleModel rule, boolean warn) {
		if (rule.getGeometryType() != GeometryType.UNSPECIFIED) {
			if (rule.getFilterModel() != null) {
				currentModel = rule.getFilterModel();
				getView().modelToView(currentModel);
				if (currentModel.getState() == FilterModelState.UNSUPPORTED && warn) {
					viewUtil.showWarning(currentModel.getSupportedWarning());
				}
			} else {
				clearModelAndView();
			}
		} else {
			clearModelAndView();
		}

	}

	@ProxyEvent
	public void onInitSldLayout(InitSldLayoutEvent event) {
		forceReveal();
	}

	private void clearModelAndView() {
		currentModel = null;
		getView().clear();
		getView().hide();
	}

}
