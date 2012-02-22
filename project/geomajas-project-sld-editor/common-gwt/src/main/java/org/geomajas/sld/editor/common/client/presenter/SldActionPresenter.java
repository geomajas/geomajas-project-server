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

import org.geomajas.sld.editor.common.client.model.SldManager;
import org.geomajas.sld.editor.common.client.model.event.SldChangedEvent;
import org.geomajas.sld.editor.common.client.model.event.SldChangedEvent.SldChangedHandler;
import org.geomajas.sld.editor.common.client.model.event.SldSelectedEvent;
import org.geomajas.sld.editor.common.client.model.event.SldSelectedEvent.SldSelectedHandler;
import org.geomajas.sld.editor.common.client.presenter.event.InitSldLayoutEvent;
import org.geomajas.sld.editor.common.client.presenter.event.InitSldLayoutEvent.InitSldLayoutHandler;
import org.geomajas.sld.editor.common.client.presenter.event.SldCloseEvent;
import org.geomajas.sld.editor.common.client.presenter.event.SldCloseEvent.SldCloseHandler;
import org.geomajas.sld.editor.common.client.presenter.event.SldRefreshEvent;
import org.geomajas.sld.editor.common.client.presenter.event.SldRefreshEvent.SldRefreshHandler;
import org.geomajas.sld.editor.common.client.presenter.event.SldSaveEvent;
import org.geomajas.sld.editor.common.client.presenter.event.SldSaveEvent.HasSldSaveHandlers;
import org.geomajas.sld.editor.common.client.presenter.event.SldSaveEvent.SldSaveHandler;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

/**
 * MVP presenter class for main actions at SLD level (save/cancel/close).
 * 
 * @author Jan De Moerloose
 * 
 */
public class SldActionPresenter extends Presenter<SldActionPresenter.MyView, SldActionPresenter.MyProxy> implements
		SldSelectedHandler, SldChangedHandler, InitSldLayoutHandler, SldSaveHandler, SldRefreshHandler,
		SldCloseHandler {

	private final SldManager manager;

	@Inject
	public SldActionPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy,
			final SldManager manager) {
		super(eventBus, view, proxy);
		this.manager = manager;
	}

	/**
	 * {@link SldActionPresenter}'s proxy.
	 */
	@ProxyStandard
	public interface MyProxy extends Proxy<SldActionPresenter> {
	}

	/**
	 * {@link SldActionPresenter}'s view.
	 */
	public interface MyView extends View, HasSldSaveHandlers {

		void setCloseEnabled(boolean enabled);

		void setResetEnabled(boolean enabled);

		void setSaveEnabled(boolean enabled);

	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(SldSelectedEvent.getType(), this);
		addRegisteredHandler(SldChangedEvent.getType(), this);
		addRegisteredHandler(InitSldLayoutEvent.getType(), this);
		addRegisteredHandler(SldSaveEvent.getType(), this);
		addRegisteredHandler(SldCloseEvent.getType(), this);
		addRegisteredHandler(SldRefreshEvent.getType(), this);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, StyledLayerDescriptorLayoutPresenter.TYPE_ACTION_CONTENT, this);
	}

	@ProxyEvent
	public void onSldSelected(SldSelectedEvent event) {
		getView().setCloseEnabled(false);
		getView().setResetEnabled(false);
		getView().setSaveEnabled(false);
	}

	@ProxyEvent
	public void onInitSldLayout(InitSldLayoutEvent event) {
		forceReveal();

	}

	public void onChanged(SldChangedEvent event) {
		if (manager.getCurrentSld() != null) {
			getView().setCloseEnabled(true);
			getView().setResetEnabled(manager.getCurrentSld().isDirty());
			getView().setSaveEnabled(manager.getCurrentSld().isDirty() && manager.getCurrentSld().isComplete());
		} else {
			getView().setCloseEnabled(false);
			getView().setResetEnabled(false);
			getView().setSaveEnabled(false);
		}
	}

	public void onSldSave(SldSaveEvent event) {
		manager.saveCurrent();
	}

	public void onSldRefresh(SldRefreshEvent event) {
		manager.refreshCurrent();
	}

	public void onSldClose(SldCloseEvent event) {
		if (event.isSave()) {
			manager.saveAndDeselectAll();
		} else {
			manager.deselectAll();
		}
	}

}