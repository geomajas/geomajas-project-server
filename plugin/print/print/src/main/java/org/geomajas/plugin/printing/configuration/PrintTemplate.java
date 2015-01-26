/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.printing.configuration;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.geomajas.global.Json;
import org.geomajas.plugin.printing.component.impl.PageComponentImpl;

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

}
