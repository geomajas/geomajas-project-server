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

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geomajas.sld.NamedLayerInfo;
import org.geomajas.sld.NamedLayerInfo.ChoiceInfo;
import org.geomajas.sld.StyledLayerDescriptorInfo;
import org.geomajas.sld.client.model.SldGeneralInfo;
import org.geomajas.sld.client.model.SldManager;
import org.geomajas.sld.client.model.event.SldSelectedEvent;
import org.geomajas.sld.client.model.event.SldSelectedEvent.SldSelectedHandler;
import org.geomajas.sld.client.presenter.StyledLayerDescriptorListPresenter.MyProxy;
import org.geomajas.sld.client.presenter.StyledLayerDescriptorListPresenter.MyView;
import org.geomajas.sld.client.presenter.event.InitSldLayoutEvent;
import org.geomajas.sld.client.presenter.event.SldContentChangedEvent;
import org.geomajas.sld.client.presenter.event.InitSldLayoutEvent.InitSldLayoutHandler;
import org.geomajas.sld.client.presenter.event.SldContentChangedEvent.HasSldContentChangedHandlers;
import org.geomajas.sld.client.presenter.event.SldContentChangedEvent.SldContentChangedHandler;
import org.geomajas.sld.client.view.ViewUtil;
import org.geomajas.sld.editor.client.GeometryType;
import org.geomajas.sld.editor.client.i18n.SldEditorMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HasHandlers;
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
public class StyledLayerDescriptorPresenter
	extends Presenter<StyledLayerDescriptorPresenter.MyView, StyledLayerDescriptorPresenter.MyProxy>  
	implements SldSelectedHandler, InitSldLayoutHandler {

	private Logger logger = Logger.getLogger("StyledLayerDescriptorPresenter");
	private static final SldEditorMessages MESSAGES = GWT.create(SldEditorMessages.class);
	private SldGeneralInfo myModel;
	private SldManager manager;

	private ViewUtil viewUtil;

	
	/**
	 * {@link StyledLayerDescriptorPresenter}'s proxy.
	 */
	@ProxyStandard
	public interface MyProxy extends Proxy<StyledLayerDescriptorPresenter> {
	}

	/**
	 * {@link StyledLayerDescriptorPresenter}'s view.
	 */
	public interface MyView extends View, HasSldContentChangedHandlers  {

		void copyToView(SldGeneralInfo myModel);

		SldGeneralInfo copyToModel(SldGeneralInfo myModel);

		void reset();

		void focus();

		void setError(String errorText);

	}


	public void setModel(SldGeneralInfo model) {
		this.myModel = model;
	}

	private SldGeneralInfo getModel() {
		return myModel;
	}

	/** Constructor.
	 * 	 
	 * @param eventBus
	 * @param view
	 * @param proxy
	 * @param viewUtil
	 * @param manager
	 */
	@Inject
	public StyledLayerDescriptorPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy,
			final ViewUtil viewUtil, final SldManager manager) {
		super(eventBus, view, proxy);
		this.manager = manager;
		this.viewUtil = viewUtil;
	}
	
	
	@Override
	protected void onBind() {
		super.onBind();
		registerHandler(getView().addSldContentChangedHandler(new SldContentChangedHandler() {
			
			public void onSldContentChanged(SldContentChangedEvent event) {
				manager.updateGeneralSldInfo((SldGeneralInfo)(event.getData())); //TODO: optimize: only update SLD when user hits save button
				
			}
		}));		

		//observe change of selected SLD (after it has been loaded) 
		addRegisteredHandler(SldSelectedEvent.getType(), this);
		
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, StyledLayerDescriptorLayoutPresenter.TYPE_GENERAL_CONTENT, this);
	}

	@ProxyEvent
	public void onInitSldLayout(InitSldLayoutEvent event) {
		forceReveal();
	}
	

	/* (non-Javadoc)
	 * Refresh any information displayed by your presenter.
	 * 
	 * @see com.gwtplatform.mvp.client.PresenterWidget#onReset()
	 */
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

	/**
	 * Handler, called when change of selected SLD  event is received.
	 * 
	 * @param event
	 */
	//TODO
	public void onSldSelected(SldSelectedEvent event) {
		// First save current model
		//TODO
		//Then load model with data for the newly selected SLD 
		StyledLayerDescriptorInfo sld = event.getSld();

		//TODO: Geometry Type can only be determined from the rule data (deeper level)

		// Retrieve the top-level info from SLD and update myModel accordingly

		myModel = new SldGeneralInfo(GeometryType.UNSPECIFIED);

		if (null == sld.getChoiceList() || sld.getChoiceList().isEmpty()) {
			//TODO: Warn dialogue
			logger.log(Level.WARNING, "Empty SLD's are not supported.");
			return; // ABORT
		}

		// make sure only 1 layer per SLD
		if (sld.getChoiceList().size() > 1) {
			//TODO: Warn dialogue
			//SC.warn("Having more than 1 layer in an SLD is not supported.");
			return; // ABORT
		}
		/* retrieve the first choice */
		StyledLayerDescriptorInfo.ChoiceInfo info = sld.getChoiceList().iterator().next();
		if (null == info || !info.ifNamedLayer()) {
			//TODO: Warn dialogue
			// warning that invalid SLD
			//SC.warn("Only SLD's with a &lt;NamedLayer&gt; element are supported.");
			return; // ABORT
		}

		NamedLayerInfo namedLayerInfo = info.getNamedLayer();

		String nameValue = (null != namedLayerInfo.getName()) ? namedLayerInfo.getName()
				: MESSAGES.nameUnspecified();

		//
		myModel.setNameOfLayer(nameValue);
		//
		List<ChoiceInfo> choiceList = namedLayerInfo.getChoiceList();
		if (null == choiceList) {
			//TODO: Warn dialogue
			//SC.warn("Ongeldige SLD: een leeg &lt;NamedLayer&gt; element wordt niet ondersteund.");
			return; // ABORT
		}

		ChoiceInfo choiceInfo = choiceList.iterator().next(); // retrieve the first constraint

		String styleTitle = null;
		if (choiceInfo.ifNamedStyle()) {
			// Only the name is specialized
			styleTitle = choiceInfo.getNamedStyle().getName();
			//TODO: Warn dialogue
			//	SC.warn("De SLD verwijst naar een externe stijl met naam '" + choiceInfo.getNamedStyle()
			//		+ "'.  Deze kan hier niet getoond worden.");
		} else if (choiceInfo.ifUserStyle()) {
			styleTitle = choiceInfo.getUserStyle().getTitle();

			if (null == choiceInfo.getUserStyle().getFeatureTypeStyleList()
					|| choiceInfo.getUserStyle().getFeatureTypeStyleList().size() == 0) {

				//TODO: Warn dialogue
				//TODO in higher level: If getFeatureTypeStyleList null or empty, then setup 
				//				a FeatureTypeStyleInfo list with 1 style, the default one 
//				List<FeatureTypeStyleInfo> featureTypeStyleList = new ArrayList<FeatureTypeStyleInfo>();
//				featureTypeStyleList.add(new FeatureTypeStyleInfo());
//				choiceInfo.getUserStyle().setFeatureTypeStyleList(featureTypeStyleList);
//				// TODO: inform others that the SLD has changed!
//				enableSave(true);
				return; // ABORT!
			}
		}
		//
		myModel.setStyleTitle(styleTitle);

		getView().copyToView(myModel);
		getView().focus();

	}

	/**
	 *  Private  
	 */
	private void informParentOfChange() {
		// TODO: code below is for testing only!!!
		SldGeneralInfo newModel = new SldGeneralInfo(getModel().getGeomType());
		getView().copyToModel(newModel);
	}

	/**
	 * 
	 */
	private void updateModelToServer() {
		// First, we validate the input.
		getView().setError("");
		SldGeneralInfo newModel = new SldGeneralInfo(getModel().getGeomType());

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
