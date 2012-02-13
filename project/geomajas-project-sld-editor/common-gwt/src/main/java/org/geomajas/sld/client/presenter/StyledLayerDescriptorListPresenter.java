/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.sld.client.presenter;

import java.util.List;

import org.geomajas.sld.client.NameTokens;
import org.geomajas.sld.client.model.SldListChangedEvent;
import org.geomajas.sld.client.model.SldListChangedEvent.SldListChangedHandler;
import org.geomajas.sld.client.model.SldManager;
import org.geomajas.sld.client.presenter.InitLayoutEvent.InitLayoutHandler;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;


/**
 * Presenter for the current list of SLD's.
 * 
 * @author Jan De Moerloose
 * 
 */
public class StyledLayerDescriptorListPresenter
	extends Presenter<StyledLayerDescriptorListPresenter.MyView, StyledLayerDescriptorListPresenter.MyProxy> implements
		SldListChangedHandler, InitLayoutHandler {

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
	public interface MyView extends View {

		void setData(List<String> sldList);

	}

	private SldManager manager;

	@Inject
	public StyledLayerDescriptorListPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy,
			final SldManager manager) {
		super(eventBus, view, proxy);
		this.manager = manager;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SIDE_CONTENT, this);
	}

	// Handler, called when SldListChangedEvent event is received 
	public void onSldListChanged(SldListChangedEvent event) {
		getView().setData(manager.getCurrentNames());
	}

	@ProxyEvent
	public void onInitLayout(InitLayoutEvent event) {
		forceReveal();
	}

	protected void onReveal() {
		super.onReveal();
		getView().setData(manager.getCurrentNames());
		addRegisteredHandler(SldListChangedEvent.getType(), this); /* observe SldListChangedEvent */
	}

	

}
