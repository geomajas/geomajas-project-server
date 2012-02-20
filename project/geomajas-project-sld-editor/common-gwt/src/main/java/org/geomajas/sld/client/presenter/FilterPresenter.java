package org.geomajas.sld.client.presenter;

import org.geomajas.sld.client.model.FilterModel;
import org.geomajas.sld.client.model.FilterModel.FilterModelState;
import org.geomajas.sld.client.model.event.RuleSelectedEvent;
import org.geomajas.sld.client.model.event.RuleSelectedEvent.RuleSelectedHandler;
import org.geomajas.sld.client.presenter.event.InitSldLayoutEvent;
import org.geomajas.sld.client.presenter.event.InitSldLayoutEvent.InitSldLayoutHandler;
import org.geomajas.sld.client.presenter.event.SldContentChangedEvent.HasSldContentChangedHandlers;
import org.geomajas.sld.client.view.ViewUtil;
import org.geomajas.sld.editor.client.GeometryType;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

public class FilterPresenter extends Presenter<FilterPresenter.MyView, FilterPresenter.MyProxy> implements
		RuleSelectedHandler, InitSldLayoutHandler {

	private final ViewUtil viewUtil;
	
	private FilterModel currentModel;

	@Inject
	public FilterPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final ViewUtil viewUtil) {
		super(eventBus, view, proxy);
		this.viewUtil = viewUtil;
	}

	@ProxyStandard
	public interface MyProxy extends Proxy<FilterPresenter> {
	}

	/**
	 * {@link RuleSelectorPresenter}'s view.
	 */
	public interface MyView extends View, HasSldContentChangedHandlers {

		void modelToView(FilterModel filterModel);

		void clear();
		
		void hide();

	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(RuleSelectedEvent.getType(), this);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, RulePresenter.TYPE_FILTER_CONTENT, this);
	}

	@Override
	protected void onReveal() {
		super.onReveal();
	}

	public void onRuleSelected(RuleSelectedEvent event) {
		if (event.isClearAll()) {
			getView().clear();
		} else {
			if (event.getRuleModel().getGeometryType() != GeometryType.UNSPECIFIED) {
				if (event.getRuleModel().getFilterModel() != null) {
					currentModel = event.getRuleModel().getFilterModel();
					getView().modelToView(currentModel);
					if (currentModel.getState() == FilterModelState.UNSUPPORTED) {
						viewUtil.showWarning("Het filter voor deze regel wordt niet ondersteund en kan dus niet getoond worden.");
					}
				} else {
					getView().clear();
				}
			} else {
				getView().clear();
			}
		}
	}

	@ProxyEvent
	public void onInitSldLayout(InitSldLayoutEvent event) {
		forceReveal();
	}

}
