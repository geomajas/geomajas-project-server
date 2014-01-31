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
package org.geomajas.plugin.printing.configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * In memory representation for stored print template.
 *
 * @author Jan De Moerloose
 */
@Component
public class InMemoryPrintTemplateDao implements PrintTemplateDao {

	private Map<String, PrintTemplate> templates = Collections.synchronizedMap(new HashMap<String, PrintTemplate>());

	public List<PrintTemplate> findAll() throws IOException {
		return new ArrayList<PrintTemplate>(templates.values());
	}

	public void merge(PrintTemplate entity) throws IOException {
		entity.setId(Long.valueOf(entity.getName().hashCode()));
		templates.put(entity.getName(), entity);
	}


}
