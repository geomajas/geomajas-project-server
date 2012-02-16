package org.geomajas.sld.client.presenter;

import java.util.logging.Logger;

import org.geomajas.sld.PolygonSymbolizerInfo;
import org.geomajas.sld.client.model.RuleModel;
import org.geomajas.sld.client.model.event.RuleSelectedEvent;
import org.geomajas.sld.client.model.event.RuleSelectedEvent.RuleSelectedHandler;
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

public class PolygonSymbolizerPresenter extends Presenter<PolygonSymbolizerPresenter.MyView, PolygonSymbolizerPresenter.MyProxy>  implements RuleSelectedHandler{

	private Logger logger = Logger.getLogger(PolygonSymbolizerPresenter.class.getName());

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
	}

	/**
	 * Constructor.
	 * 
	 * @param eventBus
	 * @param view
	 * @param proxy
	 */
	@Inject
	public PolygonSymbolizerPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(RuleSelectedEvent.getType(), this);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, RulePresenter.TYPE_SYMBOL_CONTENT, this);		
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

	@ProxyEvent
	public void onRuleSelected(RuleSelectedEvent event) {
		RuleModel rule = event.getRuleModel();
		if (rule.getGeometryType().equals(GeometryType.POLYGON)) {
			PolygonSymbolizerInfo polygonSymbolizerInfo = (PolygonSymbolizerInfo) rule.getSymbolizerTypeInfo();
			getView().modelToView(polygonSymbolizerInfo);
			forceReveal();
		}
	}

}
