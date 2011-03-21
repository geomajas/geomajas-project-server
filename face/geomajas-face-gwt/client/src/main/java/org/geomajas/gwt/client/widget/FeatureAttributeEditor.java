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

import org.geomajas.configuration.AttributeInfo;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.attribute.AttributeForm;
import org.geomajas.gwt.client.widget.attribute.AttributeFormFactory;
import org.geomajas.gwt.client.widget.attribute.DefaultAttributeFormFactory;

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

	private Feature original;

	private VectorLayer layer;

	private AttributeForm attributeForm;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * <p>
	 * Constructs an attribute form, that may possibly be edited. Actually, depending on the value of the "disabled"
	 * flag, will this widget use a different type of attribute form to display the feature's attribute values in.
	 * </p>
	 * <p>
	 * This constructor will use the {@link DefaultAttributeFormFactory} to create the actual attribute form. If you
	 * want a specific factory to be used instead, see the other constructor.
	 * </p>
	 * 
	 * @param layer
	 *            The vector layer that holds all the attribute definitions for the type of feature to display.
	 * @param disabled
	 *            Should the form initially be disabled or not? When disabled, editing is not possible.
	 */
	public FeatureAttributeEditor(VectorLayer layer, boolean disabled) {
		this(layer, disabled, new DefaultAttributeFormFactory());
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
	 *            The factory that should be used for creating the {@link AttributeForm} in this editor.
	 */
	public FeatureAttributeEditor(VectorLayer layer, boolean disabled, AttributeFormFactory formFactory) {
		this.layer = layer;
		setMembersMargin(0);
		attributeForm = formFactory.createAttributeForm(layer);
		addMember(attributeForm.getWidget());
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
		attributeForm.addItemChangedHandler(handler);
	}

	// -------------------------------------------------------------------------
	// Public methods:
	// -------------------------------------------------------------------------

	/**
	 * Overrides the normal setDisabled method, to not only disable or enable this widget, but to change the actual
	 * attribute form that's being displayed. When disabled is set to true, a {@link SimpleAttributeForm} is used,
	 * otherwise a {@link EditableAttributeForm}.
	 */
	public void setDisabled(boolean disabled) {
		attributeForm.setDisabled(disabled);
		super.setDisabled(disabled);
		if (feature != null) {
			setFeature(feature);
		}
	}

	/**
	 * Validate the form. This only makes sense when the widget is not disabled. Because only then is it possible for a
	 * user to alter the attribute values.
	 * 
	 * @return
	 */
	public boolean validate() {
		return attributeForm.validate();
	}

	/** Resets the original values of the feature. */
	public void reset() {
		if (original != null) {
			feature = (Feature) original.clone();
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
			attributeForm.fromForm(info.getName(), feature.getAttributes().get(info.getName()));
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
			// TODO why do these need to be cloned? document or fix
			this.original = feature.clone();
			this.feature = feature.clone();
			copyToForm(this.feature);
		} else {
			original = null;
			feature = null;
			attributeForm.clear();
		}
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	private void copyToForm(Feature feature) {
		for (AttributeInfo info : layer.getLayerInfo().getFeatureInfo().getAttributes()) {
			attributeForm.toForm(info.getName(), feature.getAttributes().get(info.getName()));
		}
	}
}