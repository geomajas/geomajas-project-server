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

import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import org.geomajas.command.Command;
import org.geomajas.extension.printing.PdfContainer;
import org.geomajas.extension.printing.command.dto.PrintGetTemplateRequest;
import org.geomajas.extension.printing.command.dto.PrintGetTemplateResponse;
import org.geomajas.extension.printing.component.LabelComponent;
import org.geomajas.extension.printing.component.LegendComponent;
import org.geomajas.extension.printing.component.LegendItemComponent;
import org.geomajas.extension.printing.component.PrintComponent;
import org.geomajas.extension.printing.configuration.PrintTemplate;
import org.geomajas.extension.printing.document.SinglePageDocument;
import org.geomajas.service.ConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * This command returns a link to a generated template-based pdf file of the current view.
 * </p>
 *
 * @author Oliver May
 */
@Component()
public class PrintGetTemplateCommand implements Command<PrintGetTemplateRequest, PrintGetTemplateResponse> {

	private final Logger log = LoggerFactory.getLogger(PrintGetTemplateCommand.class);

	@Autowired
	private ConfigurationService runtime;

	public PrintGetTemplateResponse getEmptyCommandResponse() {
		return new PrintGetTemplateResponse();
	}

	public void execute(PrintGetTemplateRequest request, PrintGetTemplateResponse response) throws Exception {
		// you dirty hack you...
		PrintTemplate template = new PrintTemplate(request.getTemplate());
		LegendComponent legendComponent = (LegendComponent) template.getPage().getChild(PrintTemplate.MAP).getChild(
				PrintTemplate.LEGEND);
		if (legendComponent != null) {
			LabelComponent lab = (LabelComponent) legendComponent.getChild(PrintTemplate.TITLE);
			if (lab != null) {
				lab.setText(legendComponent.getTitle());
			}
			// need to do this before setSizeAndFit
			adjustLegendFontSizeForSmallPageSizes(request, legendComponent);
		}

		if (request.getPageSize() != null) {
			template.getPage().setSize(request.getPageSize(), true);
		}
		SinglePageDocument pdfDoc = new SinglePageDocument(template.getPage(), runtime, null);
		// Set file meta options
		pdfDoc.setFileName(request.getFileName());
		pdfDoc.setDownloadMethod(request.getDownloadMethod());

		// Add document to container
		int documentId = PdfContainer.getInstance().addDocument(pdfDoc);
		response.setDocumentId(documentId);
	}

	private void adjustLegendFontSizeForSmallPageSizes(PrintGetTemplateRequest request, LegendComponent lc) {
		// adjust fontsize legend for small pagesizes
		float relPSize = getPageSizeRelativeToA3(request); // A3 == 100% fontsize
		if (relPSize < 1) {
			java.awt.Font newFont = lc.getFont().deriveFont(lc.getFont().getSize() * 0.8f);
			lc.setFont(newFont);
			log.debug("PDF: changed fontsize to: {}", lc.getFont().getSize());
			for (PrintComponent pc : lc.getChildren()) {
				if (pc instanceof LabelComponent) {
					((LabelComponent) pc).setFont(newFont.deriveFont(newFont.getSize() * 1.25f));
				} else if (pc instanceof LegendItemComponent) {
					for (PrintComponent pc2 : pc.getChildren()) {
						if (pc2 instanceof LabelComponent) {
							((LabelComponent) pc2).setFont(newFont);
						}
					}
				}
			}
		}
	}

	private float getPageSizeRelativeToA3(PrintGetTemplateRequest request) {
		Rectangle r = PageSize.getRectangle(request.getPageSize());
		return (r.getWidth() / PageSize.A3.getWidth() + r.getHeight() / PageSize.A3.getHeight()) / 2;
	}

}