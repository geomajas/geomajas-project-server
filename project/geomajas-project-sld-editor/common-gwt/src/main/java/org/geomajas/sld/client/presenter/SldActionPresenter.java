package org.geomajas.sld.client.presenter;

import org.geomajas.sld.client.model.event.SldSelectedEvent;
import org.geomajas.sld.client.model.event.SldSelectedEvent.SldSelectedHandler;
import org.geomajas.sld.client.presenter.event.InitSldLayoutEvent;
import org.geomajas.sld.client.presenter.event.InitSldLayoutEvent.InitSldLayoutHandler;
import org.geomajas.sld.client.presenter.event.SldContentChangedEvent;
import org.geomajas.sld.client.presenter.event.SldContentChangedEvent.SldContentChangedHandler;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

public class SldActionPresenter extends Presenter<SldActionPresenter.MyView, SldActionPresenter.MyProxy> implements
		SldSelectedHandler, SldContentChangedHandler, InitSldLayoutHandler {

	@Inject
	public SldActionPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	@ProxyStandard
	public interface MyProxy extends Proxy<SldActionPresenter> {
	}

	/**
	 * {@link RuleSelectorPresenter}'s view.
	 */
	public interface MyView extends View {

		void setActionsEnabled(boolean b);

	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(SldSelectedEvent.getType(), this);
		addRegisteredHandler(SldContentChangedEvent.getType(), this);
		addRegisteredHandler(InitSldLayoutEvent.getType(), this);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, StyledLayerDescriptorLayoutPresenter.TYPE_ACTION_CONTENT, this);
	}

	@ProxyEvent
	public void onSldSelected(SldSelectedEvent event) {
		getView().setActionsEnabled(false);
	}

	@ProxyEvent
	public void onInitSldLayout(InitSldLayoutEvent event) {
		forceReveal();

	}

	public void onSldGeneralInfoChanged(SldContentChangedEvent event) {
		getView().setActionsEnabled(true);
	}

	public void onFilterInfoChanged(SldContentChangedEvent event) {
		getView().setActionsEnabled(true);
	}

	public void onSymbolizerInfoChanged(SldContentChangedEvent event) {
		getView().setActionsEnabled(true);
	}

	public void onGraphicInfoChanged(SldContentChangedEvent event) {
		getView().setActionsEnabled(true);
	}

}