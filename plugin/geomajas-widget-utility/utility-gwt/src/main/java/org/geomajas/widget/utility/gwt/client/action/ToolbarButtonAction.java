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

package org.geomajas.widget.utility.gwt.client.action;

import org.geomajas.gwt.client.action.ConfigurableAction;
import org.geomajas.gwt.client.action.ToolbarAction;
import org.geomajas.gwt.client.action.ToolbarBaseAction;
import org.geomajas.gwt.client.action.event.ToolbarActionDisabledEvent;
import org.geomajas.gwt.client.action.event.ToolbarActionEnabledEvent;
import org.geomajas.gwt.client.action.event.ToolbarActionHandler;
import org.geomajas.gwt.client.action.toolbar.parameter.ButtonLayoutParameter;
import org.geomajas.gwt.client.action.toolbar.parameter.ButtonLayoutStyle;
import org.geomajas.widget.utility.common.client.action.RibbonColumnAware;
import org.geomajas.widget.utility.common.client.event.DisabledEvent;
import org.geomajas.widget.utility.common.client.event.EnabledEvent;
import org.geomajas.widget.utility.common.client.event.EnabledHandler;
import org.geomajas.widget.utility.common.client.event.HasEnabledHandlers;
import org.geomajas.widget.utility.common.client.ribbon.RibbonColumn;

import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.client.widgets.events.ClickEvent;

/**
 * Button action implementation that delegates to a {@link ToolbarBaseAction} instance.
 * 
 * @author Pieter De Graef
 * @author Emiel Ackermann
 */
public class ToolbarButtonAction implements ButtonAction, HasEnabledHandlers, RibbonColumnAware {

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
			((ToolbarAction) toolbarAction).onClick(event);
		}
	}

	/** {@inheritDoc} */
	public void configure(String key, String value) {
		if ("icon".equalsIgnoreCase(key)) {
			setIcon(value);
		} else if ("title".equalsIgnoreCase(key)) {
			setTitle(value);
		} else if ("tooltip".equalsIgnoreCase(key)) {
			setTooltip(value);
		} else if ("description".equalsIgnoreCase(key)) {
			setTooltip(value);
		} else if (ButtonLayoutParameter.NAME.equalsIgnoreCase(key)) {
			setButtonLayoutStyle(ButtonLayoutStyle.valueOf(value));
		} else if (toolbarAction instanceof ConfigurableAction) {
			ConfigurableAction ca = (ConfigurableAction) toolbarAction;
			ca.configure(key, value);
		}
	}

	/** {@inheritDoc} */
	public String getIcon() {
		return toolbarAction.getIcon();
	}

	/** {@inheritDoc} */
	public void setIcon(String icon) {
		toolbarAction.setIcon(icon);
	}

	/** {@inheritDoc} */
	public String getTitle() {
		return toolbarAction.getTitle();
	}

	/** {@inheritDoc} */
	public void setTitle(String title) {
		toolbarAction.setTitle(title);
	}

	/** {@inheritDoc} */
	public String getTooltip() {
		return toolbarAction.getTooltip();
	}

	/** {@inheritDoc} */
	public void setTooltip(String tooltip) {
		toolbarAction.setTooltip(tooltip);
	}

	public ToolbarBaseAction getToolbarAction() {
		return toolbarAction;
	}

	/** {@inheritDoc} */
	public ButtonLayoutStyle getButtonLayoutStyle() {
		return toolbarAction.getButtonLayoutStyle();
	}
	/** {@inheritDoc} */
	public void setButtonLayoutStyle(ButtonLayoutStyle buttonLayoutStyle) {
		toolbarAction.setButtonLayoutStyle(buttonLayoutStyle);
	}
	
	/** {@inheritDoc} */
	public boolean isEnabled() {
		return !toolbarAction.isDisabled();
	}

	/** {@inheritDoc} */
	public HandlerRegistration addEnabledHandler(EnabledHandler handler) {
		return toolbarAction.addToolbarActionHandler(new ToolbarActionForwarder(handler));
	}
	
	/**
	 * Forwards {@link ToolbarActionEnabledEvent} and {@link ToolbarActionDisabledEvent} to {@link EnabledHandler}s.
	 * 
	 * @author Jan De Moerloose
	 */
	class ToolbarActionForwarder implements ToolbarActionHandler {

		private EnabledHandler handler;

		ToolbarActionForwarder(EnabledHandler handler) {
			this.handler = handler;
		}

		/** {@inheritDoc} */
		public void onToolbarActionEnabled(ToolbarActionEnabledEvent event) {
			handler.onEnabled(new EnabledEvent(ToolbarButtonAction.this));
		}

		/** {@inheritDoc} */
		public void onToolbarActionDisabled(ToolbarActionDisabledEvent event) {
			handler.onDisabled(new DisabledEvent(ToolbarButtonAction.this));
		}

	}

	/** {@inheritDoc} */
	public void setRibbonColumn(RibbonColumn column) {
		if (toolbarAction instanceof RibbonColumnAware) {
			((RibbonColumnAware) toolbarAction).setRibbonColumn(column);
		}
	}

	/** {@inheritDoc} */
	public RibbonColumn getRibbonColumn() {
		if (toolbarAction instanceof RibbonColumnAware) {
			return ((RibbonColumnAware) toolbarAction).getRibbonColumn();
		} else {
			return null;
		}
	}

}