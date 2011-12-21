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

package org.geomajas.widget.utility.gwt.client.ribbon;

import org.geomajas.gwt.client.util.HtmlBuilder;
import org.geomajas.widget.utility.common.client.action.ButtonAction;
import org.geomajas.widget.utility.gwt.client.action.ToolbarButtonAction;

/**
 * Extension of the RibbonButton class that displays a single button with a description.
 * 
 * @author Emiel Ackermann
 */
public class RibbonButtonDescribed extends RibbonButton {

	private String description;

	public RibbonButtonDescribed(ButtonAction buttonAction) {
		this(buttonAction, 24);
	}

	public RibbonButtonDescribed(ButtonAction buttonAction, Integer iconSize) {
		super(buttonAction, iconSize, null);
		if (buttonAction instanceof ToolbarButtonAction) {
			description = ((ToolbarButtonAction)buttonAction).getDescription();
		}
	}

	@Override
	protected void updateGui() {
		ButtonAction buttonAction = getButtonAction();
		String title = buttonAction.getTitle() == null ? buttonAction.getTooltip() : buttonAction.getTitle();
		if (title == null) {
			title = "??";
		} else {
			title = title.trim();
		}
		String iconCell = "<td rowspan='2' style='text-align:center;'>" +
				"<img src='" + getIconUrl() + "' width='" + getIconSize() + "' height='" + getIconSize() + "/></td>";
		String titleCell = "";
		if (isShowTitles()) {
			titleCell = HtmlBuilder.tdStyle("text-align:center; margin-top: 10px;" + getTitleTextStyle(), title);
		}
		String descriptionCell = HtmlBuilder.tdStyle("text-align:center;", description);
		setContents(HtmlBuilder.tableStyleHtmlContent("", 
					HtmlBuilder.trHtmlContent(iconCell, titleCell),
					HtmlBuilder.trHtmlContent(descriptionCell)));
	}
}
