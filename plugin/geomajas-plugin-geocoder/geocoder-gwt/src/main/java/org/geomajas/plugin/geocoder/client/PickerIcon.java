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

package org.geomajas.plugin.geocoder.client;

import com.smartgwt.client.widgets.form.fields.FormItemIcon;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;

/**
 * Convenience class for setting pickers for form fields. This is a specialized subclass of FormItemIcon that sets up
 * the correct picker dimensions and spacing.
 * <p/>
 * A set of standard pickers are provided as a part of the library.
 *
 * @author Joachim Van der Auwera
 */
public class PickerIcon extends FormItemIcon {

	public static final Picker CLEAR = new Picker("[SKIN]/pickers/clear_picker.png");
	//public static final Picker COMBO_BOX = new Picker("[SKIN]/pickers/comboBoxPicker.png");
	//public static final Picker DATE = new Picker("[SKIN]/pickers/date_picker.png");
	//public static final Picker REFRESH = new Picker("[SKIN]/pickers/refresh_picker.png");
	public static final Picker SEARCH = new Picker("[SKIN]/pickers/search_picker.png");

	/**
	 * Create a new HeaderControl with the specific icon.
	 *
	 * @param icon the icon
	 */
	public PickerIcon(Picker icon) {
		super();
		setSrc(icon.url);
		setWidth(18);
		setHeight(22);
		setAttribute("hspace", 0);
	}

	/**
	 * Create a new HeaderControl with the specific icon.
	 *
	 * @param icon the icon
	 * @param clickHandler the header control click handler
	 */
	public PickerIcon(Picker icon, FormItemClickHandler clickHandler) {
		this(icon);
		addFormItemClickHandler(clickHandler);
	}

	/**
	 * Class to store the picker URL to force typesafeness.
	 */
	public static class Picker {
		private String url;

		public Picker(String url) {
			this.url = url;
		}

		public String getUrl() {
			return url;
		}
	}
}
