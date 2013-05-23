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

package org.geomajas.widget.utility.gwt.client.ribbon;

import org.geomajas.gwt.client.action.toolbar.parameter.ButtonLayoutStyle;
import org.geomajas.widget.utility.common.client.action.RibbonColumnAware;
import org.geomajas.widget.utility.common.client.event.DisabledEvent;
import org.geomajas.widget.utility.common.client.event.EnabledEvent;
import org.geomajas.widget.utility.common.client.event.EnabledHandler;
import org.geomajas.widget.utility.common.client.event.HasEnabledHandlers;
import org.geomajas.widget.utility.common.client.ribbon.RibbonColumn;
import org.geomajas.widget.utility.gwt.client.action.ButtonAction;
import org.geomajas.widget.utility.gwt.client.action.RadioAction;
import org.geomajas.widget.utility.gwt.client.util.GuwLayout;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SelectionType;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.StatefulCanvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VStack;


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

	private boolean showTitles = true;

	private TitleAlignment titleAlignment;

	private int iconSize;

	private ButtonAction buttonAction;
	private Layout outer;
	private Img icon;
	private StatefulCanvas titleLabel;
	private StatefulCanvas description;

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

		setCanHover(true);
		setShowHover(true);
		setHoverWrap(false);
		setHoverWidth(1);
		setTooltip(buttonAction.getTooltip());

		setShowRollOver(true);
		setShowDown(true);
		setShowDisabled(true);
		setShowDisabledIcon(false);

		String iconBaseUrl = buttonAction.getIcon();
		icon = new Img(iconBaseUrl, iconSize, iconSize);
		icon.setOverflow(Overflow.HIDDEN);
		icon.setLayoutAlign(Alignment.CENTER);
		
		titleLabel = new StatefulCanvas();
		titleLabel.setContents(prepareTitle());
		titleLabel.setOverflow(Overflow.VISIBLE);
		
		description = new StatefulCanvas();
		description.setContents(buttonAction.getTooltip());
		description.setOverflow(Overflow.VISIBLE);
		description.setAutoHeight();
		description.setWidth100();
		
		setBaseStyle("ribbonButton");
		setCursor(Cursor.HAND);

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
					buttonAction.onClick(null);
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
	}
	
	@Override
	protected void onDraw() {
		updateGui();
		addChild(outer);
	}
	
	// ------------------------------------------------------------------------
	// RibbonColumn implementation:
	// ------------------------------------------------------------------------

	@Override
	public void setTitle(String title) {
		buttonAction.setTitle(title);
		titleLabel.setContents(prepareTitle());
	}

	@Override
	public void setIcon(String iconUrl) {
		buttonAction.setIcon(iconUrl);
		icon.setSrc(iconUrl);
	}

	@Override
	public void setDisabled(boolean disabled) {
		super.setDisabled(disabled);
		if (null != icon) {
			icon.setDisabled(disabled);
		}
		if (null != titleLabel) {
			titleLabel.setDisabled(disabled);
		}
		if (null != description) {
			description.setDisabled(disabled);
		}
	}

	@Override
	public void setButtonBaseStyle(String buttonBaseStyle) {
		setBaseStyle(buttonBaseStyle);
	}

	@Override
	public void setShowTitles(boolean showTitles) {
		this.showTitles = showTitles;
	}

	@Override
	public boolean isShowTitles() {
		return showTitles;
	}

	@Override
	public void setTitleAlignment(TitleAlignment titleAlignment) {
		this.titleAlignment = titleAlignment;
	}

	@Override
	public TitleAlignment getTitleAlignment() {
		return titleAlignment;
	}
	
	/**
	 * Get the {@link Layout} that is directly added to this button as a child (in {@link RibbonButton#onDraw()}).
	 * 
	 * @return a {@link VStack} or {@link HStack} depending on the layout settings.
	 */
	public Layout getOuter() {
		return outer;
	}

	@Override
	public boolean isEnabled() {
		return !isDisabled();
	}

	@Override
	public void setEnabled(boolean enabled) {
		setDisabled(!enabled);
	}

	@Override
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
			icon.setShowDisabled(Boolean.parseBoolean(value));
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
		icon.setHeight(iconSize);
		icon.setWidth(iconSize);
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
		ButtonLayoutStyle buttonButtonLayoutStyle = buttonAction.getButtonLayoutStyle();
		if (ButtonLayoutStyle.ICON_TITLE_AND_DESCRIPTION.equals(buttonButtonLayoutStyle)) {
			buildGuiWithDescription();
		} else { // LayoutParameter.Layout.ICON_AND_TITLE || null || ...
			setWidth(50);
			if (titleAlignment.equals(TitleAlignment.BOTTOM)) {
				setHeight100();
				
				outer = new VStack();
				outer.setOverflow(Overflow.VISIBLE);
				outer.setWidth(GuwLayout.ribbonButtonWidth);
				outer.setAutoHeight();
				outer.addMember(icon);
				if (showTitles && !GuwLayout.hideRibbonTitles) {
					titleLabel.setBaseStyle(getBaseStyle() + "LargeTitle");
					titleLabel.setAutoHeight();
					titleLabel.setWidth(GuwLayout.ribbonButtonWidth);
					outer.addMember(titleLabel);
				}
				outer.setAlign(Alignment.CENTER);
			} else {
				setAutoHeight();
				
				outer = new HStack(GuwLayout.ribbonButtonInnerMargin);
				outer.setOverflow(Overflow.VISIBLE);
				outer.setWidth100();
				outer.setAutoHeight();
				outer.addMember(icon);
				if (showTitles) {
					titleLabel.setBaseStyle(getBaseStyle() + "SmallTitle");
					titleLabel.setAutoHeight();
					titleLabel.setAutoWidth();
					outer.addMember(titleLabel);
				}
			} 
		}
	}
	
	private void buildGuiWithDescription() {
		setCanHover(false);
		
		description.setBaseStyle(getBaseStyle() + "Description");
		description.setAutoHeight();
		description.setWidth100();
		
		VStack inner = new VStack();
		inner.setWidth("*");
		inner.setOverflow(Overflow.VISIBLE);
		inner.setAutoHeight();
		if (showTitles) {
			titleLabel.setBaseStyle(getBaseStyle() + "SmallTitle");
			titleLabel.setAutoHeight();
			titleLabel.setWidth100();
			inner.addMember(titleLabel);
		}
		inner.addMember(description);
		
		outer = new HStack(GuwLayout.ribbonButtonInnerMargin);
		outer.setOverflow(Overflow.VISIBLE);
		outer.setWidth100();
		outer.setAutoHeight();
		outer.setLayoutAlign(Alignment.CENTER);
		outer.addMember(icon);
		outer.addMember(inner);
	}

	private String prepareTitle() {
		String title = buttonAction.getTitle() == null ? buttonAction.getTooltip() : buttonAction.getTitle();
		if (title == null) {
			title = "??";
		} else {
			title = title.trim();
		}
		return title;
	}

	/**
	 * Applies state changes of the action to this widget.
	 * 
	 * @author Jan De Moerloose
	 */
	class ActionEnabler implements EnabledHandler {

		@Override
		public void onEnabled(EnabledEvent event) {
			setEnabled(true);
		}

		@Override
		public void onDisabled(DisabledEvent event) {
			setEnabled(false);
		}

	}

}