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

package org.geomajas.gwt.client.widget;

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import org.geomajas.annotation.Api;
import org.geomajas.configuration.Parameter;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientToolInfo;
import org.geomajas.configuration.client.ClientToolbarInfo;
import org.geomajas.gwt.client.action.ConfigurableAction;
import org.geomajas.gwt.client.action.ToolbarAction;
import org.geomajas.gwt.client.action.ToolbarBaseAction;
import org.geomajas.gwt.client.action.ToolbarCanvas;
import org.geomajas.gwt.client.action.ToolbarModalAction;
import org.geomajas.gwt.client.action.ToolbarWidget;
import org.geomajas.gwt.client.action.event.ToolbarActionDisabledEvent;
import org.geomajas.gwt.client.action.event.ToolbarActionEnabledEvent;
import org.geomajas.gwt.client.action.event.ToolbarActionHandler;
import org.geomajas.gwt.client.action.toolbar.ToolId;
import org.geomajas.gwt.client.action.toolbar.ToolbarRegistry;
import org.geomajas.gwt.client.map.event.MapModelChangedEvent;
import org.geomajas.gwt.client.map.event.MapModelChangedHandler;

import com.smartgwt.client.types.SelectionType;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripSeparator;
import org.geomajas.gwt.client.util.Log;
import org.geomajas.gwt.client.util.WidgetLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * A toolbar that supports two types of buttons:
 * <ul>
 * <li>modal (radiobutton-like) buttons.</li>
 * <li>non-modal or action buttons.</li>
 * </ul>
 * 
 * @author Pieter De Graef
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api
public class Toolbar extends ToolStrip {

	private List<Canvas> extraCanvasMembers = new ArrayList<Canvas>();

	/**
	 * Button size for small buttons.
	 * @deprecated use {@link WidgetLayout#toolbarSmallButtonSize}
	 */
	@Deprecated
	public static final int BUTTON_SIZE_SMALL = 24;

	/**
	 * Button size for large buttons.
	 * @deprecated use {@link WidgetLayout#toolbarLargeButtonSize}
	 */
	@Deprecated
	public static final int BUTTON_SIZE_BIG = 32;

	private static final String CONTROLLER_RADIO_GROUP = "graphicsController";

	private MapWidget mapWidget;

	private int buttonSize;
	
	/**
	 * keep track of the currently selected ModalAction.
	 */
	private ToolbarModalAction currentModalAction;

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	/**
	 * Create a toolbar for the given {@link MapWidget}.
	 *
	 * @param mapWidget map widget for toolbar
	 * @since 1.10.0
	 */
	@Api
	public Toolbar(MapWidget mapWidget) {
		this.mapWidget = mapWidget;
		setButtonSize(WidgetLayout.toolbarSmallButtonSize);
		setPadding(WidgetLayout.toolbarPadding);
		setWidth100();
		mapWidget.getMapModel().addMapModelChangedHandler(new MapModelChangedHandler() {

			public void onMapModelChanged(MapModelChangedEvent event) {
				initialize(Toolbar.this.mapWidget.getMapModel().getMapInfo());
			}
		});
	}

	@Override
	public void addMember(Canvas component) {
		extraCanvasMembers.add(component);
		super.addMember(component);
	}

	@Override
	public void removeMember(Canvas canvas) {
		extraCanvasMembers.remove(canvas);
		super.removeMember(canvas);
	}

	/**
	 * Initialize this widget.
	 * 
	 * @param mapInfo map info
	 */
	public void initialize(ClientMapInfo mapInfo) {
		// remove previous members
		super.removeMembers(getMembers());

		// add new members
		ClientToolbarInfo toolbarInfo = mapInfo.getToolbar();
		if (toolbarInfo != null) {
			for (ClientToolInfo tool : toolbarInfo.getTools()) {
				String id = tool.getToolId();
				if (ToolId.TOOL_SEPARATOR.equals(id)) {
					addToolbarSeparator();
				} else {
					ToolbarBaseAction action = ToolbarRegistry.getToolbarAction(id, mapWidget);
					if (action instanceof ConfigurableAction) {
						for (Parameter parameter : tool.getParameters()) {
							((ConfigurableAction) action).configure(parameter.getName(), parameter.getValue());
						}
					}
					String description = tool.getDescription();
					// Overrides tooltip parameter if description is set in ClientToolInfo.
					if (null != description && !"".equals(description)) {
						action.setTooltip(description);
					}
					if (action instanceof ToolbarWidget) {
						super.addMember(((ToolbarWidget) action).getWidget());
					} else if (action instanceof ToolbarCanvas) {
						super.addMember(((ToolbarCanvas) action).getCanvas());
					} else if (action instanceof ToolbarModalAction) {
						addModalButton((ToolbarModalAction) action);
					} else if (action instanceof ToolbarAction) {
						addActionButton((ToolbarAction) action);
					} else {
						String msg = "Tool with id " + id + " unknown.";
						Log.logError(msg); // console log
						SC.warn(msg); // in your face
					}
				}
			}
		}

		// add extra members
		for (Canvas extra : extraCanvasMembers) {
			super.addMember(extra);
		}
	}

	/**
	 * Add a new action button to the toolbar. An action button is the kind of button that executes immediately when
	 * clicked upon. It can not be selected or deselected, it just executes every click.
	 * 
	 * @param action
	 *            The actual action to execute on click.
	 */
	public void addActionButton(final ToolbarAction action) {
		final IButton button = new IButton();
		button.setWidth(buttonSize);
		button.setHeight(buttonSize);
		button.setIconSize(buttonSize - WidgetLayout.toolbarStripHeight);
		button.setIcon(action.getIcon());
		button.setActionType(SelectionType.BUTTON);
		button.addClickHandler(action);
		button.setShowRollOver(false);
		button.setShowFocused(false);
		button.setTooltip(action.getTooltip());
		button.setDisabled(action.isDisabled());

		if (getMembers() != null && getMembers().length > 0) {
			LayoutSpacer spacer = new LayoutSpacer();
			spacer.setWidth(2);
			super.addMember(spacer);
		}
		action.addToolbarActionHandler(new ToolbarActionHandler() {

			public void onToolbarActionDisabled(ToolbarActionDisabledEvent event) {
				button.setDisabled(true);
			}

			public void onToolbarActionEnabled(ToolbarActionEnabledEvent event) {
				button.setDisabled(false);
			}
		});
		super.addMember(button);
	}

	/**
	 * Add a new modal button (checkbox) to the toolbar. This kind of button is selected and deselected as the user
	 * clicks upon it. By selecting and deselecting, a certain state will be activate or deactivated, determined by the
	 * given <code>ModalAction</code>.
	 * 
	 * @param modalAction
	 *            The actual action that determines what should happen when the button is selected or deselected.
	 */
	public void addModalButton(final ToolbarModalAction modalAction) {
		currentModalAction = modalAction;
		final IButton button = new IButton();
		button.setWidth(buttonSize);
		button.setHeight(buttonSize);
		button.setIconSize(buttonSize - WidgetLayout.toolbarStripHeight);
		button.setIcon(modalAction.getIcon());
		button.setActionType(SelectionType.CHECKBOX);
		button.setRadioGroup(CONTROLLER_RADIO_GROUP);
		button.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				if (button.isSelected()) {
					currentModalAction.onDeselect(event);
					modalAction.onSelect(event);
				} else {
					modalAction.onDeselect(event);
				}
			}
		});
		button.setShowRollOver(false);
		button.setShowFocused(true);
		button.setTooltip(modalAction.getTooltip());
		button.setDisabled(modalAction.isDisabled());

		if (getMembers() != null && getMembers().length > 0) {
			LayoutSpacer spacer = new LayoutSpacer();
			spacer.setWidth(WidgetLayout.toolbarPadding);
			super.addMember(spacer);
		}
		modalAction.addToolbarActionHandler(new ToolbarActionHandler() {

			public void onToolbarActionDisabled(ToolbarActionDisabledEvent event) {
				button.setDisabled(true);
			}

			public void onToolbarActionEnabled(ToolbarActionEnabledEvent event) {
				button.setDisabled(false);
			}
		});
		super.addMember(button);
	}

	/** Add a vertical line to the toolbar. */
	public void addToolbarSeparator() {
		ToolStripSeparator stripSeparator = new ToolStripSeparator();
		stripSeparator.setHeight(WidgetLayout.toolbarStripHeight);
		super.addMember(stripSeparator);
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	/**
	 * Get the size of the buttons. Set this before the toolbar is drawn, because afterwards, it can't be changed
	 * anymore.
	 *
	 * @return button size
	 */
	public int getButtonSize() {
		return buttonSize;
	}

	/**
	 * Set the size of the buttons. Use this before the toolbar is drawn, because afterwards, it can't be changed
	 * anymore.
	 * 
	 * @param buttonSize
	 *            The new button size.
	 */
	public void setButtonSize(int buttonSize) {
		this.buttonSize = buttonSize;
		// TODO: resize all buttons
		setHeight(buttonSize);
	}
}
