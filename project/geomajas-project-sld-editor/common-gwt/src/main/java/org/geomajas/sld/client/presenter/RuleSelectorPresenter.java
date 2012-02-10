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

import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
//import com.google.gwt.user.client.ui.Button;
import com.google.inject.Inject;

import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;


import org.geomajas.sld.FeatureTypeStyleInfo;
import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.client.NameTokens;
import org.geomajas.sld.editor.client.GeometryTypes;
import org.geomajas.sld.editor.client.SldUtils;


/**
 * @author An Buyle
 */


public class RuleSelectorPresenter extends
			Presenter<RuleSelectorPresenter.MyView, RuleSelectorPresenter.MyProxy> {
		
		private GeometryTypes defaultGeomType = GeometryTypes.POINT;	
		private MyModel myModel;
		private Logger logger = Logger.getLogger("SldEditor");
		
		
		/**
		 * {@link RuleSelectorPresenter}'s proxy.
		 */
		//TODO: check if NameTokens.HOME_PAGE is OK
		@ProxyCodeSplit
		@NameToken(NameTokens.HOME_PAGE)
		public interface MyProxy extends ProxyPlace<RuleSelectorPresenter> {
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

			
			private List<FeatureTypeStyleInfo> styleList; 	// Note that only 1 size == 1 is supported 
													// for the moment!
			// Note actual rule data (type = RuleInfo, e.g. styleList.get(0).ruleList.get(i))
			// will not be read/updated here except that the order of the rules can change and rules
			// can be removed/inserted.
			
			private GeometryTypes geomType = GeometryTypes.POINT; // Default
			
			// TODO: Needed???
			//Integer indexRuleInFocus ;
			
			
			public MyModel(GeometryTypes geomType) {
				this.geomType  = geomType;
			}
			
			public GeometryTypes getGeomType() {
				return geomType;
			}
			
			public void setGeomType(GeometryTypes geomType) {
				this.geomType  = geomType;
			}
			public List<FeatureTypeStyleInfo> getStyleList() {
				return styleList;
			}

			
			public void setStyleList(List<FeatureTypeStyleInfo> styleList) {
				this.styleList = styleList;
			}

			
		}
		
		public void setModel(MyModel model) {
			// TODO: validation
			List<FeatureTypeStyleInfo> styleList = 	model.getStyleList();
			if (null == styleList) {
				//TODO: error
				return; // ABORT
			}
			if (model.getStyleList().size() > 1) {
				logger.log(Level.WARNING, 
							"Meer dan 1 groep van regels (&lt;FeatureTypeStyle&gt;) in deze SLD." 
						+ "  Enkel de eerste wordt getoond.");
				// Can be supported later via groups of rules in ruleSelector
			}
			FeatureTypeStyleInfo featureTypeStyle = styleList.iterator().next(); // retrieve the first 
									// <FeatureTypeStyle> element

			if (featureTypeStyle.getRuleList().size() < 1) {
				// 	If featureTypeStyle.getRuleList() is NULL or empty, create default rule
				logger.log(Level.WARNING, "Een SLD zonder of met leeg stijlelement (&lt;FeatureTypeStyle&gt; element)"
						+ " wordt ingeladen.  Een standaard stijl voor een laag met geometrie-type '"
						+ defaultGeomType.value() + "' wordt toegevoegd.");

				// featureTypeStyle.setName("Nieuwe stijl"); // TODO, for the moment most rule groups don't have a name

				List<RuleInfo> ruleList = new ArrayList<RuleInfo>();
				RuleInfo defaultRule = SldUtils.createDefaultRule(defaultGeomType); 
					// TODO: open a dialog window to ask the geomType

				ruleList.add(defaultRule);

				featureTypeStyle.setRuleList(ruleList);
				
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
						
						public void onChange(Integer indexRuleInFocus) {
							informParentOfChange();
							
						}
					}));

		}
		
		@Override
		protected void revealInParent() {
			//TODO: check if MainPagePresenter.TYPE_MAIN_CONTENT is OK
			RevealContentEvent.fire(this, MainPagePresenter.TYPE_MAIN_CONTENT,
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

	}
