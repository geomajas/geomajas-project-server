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

import org.geomajas.widget.utility.common.client.action.ButtonAction;
import org.geomajas.widget.utility.gwt.client.util.GuwLayout;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.VStack;

/**
 * Extension of the RibbonButton class that displays a single button with a description.
 * 
 * @author Emiel Ackermann
 */
public class RibbonButtonDescribed extends RibbonButton {

	private Label titleLabel;
	private Canvas description;
	
	public RibbonButtonDescribed(ButtonAction buttonAction) {
		this(buttonAction, 24);
	}

	public RibbonButtonDescribed(ButtonAction buttonAction, Integer iconSize) {
		super(buttonAction, iconSize, null);
		
		String iconBaseUrl = buttonAction.getIcon();
		Img icon = new Img(applyDisabled(iconBaseUrl), iconSize, iconSize);
		icon.setLayoutAlign(Alignment.CENTER);
		
		String title = buttonAction.getTitle() == null ? buttonAction.getTooltip() : buttonAction.getTitle();
		if (title == null) {
			title = "??";
		} else {
			title = title.trim();
		}
		
		titleLabel = new Label(title);
		titleLabel.setOverflow(Overflow.VISIBLE);
		titleLabel.setAutoHeight();
		titleLabel.setWidth100();
		
		description = new Canvas();
		description.setContents(buttonAction.getTooltip());
		description.setOverflow(Overflow.VISIBLE);
		description.setAutoHeight();
		description.setWidth100();
		
		VStack inner = new VStack();
		inner.setWidth("*");
		inner.setOverflow(Overflow.VISIBLE);
		inner.setAutoHeight();
		inner.addMember(titleLabel);
		inner.addMember(description);
		
		HStack outer = new HStack(GuwLayout.describedButtonInnerMargin);
		outer.setOverflow(Overflow.VISIBLE);
		outer.setWidth100();
		outer.setAutoHeight();
		outer.addMember(icon);
		outer.addMember(inner);
		
		addChild(outer);
	}

	@Override
	public void setButtonBaseStyle(String buttonBaseStyle) {
		setBaseStyle(buttonBaseStyle);
		titleLabel.setStyleName(buttonBaseStyle + "Title");
		description.setStyleName(buttonBaseStyle + "Description");
	}

	@Override
	protected void updateGui() {
		// do nothing.
	}
}