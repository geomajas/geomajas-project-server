/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.plugin.geocoder.gwt.example.client;

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
	}
}
