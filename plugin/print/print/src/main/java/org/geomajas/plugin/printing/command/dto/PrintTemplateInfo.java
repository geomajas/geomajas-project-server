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

package org.geomajas.plugin.printing.command.dto;

import java.io.Serializable;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.printing.component.dto.PageComponentInfo;


/**
 * DTO version of the PrintTemplate class.
 *
 * @author Pieter De Graef
 * @since 2.0.0
 */
@Api(allMethods = true)
public class PrintTemplateInfo implements Serializable {

	private static final long serialVersionUID = 200L;

	private Long id;

	private String name;

	private boolean template;
	
	private PageComponentInfo page;

	/**
	 * Get template id.
	 *
	 * @return template id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Set template id.
	 *
	 * @param id template id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Get template name.
	 *
	 * @return template name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set template name.
	 *
	 * @param name template name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Is this a template?
	 *
	 * @return true when this is a template
	 * @deprecated not used
	 */
	@Deprecated
	public boolean isTemplate() {
		return template;
	}

	/**
	 * Set whether this is a template.
	 *
	 * @param template true when this is a template
	 * @deprecated not used
	 */
	@Deprecated
	public void setTemplate(boolean template) {
		this.template = template;
	}

	/**
	 * Get page configuration.
	 *
	 * @return page configuration
	 */
	public PageComponentInfo getPage() {
		return page;
	}

	/**
	 * Set page configuration.
	 *
	 * @param page page configuration
	 */
	public void setPage(PageComponentInfo page) {
		this.page = page;
	}
	
}
