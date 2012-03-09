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

import org.geomajas.sld.editor.common.client.model.SldGeneralInfo;
import org.geomajas.sld.editor.common.client.model.SldModel;
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
public class StyledLayerDescriptorPresenter
	extends Presenter<StyledLayerDescriptorPresenter.MyView, StyledLayerDescriptorPresenter.MyProxy> implements
		SldSelectedHandler, InitSldLayoutHandler {

	private SldModel model;

	/**
	 * {@link StyledLayerDescriptorPresenter}'s proxy.
	 */
	@ProxyStandard
	public interface MyProxy extends Proxy<StyledLayerDescriptorPresenter> {
	}

	/**
	 * {@link StyledLayerDescriptorPresenter}'s view.
	 */
	public interface MyView extends View, HasSldContentChangedHandlers {

		void copyToView(SldGeneralInfo myModel);

		void clear();

		void reset();

		void focus();

		void setError(String errorText);

	}

	/**
	 * Constructor.
	 * 
	 * @param eventBus
	 * @param view
	 * @param proxy
	 * @param viewUtil
	 * @param manager
	 */
	@Inject
	public StyledLayerDescriptorPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, StyledLayerDescriptorLayoutPresenter.TYPE_GENERAL_CONTENT, this);
	}

	@ProxyEvent
	public void onInitSldLayout(InitSldLayoutEvent event) {
		forceReveal();
	}

	/*
	 * (non-Javadoc) Refresh any information displayed by your presenter.
	 * 
	 * @see com.gwtplatform.mvp.client.PresenterWidget#onReset()
	 */
	@Override
	protected void onReset() {
		super.onReset();
		getView().reset();
	}

	/**
	 * Handler, called when change of selected SLD event is received.
	 * 
	 * @param event
	 */
	@ProxyEvent
	public void onSldSelected(SldSelectedEvent event) {
		model = event.getSld();

		if (null == model) {
			// No SLD selected, so empty the View
			getView().clear();
		} else {
			if (!model.isSupported()) {
				getView().setError(model.getSupportedWarning());
			} else {
				getView().copyToView(model);
				getView().focus();
			}
		}
		forceReveal(); //TODO: needed if no left side ???
	}

}
