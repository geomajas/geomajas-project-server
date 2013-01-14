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
package org.geomajas.plugin.deskmanager.client.gwt.manager.common;

import org.geomajas.gwt.client.widget.ScaleConverter;

import com.smartgwt.client.widgets.form.validator.CustomValidator;

/**
 * (copied from Geomajas, is a private class so can't use it). Custom validation of user entered scale
 * 
 * @author Oliver May
 */
public class ScaleValidator extends CustomValidator {

	@Override
	protected boolean condition(Object value) {
		try {
			return ScaleConverter.stringToScale((String) value) >= 0.0;
		} catch (NumberFormatException t) {
			return false;
		}
	}
}
