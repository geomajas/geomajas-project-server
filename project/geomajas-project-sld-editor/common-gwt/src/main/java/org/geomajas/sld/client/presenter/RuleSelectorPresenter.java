/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.sld.client.presenter;

import java.util.logging.Logger;

import org.geomajas.sld.client.model.RuleGroup;
import org.geomajas.sld.client.model.SldModel;
import org.geomajas.sld.client.model.event.RuleSelectedEvent.HasRuleSelectedHandlers;
import org.geomajas.sld.client.model.event.SldSelectedEvent;
import org.geomajas.sld.client.model.event.SldSelectedEvent.SldSelectedHandler;
import org.geomajas.sld.client.presenter.event.InitSldLayoutEvent;
import org.geomajas.sld.client.presenter.event.InitSldLayoutEvent.InitSldLayoutHandler;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

/**
 * @author An Buyle
 */

public class RuleSelectorPresenter extends Presenter<RuleSelectorPresenter.MyView, RuleSelectorPresenter.MyProxy>
		implements InitSldLayoutHandler, SldSelectedHandler {

	@Inject
	public RuleSelectorPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	private RuleGroup currentModel;

	private Logger logger = Logger.getLogger("RuleSelectorPresenter");

	/**
	 * {@link RuleSelectorPresenter}'s proxy.
	 */
	@ProxyStandard
	public interface MyProxy extends Proxy<RuleSelectorPresenter> {
	}

	/**
	 * {@link RuleSelectorPresenter}'s view.
	 */
	public interface MyView extends View, HasRuleSelectedHandlers {

		void copyToView(RuleGroup model);

		// TODO: void setViewChangedHandler(ViewChangedHandler);
		void reset();

		void focus();

		void setError(String errorText);
	}

	@Override
	protected void onBind() {
		super.onBind();
		// observe change of selected SLD (after it has been loaded)
		addRegisteredHandler(SldSelectedEvent.getType(), this);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, StyledLayerDescriptorLayoutPresenter.TYPE_RULES_CONTENT, this);
	}

	@ProxyEvent
	public void onInitSldLayout(InitSldLayoutEvent event) {
		forceReveal();

	}

	/**
	 * Handler, called when change of selected SLD event is received.
	 * 
	 * @param event
	 */
	// TODO
	@ProxyEvent
	public void onSldSelected(SldSelectedEvent event) {

		SldModel model = event.getSld();

		if (null == model) {
			// No SLD selected
			// TODO implement
			return;
		} else {
			currentModel = model.getRuleGroup();
		}
		getView().copyToView(currentModel);
	}

}
