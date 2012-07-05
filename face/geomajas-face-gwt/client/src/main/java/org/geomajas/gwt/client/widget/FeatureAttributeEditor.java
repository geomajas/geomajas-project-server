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

import org.geomajas.configuration.AttributeInfo;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.attribute.DefaultFeatureFormFactory;
import org.geomajas.gwt.client.widget.attribute.FeatureForm;
import org.geomajas.gwt.client.widget.attribute.FeatureFormFactory;

import com.smartgwt.client.widgets.form.events.ItemChangedHandler;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p>
 * An attribute form widget, that may possibly be edited. Actually, depending on the value of the "disabled" flag, will
 * this widget use a different type of attribute form to display the feature's attribute values in.
 * </p>
 * 
 * @author Jan De Moerloose
 * @author Pieter De Graef
 */
public class FeatureAttributeEditor extends VLayout {

	private Feature feature;

	private VectorLayer layer;

	private FeatureForm<?> featureForm;
	
	private boolean disabled;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * <p>
	 * Constructs an attribute form, that may possibly be edited. Actually, depending on the value of the "disabled"
	 * flag, will this widget use a different type of attribute form to display the feature's attribute values in.
	 * </p>
	 * <p>
	 * This constructor will use the {@link DefaultFeatureFormFactory} to create the actual attribute form. If you want
	 * a specific factory to be used instead, see the other constructor.
	 * </p>
	 * 
	 * @param layer
	 *            The vector layer that holds all the attribute definitions for the type of feature to display.
	 * @param disabled
	 *            Should the form initially be disabled or not? When disabled, editing is not possible.
	 */
	public FeatureAttributeEditor(VectorLayer layer, boolean disabled) {
		this(layer, disabled, new DefaultFeatureFormFactory());
	}

	/**
	 * Constructs an attribute form, that may possibly be edited. Actually, depending on the value of the "disabled"
	 * flag, will this widget use a different type of attribute form to display the feature's attribute values in.
	 * 
	 * @param layer
	 *            The vector layer that holds all the attribute definitions for the type of feature to display.
	 * @param disabled
	 *            Should the form initially be disabled or not? When disabled, editing is not possible.
	 * @param formFactory
	 *            The factory that should be used for creating the {@link DefaultFeatureForm} in this editor.
	 */
	public FeatureAttributeEditor(VectorLayer layer, boolean disabled, FeatureFormFactory<?> formFactory) {
		this.layer = layer;
		setMembersMargin(0);
		featureForm = formFactory.createFeatureForm(layer);
		addMember(featureForm.getWidget());
		setDisabled(disabled);
	}

	// -------------------------------------------------------------------------
	// HasItemChangedHandlers implementation:
	// -------------------------------------------------------------------------

	/**
	 * Add a handler to the change events of the attribute values in the form. Note that editing is only possible when
	 * this widget is not disabled.
	 */
	public void addItemChangedHandler(ItemChangedHandler handler) {
		featureForm.addItemChangedHandler(handler);
	}

	// -------------------------------------------------------------------------
	// Public methods:
	// -------------------------------------------------------------------------

	/**
	 * Overrides the normal setDisabled method and propagates the state to the form. In this way forms can implement
	 * custom item enabling/disabling behavior if required.
	 * 
	 * @param disabled editable state of this editor
	 */
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
		featureForm.setDisabled(disabled);
	}
	
	/**
	 * Overrides the normal isDisabled method.
	 * 
	 * @return true if in editable state.
	 */
	public Boolean isDisabled() {
		return disabled;
	}
	

	/**
	 * Validate the form. This only makes sense when the widget is not disabled. Because only then is it possible for a
	 * user to alter the attribute values.
	 * 
	 * @return true when validation succeeded
	 */
	public boolean validate() {
		return featureForm.validate();
	}

	/** Resets the original values of the feature. */
	public void reset() {
		if (feature != null) {
			copyToForm(feature);
		}
	}

	/**
	 * Return the feature, with the current values in the "editable" form. This feature will not necessarily contain
	 * validated attribute values, so it is recommended to call the <code>validate</code> method first.
	 * 
	 * @return
	 */
	public Feature getFeature() {
		for (AttributeInfo info : layer.getLayerInfo().getFeatureInfo().getAttributes()) {
			featureForm.fromForm(info.getName(), feature.getAttributes().get(info.getName()));
		}
		return feature;
	}

	/**
	 * Apply a new feature onto this widget. The feature will be immediately shown on the attribute form.
	 * 
	 * @param feature
	 *            feature
	 */
	public void setFeature(Feature feature) {
		if (feature != null) {
			this.feature = feature;
			copyToForm(this.feature);
		} else {
			this.feature = null;
			featureForm.clear();
		}
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	private void copyToForm(Feature feature) {
		featureForm.toForm(feature);
	}
}