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

import org.geomajas.gwt.client.action.ToolbarModalAction;

/**
 * Radio action implementation that delegates to a {@link ToolbarModalAction} instance.
 * 
 * @author Pieter De Graef
 */
public class ToolbarRadioAction extends ToolbarButtonAction implements RadioAction {

	private String radioGroup;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Initialize this object with the toolbar modal action to delegates most of the work to. By default this action
	 * will not be part of any radio group.
	 * 
	 * @param toolbarAction
	 *            The toolbar modal action to delegates most of the work to.
	 */
	public ToolbarRadioAction(ToolbarModalAction toolbarAction) {
		super(toolbarAction);
	}

	/**
	 * Initialize this object with the toolbar modal action to delegates most of the work to and with the name of the
	 * radio group this action should belong to.
	 * 
	 * @param toolbarAction
	 *            The toolbar modal action to delegates most of the work to.
	 * @param radioGroup
	 *            The name of radio group this action should belong to.
	 */
	public ToolbarRadioAction(ToolbarModalAction toolbarAction, String radioGroup) {
		super(toolbarAction);
		this.radioGroup = radioGroup;
	}

	// ------------------------------------------------------------------------
	// RadioAction implementation:
	// ------------------------------------------------------------------------

	/** {@inheritDoc} */
	public void setSelected(boolean selected) {
		if (selected) {
			((ToolbarModalAction) toolbarAction).onSelect(null);
		} else {
			((ToolbarModalAction) toolbarAction).onDeselect(null);
		}
	}

	/** {@inheritDoc} */
	public String getRadioGroup() {
		return radioGroup;
	}

	/** {@inheritDoc} */
	public void setRadioGroup(String radioGroup) {
		this.radioGroup = radioGroup;
	}
}