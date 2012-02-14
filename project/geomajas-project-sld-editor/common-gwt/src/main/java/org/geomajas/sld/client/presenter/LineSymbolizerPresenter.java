package org.geomajas.sld.client.presenter;

import java.util.logging.Logger;

import org.geomajas.sld.LineSymbolizerInfo;
import org.geomajas.sld.client.presenter.event.SldContentChangedEvent.HasSldContentChangedHandlers;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Proxy;

public class LineSymbolizerPresenter extends Presenter<LineSymbolizerPresenter.MyView, LineSymbolizerPresenter.MyProxy> {

	private Logger logger = Logger.getLogger(LineSymbolizerPresenter.class.getName());

	/**
	 * {@link LineSymbolizerPresenter}'s proxy.
	 */
	@ProxyStandard
	public interface MyProxy extends Proxy<LineSymbolizerPresenter> {
	}

	/**
	 * {@link StyledLayerDescriptorPresenter}'s view.
	 */
	public interface MyView extends View, HasSldContentChangedHandlers {

		void modelToView(LineSymbolizerInfo lineSymbolizerInfo);
	}

	/**
	 * Constructor.
	 * 
	 * @param eventBus
	 * @param view
	 * @param proxy
	 */
	@Inject
	public LineSymbolizerPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	@Override
	protected void onBind() {
		super.onBind();

	}

	@Override
	protected void revealInParent() {
		// RevealContentEvent.fire(this, StyledLayerDescriptorLayoutPresenter.TYPE_GENERAL_CONTENT, this);
	}

	/*
	 * (non-Javadoc) Refresh any information displayed by your presenter.
	 * 
	 * @see com.gwtplatform.mvp.client.PresenterWidget#onReset()
	 */
	@Override
	protected void onReset() {
		super.onReset();
	}

}
