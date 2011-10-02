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

package org.geomajas.widget.utility.gwt.client.action;

import org.geomajas.gwt.client.action.ConfigurableAction;
import org.geomajas.gwt.client.action.ToolbarAction;
import org.geomajas.gwt.client.action.ToolbarBaseAction;
import org.geomajas.widget.utility.common.client.action.ButtonAction;

import com.google.gwt.event.dom.client.ClickEvent;

/**
 * Button action implementation that delegates to a {@link ToolbarBaseAction} instance.
 * 
 * @author Pieter De Graef
 */
public class ToolbarButtonAction implements ButtonAction {

	protected ToolbarBaseAction toolbarAction;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Initialize this action with a toolbar action.
	 * 
	 * @param toolbarAction
	 *            The toolbar action instance to delegate to. By default a {@link ToolbarAction} is expected.
	 */
	public ToolbarButtonAction(ToolbarBaseAction toolbarAction) {
		this.toolbarAction = toolbarAction;
		setTitle(toolbarAction.getTitle());
	}

	// ------------------------------------------------------------------------
	// ButtonAction implementation:
	// ------------------------------------------------------------------------

	/**
	 * If the given toolbar action during construction was of the type {@link ToolbarAction}, this method is passed to
	 * it. Note though that the event types are different, and thus <code>null</code> is passed.
	 */
	public void onClick(ClickEvent event) {
		if (toolbarAction instanceof ToolbarAction) {
			((ToolbarAction) toolbarAction).onClick(null);
		}
	}

	public void configure(String key, String value) {
		if ("icon".equalsIgnoreCase(key)) {
			setIcon(value);
		} else if ("title".equalsIgnoreCase(key)) {
			setTitle(value);
		} else if ("tooltip".equalsIgnoreCase(key)) {
			setTooltip(value);
		} else if (toolbarAction instanceof ConfigurableAction) {
			ConfigurableAction ca = (ConfigurableAction) toolbarAction;
			ca.configure(key, value);
		}
	}

	public String getIcon() {
		return toolbarAction.getIcon();
	}

	public void setIcon(String icon) {
		toolbarAction.setIcon(icon);
	}

	public String getTitle() {
		return toolbarAction.getTitle();
	}

	public void setTitle(String title) {
		toolbarAction.setTitle(title);
	}

	public String getTooltip() {
		return toolbarAction.getTooltip();
	}

	public void setTooltip(String tooltip) {
		toolbarAction.setTooltip(tooltip);
	}

	public ToolbarBaseAction getToolbarAction() {
		return toolbarAction;
	}
}