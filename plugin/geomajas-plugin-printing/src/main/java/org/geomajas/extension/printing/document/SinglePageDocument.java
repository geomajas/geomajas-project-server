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
package org.geomajas.extension.printing.document;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.geomajas.extension.printing.PdfContext;
import org.geomajas.extension.printing.component.MapComponent;
import org.geomajas.extension.printing.component.PageComponent;
import org.geomajas.extension.printing.component.PrintComponent;
import org.geomajas.service.ApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * ???
 *
 * @author check subversion
 */
public class SinglePageDocument extends AbstractDocument {

	private final Logger log = LoggerFactory.getLogger(SinglePageDocument.class);

	/**
	 * the page to render
	 */
	protected PageComponent page;

	protected ApplicationService runtime;

	/**
	 * filters to apply to layers
	 */
	protected Map<String, String> filters;

	/**
	 * If true, only layout, no rendering
	 */
	private boolean layoutOnly;

	/**
	 * Constructs a document with the specified dimensions.
	 *
	 * @param page
	 * @param application
	 * @param runtime
	 * @param filters
	 */
	public SinglePageDocument(PageComponent page, ApplicationService runtime,
			Map<String, String> filters) {
		this.page = page;
		this.runtime = runtime;
		this.filters = (filters == null ? new HashMap<String, String>() : filters);

		// set filters
		for (PrintComponent comp : getPage().getChildren()) {
			if (comp instanceof MapComponent) {
				((MapComponent) comp).setFilter(filters);
			}
		}
	}

	public void render() {
		try {
			// Create a document in the requested ISO scale.
			Document document = new Document(page.getBounds(), 0, 0, 0, 0);
			PdfWriter writer = PdfWriter.getInstance(document, documentStream);
			// Render in correct colors for transparent rasters 
			writer.setRgbTransparencyBlending(true);

			// The mapView is not scaled to the document, we assume the mapView
			// has the right ratio.

			// Write document title and metadata
			document.open();
			document.addTitle("Geomajas");

			// Actual drawing
			PdfContext context = new PdfContext(writer, runtime);
			context.initSize(page.getBounds());
			// first pass of all children to calculate size
			page.calculateSize(context);
			// second pass to layout
			page.layout(context);
			// render
			if (!isLayoutOnly()) {
				page.render(context);
				document.add(context.getImage());
				// Now close the document
				document.close();
			}
		} catch (Exception e) {
			log.error("Problem creating template document", e);
		}

	}

	public boolean isLayoutOnly() {
		return layoutOnly;
	}

	public void setLayoutOnly(boolean layoutOnly) {
		this.layoutOnly = layoutOnly;
	}

	public PageComponent getPage() {
		return page;
	}

}
