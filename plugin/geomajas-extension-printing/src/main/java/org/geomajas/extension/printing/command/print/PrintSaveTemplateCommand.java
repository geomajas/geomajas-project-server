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
import org.geomajas.command.CommandResponse;
import org.geomajas.extension.printing.command.dto.PrintSaveTemplateRequest;
import org.geomajas.extension.printing.configuration.PrintConfiguration;
import org.geomajas.extension.printing.configuration.PrintTemplate;
import org.geomajas.extension.printing.configuration.PrintTemplateDao;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;
import java.io.IOException;

/**
 * Save print template command.
 *
 * @author Jan De Moerlose
 */
@Component()
public class PrintSaveTemplateCommand implements Command<PrintSaveTemplateRequest, CommandResponse> {

	public CommandResponse getEmptyCommandResponse() {
		return new CommandResponse();
	}

	public void execute(PrintSaveTemplateRequest request, CommandResponse response) throws Exception {
		PrintTemplate template = new PrintTemplate(request.getTemplate());
		try {
			PrintTemplateDao dao = PrintConfiguration.getDao();
			PrintTemplate old = dao.findPrintByName(template.getName());
			try {
				template.encode();
			} catch (JAXBException e) {
				throw new GeomajasException(e, ExceptionCode.PRINT_TEMPLATE_XML_PROBLEM, template.getName());
			}
			template.setId(null);
			if (old == null) {
				dao.makePersistent(template);
			} else {
				template.setId(old.getId());
				dao.merge(template);
			}
		} catch (IOException e) {
			throw new GeomajasException(e, ExceptionCode.PRINT_TEMPLATE_PERSIST_PROBLEM, template.getName());
		}
	}

}