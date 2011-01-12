/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.printing.document;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.geomajas.plugin.printing.PrintingException;
import org.geomajas.plugin.printing.component.MapComponent;
import org.geomajas.plugin.printing.component.PageComponent;
import org.geomajas.plugin.printing.component.PdfContext;
import org.geomajas.plugin.printing.component.PrintComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Single page document for printing.
 * 
 * @author Jan De Moerloose
 */
public class SinglePageDocument extends AbstractDocument {

	private final Logger log = LoggerFactory.getLogger(SinglePageDocument.class);

	/**
	 * the page to render
	 */
	protected PageComponent page;

	/**
	 * filters to apply to layers
	 */
	protected Map<String, String> filters;

	/**
	 * In-memory output stream to know content length.
	 */
	private ByteArrayOutputStream baos;

	/**
	 * Constructs a document with the specified dimensions.
	 * 
	 * @param page
	 * @param application
	 * @param runtime
	 * @param filters
	 */
	public SinglePageDocument(PageComponent page, Map<String, String> filters) {
		this.page = page;
		this.filters = (filters == null ? new HashMap<String, String>() : filters);

		// set filters
		for (PrintComponent comp : getPage().getChildren()) {
			if (comp instanceof MapComponent) {
				((MapComponent) comp).setFilter(filters);
			}
		}
	}

	/**
	 * Renders the document to the specified output stream.
	 */
	public void render(OutputStream outputStream) throws PrintingException {
		try {
			doRender(outputStream);
		} catch (Exception e) {
			throw new PrintingException(e, PrintingException.DOCUMENT_RENDER_PROBLEM);
		}
	}

	/**
	 * Re-calculates the layout and renders to internal memory stream. Always call this method before calling render()
	 * to make sure that the latest document changes have been taken into account for rendering.
	 * 
	 * @throws PrintingException
	 */
	public void layout() throws PrintingException {
		try {
			doRender(null);
		} catch (Exception e) {
			throw new PrintingException(e, PrintingException.DOCUMENT_LAYOUT_PROBLEM);
		}
	}

	/**
	 * Prepare the document before rendering.
	 * 
	 * @param outputStream
	 *            outputstream to render to, null if only for layout
	 * @throws DocumentException
	 * @throws IOException
	 */
	private void doRender(OutputStream outputStream) throws IOException, DocumentException {
		// first render or re-render for different layout
		if (outputStream == null || baos == null) {
			if (baos == null) {
				baos = new ByteArrayOutputStream();
			}
			baos.reset();
			// Create a document in the requested ISO scale.
			Document document = new Document(page.getBounds(), 0, 0, 0, 0);
			PdfWriter writer;
			writer = PdfWriter.getInstance(document, baos);

			// Render in correct colors for transparent rasters
			writer.setRgbTransparencyBlending(true);

			// The mapView is not scaled to the document, we assume the mapView
			// has the right ratio.

			// Write document title and metadata
			document.open();
			document.addTitle("Geomajas");

			// Actual drawing
			PdfContext context = new PdfContext(writer);
			context.initSize(page.getBounds());
			// first pass of all children to calculate size
			page.calculateSize(context);
			// second pass to layout
			page.layout(context);
			// finally render
			page.render(context);
			document.add(context.getImage());
			// Now close the document
			document.close();
			if (outputStream != null) {
				baos.writeTo(outputStream);
			}
		} else {
			baos.writeTo(outputStream);
		}
	}

	public PageComponent getPage() {
		return page;
	}

	public int getContentLength() {
		return baos == null ? 0 : baos.size();
	}

}
