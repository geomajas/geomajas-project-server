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

package org.geomajas.gwt.client.widget;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.global.Api;
import org.geomajas.global.GeomajasConstant;
import org.geomajas.gwt.client.action.menu.SaveEditingAction;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.MapView.ZoomOption;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.feature.LazyLoadCallback;
import org.geomajas.gwt.client.map.feature.LazyLoader;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.widget.attribute.AttributeFormFactory;
import org.geomajas.gwt.client.widget.attribute.DefaultAttributeFormFactory;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.form.events.ItemChangedEvent;
import com.smartgwt.client.widgets.form.events.ItemChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * <p>
 * The <code>FeatureAttributeWindow</code> is a floating window that uses a
 * {@link org.geomajas.gwt.client.widget.FeatureAttributeEditor} to change the alpha-numerical attributes of a feature
 * and persist these changes on the server. In essence, this widget is a
 * {@link org.geomajas.gwt.client.widget.FeatureAttributeEditor} with some extra buttons. One of these extra buttons is
 * a "save" button to actually save the widget. When setting a feature, the underlying
 * {@link org.geomajas.gwt.client.widget.FeatureAttributeEditor} automatically creates a clone. That way you're not
 * editing the feature directly, and changes are only applied when the save is clicked. This widget will also check
 * whether or not all fields are valid, and will not allow saving when at least one of the fields is not valid.
 * </p>
 * <p>
 * On top of that, this widget has a few options regarding the editing of a feature's attributes:
 * <ul>
 * <li><b>editingAllowed</b>: Is editing �berhaupt allowed? This must be set BEFORE the widget is actually drawn,
 * because afterwards it won't have any effect anymore.</li>
 * <li><b>editingEnabled</b>: Is editing currently enabled or not? This widget can toggle this value on the fly. When
 * editing is enabled, it will display an editable attribute form with save, cancel and reset buttons. When editing is
 * not enabled, these buttons will disappear, and a simple attribute form is shown that displays the attribute values,
 * but does not allow for editing.</li>
 * </ul>
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.6.0
 */
@Api
public class FeatureAttributeWindow extends Window {

	/**
	 * The ToolStrip that contains the different buttons (save, reset, ...).
	 */
	private ToolStrip toolStrip;

	/**
	 * The editor instance that actually does the attribute editing and validation.
	 */
	private FeatureAttributeEditor attributeTable;

	private AttributeFormFactory factory;

	private boolean editingAllowed;

	private boolean editingEnabled;

	/**
	 * Reference to the MapModel. Needed when we want to create a
	 * {@link org.geomajas.gwt.client.map.feature.FeatureTransaction} to actually save changes.
	 */
	private MapModel mapModel;

	private IButton saveButton;

	private IButton resetButton;

	private IButton cancelButton;

	private IButton editButton;

	private HLayout savePanel;

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	/**
	 * Create an instance and immediately apply a feature onto it. Also specify whether or not editing is allowed. By
	 * default this constructor will use a {@link DefaultAttributeFormFactory} to create the attribute form. If you want
	 * to have some influence on how the feature attribute form should look, than use the other constructor.
	 * 
	 * @param feature
	 *            The feature instance to display and/or edit.
	 * @param editingAllowed
	 *            This must be set before the widget is actually drawn, so it makes sense to set it at construction
	 *            time.
	 * @since 1.6.0
	 */
	@Api
	public FeatureAttributeWindow(Feature feature, boolean editingAllowed) {
		this(feature, editingAllowed, new DefaultAttributeFormFactory());
	}

	/**
	 * Create an instance and immediately apply a feature onto it. Also specify whether or not editing is allowed.
	 * 
	 * @param feature
	 *            The feature instance to display and/or edit.
	 * @param editingAllowed
	 *            This must be set before the widget is actually drawn, so it makes sense to set it at construction
	 *            time.
	 * @param factory
	 *            A specific factory for creating the attribute forms within this widget.
	 * @since 1.9.0
	 */
	@Api
	public FeatureAttributeWindow(Feature feature, boolean editingAllowed, AttributeFormFactory factory) {
		super();
		if (factory == null) {
			this.factory = new DefaultAttributeFormFactory();
		} else {
			this.factory = factory;
		}
		setEditingAllowed(editingAllowed);
		if (feature != null) {
			buildWidget(feature.getLayer());
			setFeature(feature);
		}
	}

	// -------------------------------------------------------------------------
	// Public methods:
	// -------------------------------------------------------------------------

	/**
	 * Validate the current attribute values on display. Returns true if all attribute values have been validated, false
	 * otherwise.
	 * 
	 * @return
	 */
	public boolean validate() {
		if (attributeTable != null) {
			return attributeTable.validate();
		}
		return false;
	}

	/**
	 * Get the feature with attribute values as they are displayed in this widget. When editing is enabled, it may be
	 * possible that this feature will hold values that have not been validated, so it is recommended to run the
	 * <code>validate</code> method first.
	 * 
	 * @return
	 */
	public Feature getFeature() {
		if (attributeTable != null) {
			return attributeTable.getFeature();
		}
		return null;
	}

	/**
	 * Apply a new feature onto this widget, assuring the attributes are loaded.
	 * 
	 * @param feature
	 *            feature
	 */
	public void setFeature(Feature feature) {
		List<Feature> features = new ArrayList<Feature>();
		features.add(feature);
		LazyLoader.lazyLoad(features, GeomajasConstant.FEATURE_INCLUDE_ATTRIBUTES, new LazyLoadCallback() {

			public void execute(List<Feature> response) {
				Feature feature = response.get(0);
				if (attributeTable == null) {
					buildWidget(feature.getLayer());
				}
				if (feature != null) {
					setTitle(I18nProvider.getAttribute().getAttributeWindowTitle(feature.getLabel()));
				} else {
					setTitle(I18nProvider.getAttribute().getAttributeWindowTitle(""));
				}
				attributeTable.setFeature(feature);
			}
		});
	}

	/**
	 * Is editing allowed? This must be set BEFORE the widget is actually drawn, because afterwards it won't have any
	 * effect anymore.
	 * 
	 * @return true when editing is allowed
	 */
	public boolean isEditingAllowed() {
		return editingAllowed;
	}

	/**
	 * Is editing allowed? This must be set BEFORE the widget is actually drawn, because afterwards it won't have any
	 * effect anymore.
	 * 
	 * @param editingAllowed
	 *            editing allowed status
	 */
	public void setEditingAllowed(boolean editingAllowed) {
		this.editingAllowed = editingAllowed;
	}

	/**
	 * Is editing currently enabled or not? This widget can toggle this value on the fly. When editing is enabled, it
	 * will display an editable attribute form with save, cancel and reset buttons. When editing is not enabled, these
	 * buttons will disappear, and a simple attribute form is shown that displays the attribute values, but does not
	 * allow for editing.
	 * 
	 * @return
	 */
	public boolean isEditingEnabled() {
		return editingEnabled;
	}

	/**
	 * Is editing currently enabled or not? This widget can toggle this value on the fly. When editing is enabled, it
	 * will display an editable attribute form with save, cancel and reset buttons. When editing is not enabled, these
	 * buttons will disappear, and a simple attribute form is shown that displays the attribute values, but does not
	 * allow for editing.
	 * 
	 * @param editingEnabled
	 *            editing enabled status
	 */
	public void setEditingEnabled(boolean editingEnabled) {
		if (isEditingAllowed()) {
			this.editingEnabled = editingEnabled;
			if (editingEnabled) {
				savePanel.setVisible(true);
				if (attributeTable != null && attributeTable.isDisabled()) {
					attributeTable.setDisabled(false);
				}
			} else {
				savePanel.setVisible(false);
				if (attributeTable != null && !attributeTable.isDisabled()) {
					attributeTable.setDisabled(true);
				}
			}
		}
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	/**
	 * Build the entire widget.
	 * 
	 * @param layer
	 *            layer
	 */
	private void buildWidget(VectorLayer layer) {
		mapModel = layer.getMapModel();

		setAutoSize(true);
		setTitle(I18nProvider.getAttribute().getAttributeWindowTitle(""));
		setCanDragReposition(true);
		setCanDragResize(true);

		attributeTable = new FeatureAttributeEditor(layer, true, factory);

		toolStrip = new ToolStrip();
		toolStrip.setWidth100();
		toolStrip.setPadding(2);

		toolStrip.addMember(new ZoomButton());
		editButton = new EditButton();
		LayoutSpacer spacer = new LayoutSpacer();
		spacer.setWidth(2);
		toolStrip.addMember(spacer);
		if (editingAllowed) {
			toolStrip.addMember(editButton);
		}

		savePanel = new HLayout(10);
		saveButton = new SaveButton();
		resetButton = new ResetButton();
		cancelButton = new CancelButton();
		savePanel.addMember(saveButton);
		savePanel.addMember(resetButton);
		savePanel.addMember(cancelButton);
		savePanel.setVisible(false);
		savePanel.setAlign(Alignment.CENTER);
		savePanel.setPadding(10);

		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.addMember(toolStrip);
		layout.addMember(attributeTable);
		layout.addMember(savePanel);

		VLayout bottom = new VLayout();
		bottom.setHeight100();
		layout.addMember(bottom);
		layout.setWidth(450);
		addItem(layout);

		// Set the save button as disabled at startup:
		addDrawHandler(new DrawHandler() {

			public void onDraw(DrawEvent event) {
				saveButton.setDisabled(true);
			}
		});
	}

	// -------------------------------------------------------------------------
	// Private class EditButton:
	// -------------------------------------------------------------------------

	/** Definition of the edit button, that switches on editing. (editingEnabled=true) */
	private class EditButton extends IButton implements com.smartgwt.client.widgets.events.ClickHandler {

		public EditButton() {
			setIcon("[ISOMORPHIC]/geomajas/osgeo/edit.png");
			setShowDisabledIcon(false);
			setTitle(I18nProvider.getAttribute().btnEditTitle());
			setTooltip(I18nProvider.getAttribute().btnEditTooltip());
			addClickHandler(this);
			setWidth(80);
		}

		public void onClick(ClickEvent event) {
			if (isEditingEnabled()) {
				setEditingEnabled(false);
				editButton.setDisabled(false);
			} else {
				setEditingEnabled(true);
				editButton.setDisabled(true);
				saveButton.setDisabled(true);
			}
		}

	}

	// -------------------------------------------------------------------------
	// Private class ZoomButton:
	// -------------------------------------------------------------------------

	/** Definition of the zoom button that zooms to the feature on the map. */
	private class ZoomButton extends IButton implements com.smartgwt.client.widgets.events.ClickHandler {

		public ZoomButton() {
			setIcon("[ISOMORPHIC]/geomajas/osgeo/zoom-selection.png");
			setShowDisabledIcon(false);
			setTitle(I18nProvider.getAttribute().btnZoomFeature());
			setTooltip(I18nProvider.getAttribute().btnZoomTooltip());
			addClickHandler(this);
			setWidth(150);
		}

		public void onClick(ClickEvent event) {
			Bbox bounds = getFeature().getGeometry().getBounds();
			mapModel.getMapView().applyBounds(bounds, ZoomOption.LEVEL_FIT);
		}
	}

	// -------------------------------------------------------------------------
	// Private class SaveButton:
	// -------------------------------------------------------------------------

	/** Definition of the Save button. */
	private class SaveButton extends IButton implements com.smartgwt.client.widgets.events.ClickHandler {

		public SaveButton() {
			setIcon("[ISOMORPHIC]/geomajas/osgeo/save1.png");
			setShowDisabledIcon(false);
			setTitle(I18nProvider.getAttribute().btnSaveTitle());
			setTooltip(I18nProvider.getAttribute().btnSaveTooltip());
			attributeTable.addItemChangedHandler(new ItemChangedHandler() {

				public void onItemChanged(ItemChangedEvent event) {
					if (attributeTable.validate()) {
						setDisabled(false);
					} else {
						setDisabled(true);
					}
				}
			});
			addClickHandler(this);
		}

		public void onClick(ClickEvent event) {
			mapModel.getFeatureEditor().startEditing(new Feature[] { getFeature() }, new Feature[] { getFeature() });
			SaveEditingAction action = new SaveEditingAction(mapModel);
			action.onClick(null);

			setEditingEnabled(false);
			editButton.setDisabled(false);
		}
	}

	// -------------------------------------------------------------------------
	// Private class ResetButton:
	// -------------------------------------------------------------------------

	/** Definition of the Reset button that resets the features original attribute values. */
	private class ResetButton extends IButton implements com.smartgwt.client.widgets.events.ClickHandler {

		public ResetButton() {
			setIcon("[ISOMORPHIC]/geomajas/osgeo/undo.png");
			setShowDisabledIcon(false);
			setTitle(I18nProvider.getAttribute().btnResetTitle());
			setTooltip(I18nProvider.getAttribute().btnResetTooltip());
			addClickHandler(this);
		}

		public void onClick(ClickEvent event) {
			attributeTable.reset();
		}
	}

	// -------------------------------------------------------------------------
	// Private class CancelButton:
	// -------------------------------------------------------------------------

	/** Definition of the cancel button that cancels editing (editingEnabled=false). */
	private class CancelButton extends IButton implements com.smartgwt.client.widgets.events.ClickHandler {

		public CancelButton() {
			setIcon("[ISOMORPHIC]/geomajas/osgeo/quit.png");
			setShowDisabledIcon(false);
			setTitle(I18nProvider.getAttribute().btnCancelTitle());
			setTooltip(I18nProvider.getAttribute().btnCancelTooltip());
			addClickHandler(this);
		}

		public void onClick(ClickEvent event) {
			attributeTable.reset();
			setEditingEnabled(false);
			editButton.setDisabled(false);
		}
	}
}
