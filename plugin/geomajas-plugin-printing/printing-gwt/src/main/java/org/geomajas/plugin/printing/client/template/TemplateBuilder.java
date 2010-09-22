/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geomajas.plugin.printing.client.template;

import org.geomajas.configuration.FontStyleInfo;
import org.geomajas.plugin.printing.command.dto.PrintTemplateInfo;
import org.geomajas.plugin.printing.component.dto.ImageComponentInfo;
import org.geomajas.plugin.printing.component.dto.LabelComponentInfo;
import org.geomajas.plugin.printing.component.dto.LayoutConstraintInfo;
import org.geomajas.plugin.printing.component.dto.LegendComponentInfo;
import org.geomajas.plugin.printing.component.dto.MapComponentInfo;
import org.geomajas.plugin.printing.component.dto.PageComponentInfo;
import org.geomajas.plugin.printing.component.dto.ScaleBarComponentInfo;

/**
 * Builder pattern for templates.
 * 
 * @author Jan De Moerloose
 * 
 */

public abstract class TemplateBuilder {
	
	protected PrintTemplateInfo buildTemplate() {
		PrintTemplateInfo template = new PrintTemplateInfo();
		template.setPage(buildPage());
		return template;
	}

	protected PageComponentInfo buildPage() {
		PageComponentInfo page = new PageComponentInfo();
		page.addChild(buildMap());
		page.addChild(buildTitle());
		page.setTag("page");
		return page;
	}

	protected MapComponentInfo buildMap() {
		MapComponentInfo map = new MapComponentInfo();
		map.addChild(buildScaleBar());
		map.addChild(buildLegend());
		map.addChild(buildArrow());
		return map;
	}

	protected ImageComponentInfo buildArrow() {
		ImageComponentInfo arrow = new ImageComponentInfo();
		return arrow;
	}

	protected LegendComponentInfo buildLegend() {
		LegendComponentInfo legend = new LegendComponentInfo();
		return legend;
	}

	protected ScaleBarComponentInfo buildScaleBar() {
		ScaleBarComponentInfo scaleBar = new ScaleBarComponentInfo();
		return scaleBar;
	}

	protected LabelComponentInfo buildTitle() {
		LabelComponentInfo label = new LabelComponentInfo();
		FontStyleInfo style = new FontStyleInfo();
		style.setFamily("Dialog");
		style.setStyle("Italic");
		style.setSize(14);
		label.setFont(style);
		label.setBackgroundColor("0xFFFFFF");
		label.setBorderColor("0x000000");
		label.setFontColor("0x000000");
		label.getLayoutConstraint().setAlignmentY(LayoutConstraintInfo.TOP);
		label.getLayoutConstraint().setAlignmentX(LayoutConstraintInfo.CENTER);
		label.setTag("title");
		return label;
	}
}
