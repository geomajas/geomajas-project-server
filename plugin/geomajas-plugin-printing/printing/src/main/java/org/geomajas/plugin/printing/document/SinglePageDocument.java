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

package org.geomajas.plugin.printing.document;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.geomajas.plugin.printing.PrintingException;
import org.geomajas.plugin.printing.component.MapComponent;
import org.geomajas.plugin.printing.component.PageComponent;
import org.geomajas.plugin.printing.component.PdfContext;
import org.geomajas.plugin.printing.component.PrintComponent;
import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Single page document for printing.
 * 
 * @author Jan De Moerloose
 */
public class SinglePageDocument extends AbstractDocument {

	/** The page to render. */
	protected PageComponent page;

	/** Filters to apply to layers. */
	protected Map<String, String> filters;

	/** In-memory output stream to know content length. */
	private ByteArrayOutputStream baos;

	/**
	 * Constructs a document with the specified dimensions.
	 * 
	 * @param page
	 *            page
	 * @param filters
	 *            filters
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
	public void render(OutputStream outputStream, Format format) throws PrintingException {
		try {
			doRender(outputStream, format);
		} catch (Exception e) { // NOSONAR
			throw new PrintingException(e, PrintingException.DOCUMENT_RENDER_PROBLEM);
		}
	}

	/**
	 * Re-calculates the layout and renders to internal memory stream. Always call this method before calling render()
	 * to make sure that the latest document changes have been taken into account for rendering.
	 * 
	 * @param format
	 *            format
	 * @throws PrintingException
	 *             oops
	 */
	public void layout(Format format) throws PrintingException {
		try {
			doRender(null, format);
		} catch (Exception e) { // NOSONAR
			throw new PrintingException(e, PrintingException.DOCUMENT_LAYOUT_PROBLEM);
		}
	}

	/**
	 * Prepare the document before rendering.
	 * 
	 * @param outputStream
	 *            output stream to render to, null if only for layout
	 * @param format
	 *            format
	 * @throws DocumentException
	 *             oops
	 * @throws IOException
	 *             oops
	 * @throws PrintingException
	 *             oops
	 */
	private void doRender(OutputStream outputStream, Format format) throws IOException, DocumentException,
			PrintingException {
		// first render or re-render for different layout
		if (outputStream == null || baos == null || null != format) {
			if (baos == null) {
				baos = new ByteArrayOutputStream(10 * 1024);
			}
			baos.reset();
			boolean resize = false;
			if (page.getBounds().getWidth() == 0 || page.getBounds().getHeight() == 0) {
				resize = true;
			}
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
			PdfContext context = new PdfContext(writer);
			context.initSize(page.getBounds());
			// first pass of all children to calculate size
			page.calculateSize(context);
			if (resize) {
				// we now know the bounds of the document
				// round 'm up and restart with a new document
				int width = (int) Math.ceil(page.getBounds().getWidth());
				int height = (int) Math.ceil(page.getBounds().getHeight());
				page.getConstraint().setWidth(width);
				page.getConstraint().setHeight(height);
				document = new Document(new Rectangle(width, height), 0, 0, 0, 0);
				writer = PdfWriter.getInstance(document, baos);
				// Render in correct colors for transparent rasters
				writer.setRgbTransparencyBlending(true);

				document.open();
				baos.reset();
				context = new PdfContext(writer);
				context.initSize(page.getBounds());
			}
			// int compressionLevel = writer.getCompressionLevel(); // For testing
			// writer.setCompressionLevel(0);

			// Actual drawing
			document.addTitle("Geomajas");
			// second pass to layout
			page.layout(context);
			// finally render
			page.render(context);
			document.add(context.getImage());
			// Now close the document
			document.close();
			// convert to non-pdf format
			switch (format) {
				case PDF:
					break;
				case PNG:
				case JPG:
					/** instance of PdfDecoder to convert PDF into image */
					PdfDecoder decodePdf = new PdfDecoder(true);

					/** set mappings for non-embedded fonts to use */
					PdfDecoder.setFontReplacements(decodePdf);
					decodePdf.useHiResScreenDisplay(true);
					decodePdf.getDPIFactory().setDpi(2 * 72);
					decodePdf.setPageParameters(1, 1);
					try {
						decodePdf.openPdfArray(baos.toByteArray());
						/** get page 1 as an image */
						BufferedImage img = decodePdf.getPageAsImage(1);

						/** close the pdf file */
						decodePdf.closePdfFile();
						baos.reset();
						ImageIO.write(img, format.getExtension(), baos);
					} catch (PdfException e) {
						throw new PrintingException(e, PrintingException.DOCUMENT_RENDER_PROBLEM);
					}
					break;
				default:
					throw new IllegalStateException(
							"Oops, software error, need to support extra format at end of render" + format);
			}
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