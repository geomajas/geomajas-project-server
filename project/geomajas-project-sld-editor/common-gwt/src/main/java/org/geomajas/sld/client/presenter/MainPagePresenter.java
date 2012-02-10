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

import com.google.web.bindery.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;

import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.LockInteractionEvent;
import com.gwtplatform.mvp.client.proxy.Proxy;
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
public class MainPagePresenter extends Presenter<MainPagePresenter.MyView, MainPagePresenter.MyProxy> {

	/**
	 * {@link MainPagePresenter}'s proxy.
	 */
	@ProxyStandard
	public interface MyProxy extends Proxy<MainPagePresenter> {
	}

	/**
	 * {@link MainPagePresenter}'s view.
	 */
	public interface MyView extends View {

		void showLoading(boolean visibile);
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

	@Inject
	public MainPagePresenter(final EventBus eventBus, final MyView view, final MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	@Override
	protected void revealInParent() {
		RevealRootContentEvent.fire(this, this);
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
