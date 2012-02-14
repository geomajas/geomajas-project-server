package org.geomajas.sld.client.presenter;

import org.geomajas.sld.client.presenter.event.SldContentChangedEvent.HasSldContentChangedHandlers;
import org.geomajas.sld.client.view.ViewUtil;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Proxy;


public class FilterPresenter  extends Presenter<FilterPresenter.MyView, FilterPresenter.MyProxy> {

	// private SldManager manager;
	private ViewUtil viewUtil;

	@Inject
	public FilterPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	@ProxyStandard
	public interface MyProxy extends Proxy<FilterPresenter> {
	}

	/**
	 * {@link RuleSelectorPresenter}'s view.
	 */
	public interface MyView extends View, HasSldContentChangedHandlers{

	}

	@Override
	protected void revealInParent() {
		// TODO Auto-generated method stub
		
	}

}
