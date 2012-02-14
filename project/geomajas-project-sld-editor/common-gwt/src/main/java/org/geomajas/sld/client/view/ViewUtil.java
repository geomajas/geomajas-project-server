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
package org.geomajas.sld.client.view;

public interface ViewUtil {

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

	int factorToPercentage(String value);

	String percentageToFactor(int percentage);

	String percentageToFactor(float percentage);

}
