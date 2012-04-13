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
import org.geomajas.gwt.client.action.ToolbarBaseAction;
import org.geomajas.gwt.client.action.event.ToolbarActionDisabledEvent;
import org.geomajas.gwt.client.action.event.ToolbarActionEnabledEvent;
import org.geomajas.gwt.client.action.event.ToolbarActionHandler;
import org.geomajas.gwt.client.action.toolbar.parameter.ButtonLayoutParameter.Layout;
import org.geomajas.widget.utility.common.client.event.DisabledEvent;
import org.geomajas.widget.utility.common.client.event.EnabledEvent;
import org.geomajas.widget.utility.common.client.event.EnabledHandler;
import org.geomajas.widget.utility.common.client.event.HasEnabledHandlers;

import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.events.ClickEvent;

/**
 * Button action implementation that contains a {@link ToolbarBaseAction}, 
 * which implemented {@link org.geomajas.gwt.client.action.ToolbarCanvas}. 
 * An object of this class is wrapped into a 
 * {@link org.geomajas.widget.utility.common.client.ribbon.RibbonColumn} by 
 * {@link org.geomajas.widget.utility.gwt.client.ribbon.RibbonColumnCanvas}. 
 * 
 * @author Emiel Ackermann
 */
public class ToolbarButtonCanvas implements ButtonAction, HasEnabledHandlers {

	/**
	 * Implements {@link ToolbarCanvas}.
	 */
	private final ToolbarBaseAction toolbarAction;
	private VerticalAlignment verticalAlignment;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Initialize this action with a {@link ToolbarBaseAction}, that implemented {@link ToolbarCanvas}.
	 * 
	 * @param toolbarAction
	 *            the implementation of {@link ToolbarCanvas} is mandatory.
	 */
	public ToolbarButtonCanvas(ToolbarBaseAction toolbarAction) {
		this.toolbarAction = toolbarAction;
	}

	/** {@inheritDoc} */
	public void configure(String key, String value) {
		if ("verticalAlignment".equalsIgnoreCase(key)) {
			verticalAlignment = VerticalAlignment.valueOf(value.toUpperCase());
		} else if (toolbarAction instanceof ConfigurableAction) {
			ConfigurableAction ca = (ConfigurableAction) getToolbarAction();
			ca.configure(key, value);
		}
	}

	public ToolbarBaseAction getToolbarAction() {
		return toolbarAction;
	}

	public VerticalAlignment getVerticalAlignment() {
		return verticalAlignment;
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
			handler.onEnabled(new EnabledEvent(ToolbarButtonCanvas.this));
		}

		/** {@inheritDoc} */
		public void onToolbarActionDisabled(ToolbarActionDisabledEvent event) {
			handler.onDisabled(new DisabledEvent(ToolbarButtonCanvas.this));
		}

	}
	
	// ------------------------------------------------------------------------
	// Empty ButtonAction implementation, since widget is provided through ToolbarCanvas:
	// ------------------------------------------------------------------------
	
	/**
	 * Since the given toolbar action can not be of the type 
	 * {@link org.geomajas.gwt.client.action.ToolbarAction} 
	 * (see private {@link org.geomajas.widget.utility.gwt.client.ribbon.RibbonColumnRegistry}.getAction()), 
	 * no implementation is provided.
	 */
	public void onClick(ClickEvent event) {
	}
	/**
	 * Widget creation is being handled in the canvas returned by
	 * {@link org.geomajas.gwt.client.action.ToolbarCanvas#getCanvas()},
	 * so null is returned.
	 * 
	 * @return null
	 */
	public String getIcon() {
		return null;
	}
	/**
	 * Widget creation is being handled in the canvas returned by
	 * {@link org.geomajas.gwt.client.action.ToolbarCanvas#getCanvas()},
	 * so no icon can be set.
	 * 
	 * @param does nothing
	 */
	public void setIcon(String icon) {
	}
	/**
	 * Widget creation is being handled in the canvas returned by 
	 * {@link org.geomajas.gwt.client.action.ToolbarCanvas#getCanvas()},
	 * so null is returned.
	 * 
	 * @return null
	 */
	public String getTitle() {
		return null;
	}
	/**
	 * Widget creation is being handled in the canvas returned by 
	 * {@link org.geomajas.gwt.client.action.ToolbarCanvas#getCanvas()},
	 * so no title can be set.
	 * 
	 * @param does nothing
	 */
	public void setTitle(String title) {
	}
	/**
	 * Widget creation is being handled in the canvas returned by 
	 * {@link org.geomajas.gwt.client.action.ToolbarCanvas#getCanvas()},
	 * so null is returned.
	 * 
	 * @return null
	 */
	public String getTooltip() {
		return null;
	}
	/**
	 * Widget creation is being handled in the canvas returned by 
	 * {@link org.geomajas.gwt.client.action.ToolbarCanvas#getCanvas()},
	 * so no tooltip can be set.
	 * 
	 * @param does nothing
	 */
	public void setTooltip(String tooltip) {
	}
	/**
	 * Widget creation is being handled in the canvas returned by 
	 * {@link org.geomajas.gwt.client.action.ToolbarCanvas#getCanvas()},
	 * so null is returned.
	 * 
	 * @return null
	 */
	public Layout getLayout() {
		return null;
	}
	/**
	 * Widget creation is being handled in the canvas returned by 
	 * {@link org.geomajas.gwt.client.action.ToolbarCanvas#getCanvas()},
	 * so no button layout can be set.
	 * 
	 * @param does nothing
	 */
	public void setLayout(Layout layout) {
	}

}