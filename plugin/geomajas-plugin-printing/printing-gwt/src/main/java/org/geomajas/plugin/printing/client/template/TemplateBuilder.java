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
