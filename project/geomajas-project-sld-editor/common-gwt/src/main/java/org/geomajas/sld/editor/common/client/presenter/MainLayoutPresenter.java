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

import org.geomajas.sld.editor.common.client.NameTokens;
import org.geomajas.sld.editor.common.client.model.SldManager;
import org.geomajas.sld.editor.common.client.presenter.event.InitMainLayoutEvent;

import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.LockInteractionEvent;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.client.proxy.RevealRootContentEvent;


/**
 * This is the top-level presenter of the hierarchy. Other presenters reveal themselves within this presenter.
 * <p />
 * The goal of this sample is to show how to use nested presenters. These can be useful to decouple multiple presenters
 * that need to be displayed on the screen simultaneously.
 * 
 * @author Jan De Moerloose
 */
public class MainLayoutPresenter extends Presenter<MainLayoutPresenter.MyView, MainLayoutPresenter.MyProxy> {

	/**
	 * {@link MainLayoutPresenter}'s proxy.
	 */
	@ProxyStandard
	@NameToken(NameTokens.HOME_PAGE)
	public interface MyProxy extends ProxyPlace<MainLayoutPresenter> {
	}

	/**
	 * {@link MainLayoutPresenter}'s view.
	 */
	public interface MyView extends View {

		void showLoading(boolean visibile);

		boolean isLoaded();
	}

	/**
	 * Use this in leaf presenters, inside their {@link #revealInParent} method.
	 */
	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_MAIN_CONTENT = new Type<RevealContentHandler<?>>();

	/**
	 * Use this in leaf presenters, inside their {@link #revealInParent} method.
	 */
	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_SIDE_CONTENT = new Type<RevealContentHandler<?>>();

	/**
	 * Use this in leaf presenters, inside their {@link #revealInParent} method.
	 */
	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_TOP_CONTENT = new Type<RevealContentHandler<?>>();

	private SldManager manager;

	/**
	 * Creates a {@link MainLayoutPresenter} with the specified injected fields.
	 * 
	 * @param eventBus the bus
	 * @param view the view
	 * @param proxy the proxy
	 * @param manager the manager
	 */
	@Inject
	public MainLayoutPresenter(EventBus eventBus, MyView view, MyProxy proxy, SldManager manager) {
		super(eventBus, view, proxy);
		this.manager = manager;
	}

	@Override
	protected void revealInParent() {
		//RevealSldEditorRootContentEvent.fire(this/*source*/, this);
		RevealRootContentEvent.fire(this, this);
	}

	protected void onReveal() {
		super.onReveal();
		if (!getView().isLoaded()) {
			InitMainLayoutEvent.fire(this);
		}
		manager.fetchAll();
	}
	
	public void reveal() {
		if (!isVisible()) {
			onReveal();
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
}
