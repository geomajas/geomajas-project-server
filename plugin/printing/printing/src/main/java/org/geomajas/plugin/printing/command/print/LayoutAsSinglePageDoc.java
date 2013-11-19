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

import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.printing.command.dto.PrintGetTemplateExtRequest;
import org.geomajas.plugin.printing.command.dto.PrintGetTemplateExtResponse;
import org.geomajas.plugin.printing.command.dto.PrintTemplateInfo;
import org.geomajas.plugin.printing.component.LabelComponent;
import org.geomajas.plugin.printing.component.LegendComponent;
import org.geomajas.plugin.printing.component.PageComponent;
import org.geomajas.plugin.printing.component.PrintComponent;
import org.geomajas.plugin.printing.component.impl.LabelComponentImpl;
import org.geomajas.plugin.printing.component.impl.LegendComponentImpl;
import org.geomajas.plugin.printing.component.impl.LegendItemComponentImpl;
import org.geomajas.plugin.printing.component.service.PrintDtoConverterService;
import org.geomajas.plugin.printing.configuration.PrintTemplate;
import org.geomajas.plugin.printing.document.Document.Format;
import org.geomajas.plugin.printing.document.SinglePageDocument;
import org.geomajas.plugin.printing.service.PrintService;

import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;

/**
 * This class provides a static method that performs the first step in generating a template-based printout
 *  of for instance the specified map view, the map's legend,....
 * It returns the id of the document (possibly the rendering step is not yet completed).
 * The actual printout can be obtained via the PrintingController. 
 *
 * @author Oliver May
 * @author An Buyle
 *
 */
public class LayoutAsSinglePageDoc {
	//private final Logger log = LoggerFactory.getLogger(LayoutAsSinglePageDoc.class);
	protected LayoutAsSinglePageDoc() {
	}
	
	/**
	 * Perform the first step in generating a template-based printout of for instance the specified map view,
	 * the map's legend,....
	 * 
	 * @param request
	 * 				(Input) It specifies the template and the output format of the document
	 * @param response 
	 * 				(Output) On return, it contains the id of the document (possibly the rendering step is not yet
	 * 				completed)
	 *
	 * @param converterService
	 * @param printService
	 * @throws GeomajasException
	 */
	public static void execute(PrintGetTemplateExtRequest request, PrintGetTemplateExtResponse response,
					PrintDtoConverterService converterService, PrintService printService) throws GeomajasException {
		PrintTemplateInfo template = request.getTemplate();
		PageComponent page = (PageComponent) converterService.toInternal(template.getPage());
		

		// Adjust the title text of the legend component if present
		updateLegendChildren(request, page);
		

		if (request.getPageSize() != null) {
			page.setSize(request.getPageSize(), true);
		}
		SinglePageDocument pdfDoc = new SinglePageDocument(page, null);
		
		Format outputFormat = getFormatForExt(null == request.getOutputFormat() ? 
						PrintGetTemplateExtRequest.DEFAULT_OUTPUT_FORMAT : request.getOutputFormat());
		
		if (null == outputFormat) {
			// Unsupported format
			throw new GeomajasException(36, (null == request.getOutputFormat()) ?
							"{null}" : request.getOutputFormat());
		}
		// First pass is to perform the layout of the rendered doc 
		pdfDoc.layout(outputFormat); // outputFormat: PDF, PNG, ... 
		// Add document to container
		String documentId = printService.putDocument(pdfDoc);
		response.setDocumentId(documentId);
	}

	private static void updateLegendChildren(PrintGetTemplateExtRequest request, PrintComponent<?> parent) {
		if (null == parent.getChildren()) {
			return; // Do nothing
		}
		// Search recursive for for all PrintTemplate.LEGEND components wherever in the document tree they are located
		for (PrintComponent<?> pc : parent.getChildren()) {
			if (pc instanceof LegendComponentImpl) {
				updateTitle(request, (LegendComponent) pc);
				adjustLegendFontSizeForSmallPageSizes(request, (LegendComponentImpl) pc);
			} else if (null != pc.getChildren()) {
				updateLegendChildren(request, pc);
			}
   		}
	}

	private static void updateTitle(PrintGetTemplateExtRequest request, LegendComponent legendComp) {
	
		PrintComponent<?> lab = legendComp.getChild(PrintTemplate.TITLE);
		if (lab != null && lab instanceof LabelComponent) {
			((LabelComponent) lab).setText(((LegendComponent) legendComp).getTitle());
			((LabelComponentImpl) lab).setFont(legendComp.getFont().deriveFont(legendComp.getFont().getSize()));
		}
		// need to do this before setSizeAndFit
		adjustLegendFontSizeForSmallPageSizes(request, (LegendComponentImpl) legendComp);
	}

	private static Format getFormatForExt(String extension) {
		Format format = null;
		
		for (Format formatCandidate : Format.values()) {
			if (formatCandidate.getExtension().equals(extension)) {
				format = formatCandidate;
				break; // Stop when found
			}
		}
		return format;
	}

	private static void adjustLegendFontSizeForSmallPageSizes(PrintGetTemplateExtRequest request,
					LegendComponentImpl legendComponent) {
		// adjust font size legend for small page sizes (smaller than A3)
		float relPSize = getPageSizeRelativeToA3(request); // A3 == 100% fontsize
		if (relPSize < 1) {
			java.awt.Font newFont = legendComponent.getFont().deriveFont(legendComponent.getFont().getSize() * 0.8f);
			legendComponent.setFont(newFont); // basis font size for labelComponent children 
			//log.debug("PDF: changed fontsize to: {}", legendComponent.getFont().getSize());
			for (PrintComponent<?> pc : legendComponent.getChildren()) {
				if (pc instanceof LabelComponentImpl) { // The top level legend title (e.g. "Legend")
					((LabelComponentImpl) pc).setFont(newFont.deriveFont(newFont.getSize() * 1.25f));
																// (0.8 * 1.25 = 1.0)
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

	private static float getPageSizeRelativeToA3(PrintGetTemplateExtRequest request) {
		Rectangle r;
		if (request.getPageSize() != null) {
			r = PageSize.getRectangle(request.getPageSize());
		} else if (request.getTemplate().getPage().getLayoutConstraint().getWidth() > 0 &&
							request.getTemplate().getPage().getLayoutConstraint().getHeight() > 0) {
			float width = request.getTemplate().getPage().getLayoutConstraint().getWidth();
			float height = request.getTemplate().getPage().getLayoutConstraint().getHeight();
			r = new Rectangle(0, 0, width, height);
		} else {
			return 1.0f;
		}
		return (r.getWidth() / PageSize.A3.getWidth() + r.getHeight() / PageSize.A3.getHeight()) / 2;
	}


}
