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

import java.util.logging.Logger;

import org.geomajas.sld.LineSymbolizerInfo;
import org.geomajas.sld.editor.common.client.GeometryType;
import org.geomajas.sld.editor.common.client.model.RuleModel;
import org.geomajas.sld.editor.common.client.model.event.RuleChangedEvent;
import org.geomajas.sld.editor.common.client.model.event.RuleChangedEvent.RuleChangedHandler;
import org.geomajas.sld.editor.common.client.model.event.RuleSelectedEvent;
import org.geomajas.sld.editor.common.client.model.event.RuleSelectedEvent.RuleSelectedHandler;
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
 * MVP Presenter class for {@link LineSymbolizerInfo}.
 * 
 * @author Jan De Moerloose
 * 
 */
public class LineSymbolizerPresenter extends Presenter<LineSymbolizerPresenter.MyView, LineSymbolizerPresenter.MyProxy>
		implements RuleSelectedHandler, InitSldLayoutHandler, RuleChangedHandler {

	private Logger logger = Logger.getLogger(LineSymbolizerPresenter.class.getName());

	private LineSymbolizerInfo currentModel;

	/**
	 * {@link LineSymbolizerPresenter}'s proxy.
	 */
	@ProxyStandard
	public interface MyProxy extends Proxy<LineSymbolizerPresenter> {
	}

	/**
	 * {@link LineSymbolizerPresenter}'s view.
	 */
	public interface MyView extends View, HasSldContentChangedHandlers {

		void modelToView(LineSymbolizerInfo lineSymbolizerInfo);

		void hide();

		void show();

		void clear();
	}

	/**
	 * Constructor.
	 * 
	 * @param eventBus
	 * @param view
	 * @param proxy
	 */
	@Inject
	public LineSymbolizerPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	@Override
	protected void onBind() {
		super.onBind();
		//addRegisteredHandler(RuleSelectedEvent.getType(), this);
		addRegisteredHandler(RuleChangedEvent.getType(), this);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, RulePresenter.TYPE_SYMBOL_CONTENT, this);
	}

	@Override
	protected void onReveal() {
		super.onReveal();
	}

	/*
	 * (non-Javadoc) Refresh any information displayed by your presenter.
	 * 
	 * @see com.gwtplatform.mvp.client.PresenterWidget#onReset()
	 */
	@Override
	protected void onReset() {
		super.onReset();
	}

	public void onChanged(RuleChangedEvent event) {
		if (event.getRuleModel() == null) {
			clearModelAndView();
		} else {
			setRule(event.getRuleModel());
		}
	}
	
	@ProxyEvent
	public void onRuleSelected(RuleSelectedEvent event) {
		if (event.isClearAll()) {
			clearModelAndView();
		} else {
			setRule(event.getRuleModel());
		}
	}

	public void setRule(RuleModel rule) {
		if (rule.getGeometryType().equals(GeometryType.LINE)) {
			forceReveal();
			currentModel = (LineSymbolizerInfo) rule.getSymbolizerTypeInfo();
			getView().modelToView(currentModel);
			getView().show();
		} else {
			clearModelAndView();
		}

	}

	private void clearModelAndView() {
		currentModel = null;
		getView().clear();
		getView().hide();
	}

	@ProxyEvent
	public void onInitSldLayout(InitSldLayoutEvent event) {
		forceReveal();
	}

}
