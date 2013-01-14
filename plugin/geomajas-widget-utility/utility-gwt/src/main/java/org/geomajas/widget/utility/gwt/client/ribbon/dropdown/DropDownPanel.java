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

package org.geomajas.widget.utility.gwt.client.ribbon.dropdown;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.gwt.client.action.ToolbarBaseAction;
import org.geomajas.gwt.client.action.toolbar.ButtonGroup;
import org.geomajas.gwt.client.action.toolbar.DropDownButtonAction;
import org.geomajas.gwt.client.action.toolbar.parameter.ButtonLayoutStyle;
import org.geomajas.widget.utility.common.client.ribbon.RibbonColumn;
import org.geomajas.widget.utility.common.client.ribbon.RibbonColumn.TitleAlignment;
import org.geomajas.widget.utility.gwt.client.action.ButtonAction;
import org.geomajas.widget.utility.gwt.client.action.ToolbarButtonAction;
import org.geomajas.widget.utility.gwt.client.action.ToolbarButtonCanvas;
import org.geomajas.widget.utility.gwt.client.ribbon.RibbonButton;
import org.geomajas.widget.utility.gwt.client.ribbon.RibbonColumnCanvas;
import org.geomajas.widget.utility.gwt.client.util.GuwLayout;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.Positioning;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.StatefulCanvas;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VStack;

/**
 * Ribbon-only class that aligns itself with its button in the animateShow override. 
 * Furthermore a {@link NativePreviewHandler} is added to {@link Event} in the override as well, 
 * that will close this panel when clicked outside this panel and button.
 * 
 * @author Emiel Ackermann
 */
public class DropDownPanel extends VStack {
	
	private final StatefulCanvas button;
	private HandlerRegistration registration;
	
	private List<VStack> groups = new ArrayList<VStack>();
	private List<Label> groupTitles = new ArrayList<Label>();
	private List<VStack> bodies = new ArrayList<VStack>();
	private List<RibbonColumn> buttons = new ArrayList<RibbonColumn>();

	public DropDownPanel(final DropDownRibbonButton button) {
		super();
		this.button = button;
		ButtonAction buttonAction = button.getButtonAction();
		ToolbarBaseAction toolbarAction = ((ToolbarButtonAction) buttonAction).getToolbarAction();
		setWidth(((DropDownButtonAction) toolbarAction).getPanelWidth());
		setPosition(Positioning.ABSOLUTE);
		setOverflow(Overflow.VISIBLE);
		setAutoHeight();
	}
	
	public void addGroup(ButtonGroup title, List<ButtonAction> actions) {
		VStack group = new VStack();
		group.setOverflow(Overflow.VISIBLE);
		group.setAutoHeight();
		group.setWidth100();
		
		ButtonLayoutStyle buttonButtonLayoutStyle = ButtonLayoutStyle.ICON_AND_TITLE;
		if (null != title) {
			Label groupTitle = new Label(title.getTitle());
			groupTitle.setOverflow(Overflow.VISIBLE);
			groupTitle.setAutoHeight();
			groupTitle.setWidth100();
			group.addMember(groupTitle);
			groupTitles.add(groupTitle);
			buttonButtonLayoutStyle = (ButtonLayoutStyle) (null == title.getButtonLayoutStyle() ?
					ButtonLayoutStyle.ICON_AND_TITLE : title.getButtonLayoutStyle());
		}
		VStack body = new VStack();
		body.setOverflow(Overflow.VISIBLE);
		body.setAutoHeight();
		body.setWidth100();
		for (ButtonAction action : actions) {
			RibbonColumn button = getButton(action, buttonButtonLayoutStyle);
			body.addMember(button.asWidget());
			buttons.add(button);
		}
		group.addMember(body);
		bodies.add(body);
		
		addMember(group);
		groups.add(group);
	}

	/**
	 * Converts the given action into a {@link RibbonColumn}.
	 * 
	 * @param action ButtonAction
	 * @param buttonButtonLayoutStyle the layout of the group. Is used if the action does not contain one itself.
	 * @return column RibbonColumn containing the button.
	 */
	private RibbonColumn getButton(ButtonAction action, ButtonLayoutStyle buttonButtonLayoutStyle) {
		RibbonColumn column;
		if (action instanceof ToolbarButtonCanvas) { 
			column = new RibbonColumnCanvas((ToolbarButtonCanvas) action);
		} else {
			// if no layout was given, use the one given by the group
			if (null == action.getButtonLayoutStyle()) {
				action.setButtonLayoutStyle(buttonButtonLayoutStyle);
			}
			RibbonButton button = new RibbonButton(action, 16, TitleAlignment.RIGHT);
			if (ButtonLayoutStyle.ICON_AND_TITLE.equals(buttonButtonLayoutStyle)) {
				button = new RibbonButton(action, GuwLayout.DropDown.ribbonBarDropDownButtonIconSize, 
						TitleAlignment.RIGHT);
			} else if (ButtonLayoutStyle.ICON_TITLE_AND_DESCRIPTION.equals(buttonButtonLayoutStyle)) {
				button = new RibbonButton(action, GuwLayout.DropDown.ribbonBarDropDownButtonDescriptionIconSize, 
						TitleAlignment.RIGHT);
			}
			button.setOverflow(Overflow.VISIBLE);
			button.setAutoHeight();
			button.setWidth100();
			button.setMargin(2);
			button.addClickHandler(new ClickHandler() {
				
				public void onClick(
						com.smartgwt.client.widgets.events.ClickEvent event) {
					hide();
				}
				
			});
			column = button;
		}
		return column;
	}
	
	@Override
	public void setStyleName(String styleName) {
		super.setStyleName(styleName);
		for (VStack group : groups) {
			group.setStyleName(styleName + "Group");
		}
		for (Label groupTitle : groupTitles) {
			groupTitle.setStyleName(styleName + "GroupTitle");
		}
		for (VStack body : bodies) {
			body.setStyleName(styleName + "GroupBody");
		}
		for (RibbonColumn button : buttons) {
			if (button instanceof RibbonButton) {
				button.setButtonBaseStyle(styleName + "Button");
			}
		}
	}

	@Override
	public void animateShow(final AnimationEffect effect) {
		this.moveTo(button.getPageLeft(), 
				button.getPageBottom() - 2);
		button.setSelected(true);
		registration = previewMouseUpHandler();
		super.animateShow(effect);
	}

	/**
	 * Through a {@link NativePreviewHandler} and its {@link NativePreviewEvent} 
	 * all mouse events are caught here before they are processed. 
	 * If the event is of type {@link Event#ONMOUSEUP} and the click was outside 
	 * the button or this drop-down panel, the panel is closed.
	 *
	 * @return handler registration
	 */
	private HandlerRegistration previewMouseUpHandler() {
		return Event.addNativePreviewHandler(new NativePreviewHandler() {
			public void onPreviewNativeEvent(NativePreviewEvent preview) {
				int typeInt = preview.getTypeInt();
				if (typeInt == Event.ONMOUSEUP) {
					Event event = Event.as(preview.getNativeEvent());
					// Can not retrieve the clicked widget from the Event, so here goes...
					int clientX = event.getClientX();
					int clientY = event.getClientY();
					// Was the button clicked?
					int left = button.getPageLeft();
					int right = button.getPageRight();
					int top = button.getPageTop();
					int bottom = button.getPageBottom();
					boolean mouseIsOutside = true;
					if (clientX > left && clientX < right && clientY > top && clientY < bottom) {
						mouseIsOutside = false;
					}
					if (mouseIsOutside) {
						// Was this panel clicked?
						right = getPageRight();
						top = getPageTop();
						bottom = getPageBottom();
						if (clientX > left && clientX < right && clientY > top && clientY < bottom) {
							mouseIsOutside = false;
						}
					}
					if (mouseIsOutside) {
						hide();
					}
				}
			}
		});
	}
	
	/**
	 * hide() instead of animateHide(), to make sure 
	 * the panel hides immediately (when navigating away from the ribbon).
	 */
	@Override
	public void hide() {
		button.setSelected(false);
		if (null != registration) {
			registration.removeHandler();
		}
		super.hide();
	}
}
