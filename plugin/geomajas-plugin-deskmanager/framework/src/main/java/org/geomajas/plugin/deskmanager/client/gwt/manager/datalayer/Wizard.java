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
package org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer;

/**
 * @author Kristof Heirwegh
 */
public interface Wizard {

	WizardStepPanel getStep(String name);

	/**
	 * This is part of a listener pattern, eg. the steps notify the wizard when they were changed, so the wizard can
	 * update it's buttons.
	 * 
	 * @param step
	 */
	void onChanged(WizardStepPanel step);

	/**
	 * Pretend the user pressed the [Next] button.
	 */
	void fireNextStepEvent();
}
