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
import org.geomajas.plugin.printing.document.Document.Format;
import org.geomajas.plugin.printing.document.SinglePageDocument;
import org.geomajas.plugin.printing.service.PrintService;
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
		pdfDoc.layout(Format.PDF);
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
			for (PrintComponent<?> pc : lc.getChildren()) {
				if (pc instanceof LabelComponentImpl) {
					((LabelComponentImpl) pc).setFont(newFont.deriveFont(newFont.getSize() * 1.25f));
				} else if (pc instanceof LegendItemComponentImpl) {
					for (PrintComponent<?> pc2 : pc.getChildren()) {
						if (pc2 instanceof LabelComponentImpl) {
							((LabelComponentImpl) pc2).setFont(newFont);
						}
					}
				}
			}
		}
	}

	private float getPageSizeRelativeToA3(PrintGetTemplateRequest request) {
		Rectangle r;
		if (request.getPageSize() != null) {
			r = PageSize.getRectangle(request.getPageSize());
		} else {
			float width = request.getTemplate().getPage().getLayoutConstraint().getWidth();
			float height = request.getTemplate().getPage().getLayoutConstraint().getHeight();
			r = new Rectangle(0, 0, width, height);
		}
		return (r.getWidth() / PageSize.A3.getWidth() + r.getHeight() / PageSize.A3.getHeight()) / 2;
	}

}