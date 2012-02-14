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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geomajas.sld.FeatureTypeStyleInfo;
import org.geomajas.sld.NamedLayerInfo;
import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.StyledLayerDescriptorInfo;
import org.geomajas.sld.UserStyleInfo;
import org.geomajas.sld.client.model.RuleData;
import org.geomajas.sld.client.model.RuleGroup;
import org.geomajas.sld.client.model.RuleModel;
import org.geomajas.sld.client.model.SldManager;
import org.geomajas.sld.editor.client.SldUtils;
import org.geomajas.sld.client.model.event.RuleSelectedEvent.HasRuleSelectedHandlers;
import org.geomajas.sld.client.model.event.RulesLoadedEvent;
import org.geomajas.sld.client.model.event.SldSelectedEvent;
import org.geomajas.sld.client.model.event.SldSelectedEvent.SldSelectedHandler;
import org.geomajas.sld.client.presenter.event.InitSldLayoutEvent;
import org.geomajas.sld.client.presenter.event.InitSldLayoutEvent.InitSldLayoutHandler;
import org.geomajas.sld.client.presenter.event.SldContentChangedEvent;
import org.geomajas.sld.client.view.ViewUtil;
import org.geomajas.sld.editor.client.GeometryType;
import org.geomajas.sld.editor.client.i18n.SldEditorMessages;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
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
		implements InitSldLayoutHandler,SldSelectedHandler  {

	private static SldEditorMessages sldEditorMessages = GWT.create(SldEditorMessages.class);

	private final String ruleTitleUnspecified = sldEditorMessages.ruleTitleUnspecified();

	private SldManager manager;
	private ViewUtil viewUtil;

	
	@Inject
	public RuleSelectorPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy,
			final ViewUtil viewUtil, final SldManager manager) {
		super(eventBus, view, proxy);
		this.manager = manager;
		this.viewUtil = viewUtil;
	}

	private GeometryType defaultGeomType = GeometryType.POINT;

	private MyModel myModel;

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

		void copyToView(MyModel myModel);

		void copyToModel(MyModel myModel);

		// TODO: void setViewChangedHandler(ViewChangedHandler);
		void reset();

		void focus();

		void setError(String errorText);

		HandlerRegistration addChangeHandler(SelectorChangeHandler changeHandler);
		// inform others that the rule that has been selected has changed (data = index
		// of selected rule)
	}

	/**
	 * Data Model for transferring data between rule selector/manager presenter and viewer.
	 * 
	 * @author An Buyle
	 */
	public class MyModel {

		// private List<FeatureTypeStyleInfo> styleList; // Note that only size == 1 is supported
		// for the moment!
		private List<RuleGroup> ruleGroupList;

		// Note actual rule data (type = RuleInfo, e.g. styleList.get(0).ruleList.get(i))
		// will not be read/updated here except that the order of the rules can change and rules
		// can be removed/inserted.

		private GeometryType geomType = GeometryType.POINT; // Default

		// TODO: Needed???
		// Integer indexRuleInFocus;

		public MyModel(GeometryType geomType) {
			this.geomType = geomType;
		}

		public GeometryType getGeomType() {
			return geomType;
		}

		public void setGeomType(GeometryType geomType) {
			this.geomType = geomType;
		}

		public List<RuleGroup> getRuleGroupList() {
			return ruleGroupList;
		}

		public void setRuleGroupList(List<RuleGroup> ruleGroupList) {
			this.ruleGroupList = ruleGroupList;
		}

		private void initFromSld(List<FeatureTypeStyleInfo> featureTypeStyleList) {
			if (null != ruleGroupList) {
				ruleGroupList.clear();
			}
			if (null == featureTypeStyleList || featureTypeStyleList.size() == 0) {
				// TODO: create list with 1 entry: default style
			}
			if (featureTypeStyleList.size() > 1) {
				// TODO Warning
				// SC.warn("Meer dan 1 groep van regels (&lt;FeatureTypeStyle&gt;) in deze SLD."
				// + "  Enkel de eerste wordt getoond.");
				// // Can be supported later via groups of rules in ruleSelector
				return; // ABORT!!
			}

			FeatureTypeStyleInfo featureTypeStyle = featureTypeStyleList.iterator().next(); // retrieve the first
																							// <FeatureTypeStyle> (which contains 1 list of rules)
			// element

			if (null == featureTypeStyle || featureTypeStyle.getRuleList().size() < 1) {
				// TODO : if no rules yet, add a default rule
			} 	else {
				if (null == ruleGroupList) {
					ruleGroupList = new ArrayList<RuleGroup>();
				}
				// We support only 1 rule group for the moment
				RuleGroup ruleGroup = new RuleGroup();

				String styleTitle = featureTypeStyle.getTitle();
				if (null == styleTitle) {
					styleTitle = "groep 1";
				}

				ruleGroup.setTitle(styleTitle);
				// TODO: parse featureTypeStyle name
				ruleGroup.setName("TODO");

				ruleGroup.setRuleModelList(new ArrayList<RuleModel>());
				for (RuleInfo rule : featureTypeStyle.getRuleList()) {
					RuleModel ruleModel = new RuleModel();
					
					//Determine the rule title to be used in the model, make sure it is NOT null or ""
					String title;

					if (null != rule.getTitle() && rule.getTitle().length() > 0) {
						title = rule.getTitle();
					} else if (null != rule.getName() && rule.getName().length() > 0) {
						title = rule.getName();
					} else {
						title = ruleTitleUnspecified;
					}
					ruleModel.setTitle(title);
					
					ruleModel.setName(rule.getName()); // The rule name can be null
					

					RuleData ruleData = new RuleData(SldUtils.GetGeometryType(rule));
					ruleData.setTypeOfRule(RuleData.TypeOfRule.COMPLETE_RULE);
					ruleData.setCompleteRuleBody(rule); // TODO: OK???

					ruleModel.setRuleData(ruleData);
					ruleGroup.getRuleModelList().add(ruleModel);
				}
				ruleGroupList.add(ruleGroup);
			} 
		}

	}

	public void setModel(MyModel model) {
		// TODO: validation

		List<RuleGroup> ruleGroupList = model.getRuleGroupList();
		if (null == ruleGroupList) {
			// TODO: error
			return; // ABORT
		}
		if (ruleGroupList.size() > 1) {
			logger.log(Level.WARNING, "Meer dan 1 groep van regels (&lt;FeatureTypeStyle&gt;) in deze SLD."
					+ "  Enkel de eerste wordt getoond.");
			// Can be supported later via groups of rules in ruleSelector
		}
		RuleGroup ruleGroup = ruleGroupList.iterator().next(); // retrieve the first
		// <FeatureTypeStyle> element

		if (ruleGroup.getRuleModelList().size() < 1) {
			// If rule model List() is NULL or empty, create default rule
			logger.log(Level.WARNING, "Een SLD zonder of met leeg stijlelement (&lt;FeatureTypeStyle&gt; element)"
					+ " wordt ingeladen.  Een standaard stijl voor een laag met geometrie-type '"
					+ getModel().getGeomType().value() + "' wordt toegevoegd.");

			// ruleGroup.setName("Nieuwe stijl"); // TODO, for the moment most rule groups don't have a name

			List<RuleModel> ruleList = new ArrayList<RuleModel>();
			// RuleInfo defaultRule = SldUtils.createDefaultRule(getModel().getGeomType());

			RuleModel defaultRuleModel = RuleModel.CreateDefaultRuleModel(getModel().getGeomType());

			ruleList.add(defaultRuleModel);

			ruleGroup.setRuleModelList(ruleList);

			modelHasChanged();
		}

		ruleGroupList.add(ruleGroup);

		if (null == this.myModel) {
			this.myModel = new MyModel(defaultGeomType);
		}

		this.myModel.setRuleGroupList(ruleGroupList);

	}

	private MyModel getModel() {
		return myModel;
	}

	private void modelHasChanged() {
		informParentOfChange();
		// TODO: inform parent presenter
	}

	@Override
	protected void onBind() {
		super.onBind();
//				registerHandler(getView().addSldContentChangedHandler(new SldContentChangedHandler() {
//			
//			public void onSldContentChanged(SldContentChangedEvent event) {
//				//TODO: manager.updateFeatureTypeList(event.getData())); 
//					//TODO: optimize: only update SLD when user hits save button
//				
//			}
//		}));		

		//observe change of selected SLD (after it has been loaded) 
		addRegisteredHandler(SldSelectedEvent.getType(), this);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, StyledLayerDescriptorLayoutPresenter.TYPE_RULES_CONTENT, this);
	}

	@Override
	protected void onReset() {
		super.onReset();

		if (null != getModel()) {
			getView().copyToView(getModel());
		} else {
			getView().reset();
		}
		getView().focus();
	}

	@ProxyEvent
	public void onInitSldLayout(InitSldLayoutEvent event) {
		forceReveal();

	}
	
	/**
	 * Handler, called when change of selected SLD  event is received.
	 * 
	 * @param event
	 */
	//TODO
	@ProxyEvent
	public void onSldSelected(SldSelectedEvent event) {

		StyledLayerDescriptorInfo sld = event.getSld();
		
		if (null == sld) {
			//No SLD selected
			// TODO implement
			return;
		}

		myModel = new MyModel(GeometryType.UNSPECIFIED);

		NamedLayerInfo namedLayerInfo = sld.getChoiceList().get(0).getNamedLayer();

		UserStyleInfo userStyle = namedLayerInfo.getChoiceList().get(0).getUserStyle();
		myModel.initFromSld(userStyle.getFeatureTypeStyleList());
		getView().copyToView(myModel);
		getView().focus();

	}

	private void informParentOfChange() {
		// TODO: inform parent presenter
		// TODO: code below is for testing only, it doesn't inform the parent !!!
		logger.log(Level.INFO, "SLD model data has changed. TODO: inform parent presenter");
	}

	/**
	 * Handler, called when change of the feature type style list (rules) is received. (This happens when a another SLD
	 * has been loaded)
	 * 
	 * @param event
	 */
	// TODO
	public void onRulesLoaded(RulesLoadedEvent event) {
		// First save current rules model
		// TODO
		// Then load model with rule data for the newly loaded SLD
		List<FeatureTypeStyleInfo> featureTypeStyleList = event.getFeatureTypeStyleList();

		if (null == featureTypeStyleList || featureTypeStyleList.size() == 0) {
			// //TODO: Warn dialogue
			return; // ABORT!
		}

		// TODO: Geometry Type can only be determined from the rule data (deeper level)

		// Retrieve the top-level info from SLD and update myModel accordingly

		myModel = new MyModel(GeometryType.UNSPECIFIED);
		myModel.initFromSld(featureTypeStyleList);

	}

	/**
		 * 
		 */
	private void updateModelToServer() {
		// First, we validate the input.
		getView().setError("");
		MyModel newModel = new MyModel(getModel().getGeomType());

		getView().copyToModel(newModel);

		// TODO validate new Model
		// if (!FieldVerifier.isValidName(textToServer)) {
		// getView().setError("Please enter at least four characters");
		// return;
		// }
		if (true) {
			setModel(newModel);
		}

		// TODO : send to parent Presenter
		// Then, we transmit it to the ResponsePresenter, which will do the server
		// call
		// placeManager.revealPlace(new PlaceRequest(ResponsePresenter.nameToken).with(
		// ResponsePresenter.textToServerParam, textToServer));

	}

}
