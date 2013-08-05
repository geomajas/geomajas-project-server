/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.smartgwt.client.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.configuration.AssociationAttributeInfo;
import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.PrimitiveType;
import org.geomajas.gwt.client.util.StringUtil;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.attribute.AssociationValue;
import org.geomajas.layer.feature.attribute.ManyToOneAttribute;
import org.geomajas.layer.feature.attribute.OneToManyAttribute;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridFieldIfFunction;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.grid.events.CellOverEvent;
import com.smartgwt.client.widgets.grid.events.CellOverHandler;

/**
 * List grid that shows association attributes as records.
 * 
 * @author Jan De Moerloose
 */
public class AttributeListGrid extends ListGrid {

	/**
	 * Feature information
	 */
	private FeatureInfo featureInfo;

	/**
	 * Show all attributes (true) or only the 'identifying' attributes (false)?
	 */
	private boolean allAttributesDisplayed;

	/**
	 * Show the feature's ID in the table. This is false by default, and should not really be necessary.
	 */
	private boolean idInTable;

	/**
	 * When hovering over image attributes, should they be shown in floating panels or not?
	 */
	private boolean showImageAttributeOnHover;

	/**
	 * List of values in the grid
	 */
	private Map<AssociationValue, String> idByValue = new HashMap<AssociationValue, String>();
	
	private int newRows;

	private static final String NEW_PREFIX = "_new_";

	private static final String VALUE_HOLDER_RECORD_ATTRIBUTE = "_value_";

	private static final String ID_NAME = "attributeId";

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	public AttributeListGrid(FeatureInfo featureInfo) {
		this.featureInfo = featureInfo;
		setShowEmptyMessage(true);
		setIdInTable(false);
	}

	// -------------------------------------------------------------------------
	// Class specific methods:
	// -------------------------------------------------------------------------

	/**
	 * Empty the grid, thereby removing all rows. It does not clear the header though.
	 */
	public void clearValues() {
		setData(new ListGridRecord[] {});
		idByValue.clear();
		newRows = 0;
	}

	/**
	 * Adds a new value to the list.
	 * 
	 * @param associationValue The value to be added to the list.
	 * @return Returns true in case of update, and false if the value is already in the list or the value is null
	 */
	public boolean saveOrUpdateValue(AssociationValue associationValue) {
		if (idByValue.containsKey(associationValue)) {
			updateValue(associationValue);
			setData(getRecords());
			return true;
		} else {
			// Calculate id
			Object idObject = associationValue.getId().getValue();
			String id;
			if (idObject != null) {
				id = idObject.toString();
			} else {
				// fake id
				id = NEW_PREFIX + "." + (newRows++);
			}
			// Feature checks out, add it to the grid:
			ListGridRecord record = new ListGridRecord();
			record.setAttribute(ID_NAME, id);
			updateAttributes(associationValue, record);
			idByValue.put(associationValue, id);
			addData(record);
			return false;
		}
	}

	/**
	 * Removes a value to the list.
	 * 
	 * @param associationValue The value to be removed from the list.
	 * @return Returns true in case of successful removal.
	 */
	public boolean deleteValue(AssociationValue associationValue) {
		if (idByValue.containsKey(associationValue)) {
			for (ListGridRecord record : getRecords()) {
				if (record.getAttributeAsObject(VALUE_HOLDER_RECORD_ATTRIBUTE) == associationValue) {
					removeData(record);
					idByValue.remove(associationValue);
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Returns true if the specified value has been added to the list.
	 * 
	 * @param associationValue value to check
	 * @return true if added, false otherwise.
	 */
	public boolean containsValue(AssociationValue associationValue) {
		return idByValue.containsKey(associationValue);
	}
	
	private void updateAttributes(AssociationValue associationValue, ListGridRecord record) {
		for (AttributeInfo attributeInfo : featureInfo.getAttributes()) {
			Attribute<?> attr = associationValue.getAllAttributes().get(attributeInfo.getName());
			if (attr.isPrimitive()) {
				Object value = attr.getValue();
				if (value != null) {
					if (value instanceof Boolean) {
						record.setAttribute(attributeInfo.getName(), (Boolean) value); // "false" != false
					} else {
						record.setAttribute(attributeInfo.getName(), value.toString());
					}
				} else {
					record.setAttribute(attributeInfo.getName(), "");
				}
			} else {
				AssociationAttributeInfo associationAttributeInfo = (AssociationAttributeInfo) attributeInfo;
				String displayName = associationAttributeInfo.getFeature().getDisplayAttributeName();
				Object value = attr.getValue();
				if (value != null) {
					if (displayName == null) {
						displayName = associationAttributeInfo.getFeature().getAttributes().get(0).getName();
					}
					switch (associationAttributeInfo.getType()) {
						case MANY_TO_ONE:
							ManyToOneAttribute manyToOneAttribute = (ManyToOneAttribute) attr;
							Object displayValue = manyToOneAttribute.getValue().getAllAttributes().get(displayName)
									.getValue();
							if (displayValue != null) {
								record.setAttribute(attributeInfo.getName(), displayValue.toString());
							} else {
								record.setAttribute(attributeInfo.getName(), "");
							}
							break;
						case ONE_TO_MANY:
							OneToManyAttribute oneToManyAttribute = (OneToManyAttribute) attr;
							List<String> values = new ArrayList<String>();
							for (AssociationValue assoc : oneToManyAttribute.getValue()) {
								Object o = assoc.getAllAttributes().get(displayName).getValue();
								if (o != null) {
									values.add(o.toString());
								}
							}
							record.setAttribute(attributeInfo.getName(), StringUtil.join(values, ","));
							break;
					}
				} else {
					record.setAttribute(attributeInfo.getName(), "");
				}
			}
		}
		record.setAttribute(VALUE_HOLDER_RECORD_ATTRIBUTE, associationValue);
	}

	public AssociationValue getSelectedValue() {
		ListGridRecord record = getSelectedRecord();
		if (record != null) {
			return (AssociationValue) record.getAttributeAsObject(VALUE_HOLDER_RECORD_ATTRIBUTE);
		} else {
			return null;
		}
	}

	public void selectValue(AssociationValue value) {
		for (ListGridRecord record : getRecords()) {
			if (record.getAttributeAsObject(VALUE_HOLDER_RECORD_ATTRIBUTE) == value) {
				selectRecord(record);
			}
		}
	}

	public void updateValue(AssociationValue value) {
		for (ListGridRecord record : getRecords()) {
			if (record.getAttributeAsObject(VALUE_HOLDER_RECORD_ATTRIBUTE) == value) {
				updateAttributes(value, record);
			}
		}
	}

	public List<AssociationValue> getValues() {
		List<AssociationValue> values = new ArrayList<AssociationValue>();
		for (ListGridRecord record : getRecords()) {
			values.add((AssociationValue) record.getAttributeAsObject(VALUE_HOLDER_RECORD_ATTRIBUTE));
		}
		return values;
	}

	/**
	 * Is the grid currently displaying all attributes, instead of only the 'identifying' ones?
	 * 
	 * @return are all attributes displayed
	 */
	public boolean isAllAttributesDisplayed() {
		return allAttributesDisplayed;
	}

	/**
	 * Determine if all attributes of a layer should be shown, or only the 'identifying' ones. Changing this value will
	 * not change the layout of the grid. So set this value in advance.
	 * 
	 * @param allAttributesDisplayed should all attributes be displayed
	 */
	public void setAllAttributesDisplayed(boolean allAttributesDisplayed) {
		this.allAttributesDisplayed = allAttributesDisplayed;
		updateFields();
	}

	/**
	 * Return whether or not the feature's ID's are currently drawn in the grid.
	 * 
	 * @return is feature id displayed
	 */
	public boolean isIdInTable() {
		return idInTable;
	}

	/**
	 * Determine whether or not the feature's ID should be displayed in the grid. This method will immediately update
	 * the entire grid.
	 *
	 * @param idInTable should id be displayed
	 */
	public void setIdInTable(boolean idInTable) {
		this.idInTable = idInTable;
		updateFields();
	}

	public boolean isShowImageAttributeOnHover() {
		return showImageAttributeOnHover;
	}

	public void setShowImageAttributeOnHover(boolean showImageAttributeOnHover) {
		this.showImageAttributeOnHover = showImageAttributeOnHover;
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	/**
	 * Actually create or update the fields.
	 */
	private void updateFields() {
		if (featureInfo != null) {
			// Create a header field for each attribute definition:
			List<ListGridField> fields = new ArrayList<ListGridField>();
			if (idInTable) {
				ListGridField gridField = new ListGridField(ID_NAME, "ID");
				gridField.setAlign(Alignment.LEFT);
				gridField.setCanEdit(false);
				fields.add(gridField);
			}

			for (AttributeInfo attributeInfo : featureInfo.getAttributes()) {
				if (!attributeInfo.isHidden() && (attributeInfo.isIdentifying() || allAttributesDisplayed)) {
					fields.add(createAttributeGridField(attributeInfo));
				}
			}
			setFields(fields.toArray(new ListGridField[fields.size()]));
			setCanResizeFields(true);
		}
	}

	/**
	 * Create a single field definition from a attribute definition.
	 * 
	 * @param attributeInfo attribute info
	 * @return field for grid
	 */
	private ListGridField createAttributeGridField(final AttributeInfo attributeInfo) {
		ListGridField gridField = new ListGridField(attributeInfo.getName(), attributeInfo.getLabel());
		gridField.setAlign(Alignment.LEFT);
		gridField.setCanEdit(false);
		gridField.setShowIfCondition(new IdentifyingListGridFieldIfFunction(attributeInfo.isIdentifying()));

		if (attributeInfo instanceof PrimitiveAttributeInfo) {
			PrimitiveAttributeInfo info = (PrimitiveAttributeInfo) attributeInfo;
			if (info.getType().equals(PrimitiveType.BOOLEAN)) {
				gridField.setType(ListGridFieldType.BOOLEAN);
			} else if (info.getType().equals(PrimitiveType.STRING)) {
				gridField.setType(ListGridFieldType.TEXT);
			} else if (info.getType().equals(PrimitiveType.DATE)) {
				gridField.setType(ListGridFieldType.DATE);
			} else if (info.getType().equals(PrimitiveType.SHORT)) {
				gridField.setType(ListGridFieldType.INTEGER);
			} else if (info.getType().equals(PrimitiveType.INTEGER)) {
				gridField.setType(ListGridFieldType.INTEGER);
			} else if (info.getType().equals(PrimitiveType.LONG)) {
				gridField.setType(ListGridFieldType.INTEGER);
			} else if (info.getType().equals(PrimitiveType.FLOAT)) {
				gridField.setType(ListGridFieldType.FLOAT);
			} else if (info.getType().equals(PrimitiveType.DOUBLE)) {
				gridField.setType(ListGridFieldType.FLOAT);
			} else if (info.getType().equals(PrimitiveType.IMGURL)) {
				gridField.setType(ListGridFieldType.IMAGE);
				if (showImageAttributeOnHover) {
					addCellOverHandler(new ImageCellHandler(attributeInfo));
				}
			} else if (info.getType().equals(PrimitiveType.CURRENCY)) {
				gridField.setType(ListGridFieldType.TEXT);
			} else if (info.getType().equals(PrimitiveType.URL)) {
				gridField.setType(ListGridFieldType.TEXT);
				gridField.setAttribute("text-decoration", "underline");
				addCellClickHandler(new UrlCellHandler(attributeInfo));
			}
		} else if (attributeInfo instanceof AssociationAttributeInfo) {
			gridField.setType(ListGridFieldType.TEXT);
		}
		return gridField;
	}

	/**
	 * Private class, implementing the {@link ListGridFieldIfFunction} interface that determines the visibility of a
	 * grid field, based upon the attribute definition's identifying value, and the {@link FeatureListGrid}'s
	 * <code>allAttributesDisplayed</code> value.
	 * 
	 * @author Pieter De Graef
	 */
	private class IdentifyingListGridFieldIfFunction implements ListGridFieldIfFunction {

		private boolean identifying;

		public IdentifyingListGridFieldIfFunction(boolean identifying) {
			this.identifying = identifying;
		}

		public boolean execute(ListGrid grid, ListGridField field, int fieldNum) {
			if (identifying) {
				return true;
			}
			if (grid instanceof AttributeListGrid) {
				AttributeListGrid table = (AttributeListGrid) grid;
				if (table.isAllAttributesDisplayed()) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * Display the actual image of an image-cell when the mouse goes over it. The image self-destructs after 3 seconds.
	 * 
	 * @author Pieter De Graef
	 */
	private class ImageCellHandler implements CellOverHandler {

		private Timer killTimer;

		private Img img;

		private int row = -1;

		private AttributeInfo attributeInfo;

		ImageCellHandler(AttributeInfo attributeInfo) {
			this.attributeInfo = attributeInfo;
		}

		public void onCellOver(CellOverEvent event) {
			ListGridField gridField = AttributeListGrid.this.getField(event.getColNum());
			if (gridField.getName().equals(attributeInfo.getName())) {
				ListGridRecord record = event.getRecord();
				String value = record.getAttribute(attributeInfo.getName());
				if (event.getRowNum() != row) {
					if (img != null) {
						cleanup();
					}
					img = new Img(value);
					img.setMaxWidth(300);
					img.setMaxHeight(300);
					img.setKeepInParentRect(true);
					img.setShowEdges(true);
					img.setLeft(AttributeListGrid.this.getAbsoluteLeft() + 10);
					img.setTop(AttributeListGrid.this.getAbsoluteTop() + 10);
					img.draw();
					killTimer = new Timer() {

						public void run() {
							img.destroy();
						}
					};
					killTimer.schedule(Math.round(3000));
					row = event.getRowNum();
				}
			}
		}

		private void cleanup() {
			killTimer.cancel();
			img.destroy();
			img = null;
		}
	}

	/**
	 * Open the url in a separate browser window
	 * 
	 * @author Jan De Moerloose
	 */
	private class UrlCellHandler implements CellClickHandler {

		private AttributeInfo attributeInfo;

		UrlCellHandler(AttributeInfo attributeInfo) {
			this.attributeInfo = attributeInfo;
		}

		public void onCellClick(CellClickEvent event) {
			ListGridField gridField = AttributeListGrid.this.getField(event.getColNum());
			if (gridField.getName().equals(attributeInfo.getName())) {
				ListGridRecord record = event.getRecord();
				String value = record.getAttribute(attributeInfo.getName());
				Window.open(value, "urlWindow", null);
			}
		}

	}
}
