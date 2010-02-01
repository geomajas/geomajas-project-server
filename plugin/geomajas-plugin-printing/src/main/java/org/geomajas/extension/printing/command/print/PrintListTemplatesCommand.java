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
package org.geomajas.extension.printing.command.print;

import org.geomajas.command.Command;
import org.geomajas.command.EmptyCommandRequest;
import org.geomajas.configuration.ApplicationInfo;
import org.geomajas.extension.printing.command.dto.DtoPrintTemplate;
import org.geomajas.extension.printing.command.dto.PrintListTemplatesResponse;
import org.geomajas.extension.printing.configuration.PrintConfiguration;
import org.geomajas.extension.printing.configuration.PrintTemplate;
import org.geomajas.extension.printing.configuration.PrintTemplateDao;
import org.geomajas.extension.printing.document.SinglePageDocument;
import org.geomajas.service.ApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * List print templates command.
 *
 * @author Jan De Moerlose
 */
@Component()
public class PrintListTemplatesCommand implements Command<EmptyCommandRequest, PrintListTemplatesResponse> {

	private final Logger log = LoggerFactory.getLogger(PrintListTemplatesCommand.class);

	@Autowired
	private ApplicationService runtime;

	@Autowired
	private ApplicationInfo application;

	public PrintListTemplatesResponse getEmptyCommandResponse() {
		return new PrintListTemplatesResponse();
	}

	public void execute(EmptyCommandRequest request, PrintListTemplatesResponse response) throws Exception {
		try {
			PrintTemplateDao dao = PrintConfiguration.getDao();
			List<PrintTemplate> templates = dao.findAll();
			for (PrintTemplate template : templates) {
				try {
					// decode the page
					template.decode();
					// calculate the sizes (if not already calculated !)
					SinglePageDocument document =
							new SinglePageDocument(template.getPage(), application, runtime, getFilters());
					document.setLayoutOnly(true);
					document.render();
					// add to the result
					response.getTemplates().add(template.toDto());
				} catch (JAXBException e) {
					log.warn("Bad template : " + template.getName(), e);
				}
			}
		} catch (IOException e) {
			log.error("Could not access templates, falling back to default", e);
		}
		addDefaults(response);
		// check for Default

	}

	private void addDefaults(PrintListTemplatesResponse response) {
		response.getTemplates().add(createDefault("A4", true));
		response.getTemplates().add(createDefault("A3", true));
		response.getTemplates().add(createDefault("A2", true));
		response.getTemplates().add(createDefault("A0", true));
		response.getTemplates().add(createDefault("A1", true));
		response.getTemplates().add(createDefault("A4", false));
		response.getTemplates().add(createDefault("A3", false));
		response.getTemplates().add(createDefault("A2", false));
		response.getTemplates().add(createDefault("A1", false));
		response.getTemplates().add(createDefault("A0", false));
	}

	private DtoPrintTemplate createDefault(String pagesize, boolean landscape) {
		PrintTemplate template = PrintTemplate.createDefaultTemplate(pagesize, landscape);
		// calculate the sizes (if not already calculated !)
		SinglePageDocument document = new SinglePageDocument(template.getPage(), application, runtime, getFilters());
		document.setLayoutOnly(true);
		document.render();
		return template.toDto();
	}

	private Map<String, String> getFilters() {
		// @todo security, provide data filters for the layers
		return new HashMap<String, String>();
	}

}
