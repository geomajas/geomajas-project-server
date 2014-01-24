/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
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
import org.geomajas.plugin.printing.command.dto.PrintGetTemplateRequest;
import org.geomajas.plugin.printing.command.dto.PrintGetTemplateResponse;
import org.geomajas.plugin.printing.component.service.PrintDtoConverterService;
import org.geomajas.plugin.printing.service.PrintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This command returns a link to a generated template-based printout in PDF format.
 * Note that the newer {@link PrintGetTemplateExtCommand} class allows the specification of 
 * the output format.
 *
 * @author Oliver May 
 * @author An Buyle
 */
@Component()
public class PrintGetTemplateCommand implements Command<PrintGetTemplateRequest, PrintGetTemplateResponse> {

	@Autowired
	private PrintDtoConverterService converterService;

	@Autowired
	private PrintService printService;

	
	public PrintGetTemplateResponse getEmptyCommandResponse() {
		return new PrintGetTemplateResponse();
	}

	public void execute(PrintGetTemplateRequest request, PrintGetTemplateResponse response) throws Exception {
		PrintGetTemplateExtRequest extRequest = new PrintGetTemplateExtRequest(request); 
		PrintGetTemplateExtResponse extResponse = new PrintGetTemplateExtResponse(); 
		
		LayoutAsSinglePageDoc.execute(extRequest, extResponse, converterService, printService);
		
		response.setDocumentId(extResponse.getDocumentId());
	}
	
}