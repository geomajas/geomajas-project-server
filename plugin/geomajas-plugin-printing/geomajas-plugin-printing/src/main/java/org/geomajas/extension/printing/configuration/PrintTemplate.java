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
package org.geomajas.extension.printing.configuration;

import com.vividsolutions.jts.geom.Coordinate;
import org.geomajas.extension.printing.command.dto.DtoPrintTemplate;
import org.geomajas.extension.printing.component.ImageComponent;
import org.geomajas.extension.printing.component.LabelComponent;
import org.geomajas.extension.printing.component.LayoutConstraint;
import org.geomajas.extension.printing.component.LegendComponent;
import org.geomajas.extension.printing.component.MapComponent;
import org.geomajas.extension.printing.component.PageComponent;
import org.geomajas.extension.printing.component.ScaleBarComponent;
import org.geomajas.global.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.awt.Color;
import java.awt.Font;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;

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

	private transient PageComponent page;

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

	public PrintTemplate(DtoPrintTemplate dto) {
		this.id = dto.getId();
		this.name = dto.getName();
		this.template = dto.isTemplate();
	}

	// Class specific functions:

	public void decode() throws JAXBException {
		JAXBContext context = JAXBContext.newInstance("org.geomajas.extension.printing.component", getClass()
				.getClassLoader());
		Unmarshaller u = context.createUnmarshaller();
		Object result = u.unmarshal(new StringReader(pageXml));
		if (result instanceof PageComponent) {
			setPage((PageComponent) result);
		} else {
			throw new JAXBException("could not decode template");
		}
	}

	public void encode() throws JAXBException {
		JAXBContext context = JAXBContext.newInstance("org.geomajas.extension.printing.component", getClass()
				.getClassLoader());
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		m.marshal(page, baos);
		setPageXml(baos.toString());
	}

	public DtoPrintTemplate toDto() {
		DtoPrintTemplate dto = new DtoPrintTemplate();
		dto.setId(id);
		dto.setName(name);
		dto.setTemplate(template);
		return dto;
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

	public PageComponent getPage() {
		return page;
	}

	public void setPage(PageComponent page) {
		this.page = page;
	}

	public boolean isTemplate() {
		return template;
	}

	public void setTemplate(boolean isTemplate) {
		this.template = isTemplate;
	}

	public static PrintTemplate createDefaultTemplate(String pageSize, boolean landscape) {
		PageComponent page = new PageComponent(pageSize, landscape);
		page.setTag(PAGE);
		MapComponent map = createMap();
		ImageComponent northarrow = createArrow();
		ScaleBarComponent bar = createBar();
		LabelComponent title = createTitle();

		LegendComponent legend = new LegendComponent();
		legend.setTag(LEGEND);
		map.addComponent(bar);
		map.addComponent(legend);
		map.addComponent(northarrow);
		page.addComponent(map);
		page.addComponent(title);
		PrintTemplate template = new PrintTemplate(true);
		template.setName("Default" + "-" + pageSize + "-" + (landscape ? "landscape" : "portrait"));
		template.setPage(page);
		try {
			template.encode();
		} catch (JAXBException e) {
			Logger log = LoggerFactory.getLogger(PrintTemplate.class);
			log.error("could not encode default template", e);
			e.printStackTrace();
			return null;
		}
		return template;
	}

	private static MapComponent createMap() {
		MapComponent map = new MapComponent();
		map.getConstraint().setMarginX(20);
		map.getConstraint().setMarginY(20);
		map.setLocation(new Coordinate());
		map.setPpUnit(1.0f);
		map.setTag(MAP);
		return map;
	}

	private static ImageComponent createArrow() {
		ImageComponent northarrow = new ImageComponent();
		northarrow.setImagePath("/images/northarrow.gif");
		northarrow.getConstraint().setAlignmentX(LayoutConstraint.RIGHT);
		northarrow.getConstraint().setAlignmentY(LayoutConstraint.TOP);
		northarrow.getConstraint().setMarginX(20);
		northarrow.getConstraint().setMarginY(20);
		northarrow.getConstraint().setWidth(10);
		northarrow.setTag(ARROW);
		return northarrow;
	}

	private static LabelComponent createTitle() {
		LabelComponent title = new LabelComponent();
		title.setFont(new Font("Dialog", Font.ITALIC, 14));
		title.setBackgroundColor(Color.white);
		title.setBorderColor(Color.BLACK);
		title.setFontColor(Color.BLACK);
		title.setText("<Title>");
		title.getConstraint().setAlignmentY(LayoutConstraint.TOP);
		title.getConstraint().setAlignmentX(LayoutConstraint.CENTER);
		title.getConstraint().setMarginY(40);
		title.setTag(TITLE);
		return title;
	}

	private static ScaleBarComponent createBar() {
		ScaleBarComponent bar = new ScaleBarComponent();
		bar.setTicNumber(3);
		bar.setUnit("m");
		bar.setTag(SCALEBAR);
		return bar;
	}
}
