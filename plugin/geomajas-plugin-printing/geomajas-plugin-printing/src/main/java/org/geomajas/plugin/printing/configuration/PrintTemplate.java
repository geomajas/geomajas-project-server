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
package org.geomajas.plugin.printing.configuration;

import java.awt.Color;
import java.awt.Font;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.geomajas.global.Json;
import org.geomajas.plugin.printing.component.dto.LayoutConstraintInfo;
import org.geomajas.plugin.printing.component.impl.ImageComponentImpl;
import org.geomajas.plugin.printing.component.impl.LabelComponentImpl;
import org.geomajas.plugin.printing.component.impl.LegendComponentImpl;
import org.geomajas.plugin.printing.component.impl.MapComponentImpl;
import org.geomajas.plugin.printing.component.impl.PageComponentImpl;
import org.geomajas.plugin.printing.component.impl.ScaleBarComponentImpl;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * ???
 *
 * @author Jan De Moerloose
 */
@Entity
@Table(name = "print_template")
public class PrintTemplate {

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "page_xml")
	private String pageXml;

	@Column(name = "template")
	private boolean template;

	private transient PageComponentImpl page;

	public static final String MAP = "map";

	public static final String LEGEND = "legend";

	public static final String PAGE = "page";

	public static final String TITLE = "title";

	public static final String DATE = "date";

	public static final String ARROW = "arrow";

	public static final String SCALEBAR = "scalebar";

	// Constructors:

	public PrintTemplate(boolean isTemplate) {
		this.template = isTemplate;
	}

	public PrintTemplate() {
		this(false);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Json(serialize = false)
	public String getPageXml() {
		return pageXml;
	}

	public void setPageXml(String pageXml) {
		this.pageXml = pageXml;
	}

	public PageComponentImpl getPage() {
		return page;
	}

	public void setPage(PageComponentImpl page) {
		this.page = page;
	}

	public boolean isTemplate() {
		return template;
	}

	public void setTemplate(boolean isTemplate) {
		this.template = isTemplate;
	}

	public static PrintTemplate createDefaultTemplate(String pageSize, boolean landscape) {
		PageComponentImpl page = new PageComponentImpl(pageSize, landscape);
		page.setTag(PAGE);
		MapComponentImpl map = createMap();
		ImageComponentImpl northarrow = createArrow();
		ScaleBarComponentImpl bar = createBar();
		LabelComponentImpl title = createTitle();

		LegendComponentImpl legend = new LegendComponentImpl();
		legend.setTag(LEGEND);
		map.addComponent(bar);
		map.addComponent(legend);
		map.addComponent(northarrow);
		page.addComponent(map);
		page.addComponent(title);
		PrintTemplate template = new PrintTemplate(true);
		template.setName("Default" + "-" + pageSize + "-" + (landscape ? "landscape" : "portrait"));
		template.setPage(page);
		return template;
	}

	private static MapComponentImpl createMap() {
		MapComponentImpl map = new MapComponentImpl();
		map.getConstraint().setMarginX(20);
		map.getConstraint().setMarginY(20);
		map.setLocation(new Coordinate());
		map.setPpUnit(1.0f);
		map.setTag(MAP);
		return map;
	}

	private static ImageComponentImpl createArrow() {
		ImageComponentImpl northarrow = new ImageComponentImpl();
		northarrow.setImagePath("/images/northarrow.gif");
		northarrow.getConstraint().setAlignmentX(LayoutConstraintInfo.RIGHT);
		northarrow.getConstraint().setAlignmentY(LayoutConstraintInfo.TOP);
		northarrow.getConstraint().setMarginX(20);
		northarrow.getConstraint().setMarginY(20);
		northarrow.getConstraint().setWidth(10);
		northarrow.setTag(ARROW);
		return northarrow;
	}

	private static LabelComponentImpl createTitle() {
		LabelComponentImpl title = new LabelComponentImpl();
		title.setFont(new Font("Dialog", Font.ITALIC, 14));
		title.setBackgroundColor(Color.white);
		title.setBorderColor(Color.BLACK);
		title.setFontColor(Color.BLACK);
		title.setText("<Title>");
		title.getConstraint().setAlignmentY(LayoutConstraintInfo.TOP);
		title.getConstraint().setAlignmentX(LayoutConstraintInfo.CENTER);
		title.getConstraint().setMarginY(40);
		title.setTag(TITLE);
		return title;
	}

	private static ScaleBarComponentImpl createBar() {
		ScaleBarComponentImpl bar = new ScaleBarComponentImpl();
		bar.setTicNumber(3);
		bar.setUnit("m");
		bar.setTag(SCALEBAR);
		return bar;
	}
}
