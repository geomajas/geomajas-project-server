/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.sld.editor.client.view;

import java.util.LinkedHashMap;

import org.geomajas.sld.client.model.FilterModel;
import org.geomajas.sld.client.model.FilterModel.FilterModelState;
import org.geomajas.sld.client.model.FilterModel.OperatorType;
import org.geomajas.sld.client.presenter.FilterPresenter;
import org.geomajas.sld.client.presenter.event.SldContentChangedEvent;
import org.geomajas.sld.client.presenter.event.SldContentChangedEvent.SldContentChangedHandler;
import org.geomajas.sld.editor.client.i18n.SldEditorMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.gwtplatform.mvp.client.ViewImpl;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Default implementation of {@link FilterPresenter.MyView}.
 * 
 * @author Jan De Moerloose
 * @author An Buyle
 *
 */
public class FilterView extends ViewImpl implements FilterPresenter.MyView {

	// TODO: also for other operators

	private SldEditorMessages sldEditorMessages = GWT.create(SldEditorMessages.class);

	private DynamicForm filterForm;

	private TextItem filterAttributeValue;

	private SelectItem filterOperatorSelect;

	private TextItem filterAttributeName;

	private TextItem filterAttributeLowerBoundaryValue;

	private TextItem filterAttributeUpperBoundaryValue;

	private StaticTextItem likeFilterSpec;

	private Label filterIsNotSupportedMessage;

	private VLayout filterDetailContainer;

	private FilterModel filterModel;

	private final EventBus eventBus;

	@Inject
	public FilterView(final EventBus eventBus) {
		this.eventBus = eventBus;
		filterDetailContainer = new VLayout();
		filterForm = new DynamicForm();
		setupFilterForm();
		hide();
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public Widget asWidget() {
		return filterDetailContainer;
	}

	public void modelToView(FilterModel filterModel) {
		this.filterModel = filterModel;
		filterForm.setVisible(true);
		filterIsNotSupportedMessage.hide();
		filterForm.clearValues();
		filterForm.hideItem("attributeLowerBoundaryValue");
		filterForm.hideItem("attributeUpperBoundaryValue");
		filterForm.showItem("attributeValue");
		filterAttributeValue.hide();
		filterOperatorSelect.setDisabled(true);
		filterForm.hideItem("likeFilterSpec");
		if (filterModel.getState() == FilterModelState.UNSUPPORTED) {
			setFilterIsNotSupported();
		} else {
			if (null != filterModel.getPropertyName()) {
				filterAttributeName.setValue(filterModel.getPropertyName());
				filterOperatorSelect.setDisabled(false);
			}
			if (null != filterModel.getOperatorType()) {
				filterOperatorSelect.setValue(filterModel.getOperatorType().value());
				switch (filterModel.getOperatorType()) {
					case PROPERTY_IS_BETWEEN:
					case PROPERTY_IS_NOT_BETWEEN:
						filterForm.hideItem("attributeValue");
						filterForm.showItem("attributeLowerBoundaryValue");
						filterForm.showItem("attributeUpperBoundaryValue");
						filterAttributeLowerBoundaryValue.show();
						filterAttributeUpperBoundaryValue.show();
						filterAttributeLowerBoundaryValue.setValue(filterModel.getLowerValue());
						filterAttributeUpperBoundaryValue.setValue(filterModel.getUpperValue());
						break;
					case PROPERTY_IS_EQUAL_TO:
					case PROPERTY_IS_GREATER_THAN:
					case PROPERTY_IS_GREATER_THAN_OR_EQUAL:
					case PROPERTY_IS_LESS_THAN:
					case PROPERTY_IS_LESS_THAN_OR_EQUAL:
					case PROPERTY_IS_NOT_EQUAL_TO:
						filterAttributeValue.show();
						filterAttributeValue.setValue(filterModel.getPropertyValue());
						break;
					case PROPERTY_IS_LIKE:
					case PROPERTY_IS_NOT_LIKE:
						setLikeFilterSpec(filterModel.getPatternMatchingWildCard(),
								filterModel.getPatternMatchingSingleChar(), filterModel.getPatternMatchingEscape());
						filterForm.showItem("likeFilterSpec");
						filterAttributeValue.show();
						filterAttributeValue.setValue(filterModel.getPropertyValue());
						break;
					case PROPERTY_IS_NOT_NULL:
					case PROPERTY_IS_NULL:
						filterAttributeValue.show(); /* unary operator ! */
						break;

				}
			}
		}

	}

	private void setFilterIsNotSupported() {
		filterIsNotSupportedMessage.show();
	}

	private void setupFilterForm() {

		filterIsNotSupportedMessage = new Label(
				"Filter in de SLD wordt niet ondersteund, maar blijft wel in de SLD behouden.");
		filterDetailContainer.addMember(filterIsNotSupportedMessage);
		filterIsNotSupportedMessage.setVisible(false);

		filterAttributeName = new TextItem("attributeName", "Attribuut");
		filterAttributeName.setWidth(300);

		filterOperatorSelect = new SelectItem();
		filterOperatorSelect.setName("operator");
		filterOperatorSelect.setTitle("Operator");
		filterOperatorSelect.setWidth(300);
		// filterOperatorSelect.setDisabled(true);
		LinkedHashMap<String, String> operatorMap = new LinkedHashMap<String, String>();
		operatorMap.put(OperatorType.PROPERTY_IS_EQUAL_TO.value(), "="); // PropertyIsEqualTo
		operatorMap.put(OperatorType.PROPERTY_IS_NOT_EQUAL_TO.value(), "<>"); // PropertyIsNotEqualTo
		operatorMap.put(OperatorType.PROPERTY_IS_LESS_THAN.value(), "&lt;"); // PropertyIsLessThan
		operatorMap.put(OperatorType.PROPERTY_IS_LESS_THAN_OR_EQUAL.value(), "<="); // PropertyIsLessThanOrEqualTo
		operatorMap.put(OperatorType.PROPERTY_IS_GREATER_THAN.value(), ">"); // PropertyIsGreaterThan
		operatorMap.put(OperatorType.PROPERTY_IS_GREATER_THAN_OR_EQUAL.value(), ">="); // PropertyIsGreaterThanOrEqualTo

		operatorMap.put(OperatorType.PROPERTY_IS_BETWEEN.value(), "ligt tussen"); // PropertyIsBetween
		operatorMap.put(OperatorType.PROPERTY_IS_NOT_BETWEEN.value(), "ligt niet tussen"); // extension for
																							// NOT(PropertyIsBetween())

		operatorMap.put(OperatorType.PROPERTY_IS_LIKE.value(), "voldaat aan patroon (string)"); // PropertyIsLike
		operatorMap.put(OperatorType.PROPERTY_IS_NOT_LIKE.value(), "voldaat niet aan patroon (string)"); // extension
																											// for
		// NOT(PropertyIsLike())

		operatorMap.put(OperatorType.PROPERTY_IS_NULL.value(), "is null"); // PropertyIsNull
		operatorMap.put(OperatorType.PROPERTY_IS_NOT_NULL.value(), "is niet null"); // extension for
																					// NOT(PropertyIsNull())

		filterOperatorSelect.setValueMap(operatorMap);
		filterOperatorSelect.setDisabled(true); /* The attribute name must be filled in first */

		filterAttributeValue = new TextItem("attributeValue", "Waarde");
		filterAttributeValue.setWidth(300);
		/* The operator field value must be non-null before enabling the value fields */
		filterAttributeValue.setVisible(false);

		filterAttributeLowerBoundaryValue = new TextItem("attributeLowerBoundaryValue", "Ondergrens");
		filterAttributeUpperBoundaryValue = new TextItem("attributeUpperBoundaryValue", "Bovengrens");
		/* The operator field value must be non-null before enabling the value fields */
		filterAttributeLowerBoundaryValue.setVisible(false);
		filterAttributeUpperBoundaryValue.setVisible(false);

		likeFilterSpec = new StaticTextItem("likeFilterSpec"); // com.smartgwt.client.widgets.HTMLPane();

		likeFilterSpec.setTitle("Filter notaties");
		// likeFilterSpec.setCellStyle("formCell"); /* TODO */

		likeFilterSpec.setWidth(300);

		filterAttributeName.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				String propertyName = filterAttributeName.getValueAsString();
				filterModel.setPropertyName(propertyName);
				if (propertyName != null && !propertyName.isEmpty()) {
					filterOperatorSelect.enable();
				}
				filterHasChanged();
			}

		});

		filterOperatorSelect.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				String operator = event.getValue().toString();
				if (null != operator) {
					/** note: filterOperatorSelect can only be changed when filterAttributeName value is non-null **/
					filterForm.showItem("attributeValue");

					filterForm.hideItem("likeFilterSpec");
					filterForm.hideItem("attributeLowerBoundaryValue");
					filterForm.hideItem("attributeUpperBoundaryValue");

					filterModel.setOperatorType(OperatorType.fromValue(operator));
					filterHasChanged();
				}
			}
		});

		filterAttributeValue.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				filterModel.setPropertyValue(filterAttributeValue.getValueAsString());
				filterHasChanged();
			}
		});

		filterAttributeLowerBoundaryValue.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				filterModel.setLowerValue(filterAttributeLowerBoundaryValue.getValueAsString());
				filterHasChanged();
			}
		});

		filterAttributeUpperBoundaryValue.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				filterModel.setUpperValue(filterAttributeUpperBoundaryValue.getValueAsString());
				filterHasChanged();
			}
		});

		filterForm.setItems(filterAttributeName, filterOperatorSelect, filterAttributeValue, likeFilterSpec,
				filterAttributeLowerBoundaryValue, filterAttributeUpperBoundaryValue);

		filterForm.hideItem("likeFilterSpec");

		filterForm.hideItem("attributeLowerBoundaryValue");
		filterForm.hideItem("attributeUpperBoundaryValue");

		filterDetailContainer.addMember(filterForm);
	}

	private void filterHasChanged() {
		filterModel.synchronize();
		modelToView(filterModel);
		SldContentChangedEvent.fire(this);
	}

	private void setLikeFilterSpec(String wildCard, String singleChar, String escape) {
		likeFilterSpec.setValue(sldEditorMessages.likeFilterSpecTemplate(wildCard, singleChar, escape));

	}

	public HandlerRegistration addSldContentChangedHandler(SldContentChangedHandler handler) {
		return eventBus.addHandler(SldContentChangedEvent.getType(), handler);
	}

	public void fireEvent(GwtEvent<?> event) {
		eventBus.fireEvent(event);
	}

	public void clear() {
		filterForm.clearValues();
	}

	public void hide() {
		filterForm.hide();
	}

}
