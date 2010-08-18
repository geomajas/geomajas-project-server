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
package org.geomajas.plugin.printing.command.print;

import org.geomajas.command.Command;
import org.geomajas.plugin.printing.command.dto.PrintGetTemplateRequest;
import org.geomajas.plugin.printing.command.dto.PrintGetTemplateResponse;
import org.geomajas.plugin.printing.command.dto.PrintTemplateInfo;
import org.geomajas.plugin.printing.component.PageComponent;
import org.geomajas.plugin.printing.component.PrintComponent;
import org.geomajas.plugin.printing.component.impl.LabelComponentImpl;
import org.geomajas.plugin.printing.component.impl.LegendComponentImpl;
import org.geomajas.plugin.printing.component.impl.LegendItemComponentImpl;
import org.geomajas.plugin.printing.component.impl.MapComponentImpl;
import org.geomajas.plugin.printing.component.service.PrintDtoConverterService;
import org.geomajas.plugin.printing.configuration.PrintTemplate;
import org.geomajas.plugin.printing.document.SinglePageDocument;
import org.geomajas.plugin.printing.service.PrintService;
import org.geomajas.service.ConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;

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

	@Autowired
	private PrintDtoConverterService converterService;

	@Autowired
	private PrintService printService;

	public PrintGetTemplateResponse getEmptyCommandResponse() {
		return new PrintGetTemplateResponse();
	}

	public void execute(PrintGetTemplateRequest request, PrintGetTemplateResponse response) throws Exception {
		// you dirty hack you...
		PrintTemplateInfo template = request.getTemplate();
		PageComponent page = (PageComponent) converterService.toInternal(template.getPage());
		MapComponentImpl mapComponent = (MapComponentImpl) page.getChild(PrintTemplate.MAP);
		if (mapComponent != null) {
			LegendComponentImpl legendComponent = (LegendComponentImpl) mapComponent.getChild(PrintTemplate.LEGEND);
			if (legendComponent != null) {
				LabelComponentImpl lab = (LabelComponentImpl) legendComponent.getChild(PrintTemplate.TITLE);
				if (lab != null) {
					lab.setText(legendComponent.getTitle());
				}
				// need to do this before setSizeAndFit
				adjustLegendFontSizeForSmallPageSizes(request, legendComponent);
			}
		}

		if (request.getPageSize() != null) {
			page.setSize(request.getPageSize(), true);
		}
		SinglePageDocument pdfDoc = new SinglePageDocument(page, null);
		// layout the document
		pdfDoc.layout();
		// Add document to container
		String documentId = printService.putDocument(pdfDoc);
		response.setDocumentId(documentId);
	}

	private void adjustLegendFontSizeForSmallPageSizes(PrintGetTemplateRequest request, LegendComponentImpl lc) {
		// adjust fontsize legend for small pagesizes
		float relPSize = getPageSizeRelativeToA3(request); // A3 == 100% fontsize
		if (relPSize < 1) {
			java.awt.Font newFont = lc.getFont().deriveFont(lc.getFont().getSize() * 0.8f);
			lc.setFont(newFont);
			log.debug("PDF: changed fontsize to: {}", lc.getFont().getSize());
			for (PrintComponent pc : lc.getChildren()) {
				if (pc instanceof LabelComponentImpl) {
					((LabelComponentImpl) pc).setFont(newFont.deriveFont(newFont.getSize() * 1.25f));
				} else if (pc instanceof LegendItemComponentImpl) {
					for (PrintComponent pc2 : pc.getChildren()) {
						if (pc2 instanceof LabelComponentImpl) {
							((LabelComponentImpl) pc2).setFont(newFont);
						}
					}
				}
			}
		}
	}

	private float getPageSizeRelativeToA3(PrintGetTemplateRequest request) {
		Rectangle r = null;
		if (request.getPageSize() != null) {
			PageSize.getRectangle(request.getPageSize());
		} else {
			float width = request.getTemplate().getPage().getLayoutConstraint().getWidth();
			float height = request.getTemplate().getPage().getLayoutConstraint().getHeight();
			r = new Rectangle(0, 0, width, height);
		}
		return (r.getWidth() / PageSize.A3.getWidth() + r.getHeight() / PageSize.A3.getHeight()) / 2;
	}

}