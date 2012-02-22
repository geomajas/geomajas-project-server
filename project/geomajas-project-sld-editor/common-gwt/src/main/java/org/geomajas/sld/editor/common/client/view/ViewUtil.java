/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.sld.editor.common.client.view;

/**
 * A collection of utility functions for the view.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface ViewUtil {

	/**
	 * Callback interface with user response after confirmation dialog has been shown.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	interface YesNoCallback {

		void onYes();

		void onNo();

		void onCancel();
	}

	void showMessage(String message);

	void showWarning(String message);

	void showYesNoMessage(String message, YesNoCallback callback);

	String numericalToString(Object value, String defaultStringValue);

	float numericalToFloat(Object value, float defaultValue);
	
	int numericalToInteger(String numerical);

	int factorToPercentage(String value);

	String percentageToFactor(int percentage);

	String percentageToFactor(float percentage);

}
