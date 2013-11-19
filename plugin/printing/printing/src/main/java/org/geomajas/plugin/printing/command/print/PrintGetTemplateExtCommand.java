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
package org.geomajas.plugin.printing.command.print;

import org.geomajas.command.Command;
import org.geomajas.plugin.printing.command.dto.PrintGetTemplateExtRequest;
import org.geomajas.plugin.printing.command.dto.PrintGetTemplateExtResponse;
import org.geomajas.plugin.printing.component.service.PrintDtoConverterService;
import org.geomajas.plugin.printing.service.PrintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This class implements the command that performs the first step in generating a template-based printout
 * of for instance the specified map view, the map's legend,....
 * The user can specify the format of the output document (default is PDF). 
 * It returns the id of the document (possibly the rendering step is not yet completed).
 * The actual printout can be obtained via the PrintingController.
 *  
 * @author An Buyle
 *
 * @since 2.4.0
 */
@Component()
public class PrintGetTemplateExtCommand implements Command<PrintGetTemplateExtRequest, PrintGetTemplateExtResponse> {

	@Autowired
	private PrintDtoConverterService converterService;

	@Autowired
	private PrintService printService;

	public PrintGetTemplateExtResponse getEmptyCommandResponse() {
		return new PrintGetTemplateExtResponse();
	}

	public void execute(PrintGetTemplateExtRequest request, PrintGetTemplateExtResponse response) throws Exception {

		LayoutAsSinglePageDoc.execute(request, response, converterService, printService);
		
	}

}