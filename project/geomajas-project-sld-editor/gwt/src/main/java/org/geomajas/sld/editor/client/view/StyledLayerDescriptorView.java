/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.sld.editor.client.view;

import org.geomajas.sld.editor.common.client.i18n.SldEditorMessages;
import org.geomajas.sld.editor.common.client.model.SldGeneralInfo;
import org.geomajas.sld.editor.common.client.presenter.StyledLayerDescriptorPresenter;
import org.geomajas.sld.editor.common.client.presenter.event.SldContentChangedEvent;
import org.geomajas.sld.editor.common.client.presenter.event.SldContentChangedEvent.SldContentChangedHandler;
import org.geomajas.sld.editor.common.client.view.ViewUtil;

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
 * @author Jan De Moerloose
 */
public class StyledLayerDescriptorView extends ViewImpl implements StyledLayerDescriptorPresenter.MyView {

	private VLayout layoutContainer;

	private DynamicForm topLevelAttributesForm;

	private final Label errorMessage;

	private TextItem nameOfLayerItem;

	private TextItem styleTitleItem;

	private StaticTextItem geomTypeItem;

	private SldGeneralInfo model;

	private EventBus eventBus;

	private final SldEditorMessages messages;

	@Inject
	public StyledLayerDescriptorView(final EventBus eventBus, final ViewUtil viewUtil,
			final SldEditorMessages messages) {
		this.eventBus = eventBus;
		this.messages = messages;
		topLevelAttributesForm = new DynamicForm();
		topLevelAttributesForm.setNumCols(4);

		nameOfLayerItem = new TextItem("Layer", messages.layerTitle());
		nameOfLayerItem.setWidth(200);
		nameOfLayerItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				if (!nameOfLayerItem.validate()) {
					return;
				}
				model.setNameOfLayer(nameOfLayerItem.getValueAsString());
				// Inform observer(s) of change of SLD data
				SldContentChangedEvent.fire(StyledLayerDescriptorView.this);

			}
		});

		nameOfLayerItem.setRequired(true);
		nameOfLayerItem.setRequiredMessage("De naam van de laag mag niet leeg zijn");
		nameOfLayerItem.setValidateOnChange(true);

		geomTypeItem = new StaticTextItem("Geometry", messages.geometryTitle());
		geomTypeItem.setWidth(150);

		styleTitleItem = new TextItem("Style", messages.styleTitle());
		styleTitleItem.setWidth(300);
		styleTitleItem.setColSpan(4);

		styleTitleItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				if (!styleTitleItem.validate()) {
					return;
				}

				model.setNameOfLayer(styleTitleItem.getValueAsString());

				// Inform observer(s) of change of SLD data
				SldContentChangedEvent.fire(StyledLayerDescriptorView.this);

			}
		});

		styleTitleItem.setRequired(true);
		styleTitleItem.setRequiredMessage("De naam van de stijl mag niet leeg zijn");
		styleTitleItem.setValidateOnChange(true);

		topLevelAttributesForm.setGroupTitle(messages.generalFormTitle());
		topLevelAttributesForm.setIsGroup(true);

		topLevelAttributesForm.setItems(nameOfLayerItem, geomTypeItem, styleTitleItem);

		errorMessage = new Label("<i>" + messages.noSldMessage() + "</i>");
		errorMessage.setAlign(Alignment.CENTER);
		// errorMessage.hide();
		layoutContainer = new VLayout(5);
		// layoutContainer.setMinHeight(100); // TODO: was 200

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
		// errorMessage.markForRedraw();
	}

	// @Override
	public void copyToView(SldGeneralInfo model) {
		this.model = model;
		topLevelAttributesForm.enable();
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
		topLevelAttributesForm.disable();
		errorMessage.setContents("<i>" + messages.noSldMessage() + "</i>");
		errorMessage.markForRedraw();
	}

	public void focus() {
		// Set focus on nameOfLayerItem
		nameOfLayerItem.focusInItem();

	}

}
