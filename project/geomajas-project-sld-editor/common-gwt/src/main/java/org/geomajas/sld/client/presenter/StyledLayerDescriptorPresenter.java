
package org.geomajas.sld.client.presenter;

import com.google.web.bindery.event.shared.EventBus;
import com.google.inject.Inject;

import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import org.geomajas.sld.client.NameTokens;

/**
 * @author An Buyle
 */

public class StyledLayerDescriptorPresenter extends
		Presenter<StyledLayerDescriptorPresenter.MyView, StyledLayerDescriptorPresenter.MyProxy> {

	/**
	 * {@link StyledLayerDescriptorPresenter}'s proxy.
	 */
	@ProxyCodeSplit
	@NameToken(NameTokens.homePage)
	public interface MyProxy extends ProxyPlace<StyledLayerDescriptorPresenter> {
	}

	/**
	 * {@link StyledLayerDescriptorPresenter}'s view.
	 */
	public interface MyView extends View {

		void copyToView();
		void copyToModel();
		//TODO: void setViewChangedHandler();
		void setError(String errorText);
	}

	@Inject
	public StyledLayerDescriptorPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetMainContent,
				this);
	}
}
