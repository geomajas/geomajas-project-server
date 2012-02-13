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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.geomajas.sld.FeatureTypeStyleInfo;
import org.geomajas.sld.NamedLayerInfo;
import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.StyledLayerDescriptorInfo;
import org.geomajas.sld.StyledLayerDescriptorInfo.ChoiceInfo;
import org.geomajas.sld.UserStyleInfo;
import org.geomajas.sld.client.presenter.StyledLayerDescriptorListPresenter;
import org.geomajas.sld.client.presenter.event.SldListPopupNewEvent;
import org.geomajas.sld.client.presenter.event.SldListPopupNewEvent.SldListPopupNewHandler;
import org.geomajas.sld.client.presenter.event.SldListRemoveEvent;
import org.geomajas.sld.client.presenter.event.SldListRemoveEvent.SldListRemoveHandler;
import org.geomajas.sld.editor.client.GeometryType;
import org.geomajas.sld.editor.client.SldUtils;
import org.geomajas.sld.editor.client.i18n.SldEditorMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.gwtplatform.mvp.client.ViewImpl;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * List view of SLD's.
 * 
 * @author Jan De Moerloose
 * 
 */
public class StyledLayerDescriptorListView extends ViewImpl implements StyledLayerDescriptorListPresenter.MyView {

	public static final String SLD_NAME_ATTRIBUTE_NAME = "SLDName";

	private SldEditorMessages sldEditorMessages = GWT.create(SldEditorMessages.class);

	private VLayout vLayout;

	private ListGrid listGrid;

	private boolean userFlagDuringSelect;

	private RecordList recordsSorted;

	private HLayout toolStrip;

	private AddButton addButton;

	private RemoveButton removeButton;

	private ListGridRecord[] recordsArray;

	private SelectionChangedHandler externalSelectionChangedHandler;
	
	private EventBus eventBus;

	@Inject
	public StyledLayerDescriptorListView(final EventBus eventBus) {
		this.eventBus = eventBus;
		vLayout = new VLayout(10);
		vLayout.setLayoutTopMargin(10);
		vLayout.setLayoutBottomMargin(5);

		listGrid = new ListGrid();

		listGrid.setMinHeight(100);
		listGrid.setOverflow(Overflow.AUTO);
		listGrid.setLeaveScrollbarGap(true);

		listGrid.setShowAllRecords(true);
		listGrid.setSelectionType(SelectionStyle.SINGLE);
		listGrid.setPrompt(sldEditorMessages.listingOfSldsTooltip());

		ListGridField nameField = new ListGridField("SLDName", sldEditorMessages.listingOfSldsTitle());

		nameField.setAlign(Alignment.LEFT);
		listGrid.setFields(nameField);
		listGrid.setSelectionType(SelectionStyle.SINGLE);

		listGrid.addSelectionChangedHandler(new SelectionChangedHandler() {

			public void onSelectionChanged(SelectionEvent event) {
				ListGridRecord record = event.getSelectedRecord();

				if (null != externalSelectionChangedHandler) {
					externalSelectionChangedHandler.onSelectionChanged(event);
				}

				enableRemoveButton(record != null);

			}
		});

		vLayout.addMember(listGrid);
		listGrid.setHeight("*");

		toolStrip = new HLayout(10/* membersMargin */);
		toolStrip.setPadding(10);
		toolStrip.setHeight(40); /* fixed size for tool strip for SLD manager */

		addButton = new AddButton();

		addButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				SldListPopupNewEvent.fire(StyledLayerDescriptorListView.this);
				// Dialog to retrieve SLD name and geometry
				//handleAddByUserCommand();
			}

		});

		toolStrip.addMember(addButton);
		removeButton = new RemoveButton();

		removeButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				handleRemoveByUserCommand();

			}

		});

		toolStrip.addMember(removeButton);
		vLayout.setWidth100();
		vLayout.setHeight100();

		vLayout.addMember(toolStrip);

	}
	

	public Widget asWidget() {
		return vLayout;
	}



	public Canvas getCanvas() {
		return vLayout;
	}

	public void addSelectionChangedHandler(SelectionChangedHandler selectionChangedHandler) {
		this.externalSelectionChangedHandler = selectionChangedHandler;
	}

	public void setData(List<String> sldList) {

		recordsArray = new ListGridRecord[sldList.size()];

		int i = 0;
		for (String sldName : sldList) {
			recordsArray[i] = new ListGridRecord();
			recordsArray[i].setAttribute(SLD_NAME_ATTRIBUTE_NAME, sldName);
			i++;
		}

		listGrid.setData(recordsArray);
		listGrid.sort();
	}

	/**
	 * Select a certain SLD in the list (if present).
	 * 
	 * @param sldName
	 * @param userFlagDuringSelect
	 */
	public void selectSld(String sldName, boolean userFlagDuringSelect) {
		if (null != sldName) {
			recordsSorted = listGrid.getDataAsRecordList();

			Record record = recordsSorted.find(SLD_NAME_ATTRIBUTE_NAME, sldName);
			this.userFlagDuringSelect = userFlagDuringSelect; // This flag can be polled during the
			// subsequent execution of selectionChangedHandler.
			listGrid.selectSingleRecord(record);
			this.userFlagDuringSelect = false;
		} else {
			deselectAllSlds(userFlagDuringSelect);
		}
	}

	/**
	 * Do not select any SLD in the list.
	 */
	public void deselectAllSlds(boolean userFlagDuringSelect) {
		this.userFlagDuringSelect = userFlagDuringSelect; // This flag can be polled during the
		// subsequent execution of selectionChangedHandler.
		listGrid.deselectAllRecords();
		this.userFlagDuringSelect = false;
	}

	/**
	 * @return true if inside select()/deselectAll() of list grid record and <code>userFlagDuringSelect</code> argument
	 *         of triggering <code>selectSld</code> or <code>deselectAll</code> was set to value true, else false.
	 */
	public boolean getUserFlagDuringSelect() {
		return userFlagDuringSelect;
	}

	private void enableRemoveButton(boolean enable) {
		if (enable) {
			removeButton.enable();
		} else {
			removeButton.disable();
		}
	}

	private void handleAddByUserCommand() {

		// addButton.disable();
		GWT.log("User clicked on 'Add new SLD' button");
		final Window winModal = new Window();
		winModal.setWidth(360);
		winModal.setHeight(150);
		winModal.setTitle("Voeg SLD toe");
		winModal.setLayoutTopMargin(10);
		winModal.setShowMinimizeButton(false);
		winModal.setIsModal(true);
		winModal.setShowModalMask(true); // darken all other elements on the screen when a modal dialog is showing.
		winModal.setShowCloseButton(false);
		winModal.centerInPage();

		final DynamicForm addSldForm = new DynamicForm();

		final TextItem nameOfSldItem = new TextItem("NameOfSld", sldEditorMessages.nameSld());

		nameOfSldItem.setWidth(200);
		nameOfSldItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				if (!nameOfSldItem.validate()) {
					return;
				}
			}
		});

		nameOfSldItem.setRequired(true);
		nameOfSldItem.setRequiredMessage(sldEditorMessages.nameSldCanNotBeEmpty());

		final SelectItem typeOfGeomItem = new SelectItem();
		typeOfGeomItem.setTitle(sldEditorMessages.geometryTitle());

		final LinkedHashMap<String, String> typeOfGeomList = new LinkedHashMap<String, String>();
		typeOfGeomList.put(GeometryType.POINT.value(), sldEditorMessages.pointTitle());
		typeOfGeomList.put(GeometryType.LINE.value(), sldEditorMessages.lineTitle());
		typeOfGeomList.put(GeometryType.POLYGON.value(), sldEditorMessages.polygonTitle());
		typeOfGeomItem.setValueMap(typeOfGeomList);
		typeOfGeomItem.setDefaultValue(GeometryType.POINT.value());
		typeOfGeomItem.setRequired(true);

		addSldForm.setItems(nameOfSldItem, typeOfGeomItem);
		winModal.addItem(addSldForm);

		HLayout toolStrip = new HLayout(10/* membersMargin */);
		toolStrip.setPadding(10);
		toolStrip.setAlign(Alignment.RIGHT);
		toolStrip.setHeight(30); /* fixed size for buttons strip for dialogue to add an SLD */

		final IButton createButton = new IButton();
		// @todo FIX createButton.setIcon(WidgetLayout.iconCreate);
		// createButton.setShowDisabledIcon(false);
		createButton.setPrompt(sldEditorMessages.createButtonTooltip());

		// TODO: validate form first
		createButton.setTitle(sldEditorMessages.createButtonTitle());
		createButton.setShowDisabledIcon(false);

		final IButton cancelButton = new IButton();
		// @todo FIX cancelButton.setIcon(WidgetLayout.iconCancel);
		cancelButton.setShowDisabledIcon(false);
		cancelButton.setTitle(sldEditorMessages.cancelButtonTitle());
		cancelButton.setTooltip(sldEditorMessages.createSldCancelButtonTitle());

		createButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

//				if (addSldForm.validate()) {
//					winModal.setIsModal(false);
//
//					createButton.disable();
//					cancelButton.disable();
//					StyledLayerDescriptorInfo sld = createEmptySld(GeometryTypes.fromValue(typeOfGeomItem
//							.getValueAsString()));
//					sld.setName(nameOfSldItem.getValueAsString());
//					service.create(sld, new AsyncCallback<StyledLayerDescriptorInfo>() {
//
//						/** call-back for handling saveOrUpdate() success return **/
//
//						public void onSuccess(StyledLayerDescriptorInfo sld) {
//							SC.say("De SLD met standaard inhoud is succesvol gecre&euml;erd.");
//							if (null != sld) {
//								sldList.add(sld.getName());
//								setData(sldList);
//								GWT.log("SldManager: new SLD was successfully created. Execute selectSld()");
//								selectSld(sld.getName(), false);
//								listGrid.markForRedraw();
//							}
//							winModal.destroy();
//						}
//
//						public void onFailure(Throwable caught) {
//							SC.warn("De SLD met standaard inhoud kon niet gecre&euml;erd worden. (Interne fout: "
//									+ caught.getMessage() + ")");
//
//							winModal.destroy();
//
//						}
//					});
//
//				}

			}

		});

		toolStrip.addMember(createButton);

		cancelButton.addClickHandler(new ClickHandler() {

			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				winModal.setIsModal(false);
				winModal.destroy();
			}

		});

		toolStrip.addMember(cancelButton);

		winModal.addItem(toolStrip);
		winModal.show();

	}

	private void handleRemoveByUserCommand() {
		final ListGridRecord record = listGrid.getSelectedRecord();
		if (null == record) {
			return;
		}

		SC.confirm(
				"Bent u zeker dat u de SLD '" + record.getAttribute(SLD_NAME_ATTRIBUTE_NAME) + "' wilt verwijderen?",
				new BooleanCallback() {

					public void execute(Boolean value) {
						if (value != null && value) {

//							service.remove(record.getAttribute(SLD_NAME_ATTRIBUTE_NAME), new AsyncCallback<Boolean>() {
//
//								/** call-back for handling success return **/
//								public void onSuccess(Boolean result) {
//									if (result) {
//										sldList.remove(record.getAttribute(SLD_NAME_ATTRIBUTE_NAME));
//										setData(sldList);
//										listGrid.deselectAllRecords(); /* results in call-back */
//										listGrid.markForRedraw();
//
//									} else {
//										SC.warn("De SLD kon niet verwijderd worden.");
//									}
//								}
//
//								/** call-back for handling failure **/
//								public void onFailure(Throwable caught) {
//									SC.warn("De SLD kon niet verwijderd worden. (Interne fout: " + caught.getMessage()
//											+ ")");
//								}
//							});
//
						}
					}
				});
	}

	private StyledLayerDescriptorInfo createEmptySld(GeometryType geomType) {
		StyledLayerDescriptorInfo sld = new StyledLayerDescriptorInfo();
		sld.setName("NewSLD");
		sld.setVersion("1.0.0");

		List<ChoiceInfo> choiceList = new ArrayList<ChoiceInfo>();
		sld.setChoiceList(choiceList);

		choiceList.add(new ChoiceInfo());

		NamedLayerInfo namedLayerInfo = new NamedLayerInfo();

		List<NamedLayerInfo.ChoiceInfo> namedlayerChoicelist = new ArrayList<NamedLayerInfo.ChoiceInfo>();

		namedLayerInfo.setChoiceList(namedlayerChoicelist);

		namedlayerChoicelist.add(new org.geomajas.sld.NamedLayerInfo.ChoiceInfo());

		namedlayerChoicelist.get(0).setUserStyle(new UserStyleInfo());

		choiceList.get(0).setNamedLayer(namedLayerInfo);

		FeatureTypeStyleInfo featureTypeStyle = new FeatureTypeStyleInfo();

		List<RuleInfo> ruleList = new ArrayList<RuleInfo>();

		RuleInfo defaultRule = SldUtils.createDefaultRule(geomType);

		ruleList.add(defaultRule);

		featureTypeStyle.setRuleList(ruleList);

		List<FeatureTypeStyleInfo> featureTypeStyleList = new ArrayList<FeatureTypeStyleInfo>();
		featureTypeStyleList.add(featureTypeStyle);

		namedlayerChoicelist.get(0).getUserStyle().setFeatureTypeStyleList(featureTypeStyleList);

		return sld;
	}

	// -------------------------------------------------------------------------
	// Private class AddButton:
	// -------------------------------------------------------------------------

	/** Definition of the Add button. */
	private class AddButton extends IButton {
		public static final String ICON = "[ISOMORPHIC]/" + "geomajas/icons/silk/add.png";

		public AddButton() {
			// @todo FIX OK ? setIcon(WidgetLayout.iconAdd);
			setIcon(ICON);
			setShowDisabledIcon(false);
			setPrompt(sldEditorMessages.addSldButtonTooltip());
			// TODO: validate form first
			setWidth(24);
			setDisabled(false);

			// TODO: setTitle(I18nProvider.getAttribute().btnSaveTitle());
			// TODO: setTooltip(I18nProvider.getAttribute().btnSaveTooltip());

		}
	}

	// -------------------------------------------------------------------------
	// Private class RemoveButton:
	// -------------------------------------------------------------------------

	/** Definition of the Add button. */
	private class RemoveButton extends IButton {
		public static final String ICON = "[ISOMORPHIC]/" + "geomajas/icons/silk/cancel.png";
		public RemoveButton() {
			// @todo FIX OK? setIcon(WidgetLayout.iconRemove);
			setIcon(ICON);
			setShowDisabledIcon(true);
			setPrompt(sldEditorMessages.removeSldButtonTooltip());
			setWidth(24);
			setDisabled(true);

		}
	}

	public void fireEvent(GwtEvent<?> event) {
		eventBus.fireEvent(event);		
	}

	public HandlerRegistration addSldListPopupNewHandler(SldListPopupNewHandler handler) {
		return eventBus.addHandler(SldListPopupNewEvent.getType(), handler);
	}


	public HandlerRegistration addSldListRemoveHandler(SldListRemoveHandler handler) {
		return eventBus.addHandler(SldListRemoveEvent.getType(), handler);
	}


}