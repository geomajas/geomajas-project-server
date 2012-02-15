package org.geomajas.sld.client.presenter;

import org.geomajas.sld.client.model.event.RuleSelectedEvent;
import org.geomajas.sld.client.model.event.RuleSelectedEvent.RuleSelectedHandler;

import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

public class RulePresenter extends Presenter<RulePresenter.MyView, RulePresenter.MyProxy>  implements RuleSelectedHandler{

	/**
	 * Use this in leaf presenters, inside their {@link #revealInParent} method.
	 */
	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_SYMBOL_CONTENT = new Type<RevealContentHandler<?>>();

	/**
	 * Use this in leaf presenters, inside their {@link #revealInParent} method.
	 */
	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_FILTER_CONTENT = new Type<RevealContentHandler<?>>();


	@Inject
	public RulePresenter(final EventBus eventBus, final MyView view, final MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	@ProxyStandard
	public interface MyProxy extends Proxy<RulePresenter> {
	}

	/**
	 * {@link RuleSelectorPresenter}'s view.
	 */
	public interface MyView extends View {

	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(RuleSelectedEvent.getType(), this);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, StyledLayerDescriptorLayoutPresenter.TYPE_RULE_CONTENT, this);
	}
	
	@ProxyEvent
	public void onRuleSelected(RuleSelectedEvent event) {
		forceReveal();
	}
	

}
