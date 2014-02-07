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
package org.geomajas.plugin.printing.document;

import java.io.OutputStream;
import java.util.Map;

import org.geomajas.plugin.printing.PrintingException;
import org.geomajas.plugin.printing.configuration.DefaultConfigurationVisitor;
import org.geomajas.plugin.printing.configuration.MapConfigurationVisitor;
import org.geomajas.plugin.printing.configuration.PrintTemplate;

/**
 * Default document for printing.
 * 
 * @author Jan De Moerloose
 */
public class DefaultDocument extends SinglePageDocument {

	private DefaultConfigurationVisitor defaultVisitor;

	private MapConfigurationVisitor visitor;

	public DefaultDocument(PrintTemplate template, Map<String, String> filters,
			DefaultConfigurationVisitor defaultVisitor, MapConfigurationVisitor visitor) {
		super(template.getPage(), filters);
		this.defaultVisitor = defaultVisitor;
		this.visitor = visitor;
	}

	@Override
	public void render(OutputStream outputStream, Format format) throws PrintingException {
		defaultVisitor.visitTree(getPage());
		visitor.visitTree(getPage());
		super.render(outputStream, format);
	}

}
