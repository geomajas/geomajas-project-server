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

package org.geomajas.plugin.printing.client.template;

import org.geomajas.configuration.FontStyleInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.plugin.printing.client.util.PrintingLayout;
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
 */
public abstract class AbstractTemplateBuilder {

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
		return buildMap(null);
	}

	protected MapComponentInfo buildMap(Bbox bounds) {
		MapComponentInfo map = new MapComponentInfo();
		if (PrintingLayout.templateIncludeScaleBar) {
			map.addChild(buildScaleBar());
		}
		if (PrintingLayout.templateIncludeLegend) {
			LegendComponentInfo legend;
			if (null == bounds) {
				legend = buildLegend();
			} else {
				legend = buildLegend(bounds);
			}
			map.addChild(legend);
		}
		if (PrintingLayout.templateIncludeNorthArrow) {
			map.addChild(buildArrow());
		}
		return map;
	}

	protected ImageComponentInfo buildArrow() {
		return new ImageComponentInfo();
	}

	protected LegendComponentInfo buildLegend() {
		return new LegendComponentInfo();
	}

	protected LegendComponentInfo buildLegend(Bbox bounds) {
		return new LegendComponentInfo();
	}

	protected ScaleBarComponentInfo buildScaleBar() {
		return new ScaleBarComponentInfo();
	}

	protected LabelComponentInfo buildTitle() {
		LabelComponentInfo label = new LabelComponentInfo();
		FontStyleInfo style = new FontStyleInfo();
		style.setFamily(PrintingLayout.templateDefaultFontFamily);
		style.setStyle(PrintingLayout.templateDefaultFontStyle);
		style.setSize((int) PrintingLayout.templateDefaultFontSize);
		label.setFont(style);
		label.setBackgroundColor(PrintingLayout.templateDefaultBackgroundColor);
		label.setBorderColor(PrintingLayout.templateDefaultBorderColor);
		label.setFontColor(PrintingLayout.templateDefaultColor);
		label.getLayoutConstraint().setAlignmentY(LayoutConstraintInfo.TOP);
		label.getLayoutConstraint().setAlignmentX(LayoutConstraintInfo.CENTER);
		label.setTag("title");
		return label;
	}
}