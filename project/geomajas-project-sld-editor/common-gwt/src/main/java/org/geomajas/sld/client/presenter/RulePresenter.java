package org.geomajas.sld.client.presenter;

import org.geomajas.sld.client.model.RuleModel;
import org.geomajas.sld.client.model.SldManager;
import org.geomajas.sld.client.model.event.RuleChangedEvent;
import org.geomajas.sld.client.model.event.RuleSelectedEvent;
import org.geomajas.sld.client.model.event.RuleSelectedEvent.RuleSelectedHandler;
import org.geomajas.sld.client.model.event.SldChangedEvent;
import org.geomajas.sld.client.model.event.SldChangedEvent.SldChangedHandler;
import org.geomajas.sld.client.presenter.event.InitSldLayoutEvent;
import org.geomajas.sld.client.presenter.event.InitSldLayoutEvent.InitSldLayoutHandler;

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

public class RulePresenter extends Presenter<RulePresenter.MyView, RulePresenter.MyProxy> implements
		RuleSelectedHandler, InitSldLayoutHandler, SldChangedHandler {

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

	private final SldManager manager;

	@Inject
	public RulePresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final SldManager manager) {
		super(eventBus, view, proxy);
		this.manager = manager;
	}

	private RuleModel currentModel;

	@ProxyStandard
	public interface MyProxy extends Proxy<RulePresenter> {
	}

	/**
	 * {@link RuleSelectorPresenter}'s view.
	 */
	public interface MyView extends View {

		void reset();
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(RuleSelectedEvent.getType(), this);
		addRegisteredHandler(SldChangedEvent.getType(), this);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, StyledLayerDescriptorLayoutPresenter.TYPE_RULE_CONTENT, this);
	}

	public void onRuleSelected(RuleSelectedEvent event) {
		if (event.isClearAll()) {
			getView().reset();
			currentModel = null;
		} else {
			currentModel = event.getRuleModel();
		}
	}

	@ProxyEvent
	public void onInitSldLayout(InitSldLayoutEvent event) {
		forceReveal();
	}

	public void onChanged(SldChangedEvent event) {
		// resets the model after changes of the SLD (may have completely changed !)
		currentModel = manager.getCurrentSld().getRuleGroup().findByReference(currentModel.getReference());
		// notify all rule-specific presenters
		RuleChangedEvent.fire(this, currentModel);
	}
}
