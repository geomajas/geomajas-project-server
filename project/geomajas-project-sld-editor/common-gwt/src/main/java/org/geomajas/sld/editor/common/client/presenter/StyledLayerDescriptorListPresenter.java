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

import java.util.List;

import org.geomajas.sld.client.NameTokens;
import org.geomajas.sld.client.model.SldListChangedEvent;
import org.geomajas.sld.client.model.SldManager;
import org.geomajas.sld.client.model.event.SldAddedEvent;
import org.geomajas.sld.client.model.event.SldAddedEvent.SldAddedHandler;
import org.geomajas.sld.client.model.event.SldLoadedEvent;
import org.geomajas.sld.client.model.event.SldLoadedEvent.SldLoadedHandler;
import org.geomajas.sld.client.presenter.event.InitMainLayoutEvent;
import org.geomajas.sld.client.presenter.event.InitMainLayoutEvent.InitMainLayoutHandler;
import org.geomajas.sld.client.presenter.event.SldListPopupNewEvent;
import org.geomajas.sld.client.presenter.event.SldListPopupNewEvent.HasSldListPopupNewHandlers;
import org.geomajas.sld.client.presenter.event.SldListPopupNewEvent.SldListPopupNewHandler;
import org.geomajas.sld.client.presenter.event.SldListRemoveEvent;
import org.geomajas.sld.client.presenter.event.SldListRemoveEvent.HasSldListRemoveHandlers;
import org.geomajas.sld.client.presenter.event.SldListRemoveEvent.SldListRemoveHandler;
import org.geomajas.sld.client.presenter.event.SldListSelectEvent;
import org.geomajas.sld.client.presenter.event.SldListSelectEvent.HasSldListSelectHandlers;
import org.geomajas.sld.client.presenter.event.SldListSelectEvent.SldListSelectHandler;
import org.geomajas.sld.client.view.ViewUtil;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.client.proxy.RevealRootPopupContentEvent;

/**
 * Presenter for the current list of SLD's.
 * 
 * @author Jan De Moerloose
 * 
 */
public class StyledLayerDescriptorListPresenter
	extends Presenter<StyledLayerDescriptorListPresenter.MyView, StyledLayerDescriptorListPresenter.MyProxy> implements
		SldLoadedHandler, SldAddedHandler, InitMainLayoutHandler {

	/**
	 * {@linkStyledLayerDescriptorListPresenter}'s proxy.
	 */
	@ProxyStandard
	@NameToken(NameTokens.HOME_PAGE)
	public interface MyProxy extends ProxyPlace<StyledLayerDescriptorListPresenter> {
	}

	/**
	 * {@link StyledLayerDescriptorListPresenter}'s view.
	 */
	public interface MyView extends View, HasSldListPopupNewHandlers, HasSldListRemoveHandlers,
			HasSldListSelectHandlers {

		void setData(List<String> sldList);

	}

	private final CreateSldDialogPresenterWidget createDialog;

	private SldManager manager;

	private ViewUtil viewUtil;

	@Inject
	public StyledLayerDescriptorListPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy,
			final ViewUtil viewUtil, final SldManager manager, final CreateSldDialogPresenterWidget createDialog) {
		super(eventBus, view, proxy);
		this.manager = manager;
		this.createDialog = createDialog;
		this.viewUtil = viewUtil;
	}

	@Override
	protected void onBind() {
		super.onBind();
		registerHandler(getView().addSldListRemoveHandler(new SldListRemoveHandler() {

			public void onSldListRemove(SldListRemoveEvent event) {
				manager.removeCurrent();
			}
		}));
		registerHandler(getView().addSldListPopupNewHandler(new SldListPopupNewHandler() {

			public void onPopupNewList(SldListPopupNewEvent event) {
				showCreateDialog();
			}
		}));
		registerHandler(getView().addSldListSelectHandler(new SldListSelectHandler() {

			public void onSldListSelect(SldListSelectEvent event) {
				manager.select(event.getName());
			}
		}));
		addRegisteredHandler(SldLoadedEvent.getType(), this);
		addRegisteredHandler(SldAddedEvent.getType(), this);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainLayoutPresenter.TYPE_SIDE_CONTENT, this);
	}

	// Handler, called when SldListChangedEvent event is received
	public void onSldListChanged(SldListChangedEvent event) {
	}

	public void showCreateDialog() {
		RevealRootPopupContentEvent.fire(this, createDialog);
	}

	@ProxyEvent
	public void onInitMainLayout(InitMainLayoutEvent event) {
		forceReveal();
	}

	protected void onReveal() {
		super.onReveal();
	}

	public void onSldAdded(SldAddedEvent event) {
		getView().setData(manager.getCurrentNames());
		createDialog.getView().hide();
	}

	public void onSldLoaded(SldLoadedEvent event) {
		getView().setData(manager.getCurrentNames());
	}

}
