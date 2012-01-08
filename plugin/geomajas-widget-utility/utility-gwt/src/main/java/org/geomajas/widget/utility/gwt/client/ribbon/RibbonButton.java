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

package org.geomajas.widget.utility.gwt.client.ribbon;

import org.geomajas.gwt.client.Geomajas;
import org.geomajas.widget.utility.common.client.action.ButtonAction;
import org.geomajas.widget.utility.common.client.action.RadioAction;
import org.geomajas.widget.utility.common.client.action.RibbonColumnAware;
import org.geomajas.widget.utility.common.client.event.DisabledEvent;
import org.geomajas.widget.utility.common.client.event.EnabledEvent;
import org.geomajas.widget.utility.common.client.event.EnabledHandler;
import org.geomajas.widget.utility.common.client.event.HasEnabledHandlers;
import org.geomajas.widget.utility.common.client.ribbon.RibbonColumn;

import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.SelectionType;
import com.smartgwt.client.widgets.StatefulCanvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;

/**
 * Implementation of the RibbonColumn interface that displays a single button. Instances of this class are initialized
 * using a {@link ButtonAction} that determines the icon, title, tool-tip and click action. If the {@link ButtonAction}
 * implements {@link RibbonColumnAware}, it will be passed a reference to this class. If the {@link ButtonAction}
 * implements {@link HasEnabledHandlers}, the enabled/disabled state of this class will follow the enabled/disabled
 * state of the action.
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 */
public class RibbonButton extends StatefulCanvas implements RibbonColumn {

	public static final String PARAMETER_RADIOGROUP = "radioGroup";
	public static final String PARAMETER_SELECTED = "selected";
	public static final String PARAMETER_SHOWDISABLEDICON = "showDisabledIcon";

	private static final String DISABLED_MARKER = "-disabled";

	private boolean showTitles = true;

	private boolean showDisabledIcon;

	private TitleAlignment titleAlignment;

	private int iconSize;

	private ButtonAction buttonAction;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Initialize this object using a {@link ButtonAction}. This button will use the icon, title, tool-tip and click
	 * action that this {@link ButtonAction} provides. By default, an icon size of 24px will be used in combination with
	 * a BOTTOM title alignment.
	 * 
	 * @param buttonAction
	 *            The {@link ButtonAction} to use as template.
	 */
	public RibbonButton(final ButtonAction buttonAction) {
		this(buttonAction, 24, TitleAlignment.BOTTOM);
	}

	/**
	 * Initialize this object using a {@link ButtonAction}. This button will use the icon, title, tool-tip and click
	 * action that this {@link ButtonAction} provides.
	 * 
	 * @param buttonAction
	 *            The {@link ButtonAction} to use as template.
	 * @param iconSize
	 *            The size (width & height) for the icon - in pixels.
	 * @param titleAlignment
	 *            The alignment for the title (BOTTOM, RIGHT).
	 */
	public RibbonButton(final ButtonAction buttonAction, int iconSize, TitleAlignment titleAlignment) {
		this.buttonAction = buttonAction;
		this.iconSize = iconSize;
		this.titleAlignment = titleAlignment;

		setBaseStyle("ribbonButton");
		setWidth(50);
		setCursor(Cursor.HAND);

		setCanHover(true);
		setShowHover(true);
		setHoverWrap(false);
		setHoverWidth(1);
		setTooltip(buttonAction.getTooltip());

		setShowRollOver(true);
		setShowDown(true);
		setShowDisabled(true);
		setShowDisabledIcon(false);

		if (buttonAction instanceof RadioAction) {
			final RadioAction radioAction = (RadioAction) buttonAction;
			setActionType(SelectionType.CHECKBOX);
			setRadioGroup(radioAction.getRadioGroup());
			addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					radioAction.setSelected(isSelected());
				}
			});
		} else {
			addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					buttonAction.onClick(event);
				}
			});
		}

		if (buttonAction instanceof RibbonColumnAware) {
			((RibbonColumnAware) buttonAction).setRibbonColumn(this);
		}

		if (isEnabled()) {
			if (buttonAction instanceof HasEnabledHandlers) {
				HasEnabledHandlers h = (HasEnabledHandlers) buttonAction;
				h.addEnabledHandler(new ActionEnabler());
				setEnabled(h.isEnabled());
			}
		}

		updateGui();
	}
	
	// ------------------------------------------------------------------------
	// RibbonColumn implementation:
	// ------------------------------------------------------------------------

	@Override
	public void setTitle(String title) {
		super.setTitle(title);
		buttonAction.setTitle(title);
		updateGui();
	}

	@Override
	public void setIcon(String icon) {
		super.setIcon(icon);
		buttonAction.setIcon(icon);
		updateGui();
	}

	@Override
	public void setDisabled(boolean disabled) {
		super.setDisabled(disabled);
		updateGui();
	}

	/** {@inheritDoc} */
	public void setButtonBaseStyle(String buttonBaseStyle) {
		setBaseStyle(buttonBaseStyle);
	}

	/** {@inheritDoc} */
	public void setShowTitles(boolean showTitles) {
		this.showTitles = showTitles;
		updateGui();
	}

	/** {@inheritDoc} */
	public boolean isShowTitles() {
		return showTitles;
	}

	/** {@inheritDoc} */
	public void setTitleAlignment(TitleAlignment titleAlignment) {
		this.titleAlignment = titleAlignment;
		updateGui();
	}

	/** {@inheritDoc} */
	public TitleAlignment getTitleAlignment() {
		return titleAlignment;
	}

	/** {@inheritDoc} */
	public boolean isEnabled() {
		return !isDisabled();
	}

	/** {@inheritDoc} */
	public void setEnabled(boolean enabled) {
		setDisabled(!enabled);
	}

	/** {@inheritDoc} */
	public void configure(String key, String value) {
		if (PARAMETER_SELECTED.equalsIgnoreCase(key)) {
			if (buttonAction instanceof RadioAction) {
				boolean selected = Boolean.parseBoolean(value);
				setSelected(selected);
				RadioAction ra = (RadioAction) buttonAction;
				ra.setSelected(selected);
			}
		} else if (PARAMETER_RADIOGROUP.equalsIgnoreCase(key)) {
			if (buttonAction instanceof RadioAction) {
				addToRadioGroup(value);
				RadioAction ra = (RadioAction) buttonAction;
				ra.setRadioGroup(value);
			}
		} else if (PARAMETER_SHOWDISABLEDICON.equalsIgnoreCase(key)) {
			showDisabledIcon = Boolean.parseBoolean(value);
			updateGui();
		} else {
			buttonAction.configure(key, value);
		}
	}

	// ------------------------------------------------------------------------
	// Class specific getters and setters:
	// ------------------------------------------------------------------------

	@Override
	public int getIconSize() {
		return iconSize;
	}

	@Override
	public void setIconSize(int iconSize) {
		this.iconSize = iconSize;
		updateGui();
	}

	/**
	 * Return the button action that is used as a template for this button.
	 * 
	 * @return The button action that is used as a template for this button.
	 */
	public ButtonAction getButtonAction() {
		return buttonAction;
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	/**
	 * Update the GUI to reflect the settings.
	 */
	protected void updateGui() {
		String title = buttonAction.getTitle() == null ? buttonAction.getTooltip() : buttonAction.getTitle();
		if (title == null) {
			title = "??";
		} else {
			title = title.trim();
		}

		if (titleAlignment.equals(TitleAlignment.BOTTOM)) {
			String titleContent = "";
			if (showTitles) {
				titleContent = "<div style='text-align:center; margin-top: 10px; " + getTitleTextStyle() + "'>" + title
						+ "</div>";
			}
			setContents("<div style='text-align:center;'><img src='" + getIconUrl() + "' width='" + iconSize
					+ "' height='" + iconSize + "' /></div>" + titleContent);
		} else {
			setWidth100();
			String titleContent = "";
			if (showTitles) {
				titleContent = "<td style='text-align:left; padding-left: 8px; font-size: 11px; white-space:nowrap; "
						+ getTitleTextStyle() + "'>" + title + "</td>";
			}
			setContents("<table style='border-spacing: 0px;' cellpadding='0px'><tr><td style='text-align:center;'>"
					+ "<img src='" + getIconUrl() + "' width='" + iconSize + "' height='" + iconSize + "' />" + "</td>"
					+ titleContent + "</tr></table>");
		}
	}

	/**
	 * Get the style for the title text.
	 *
	 * @return title text style
	 */
	protected String getTitleTextStyle() {
		if (isDisabled()) {
			return "color: #777777;";
		} else {
			return "";
		}
	}

	/**
	 * Get the URL for the icon.
	 *
	 * @return icon URL
	 */
	protected String getIconUrl() {
		String icon = buttonAction.getIcon().replaceFirst("\\[ISOMORPHIC\\]", Geomajas.getIsomorphicDir());
		return applyDisabled(icon);
	}

	/**
	 * Applies the disabled state to the icon's url if necessary.
	 * 
	 * @param icon
	 * @return disabled icon's url
	 */
	protected String applyDisabled(String icon) {
		if (isDisabled() && showDisabledIcon) {
			int dot = icon.lastIndexOf(".");
			if (dot > -1) {
				icon = icon.substring(0, dot) + DISABLED_MARKER + icon.substring(dot, icon.length());
			}
		}
		return icon;
	}

	/**
	 * Applies state changes of the action to this widget.
	 * 
	 * @author Jan De Moerloose
	 */
	class ActionEnabler implements EnabledHandler {

		/** {@inheritDoc} */
		public void onEnabled(EnabledEvent event) {
			setEnabled(true);
		}

		/** {@inheritDoc} */
		public void onDisabled(DisabledEvent event) {
			setEnabled(false);
		}

	}

}