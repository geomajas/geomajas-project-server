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

import org.geomajas.sld.client.NameTokens;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

/**
 * Presenter for the current list of SLD's.
 * 
 * @author Jan De Moerloose
 * 
 */
public class StyledLayerDescriptorListPresenter
	extends Presenter<StyledLayerDescriptorListPresenter.MyView, StyledLayerDescriptorListPresenter.MyProxy> {

	/**
	 * {@link StyledLayerDescriptorPresenter}'s proxy.
	 */
	@ProxyCodeSplit
	@NameToken(NameTokens.HOME_PAGE)
	public interface MyProxy extends ProxyPlace<StyledLayerDescriptorListPresenter> {
	}

	/**
	 * {@link StyledLayerDescriptorPresenter}'s view.
	 */
	public interface MyView extends View {

	}

	@Inject
	public StyledLayerDescriptorListPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy) {
		super(eventBus, null, proxy);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SIDE_CONTENT, this);
	}
}
