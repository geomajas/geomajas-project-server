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
package org.geomajas.plugin.deskmanager.client.gwt.manager.beheer.datalayers.panels;

import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.gwt.client.Geomajas;
import org.geomajas.plugin.deskmanager.command.manager.dto.VectorLayerConfiguration;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.ChangeEvent;
import com.smartgwt.client.widgets.grid.events.ChangeHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * @author Kristof Heirwegh
 */
@SuppressWarnings("deprecation")
public class LayerAttributesGrid extends VLayout {

	private static final String FLD_NAME = "name";

	private static final String FLD_TYPE = "type";

	private static final String FLD_IDENTIFYING = "identifying";

	private static final String FLD_IDFIELD = "idField";

	private static final String FLD_LABELFIELD = "labelField";

	private static final String FLD_LABEL = "label";

	private static final String FLD_OBJECT = "_object";

	private VectorLayerConfiguration layerConfig;

	private ListGrid grid;

	private Label warnings;

	private ListGridRecord currentIdField;

	private ListGridRecord currentLabelField;

	public LayerAttributesGrid() {
		super(5);
		grid = new ListGrid();
		grid.setWidth100();
		grid.setHeight("*");
		grid.setSelectionType(SelectionStyle.SINGLE);
		grid.setShowAllRecords(true);
		grid.setSelectionType(SelectionStyle.SIMPLE);
		grid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		grid.setCanEdit(true);
		grid.setEditEvent(ListGridEditEvent.CLICK);
		grid.setEditByCell(true);
		grid.setShowEmptyMessage(true);
		grid.setEmptyMessage("<i>Opvragen gegevens van Server... <img src='" + Geomajas.getIsomorphicDir()
				+ "/images/circle.gif' style='height: 1em' /></i>");

		ListGridField nameFld = new ListGridField(FLD_NAME, "Attribuut");
		nameFld.setType(ListGridFieldType.TEXT);
		nameFld.setWidth("*");
		nameFld.setCanEdit(false);

		ListGridField typeFld = new ListGridField(FLD_TYPE, "Type");
		typeFld.setType(ListGridFieldType.TEXT);
		typeFld.setCanEdit(false);
		typeFld.setWidth(55);
		// SelectItem editor = new SelectItem();
		// editor.setValueMap(getTypes());
		// typeFld.setEditorType(editor);

		ListGridField identifyingFld = new ListGridField(FLD_IDENTIFYING, "Kerngegeven");
		identifyingFld.setType(ListGridFieldType.BOOLEAN);
		identifyingFld.setCanEdit(true);
		identifyingFld.setPrompt("Aangevinkte velden worden opgenomen in lijsten (bv. zoekresultaat)");

		ListGridField idFieldFld = new ListGridField(FLD_IDFIELD, "Id veld");
		idFieldFld.setType(ListGridFieldType.BOOLEAN);
		idFieldFld.setCanEdit(true);
		idFieldFld.setPrompt("Kies het veld dat de unieke sleuten van de records bevat. (gebruikt bij bv. zoeken)");
		idFieldFld.addChangeHandler(new ChangeHandler() {

			public void onChange(ChangeEvent event) {
				ListGridRecord lgr = grid.getRecord(event.getRowNum());
				if (!lgr.equals(currentIdField)) {
					currentIdField.setAttribute(FLD_IDFIELD, false);
					currentIdField = lgr;
				} else {
					event.cancel();
				}
			}
		});

		ListGridField labelFieldFld = new ListGridField(FLD_LABELFIELD, "Label veld");
		labelFieldFld.setType(ListGridFieldType.BOOLEAN);
		labelFieldFld.setCanEdit(true);
		labelFieldFld.setPrompt("Kies het veld dat dient gebruikt te worden bij het weergeven van labels op de laag.");
		labelFieldFld.addChangeHandler(new ChangeHandler() {

			public void onChange(ChangeEvent event) {
				ListGridRecord lgr = grid.getRecord(event.getRowNum());
				if (!lgr.equals(currentLabelField)) {
					currentLabelField.setAttribute(FLD_LABELFIELD, false);
					currentLabelField = lgr;
				} else {
					event.cancel();
				}
			}
		});

		ListGridField labelFld = new ListGridField(FLD_LABEL, "Naam");
		labelFld.setType(ListGridFieldType.TEXT);
		labelFld.setCanEdit(true);
		labelFld.setPrompt("Een gebruiksvriendelijke naam voor het veld.");

		grid.setFields(nameFld, typeFld, identifyingFld, idFieldFld, labelFieldFld, labelFld);
		grid.setCanResizeFields(true);
		grid.addSelectionChangedHandler(new SelectionChangedHandler() {

			public void onSelectionChanged(SelectionEvent event) {
				ListGridRecord lgr = (ListGridRecord) event.getRecord();
				if (!grid.isSelected(lgr)) { // deselected
					if (lgr.equals(currentIdField)) {
						warnings.setVisible(true);
						warnings.setContents("Dit attribuut wordt gebruikt als Id-veld," +
								" kies een nieuw Id-veld vooraleer het attribuut te deactiveren.");
						grid.selectRecord(lgr);
					} else if (lgr.equals(currentLabelField)) {
						warnings.setVisible(true);
						warnings.setContents("Dit attribuut wordt gebruikt als Label-veld," +
								" kies een nieuw Label-veld vooraleer het attribuut te deactiveren.");
						grid.selectRecord(lgr);
					}
				}
			}
		});

		// -------------------------------------------------

		warnings = new Label();
		warnings.setWidth100();
		warnings.setAutoHeight();
		warnings.setPadding(3);
		warnings.setOverflow(Overflow.VISIBLE);
		warnings.setVisible(false);
		warnings.setBackgroundColor("#FFCCCC");

		addMember(grid);
		addMember(warnings);
	}

	public void setData(VectorLayerConfiguration layerConfig) {
		this.layerConfig = layerConfig;
		FeatureInfo fi = layerConfig.getClientVectorLayerInfo().getFeatureInfo();
		String labelFieldName = layerConfig.getClientVectorLayerInfo().getNamedStyleInfo().getLabelStyle()
				.getLabelAttributeName();
		PrimitiveAttributeInfo idField = fi.getIdentifier();
		for (AttributeInfo ai : fi.getAttributes()) {
			if (ai instanceof PrimitiveAttributeInfo) {
				ListGridRecord lgr = new ListGridRecord();
				PrimitiveAttributeInfo pai = (PrimitiveAttributeInfo) ai;
				lgr.setAttribute(FLD_NAME, pai.getName());
				lgr.setAttribute(FLD_TYPE, pai.getType().name());
				lgr.setAttribute(FLD_IDENTIFYING, pai.isIdentifying());
				lgr.setAttribute(FLD_IDFIELD, (pai.equals(idField)));
				lgr.setAttribute(FLD_LABELFIELD, pai.getName().equals(labelFieldName));
				lgr.setAttribute(FLD_LABEL, pai.getLabel());
				lgr.setAttribute(FLD_OBJECT, pai);
				grid.addData(lgr);
				if (pai.getName().equals(labelFieldName)) {
					currentLabelField = lgr;
				}
				if (pai.equals(idField)) {
					currentIdField = lgr;
				}
				if (!pai.isHidden()) {
					grid.selectRecord(lgr);
				}
			} // else unsupported
		}
	}

	public VectorLayerConfiguration getData() {
		if (currentIdField == null || currentLabelField == null) {
			return null;
		}

		FeatureInfo fi = layerConfig.getClientVectorLayerInfo().getFeatureInfo();
		fi.setIdentifier((PrimitiveAttributeInfo) currentIdField.getAttributeAsObject(FLD_OBJECT));
		layerConfig.getClientVectorLayerInfo().getNamedStyleInfo().getLabelStyle()
				.setLabelAttributeName(currentLabelField.getAttributeAsString(FLD_NAME));
		for (ListGridRecord r : grid.getRecords()) {
			PrimitiveAttributeInfo pai = (PrimitiveAttributeInfo) r.getAttributeAsObject(FLD_OBJECT);
			pai.setLabel(r.getAttributeAsString(FLD_LABEL));
			pai.setIdentifying(r.getAttributeAsBoolean(FLD_IDENTIFYING));
			pai.setHidden(!grid.isSelected(r));
		}
		return layerConfig;
	}

	public void reset() {
		grid.deselectAllRecords();
		grid.setData(new Record[] {});
		warnings.setVisible(false);
		warnings.setContents("");
	}

	public void setWarning(String warning) {
		if (warning == null || "".equals(warning)) {
			warnings.setVisible(false);
			warnings.setContents("");
		} else {
			warnings.setVisible(true);
			warnings.setContents(warning);
		}
	}

	public boolean isValid() {
		return (!warnings.isVisible());
	}

	// -------------------------------------------------

	// private LinkedHashMap<String, String> getTypes() {
	// LinkedHashMap<String, String> values = new LinkedHashMap<String, String>();
	// values.put(PrimitiveType.BOOLEAN.name(), "Boolean");
	// values.put(PrimitiveType.CURRENCY.name(), "Boolean");
	// values.put(PrimitiveType.DATE.name(), "Boolean");
	// values.put(PrimitiveType.DOUBLE.name(), "Boolean");
	// values.put(PrimitiveType.FLOAT.name(), "Boolean");
	// values.put(PrimitiveType.IMGURL.name(), "Boolean");
	// values.put(PrimitiveType.INTEGER.name(), "Boolean");
	// values.put(PrimitiveType.LONG.name(), "Boolean");
	// values.put(PrimitiveType.SHORT.name(), "Boolean");
	// values.put(PrimitiveType.STRING.name(), "Boolean");
	// values.put(PrimitiveType.URL.name(), "Boolean");
	// return values;
	// }

}
