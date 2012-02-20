package org.geomajas.sld.client.presenter;

import org.geomajas.sld.client.model.SldManager;
import org.geomajas.sld.client.model.event.SldChangedEvent;
import org.geomajas.sld.client.model.event.SldChangedEvent.SldChangedHandler;
import org.geomajas.sld.client.model.event.SldSelectedEvent;
import org.geomajas.sld.client.model.event.SldSelectedEvent.SldSelectedHandler;
import org.geomajas.sld.client.presenter.event.InitSldLayoutEvent;
import org.geomajas.sld.client.presenter.event.InitSldLayoutEvent.InitSldLayoutHandler;
import org.geomajas.sld.client.presenter.event.SldContentChangedEvent;
import org.geomajas.sld.client.presenter.event.SldSaveEvent;
import org.geomajas.sld.client.presenter.event.SldSaveEvent.HasSldSaveHandlers;
import org.geomajas.sld.client.presenter.event.SldSaveEvent.SldSaveHandler;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

public class SldActionPresenter extends Presenter<SldActionPresenter.MyView, SldActionPresenter.MyProxy> implements
		SldSelectedHandler, SldChangedHandler, InitSldLayoutHandler, SldSaveHandler {

	private final SldManager manager;

	@Inject
	public SldActionPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final SldManager manager) {
		super(eventBus, view, proxy);
		this.manager = manager;
	}

	@ProxyStandard
	public interface MyProxy extends Proxy<SldActionPresenter> {
	}

	/**
	 * {@link RuleSelectorPresenter}'s view.
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
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, StyledLayerDescriptorLayoutPresenter.TYPE_ACTION_CONTENT, this);
	}

	@ProxyEvent
	public void onSldSelected(SldSelectedEvent event) {
		getView().setCloseEnabled(true);
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
			getView().setCloseEnabled(true);
			getView().setResetEnabled(false);
			getView().setSaveEnabled(false);
		}
	}

	public void onSldSave(SldSaveEvent event) {
		manager.saveCurrent();
	}

}