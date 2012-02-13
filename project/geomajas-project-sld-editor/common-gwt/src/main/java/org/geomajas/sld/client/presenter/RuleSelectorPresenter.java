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

import org.geomajas.sld.client.model.RuleData;
import org.geomajas.sld.client.model.RuleGroup;
import org.geomajas.sld.client.model.RuleModel;
import org.geomajas.sld.client.presenter.event.InitSldLayoutEvent;
import org.geomajas.sld.client.presenter.event.InitSldLayoutEvent.InitSldLayoutHandler;
import org.geomajas.sld.editor.client.GeometryTypes;

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


public class RuleSelectorPresenter extends
			Presenter<RuleSelectorPresenter.MyView, RuleSelectorPresenter.MyProxy> implements InitSldLayoutHandler {
		
		private GeometryTypes defaultGeomType = GeometryTypes.POINT;	
		private MyModel myModel;
		private Logger logger = Logger.getLogger("SldEditor");
		
		
		/**
		 * {@link RuleSelectorPresenter}'s proxy.
		 */
		@ProxyStandard
		public interface MyProxy extends Proxy<RuleSelectorPresenter> {
		}


		
		/**
		 * {@link RuleSelectorPresenter}'s view.
		 */
		public interface MyView extends View {

		  	void copyToView(MyModel myModel);
			void copyToModel(MyModel myModel);
			//TODO: void setViewChangedHandler(ViewChangedHandler);
			void reset();
			void focus();
			void setError(String errorText);

			HandlerRegistration addChangeHandler(SelectorChangeHandler changeHandler);
			// inform others that the rule that has been selected has changed (data = index
			// of selected rule)
		}
		
		/**
		 * Data Model for transferring data between rule selector/manager presenter and 
		 * viewer. 
		 *
		 * @author An Buyle
 		 */
		public class MyModel {

			
			// private List<FeatureTypeStyleInfo> styleList; 	// Note that only size == 1 is supported 
													// for the moment!
			private List<RuleGroup> ruleGroupList;
			// Note actual rule data (type = RuleInfo, e.g. styleList.get(0).ruleList.get(i))
			// will not be read/updated here except that the order of the rules can change and rules
			// can be removed/inserted.
			
			private GeometryTypes geomType = GeometryTypes.POINT; // Default
			
			// TODO: Needed???
			//Integer indexRuleInFocus;
			
			
			public MyModel(GeometryTypes geomType) {
				this.geomType  = geomType;
			}
			
			public GeometryTypes getGeomType() {
				return geomType;
			}
			
			public void setGeomType(GeometryTypes geomType) {
				this.geomType  = geomType;
			}
			public List<RuleGroup> getRuleGroupList() {
				return ruleGroupList;
			}

			
			public void setRuleGroupList(List<RuleGroup> ruleGroupList) {
				this.ruleGroupList = ruleGroupList;
			}

			
		}
		
		public void setModel(MyModel model) {
			// TODO: validation
			List<RuleGroup> ruleGroupList = model.getRuleGroupList();
			if (null == ruleGroupList) {
				//TODO: error
				return; // ABORT
			}
			if (ruleGroupList.size() > 1) {
				logger.log(Level.WARNING, 
							"Meer dan 1 groep van regels (&lt;FeatureTypeStyle&gt;) in deze SLD." 
						+ "  Enkel de eerste wordt getoond.");
				// Can be supported later via groups of rules in ruleSelector
			}
			RuleGroup ruleGroup = ruleGroupList.iterator().next(); // retrieve the first 
									// <FeatureTypeStyle> element

			if (ruleGroup.getRuleModelList().size() < 1) {
				// 	If rule model List() is NULL or empty, create default rule
				logger.log(Level.WARNING, "Een SLD zonder of met leeg stijlelement (&lt;FeatureTypeStyle&gt; element)"
						+ " wordt ingeladen.  Een standaard stijl voor een laag met geometrie-type '"
						+ getModel().getGeomType().value() + "' wordt toegevoegd.");

				// ruleGroup.setName("Nieuwe stijl"); // TODO, for the moment most rule groups don't have a name

				List<RuleModel> ruleList = new ArrayList<RuleModel>();
//				RuleInfo defaultRule = SldUtils.createDefaultRule(getModel().getGeomType()); 


				RuleModel defaultRuleModel = new RuleModel().createDefaultRuleModel(getModel().getGeomType());
				
				ruleList.add(defaultRuleModel);

				ruleGroup.setRuleModelList(ruleList);
				
				modelHasChanged();
			}
			
			

			this.myModel = model;
		}

		private MyModel getModel() {
				return myModel;
		}

		private void modelHasChanged() {
			informParentOfChange();
			// TODO: inform parent presenter
		}

		@Inject
		public RuleSelectorPresenter(final EventBus eventBus, final MyView view,
				final MyProxy proxy) {
			super(eventBus, view, proxy);
		}
		


		
		@Override
		protected void onBind() {
			super.onBind();
			registerHandler(getView().addChangeHandler(
					new SelectorChangeHandler() {
						

						public void onChange(Integer indexRuleInFocus, RuleData ruleData) {
							// TODO 
							informParentOfChange();
							
						}
					}));

		}
		
		@Override
		protected void revealInParent() {
			//TODO: check if MainPagePresenter.TYPE_MAIN_CONTENT is OK
			RevealContentEvent.fire(this, StyledLayerDescriptorLayoutPresenter.TYPE_RULES_CONTENT,
					this);
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
		
		private void informParentOfChange() {
			// TODO: inform parent presenter
			//TODO: code below is for testing only, it doesn't inform the parent !!!
			logger.log(Level.INFO, "SLD model data has changed. TODO: inform parent presenter");
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
	//		    if (!FieldVerifier.isValidName(textToServer)) {
	//		      getView().setError("Please enter at least four characters");
	//		      return;
	//		    }
			if (true) {
				setModel(newModel);
			}
	
			//TODO : send to parent Presenter
			// Then, we transmit it to the ResponsePresenter, which will do the server
			// call
	//		    placeManager.revealPlace(new PlaceRequest(ResponsePresenter.nameToken).with(
	//		        ResponsePresenter.textToServerParam, textToServer));
	
		}

		@ProxyEvent
		public void onInitSldLayout(InitSldLayoutEvent event) {
			forceReveal();
			
		}

	}
