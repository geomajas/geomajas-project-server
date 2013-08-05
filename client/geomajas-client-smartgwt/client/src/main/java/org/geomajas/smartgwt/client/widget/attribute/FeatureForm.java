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
package org.geomajas.smartgwt.client.widget.attribute;

import org.geomajas.annotation.Api;
import org.geomajas.smartgwt.client.map.feature.Feature;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.attribute.AssociationValue;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.form.events.HasItemChangedHandlers;
import com.smartgwt.client.widgets.form.events.ItemChangedHandler;

/**
 * <p>
 * A form for editing a feature's first-level attributes or the sub-attributes of an association attribute. The
 * interface is generically defined using the actual Widget class as a parameter. This allows to accommodate different
 * form representations (DynamicForm, Canvas, etc...) without having to resort to casting. The widget is accessible
 * through the getWidget() method.
 * </p>
 * <p>
 * A feature form is responsible for copying attribute date back-and-forth between the form and the actual attribute
 * objects. Feature forms should support validation, clearance, notification of item changes and the possibility to
 * enable/disable editing.
 * </p>
 * <p>
 * Do not implement directly, please extend {@link DefaultFeatureForm}.
 * </p>
 * 
 * @param <W> the widget class of the form
 * 
 * @author Jan De Moerloose
 * @since 1.11.1
 */
@Api(allMethods = true)
public interface FeatureForm<W extends Widget> extends HasItemChangedHandlers {

	/**
	 * Return the actual widget that is used behind the screens. This method is used in the {@link FeatureFormFactory}s
	 * to retrieve the actual form, and apply the definitive form layout.
	 * 
	 * @return the actual widget
	 */
	W getWidget();

	/**
	 * Enable or disable the form for editing. This means that when the form is disabled, no editing is possible.
	 * 
	 * @param disabled Should editing be enabled or disabled?
	 */
	void setDisabled(boolean disabled);

	/**
	 * See if the form is currently disabled or not?
	 * 
	 * @return Is the form currently disabled or not?
	 */
	boolean isDisabled();

	/**
	 * Validate the contents of the entire attribute form and show warning icons.
	 * 
	 * @return Returns true if all values in the form are validated correctly, false otherwise.
	 */
	boolean validate();
	
	/**
	 * Validate the contents of the entire attribute form without showing warnings.
	 * 
	 * @return Returns true if all values in the form are validated correctly, false otherwise.
	 */
	boolean silentValidate();

	/**
	 * Attach a handler that reacts to changes in the fields as the user makes them.
	 * 
	 * @param handler item changed handler
	 * @return handler registration
	 */
	HandlerRegistration addItemChangedHandler(final ItemChangedHandler handler);

	/**
	 * Copy the attribute values from the specified association value to the form.
	 * 
	 * @param value the association value to copy the form values from.
	 */
	void toForm(AssociationValue value);

	/**
	 * Copy the attribute values from the form to the specified association value.
	 * 
	 * @param value the association value to copy the form values to.
	 */
	void fromForm(AssociationValue value);

	/**
	 * Apply an <code>attribute</code> value with the given <code>name</code> onto the form.
	 * 
	 * @param name The name of the attribute to apply onto the form.
	 * @param attribute The attribute value.
	 */
	void toForm(String name, Attribute<?> attribute);

	/**
	 * Get the value of the attribute associated with the given <code>name</code>, and place it in the
	 * <code>attribute</code>.
	 * 
	 * @param name The name of the attribute to get the value from. This value must be fetched from the form.
	 * @param attribute The actual attribute to place the value in.
	 */
	void fromForm(String name, Attribute<?> attribute);

	/**
	 * Put all the attributes from the given feature on the form.
	 *
	 * @param feature feature
	 */
	void toForm(Feature feature);

	/**
	 * Clear all form values.
	 */
	void clear();

}