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

package org.geomajas.sld.editor.client.view;

import org.geomajas.sld.client.model.SldGeneralInfo;
import org.geomajas.sld.client.presenter.StyledLayerDescriptorPresenter;
import org.geomajas.sld.client.presenter.event.SldContentChangedEvent;
import org.geomajas.sld.client.presenter.event.SldContentChangedEvent.SldContentChangedHandler;
import org.geomajas.sld.client.view.ViewUtil;
import org.geomajas.sld.editor.client.i18n.SldEditorMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.gwtplatform.mvp.client.ViewImpl;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * SmartGwt implementation of {@link StyledLayerDescriptorPresenter.MyView}.
 * 
 * @author An Buyle
 */
public class StyledLayerDescriptorView extends ViewImpl
				implements StyledLayerDescriptorPresenter.MyView {

	private static final SldEditorMessages MESSAGES = GWT.create(SldEditorMessages.class);

	private static final String NO_SLD_MESSAGE = "<i>Geen SLD ingeladen!</i>";

	private VLayout layoutContainer;

	private DynamicForm topLevelAttributesForm;

	private final Label errorMessage;

	private TextItem nameOfLayerItem;

	private TextItem styleTitleItem;

	private StaticTextItem geomTypeItem;

	private SldGeneralInfo model;

	private EventBus eventBus;

	@Inject
	public StyledLayerDescriptorView(final EventBus eventBus, final ViewUtil viewUtil) {
		this.eventBus = eventBus;
		topLevelAttributesForm = new DynamicForm();
		topLevelAttributesForm.setNumCols(4);

		nameOfLayerItem = new TextItem("Layer", MESSAGES.layerTitle());
		nameOfLayerItem.setWidth(200);
		nameOfLayerItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				if (!nameOfLayerItem.validate()) {
					return;
				}
				model.setNameOfLayer(nameOfLayerItem.getValueAsString());
				//Inform observer(s) of change of SLD data
				SldContentChangedEvent.fire(StyledLayerDescriptorView.this);

			}
		});

		nameOfLayerItem.setRequired(true);
		nameOfLayerItem.setRequiredMessage("De naam van de laag mag niet leeg zijn");
		nameOfLayerItem.setValidateOnChange(true);

		geomTypeItem = new StaticTextItem("Geometry", MESSAGES.geometryTitle());
		geomTypeItem.setWidth(150);

		styleTitleItem = new TextItem("Style", MESSAGES.styleTitle());
		styleTitleItem.setWidth(300);
		styleTitleItem.setColSpan(4);

		styleTitleItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				if (!styleTitleItem.validate()) {
					return;
				}

				model.setNameOfLayer(styleTitleItem.getValueAsString());

				//Inform observer(s) of change of SLD data
				SldContentChangedEvent.fire(StyledLayerDescriptorView.this);

			}
		});

		styleTitleItem.setRequired(true);
		styleTitleItem.setRequiredMessage("De naam van de stijl mag niet leeg zijn");
		styleTitleItem.setValidateOnChange(true);

		topLevelAttributesForm.setGroupTitle(MESSAGES.generalFormTitle());
		topLevelAttributesForm.setIsGroup(true);

		topLevelAttributesForm.setItems(nameOfLayerItem, geomTypeItem, styleTitleItem);

		errorMessage = new Label(NO_SLD_MESSAGE);
		errorMessage.setAlign(Alignment.CENTER);
		//errorMessage.hide();
		layoutContainer = new VLayout(5);
//		layoutContainer.setMinHeight(100); // TODO: was 200

		layoutContainer.setLayoutBottomMargin(5);

		layoutContainer.addMember(topLevelAttributesForm);
		layoutContainer.addMember(errorMessage);

	}

	// @Override
	public Widget asWidget() {
		return layoutContainer;
	}

	public void fireEvent(GwtEvent<?> event) {
		eventBus.fireEvent(event);
	}

	public HandlerRegistration addSldContentChangedHandler(SldContentChangedHandler handler) {
		return eventBus.addHandler(SldContentChangedEvent.getType(), handler);
	}

	// @Override
	public void setError(String errorText) {
		if (null == errorText || errorText.isEmpty()) {
			errorMessage.hide();
		} else {
			errorMessage.show();
		}
		errorMessage.setContents(null == errorText ? "" : errorText);
		//errorMessage.markForRedraw();
	}

	// @Override
	public void copyToView(SldGeneralInfo model) {
		this.model = model;
		nameOfLayerItem.setValue(model.getNameOfLayer());
		styleTitleItem.setValue(model.getStyleTitle());
		geomTypeItem.setValue(model.getGeomType().value());
		setError(null);
		topLevelAttributesForm.markForRedraw();

	}

	public void reset() {
		if (null == model) {
			clear();
		} else {
			copyToView(model);
		}
	}
	
	public void clear() {
		topLevelAttributesForm.clearValues();
		errorMessage.setContents(NO_SLD_MESSAGE);
		errorMessage.markForRedraw();
	}

	public void focus() {
		// Set focus on nameOfLayerItem
		nameOfLayerItem.focusInItem();

	}

}
