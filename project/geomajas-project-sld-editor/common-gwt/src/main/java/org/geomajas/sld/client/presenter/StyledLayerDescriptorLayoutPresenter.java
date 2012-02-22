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

import org.geomajas.sld.client.model.SldManager;
import org.geomajas.sld.client.presenter.event.InitMainLayoutEvent;
import org.geomajas.sld.client.presenter.event.InitMainLayoutEvent.InitMainLayoutHandler;
import org.geomajas.sld.client.presenter.event.InitSldLayoutEvent;

import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.LockInteractionEvent;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

/**
 * This is the top-level presenter of the hierarchy. Other presenters reveal themselves within this presenter.
 * <p />
 * The goal of this sample is to show how to use nested presenters. These can be useful to decouple multiple presenters
 * that need to be displayed on the screen simultaneously.
 * 
 * @author Jan De Moerloose
 */
public class StyledLayerDescriptorLayoutPresenter
	extends Presenter<StyledLayerDescriptorLayoutPresenter.MyView, StyledLayerDescriptorLayoutPresenter.MyProxy>
		implements InitMainLayoutHandler {

	/**
	 * {@link StyledLayerDescriptorLayoutPresenter}'s proxy.
	 */
	@ProxyStandard
	public interface MyProxy extends Proxy<StyledLayerDescriptorLayoutPresenter> {
	}

	/**
	 * {@link StyledLayerDescriptorLayoutPresenter}'s view.
	 */
	public interface MyView extends View {

		boolean isLoaded();

		void showLoading(boolean visibile);
	}

	/**
	 * Use this in leaf presenters, inside their {@link #revealInParent} method.
	 */
	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_GENERAL_CONTENT = new Type<RevealContentHandler<?>>();

	/**
	 * Use this in leaf presenters, inside their {@link #revealInParent} method.
	 */
	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_RULES_CONTENT = new Type<RevealContentHandler<?>>();

	/**
	 * Use this in leaf presenters, inside their {@link #revealInParent} method.
	 */
	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_RULE_CONTENT = new Type<RevealContentHandler<?>>();

	/**
	 * Use this in leaf presenters, inside their {@link #revealInParent} method.
	 */
	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_ACTION_CONTENT = new Type<RevealContentHandler<?>>();

	/**
	 * Creates a {@link StyledLayerDescriptorLayoutPresenter} with the specified injected fields.
	 * 
	 * @param eventBus the bus
	 * @param view the view
	 * @param proxy the proxy
	 * @param manager the manager
	 */
	@Inject
	public StyledLayerDescriptorLayoutPresenter(EventBus eventBus, MyView view, MyProxy proxy, SldManager manager) {
		super(eventBus, view, proxy);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainLayoutPresenter.TYPE_MAIN_CONTENT, this);
	}

	protected void onReveal() {
		super.onReveal();
		if (!getView().isLoaded()) {
			InitSldLayoutEvent.fire(this);
		}
	}

	/**
	 * We display a short lock message whenever navigation is in progress.
	 * 
	 * @param event The {@link LockInteractionEvent}.
	 */
	@ProxyEvent
	public void onLockInteraction(LockInteractionEvent event) {
		getView().showLoading(event.shouldLock());
	}

	@ProxyEvent
	public void onInitMainLayout(InitMainLayoutEvent event) {
		forceReveal();
	}
}
