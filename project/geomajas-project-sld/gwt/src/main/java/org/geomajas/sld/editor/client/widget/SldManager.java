/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.sld.editor.client.widget;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.sld.FeatureTypeStyleInfo;
import org.geomajas.sld.NamedLayerInfo;
import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.StyledLayerDescriptorInfo;
import org.geomajas.sld.StyledLayerDescriptorInfo.ChoiceInfo;
import org.geomajas.sld.UserStyleInfo;
import org.geomajas.sld.client.SldGwtServiceAsync;
import org.geomajas.sld.editor.client.GeometryTypes;
import org.geomajas.sld.editor.client.SldUtils;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.types.Alignment;
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
 * Provides GUI controls for SLD management: showing a list of available SLD's, 
 * adding/removing an SLD rule.
 * 
 * @author An Buyle
 * 
 */

public class SldManager {

	public static final String SLD_NAME_ATTRIBUTE_NAME = "SLDName";

	private SldGwtServiceAsync service;

	private VLayout vLayout;

	private ListGrid listGrid;

	private boolean selectedAfterLoad;

	private RecordList recordsSorted;

	private HLayout toolStrip;

	private AddButton addButton;

	private RemoveButton removeButton;

	private ListGridRecord[] recordsArray;

	private List<String> sldList;

	private SelectionChangedHandler externalSelectionChangedHandler;

	public SldManager(SldGwtServiceAsync sldService) {

		vLayout = new VLayout(10);
		vLayout.setSize("100%", "100%");
		vLayout.setLayoutTopMargin(10);

		listGrid = new ListGrid();
		// listGrid.setHeight("85%");
		vLayout.addMember(listGrid);

		this.service = sldService;
		listGrid.setShowAllRecords(true);
		listGrid.setSelectionType(SelectionStyle.SINGLE);
		listGrid.setPrompt("Klik op een SLD om deze te bekijken of the wijzigen."); // TODo i18n

		ListGridField nameField = new ListGridField("SLDName", "Beschikbare SLD's");
		nameField.setAlign(Alignment.LEFT);
		listGrid.setFields(nameField);
		listGrid.setSelectionType(SelectionStyle.SINGLE);
		// setSortField(nameField);
		listGrid.addSelectionChangedHandler(new SelectionChangedHandler() {

			public void onSelectionChanged(SelectionEvent event) {
				ListGridRecord record = event.getSelectedRecord();
				
				if (null != externalSelectionChangedHandler) {
					externalSelectionChangedHandler.onSelectionChanged(event);
				}
				
				enableRemoveButton(record != null);

			}
		});

		toolStrip = new HLayout(10/* membersMargin */);
		toolStrip.setHeight("30");
		addButton = new AddButton();

		addButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				// Dialog to retrieve SLD name and geometry
				handleAddByUserCommand();
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

		vLayout.addMember(toolStrip);

	}

	public Canvas getCanvas() {
		return vLayout;
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
		// winModal.setKeepInParentRect(WidgetLayout.exceptionWindowKeepInScreen);
		// winModal.addCloseClickHandler(new CloseClickHandler() {
		// public void onCloseClick(CloseClientEvent event) {
		// buttonTouchThis.setTitle("Touch This");
		// winModal.destroy();
		// }
		//
		// public void onCloseClick(TabCloseClickEvent event) {
		// // TODO Auto-generated method stub
		//
		// }
		// });

		final DynamicForm addSldForm = new DynamicForm();

		final TextItem nameOfSldItem = new TextItem("NameOfSld", "Naam SLD");
		nameOfSldItem.setWidth(200);
		nameOfSldItem.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				if (!nameOfSldItem.validate()) {
					return;
				}
			}
		});

		nameOfSldItem.setRequired(true);
		nameOfSldItem.setRequiredMessage("De naam van de SLD mag niet leeg zijn");

		final SelectItem typeOfGeomItem = new SelectItem();
		typeOfGeomItem.setTitle("Type van geometry");

		final LinkedHashMap<String, String> typeOfGeomList = new LinkedHashMap<String, String>();
		typeOfGeomList.put(GeometryTypes.POINT.value(), "Punt");
		typeOfGeomList.put(GeometryTypes.LINE.value(), "Lijn");
		typeOfGeomList.put(GeometryTypes.POLYGON.value(), "Polygoon");
		typeOfGeomItem.setValueMap(typeOfGeomList);
		typeOfGeomItem.setDefaultValue(GeometryTypes.POINT.value());
		typeOfGeomItem.setRequired(true);

		addSldForm.setItems(nameOfSldItem, typeOfGeomItem);
		winModal.addItem(addSldForm);

		HLayout toolStrip = new HLayout(10/* membersMargin */);
		toolStrip.setPadding(10);
		toolStrip.setAlign(Alignment.RIGHT);
		toolStrip.setHeight(34);

		final IButton createButton = new IButton();
		createButton.setIcon(WidgetLayout.iconCreate);
		// createButton.setShowDisabledIcon(false);
		createButton.setPrompt("Cre&euml;er de SLD en voeg ze aan de lijst toe"); // TODO i18n
		// TODO: validate form first
		createButton.setTitle("Voeg toe");
		createButton.setShowDisabledIcon(false);

		final IButton cancelButton = new IButton();
		cancelButton.setIcon(WidgetLayout.iconCancel);
		cancelButton.setShowDisabledIcon(false);
		cancelButton.setTitle("Annuleer"); // TODO i18n
		cancelButton.setTooltip("Annuleer de creatie van een nieuwe SLD.");

		createButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				if (addSldForm.validate()) {
					winModal.setIsModal(false);

					createButton.disable();
					cancelButton.disable();
					StyledLayerDescriptorInfo sld = createEmptySld(GeometryTypes.fromValue(typeOfGeomItem
							.getValueAsString()));
					sld.setName(nameOfSldItem.getValueAsString());
					service.create(sld, new AsyncCallback<StyledLayerDescriptorInfo>() {

						/** call-back for handling saveOrUpdate() success return **/
						@SuppressWarnings("deprecation")
						public void onSuccess(StyledLayerDescriptorInfo sld) {
							SC.say("De SLD met standaard inhoud is succesvol gecre&euml;erd.");
							if (null != sld) {
								sldList.add(sld.getName());
								setData(sldList);
								selectSld(sld.getName(), false);
								listGrid.markForRedraw();
							}

							DeferredCommand.addCommand(new Command() {

								public void execute() {
									winModal.destroy();
								}
							});

						}

						@SuppressWarnings("deprecation")
						public void onFailure(Throwable caught) {
							SC.warn("De SLD met standaard inhoud kon niet gecre&euml;erd worden. (Interne fout: "
									+ caught.getMessage() + ")");

							DeferredCommand.addCommand(new Command() {

								public void execute() {
									winModal.destroy();
								}
							});

						}
					});

				}

			}

		});

		toolStrip.addMember(createButton);

		cancelButton.addClickHandler(new ClickHandler() {

			@SuppressWarnings("deprecation")
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				winModal.setIsModal(false);
				DeferredCommand.addCommand(new Command() {

					public void execute() {
						winModal.destroy();
					}
				});
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

							service.remove(record.getAttribute(SLD_NAME_ATTRIBUTE_NAME), new AsyncCallback<Boolean>() {

								/** call-back for handling success return **/
								public void onSuccess(Boolean result) {
									if (result) {
										sldList.remove(record.getAttribute(SLD_NAME_ATTRIBUTE_NAME));
										setData(sldList);
										listGrid.deselectAllRecords(); /* results in call-back */
										listGrid.markForRedraw();

									} else {
										SC.warn("De SLD kon niet verwijderd worden.");
									}
								}

								/** call-back for handling failure **/
								public void onFailure(Throwable caught) {
									SC.warn("De SLD kon niet verwijderd worden. (Interne fout: " + caught.getMessage()
											+ ")");
								}
							});

						}
					}
				});
	}

	private StyledLayerDescriptorInfo createEmptySld(GeometryTypes geomType) {
		StyledLayerDescriptorInfo sld = new StyledLayerDescriptorInfo();
		sld.setName("NewSLD");
		sld.setVersion("1.0.0");

		List<ChoiceInfo> choiceList = new ArrayList<ChoiceInfo>();
		sld.setChoiceList(choiceList);

		choiceList.add(new ChoiceInfo());

		NamedLayerInfo namedLayerInfo = new NamedLayerInfo();

		List<org.geomajas.sld.NamedLayerInfo.ChoiceInfo> namedlayerChoicelist = 
			new ArrayList<org.geomajas.sld.NamedLayerInfo.ChoiceInfo>();

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

	public void addSelectionChangedHandler(SelectionChangedHandler selectionChangedHandler) {
		this.externalSelectionChangedHandler = selectionChangedHandler;
	}

	public void setData(List<String> sldList) {
		this.sldList = sldList;

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

	public void selectSld(String sldName, boolean selectedAfterLoad) {
		recordsSorted = listGrid.getDataAsRecordList();

		Record record = recordsSorted.find(SLD_NAME_ATTRIBUTE_NAME, sldName);
		this.selectedAfterLoad = selectedAfterLoad;
		listGrid.selectSingleRecord(record);
		this.selectedAfterLoad = false;
	}

	public boolean isSelectAfterLoaded() {
		return selectedAfterLoad;
	}

	// -------------------------------------------------------------------------
	// Private class AddButton:
	// -------------------------------------------------------------------------

	/** Definition of the Add button. */
	private class AddButton extends IButton {

		public AddButton() {
			setIcon(WidgetLayout.iconAdd);
			setShowDisabledIcon(false);
			setPrompt("Voeg nieuwe SLD toe"); // TODO i18n
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

		public RemoveButton() {
			setIcon(WidgetLayout.iconRemove);
			setShowDisabledIcon(true);
			setPrompt("Verwijder geselecteerde SLD"); // TODO i18n
			setWidth(24);
			setDisabled(true);

			// TODO: setTitle(I18nProvider.getAttribute().btnSaveTitle());
			// TODO: setTooltip(I18nProvider.getAttribute().btnSaveTooltip());

		}
	}

}
