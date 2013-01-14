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

package org.geomajas.widget.utility.gwt.client.action;


/**
 * <p>
 * Extension of the <code>ButtonAction</code> interface for actions that can be selected or deselected. On top of that,
 * these actions have the ability to act like radio buttons, meaning only one can be active at a time.
 * </p>
 * <p>
 * Widgets that make use of these actions, must always make sure to abide by the rules.
 * </p>
 * 
 * @author Pieter De Graef
 */
public interface RadioAction extends ButtonAction {

	/**
	 * Get the name of the radio group this action belongs to. Within such a radio group, only 1 action can be selected
	 * at a time. This value is null by default, meaning it does not belong to any radio group.
	 * 
	 * @return The name of the radio group this action belongs to.
	 */
	String getRadioGroup();

	/**
	 * Set the name of the radio group this action belongs to. Within such a radio group, only 1 action can be selected
	 * at a time. This value is null by default, meaning it does not belong to any radio group.
	 * 
	 * @param radioGroup
	 *            The name of the radio group this action belongs to.
	 */
	void setRadioGroup(String radioGroup);

	/**
	 * Determine whether or not this action should be selected.
	 * 
	 * @param selected
	 *            Select or deselect this action.
	 */
	void setSelected(boolean selected);
}