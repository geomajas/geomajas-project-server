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
package org.geomajas.sld.editor.expert.client.view;

import org.geomajas.puregwt.widget.client.dialog.MessageBox;
import org.geomajas.puregwt.widget.client.dialog.MessageBox.MessageStyleType;
import org.geomajas.sld.editor.expert.client.i18n.SldEditorExpertMessages;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.shared.GWT;

/**
 * SmartGWT specific utility class for view part.
 * 
 * @author Jan De Moerloose
 * @author Kristof Heirwegh
 */
public class ViewUtilImpl extends BaseViewUtil {

	private static final SldEditorExpertMessages MSG = GWT.create(SldEditorExpertMessages.class);
	
	@Override
	public void showMessage(String message) {
		MessageBox.showMessageBox(MSG.windowTitle(), message);
	}

	@Override
	public void showWarning(String message) {
		MessageBox.showMessageBox(MSG.windowTitle(), message, MessageStyleType.WARN);
	}

	@Override
	public void showYesNoMessage(String message, final YesNoCallback callback) {
		MessageBox.showYesNoMessageBox(MSG.windowTitle(), message, new Callback<Boolean, Void>() {
			@Override
			public void onSuccess(Boolean result) {
				if (result) {
					callback.onYes();
				} else {
					callback.onNo();
				}
			}
			
			@Override
			public void onFailure(Void reason) {
				callback.onCancel();
			}
		});
	}
}
