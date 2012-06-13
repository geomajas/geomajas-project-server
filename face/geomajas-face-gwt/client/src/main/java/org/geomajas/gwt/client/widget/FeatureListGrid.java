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

package org.geomajas.gwt.client.widget;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.AssociationAttributeInfo;
import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.PrimitiveType;
import org.geomajas.global.GeomajasConstant;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.event.FeatureDeselectedEvent;
import org.geomajas.gwt.client.map.event.FeatureSelectedEvent;
import org.geomajas.gwt.client.map.event.FeatureSelectionHandler;
import org.geomajas.gwt.client.map.event.FeatureTransactionEvent;
import org.geomajas.gwt.client.map.event.FeatureTransactionHandler;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.feature.LazyLoadCallback;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.util.StringUtil;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.attribute.AssociationValue;
import org.geomajas.layer.feature.attribute.ManyToOneAttribute;
import org.geomajas.layer.feature.attribute.OneToManyAttribute;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridFieldIfFunction;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellOverEvent;
import com.smartgwt.client.widgets.grid.events.CellOverHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;

/**
 * <p>
 * This widget represents a grid of feature attributes, where many different features from a single layer are shown in
 * rows on the grid. Each row represents a feature. To start working with this grid, it is necessary to first set a
 * layer, so this widget can build it's grid header, and so it knows what type of data to expect.
 * </p>
 * <p>
 * Furthermore this grid has a few options to determine it's looks (on top of the basic SmartGWT options).
 * <ul>
 * <li><i>selectionEnabled</i>: when true, selected rows in the grid will result in selected features in the MapModel
 * and vice versa.</li>
 * <li><i>allAttributesDisplayed</i>: show all attributes (true) or only the 'identifying' attributes (false)?</li>
 * <li><i>editingEnabled</i>: determines whether or not editing the attributes is allowed. When double clicking a row in
 * the table, a {@link FeatureAttributeWindow} will appear, containing the feature of the row upon which was double
 * clicked. This setting determines if the window allows editing or not.</li>
 * <li><i>idInTable</i>: show the feature's ID in the table. This is false by default, and should not really be
 * necessary.</li>
 * </ul>
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.10.0
 */
@Api
public class FeatureListGrid extends ListGrid implements FeatureSelectionHandler, SelectionChangedHandler {

	/** Key which is used to store the feature id in the records. */
	@Api
	public static final String FIELD_NAME_FEATURE_ID = "featureId";
	
	private MapModel mapModel;

	/**
	 * Reference to the VectorLayer for whom features can be displayed. The grid header will show the labels of the
	 * attribute definitions for this layer.
	 */
	private VectorLayer layer;

	private boolean selectionEnabled;

	/**
	 * Show all attributes (true) or only the 'identifying' attributes (false)?
	 */
	private boolean allAttributesDisplayed;

	/**
	 * Determines whether or not editing the attributes is allowed. When double clicking a row in the table, a
	 * {@link FeatureAttributeWindow} will appear, containing the feature of the row upon which was double clicked. This
	 * setting determines if the window allows editing or not.
	 */
	private boolean editingEnabled;

	/**
	 * Registration for the selection event handler onto the map model.
	 */
	private HandlerRegistration selectionRegistration;

	/**
	 * Registration for selection of rows within this grid.
	 */
	private HandlerRegistration gridSelectionRegistration;

	/**
	 * Show the feature's ID in the table. This is false by default, and should not really be necessary.
	 */
	private boolean idInTable;

	/**
	 * When hovering over image attributes, should they be shown in floating panels or not?
	 */
	private boolean showImageAttributeOnHover;

	/**
	 * Used to check on doubles.
	 */
	private List<String> featureIds;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	public FeatureListGrid(MapModel mapModel) {
		this(mapModel, null);
	}

	/**
	 * @param mapModel
	 *            If this is null it throws an IllegalArgumentException
	 * @param doubleClickhandler
	 *            It gives the possibility to show your own {@link FeatureAttributeWindow}. If this is null the default
	 *            {@link FeatureAttributeWindow} will be shown
	 */
	public FeatureListGrid(MapModel mapModel, DoubleClickHandler doubleClickhandler) {
		super();
		if (mapModel == null) {
			throw new IllegalArgumentException("The given MapModel should not be 'null'.");
		}
		this.mapModel = mapModel;
		this.mapModel.addFeatureTransactionHandler(new ApplyFeatureTransactionToGrid());
		setShowEmptyMessage(true);
		setIdInTable(false);
		setEditingEnabled(false);
		setSelectionEnabled(true);
		if (doubleClickhandler != null) {
			addDoubleClickHandler(doubleClickhandler);
		} else {
			addDoubleClickHandler(new FeatureDoubleClickHandler());
		}
	}

	// -------------------------------------------------------------------------
	// Class specific methods:
	// -------------------------------------------------------------------------

	/**
	 * Empty the grid, thereby removing all rows. It does not clear the header though.
	 */
	public void empty() {
		setData(new ListGridRecord[] {});
		featureIds = new ArrayList<String>();
	}

	/**
	 * Adds a new feature to the list. A {@link VectorLayer} must have been set first, and the feature must belong to
	 * that VectorLayer.
	 * 
	 * @param feature
	 *            The feature to be added to the list.
	 * @return Returns true in case of success, and false if the feature is already in the list or the feature is null
	 *         or if the feature does not belong to the correct layer or if the layer has not yet been set.
	 */
	public boolean addFeature(Feature feature) {
		// Basic checks:
		if (feature == null || layer == null || !feature.getLayer().getId().equals(layer.getId())) {
			return false;
		}

		// Does feature already exist?
		if (featureIds.contains(feature.getId())) {
			return false;
		}
		featureIds.add(feature.getId());

		// Feature checks out, add it to the grid:
		ListGridRecord record = new ListGridRecord();
		record.setAttribute(FIELD_NAME_FEATURE_ID, feature.getId());
		copyToRecord(feature, record);
		addData(record);
		return true;
	}

	private void copyToRecord(Feature feature, ListGridRecord record) {
		for (AttributeInfo attributeInfo : layer.getLayerInfo().getFeatureInfo().getAttributes()) {
			String attributeName = attributeInfo.getName();
			if (allAttributesDisplayed || attributeInfo.isIdentifying()) {
				Attribute<?> attr = feature.getAttributes().get(attributeName);
				if (attr.isPrimitive()) {
					Object value = attr.getValue();
					if (value != null) {
						if (value instanceof Boolean) {
							record.setAttribute(attributeName, (Boolean) value); // "false" != false
						} else if (value instanceof Number) {
							record.setAttribute(attributeName, (Number) value); 
						} else if (value instanceof Date) { // java.util
							record.setAttribute(attributeName, (Date) value);
						} else {
							record.setAttribute(attributeName, value.toString());
						}
					} else {
						record.setAttribute(attributeName, "");
					}
				} else {
					AssociationAttributeInfo associationAttributeInfo = (AssociationAttributeInfo) attributeInfo;
					FeatureInfo featureInfo = associationAttributeInfo.getFeature();
					String displayName = featureInfo.getDisplayAttributeName();
					if (displayName == null) {
						displayName = featureInfo.getAttributes().get(0).getName();
					}
					switch (associationAttributeInfo.getType()) {
						case MANY_TO_ONE:
							ManyToOneAttribute manyToOneAttribute = (ManyToOneAttribute) attr;
							AssociationValue attributeValue = manyToOneAttribute.getValue();
							if (null != attributeValue) {
								Object value = attributeValue.getAllAttributes().get(displayName).getValue();
								if (value != null) {
									record.setAttribute(attributeName, value.toString());
								} else {
									record.setAttribute(attributeName, "");
								}
							}
							break;
						case ONE_TO_MANY:
							OneToManyAttribute oneToManyAttribute = (OneToManyAttribute) attr;
							List<String> values = new ArrayList<String>();
							List<AssociationValue> associationValues = oneToManyAttribute.getValue();
							if (null != associationValues) {
								for (AssociationValue assoc : associationValues) {
									Object o = assoc.getAllAttributes().get(displayName).getValue();
									if (o != null) {
										values.add(o.toString());
									}
								}
								record.setAttribute(attributeName, StringUtil.join(values, ","));
							}
							break;
						default:
							throw new IllegalStateException("Unknown switch value " +
									associationAttributeInfo.getType());
					}
				}
			}
		}
	}

	/**
	 * Retrieve the <code>VectorLayer</code> that's currently applied on this grid.
	 * 
	 * @return Returns the layer, or null if no layer has been set yet.
	 */
	public VectorLayer getLayer() {
		return layer;
	}

	/**
	 * Apply a new layer onto the grid. This means that the table header will immediately take on the attributes of this
	 * new layer.
	 * 
	 * @param layer
	 *            The layer from whom to display the features. If this is null, the table will be cleared.
	 */
	public void setLayer(VectorLayer layer) {
		this.layer = layer;
		if (layer == null) {
			clear();
		} else {
			empty();
			updateFields();
		}
	}

	/**
	 * Is this grid currently tied in to the selection of features in the {@link MapModel}? What this means is that when
	 * selection is enabled, selected rows in the grid will result in selected features in the MapModel and vice versa.
	 * 
	 * @return is selection enabled?
	 */
	public boolean isSelectionEnabled() {
		return selectionEnabled;
	}

	/**
	 * Adds or removes this widget as a handler for selection onto the MapModel. What this means is that when selection
	 * is enabled, selected rows in the grid will result in selected features in the MapModel and vice versa.
	 * 
	 * @param selectionEnabled
	 *            is selection enabled?
	 */
	public void setSelectionEnabled(boolean selectionEnabled) {
		// Clean up first! Otherwise the handler list would just keep on growing.
		if (selectionRegistration != null) {
			selectionRegistration.removeHandler();
			selectionRegistration = null;
		}
		if (gridSelectionRegistration != null) {
			gridSelectionRegistration.removeHandler();
			gridSelectionRegistration = null;
		}
		this.selectionEnabled = selectionEnabled;

		// If enabled renew the handlers, and adjust grid selection type:
		if (selectionEnabled) {
			setAttribute("selectionType", SelectionStyle.MULTIPLE.getValue(), true);
			selectionRegistration = mapModel.addFeatureSelectionHandler(this);
			gridSelectionRegistration = addSelectionChangedHandler(this);
		} else {
			setAttribute("selectionType", SelectionStyle.NONE.getValue(), true);
		}
	}

	/**
	 * Does nothing anymore! Selection is now handled through the "selectionEnabled" flag. This is done so that
	 * selection in this grid can match selection of features in the {@link MapModel}.
	 */
	@Override
	public void setSelectionType(SelectionStyle selectionType) {
		// Disable this setting!!
	}

	/**
	 * Determines whether or not editing the attributes is allowed. When double clicking a row in the table, a
	 * {@link FeatureAttributeWindow} will appear, containing the feature of the row upon which was double clicked. This
	 * setting determines if the window allows editing or not.
	 *
	 * @return true when editing attributes is allowed
	 */
	public boolean isEditingEnabled() {
		return editingEnabled;
	}

	/**
	 * Determines whether or not editing the attributes is allowed. When double clicking a row in the table, a
	 * {@link FeatureAttributeWindow} will appear, containing the feature of the row upon which was double clicked. This
	 * setting determines if the window allows editing or not.
	 * 
	 * @param editingEnabled
	 *            The new value
	 */
	public void setEditingEnabled(boolean editingEnabled) {
		this.editingEnabled = editingEnabled;
	}

	/**
	 * Is the grid currently displaying all attributes, instead of only the 'identifying' ones?
	 * 
	 * @return true when all attributes are displayed
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
	 * @return true when feature id included in table
	 */
	public boolean isIdInTable() {
		return idInTable;
	}

	/**
	 * Determine whether or not the feature's ID should be displayed in the grid. This method will immediately update
	 * the entire grid.
	 *
	 * @param idInTable should id be included in table
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
	// FeatureSelectionHandler implementation:
	// -------------------------------------------------------------------------

	/**
	 * This method is used only when selection is enabled (see setSelectionEnabled). When a feature deselection event is
	 * sent out from the MapModel, check if we have that row selected and deselect it.
	 */
	public void onFeatureDeselected(FeatureDeselectedEvent event) {
		Feature feature = event.getFeature();

		// Only deselect if it is actually selected:
		boolean selected = false;
		ListGridRecord[] selections = getSelection();
		for (ListGridRecord selection : selections) {
			if (selection.getAttribute(FIELD_NAME_FEATURE_ID).equals(feature.getId())) {
				selected = true;
				break;
			}
		}

		// If selected, find the correct row and deselect:
		if (selected) {
			ListGridRecord[] records = this.getRecords();
			for (ListGridRecord record : records) {
				if (record.getAttribute(FIELD_NAME_FEATURE_ID).equals(feature.getId())) {
					deselectRecord(record);
					break;
				}
			}
		}
	}

	/**
	 * This method is used only when selection is enabled (see setSelectionEnabled). When a feature selection event is
	 * sent out from the MapModel, check if we have that row deselected and select it.
	 */
	public void onFeatureSelected(FeatureSelectedEvent event) {
		Feature feature = event.getFeature();

		// Only select if it is actually deselected:
		boolean selected = false;
		ListGridRecord[] selections = getSelection();
		for (ListGridRecord selection : selections) {
			if (selection.getAttribute(FIELD_NAME_FEATURE_ID).equals(feature.getId())) {
				selected = true;
				break;
			}
		}

		// If deselected, find the correct row and select:
		if (!selected) {
			ListGridRecord[] records = this.getRecords();
			for (int i = 0; i < records.length; i++) {
				if (records[i].getAttribute(FIELD_NAME_FEATURE_ID).equals(feature.getId())) {
					selectRecord(i);
					break;
				}
			}
		}
	}

	// -------------------------------------------------------------------------
	// SelectionChangedHandler implementation:
	// -------------------------------------------------------------------------

	/**
	 * This method is used only when selection is enabled (see setSelectionEnabled). When the user selects or deselect a
	 * row in the grid, the feature it represents should also be selected or deselected in the MapModel.
	 */
	public void onSelectionChanged(SelectionEvent event) {
		Record record = event.getRecord();
		String featureId = record.getAttribute(FIELD_NAME_FEATURE_ID);

		// Check if selection and deselection are really necessary, to avoid useless events.
		if (event.getState()) {
			// Only select a feature if it is not yet selected:
			if (!layer.isFeatureSelected(featureId)) {
				layer.getFeatureStore().getFeature(featureId, GeomajasConstant.FEATURE_INCLUDE_ALL,
						new LazyLoadCallback() {

							public void execute(List<Feature> response) {
								layer.selectFeature(response.get(0));
							}
						});
			}
		} else {
			// Only deselect a feature if it is not yet deselected:
			if (layer.isFeatureSelected(featureId)) {
				layer.getFeatureStore().getFeature(featureId, GeomajasConstant.FEATURE_INCLUDE_ALL,
						new LazyLoadCallback() {

							public void execute(List<Feature> response) {
								layer.deselectFeature(response.get(0));
							}
						});
			}
		}
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	/**
	 * Actually create or update the fields.
	 */
	private void updateFields() {
		if (layer != null) {
			// Create a header field for each attribute definition:
			List<ListGridField> fields = new ArrayList<ListGridField>();
			if (idInTable) {
				ListGridField gridField = new ListGridField(FIELD_NAME_FEATURE_ID, "ID");
				gridField.setAlign(Alignment.LEFT);
				gridField.setCanEdit(false);
				fields.add(gridField);
			}

			for (AttributeInfo attributeInfo : layer.getLayerInfo().getFeatureInfo().getAttributes()) {
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
	 * @param attributeInfo
	 *            attribute info
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
				gridField.setType(ListGridFieldType.LINK);
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
			if (grid instanceof FeatureListGrid) {
				FeatureListGrid table = (FeatureListGrid) grid;
				if (table.isAllAttributesDisplayed()) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * Handler for double click on feature. Shows the feature edit window.
	 */
	private class FeatureDoubleClickHandler implements DoubleClickHandler {

		/**
		 * Implementation of the {@link DoubleClickHandler}, that will show a {@link FeatureAttributeWindow} containing
		 * the feature of the row upon which was double clicked. This happens only if the
		 */
		public void onDoubleClick(DoubleClickEvent event) {
			ListGridRecord selected = getSelectedRecord();
			String featureId = selected.getAttribute(FIELD_NAME_FEATURE_ID);
			if (featureId != null && layer != null) {
				layer.getFeatureStore().getFeature(featureId, GeomajasConstant.FEATURE_INCLUDE_ATTRIBUTES,
						new LazyLoadCallback() {

							public void execute(List<Feature> response) {
								FeatureAttributeWindow window = new FeatureAttributeWindow(response.get(0),
										editingEnabled);
								window.centerInPage();
								window.draw();
							}
						});
			}
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
			ListGridField gridField = FeatureListGrid.this.getField(event.getColNum());
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
					img.setLeft(FeatureListGrid.this.getAbsoluteLeft() + 10);
					img.setTop(FeatureListGrid.this.getAbsoluteTop() + 10);
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
	 * Applies the feature transaction to the grid.
	 * 
	 * @author Jan De Moerloose
	 */
	private class ApplyFeatureTransactionToGrid implements FeatureTransactionHandler {

		public void onTransactionSuccess(FeatureTransactionEvent event) {
			List<ListGridRecord> updates = new ArrayList<ListGridRecord>();
			for (ListGridRecord record : getRecords()) {
				// update record if we can (feature in store) or leave as-is if we can't
				String featureId = record.getAttribute(FIELD_NAME_FEATURE_ID);
				Feature feature = layer.getFeatureStore().getPartialFeature(featureId);
				if (feature != null && feature.isAttributesLoaded()) {
					copyToRecord(feature, record);
				}
				updates.add(record);
			}
			setData(updates.toArray(new Record[updates.size()]));
		}

	}

}
