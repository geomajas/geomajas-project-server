package org.geomajas.sld.client.presenter;

import java.util.logging.Logger;

import org.geomajas.sld.PolygonSymbolizerInfo;
import org.geomajas.sld.client.model.RuleModel;
import org.geomajas.sld.client.model.SldManager;
import org.geomajas.sld.client.model.event.RuleChangedEvent;
import org.geomajas.sld.client.model.event.RuleChangedEvent.RuleChangedHandler;
import org.geomajas.sld.client.model.event.RuleSelectedEvent;
import org.geomajas.sld.client.model.event.RuleSelectedEvent.RuleSelectedHandler;
import org.geomajas.sld.client.presenter.event.InitSldLayoutEvent;
import org.geomajas.sld.client.presenter.event.InitSldLayoutEvent.InitSldLayoutHandler;
import org.geomajas.sld.client.presenter.event.SldContentChangedEvent.HasSldContentChangedHandlers;
import org.geomajas.sld.editor.client.GeometryType;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

public class PolygonSymbolizerPresenter
	extends Presenter<PolygonSymbolizerPresenter.MyView, PolygonSymbolizerPresenter.MyProxy> implements
		RuleSelectedHandler, InitSldLayoutHandler, RuleChangedHandler {

	private Logger logger = Logger.getLogger(PolygonSymbolizerPresenter.class.getName());
	
	private PolygonSymbolizerInfo currentModel;

	private SldManager manager;

	/**
	 * {@link PolygonSymbolizerPresenter}'s proxy.
	 */
	@ProxyStandard
	public interface MyProxy extends Proxy<PolygonSymbolizerPresenter> {
	}

	/**
	 * {@link StyledLayerDescriptorPresenter}'s view.
	 */
	public interface MyView extends View, HasSldContentChangedHandlers {

		void modelToView(PolygonSymbolizerInfo polygonSymbolizerInfo);

		void hide();

		void show();

		void clear();
	}

	/**
	 * Constructor.
	 * 
	 * @param eventBus
	 * @param view
	 * @param proxy
	 */
	@Inject
	public PolygonSymbolizerPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final SldManager manager) {
		super(eventBus, view, proxy);
		this.manager = manager;
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(RuleSelectedEvent.getType(), this);
		addRegisteredHandler(RuleChangedEvent.getType(), this);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, RulePresenter.TYPE_SYMBOL_CONTENT, this);
	}

	@Override
	protected void onReveal() {
		super.onReveal();
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

	public void onChanged(RuleChangedEvent event) {
		if(event.getRuleModel() == null) {
			clearModelAndView();
		} else {
			setRule(event.getRuleModel());
		}
	}
	public void onRuleSelected(RuleSelectedEvent event) {
		if (event.isClearAll()) {
			clearModelAndView();
		} else {
			setRule(event.getRuleModel());
		}
	}


	public void setRule(RuleModel rule) {
		if (rule.getGeometryType().equals(GeometryType.POLYGON)) {
			forceReveal();
			currentModel = (PolygonSymbolizerInfo) rule.getSymbolizerTypeInfo(); 
			getView().modelToView(currentModel);
			getView().show();
		} else {
			clearModelAndView();
		}

	}

	private void clearModelAndView() {
		currentModel = null;
		getView().clear();
		getView().hide();
	}

	@ProxyEvent
	public void onInitSldLayout(InitSldLayoutEvent event) {
		forceReveal();
	}

}
