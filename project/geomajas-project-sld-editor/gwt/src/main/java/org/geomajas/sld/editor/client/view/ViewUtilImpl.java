/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.sld.editor.client.view;

import org.geomajas.sld.editor.common.client.view.BaseViewUtil;

import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;

/**
 * SmartGWT specific utility class for view part.
 * 
 * @author Jan De Moerloose
 * 
 */
public class ViewUtilImpl extends BaseViewUtil {

	public void showMessage(String message) {
		SC.say(message);
	}

	public void showWarning(String message) {
		SC.warn(message);
	}

	public void showYesNoMessage(String message, final YesNoCallback callback) {
		SC.ask(message, new BooleanCallback() {

			public void execute(Boolean value) {
				if (value == null) {
					callback.onCancel();
				} else if (value) {
					callback.onYes();
				} else {
					callback.onNo();
				}

			}
		});
	}

}
