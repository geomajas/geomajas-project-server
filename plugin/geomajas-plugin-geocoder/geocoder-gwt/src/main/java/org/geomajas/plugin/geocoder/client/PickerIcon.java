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

package org.geomajas.plugin.geocoder.client;

import com.smartgwt.client.widgets.form.fields.FormItemIcon;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;

/**
 * Convenience class for setting pickers for form fields. This is a specialized subclass of FormItemIcon that sets up
 * the correct picker dimensions and spacing.
 * <p/>
 * A set of standard pickers are provided as a part of the library. Picker URLs are defined in
 * {@link org.geomajas.gwt.client.util.WidgetLayout}.
 * 
 * @author Joachim Van der Auwera
 * @author Jan De Moerloose
 */
public class PickerIcon extends FormItemIcon {

	/**
	 * Create a new HeaderControl with the specific icon.
	 *
	 * @param src the icon source URL
	 */
	public PickerIcon(String src) {
		super();
		setSrc(src);
		setWidth(18);
		setHeight(22);
		setAttribute("hspace", 0);
	}

	/**
	 * Create a new HeaderControl with the specific icon.
	 *
	 * @param src the icon source URL
	 * @param clickHandler the header control click handler
	 */
	public PickerIcon(String src, FormItemClickHandler clickHandler) {
		this(src);
		addFormItemClickHandler(clickHandler);
	}

}
