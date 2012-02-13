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

import org.geomajas.sld.client.model.event.SldSelectedEvent;
import org.geomajas.sld.client.model.event.SldSelectedEvent.SldSelectedHandler;
import org.geomajas.sld.client.presenter.event.InitSldLayoutEvent;
import org.geomajas.sld.client.presenter.event.InitSldLayoutEvent.InitSldLayoutHandler;

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
	extends Presenter<StyledLayerDescriptorPresenter.MyView, StyledLayerDescriptorPresenter.MyProxy> implements SldSelectedHandler, InitSldLayoutHandler {

	private MyModel myModel;

	/**
	 * {@link StyledLayerDescriptorPresenter}'s proxy.
	 */
	@ProxyStandard
	public interface MyProxy extends Proxy<StyledLayerDescriptorPresenter> {
	}

	/**
	 * {@link StyledLayerDescriptorPresenter}'s view.
	 */
	public interface MyView extends View {

		void copyToView(MyModel myModel);

		void copyToModel(MyModel myModel);

		// TODO: void setViewChangedHandler(ViewChangedHandler);
		void reset();

		void focus();

		void setError(String errorText);

		// Button getSendButton();
		HandlerRegistration addChangeHandler(ChangeHandler changeHandler);
	}

	/**
	 * 
	 * @author Jan De Moerloose
	 *
	 */
	public class MyModel {

		private String nameOfLayer;

		private String styleTitle;

		private final String geomType; // Cannot be updated

		public MyModel(String geomType) {
			this.geomType = geomType;
		}

		public String getNameOfLayer() {
			return nameOfLayer;
		}

		public void setNameOfLayer(String nameOfLayer) {
			this.nameOfLayer = nameOfLayer;
		}

		public String getStyleTitle() {
			return styleTitle;
		}

		public void setStyleTitle(String styleTitle) {
			this.styleTitle = styleTitle;
		}

		public String getGeomType() {
			return geomType;
		}

	}

	public void setModel(MyModel model) {
		this.myModel = model;
	}

	private MyModel getModel() {
		return myModel;
	}

	@Inject
	public StyledLayerDescriptorPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	@Override
	protected void onBind() {
		super.onBind();
		registerHandler(getView().addChangeHandler(new ChangeHandler() {

			public void onChange(Object changedData) {
				informParentOfChange();
				
			}
		}));

	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, StyledLayerDescriptorLayoutPresenter.TYPE_GENERAL_CONTENT, this);
	}

	@ProxyEvent
	public void onInitSldLayout(InitSldLayoutEvent event) {
		forceReveal();
	}
	
	protected void onReveal() {
		super.onReveal();
		//TODO: copied from StyledLayerDescriptorListPresenter: 
		// getView().setData(manager.getCurrentNames());
		//addRegisteredHandler(SldListChangedEvent.getType(), this);
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
		// TODO: code below is for testing only!!!
		MyModel newModel = new MyModel(getModel().getGeomType());
		getView().copyToModel(newModel);
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

	public void onSldSelected(SldSelectedEvent event) {
		// TODO Auto-generated method stub
		
	}

}
