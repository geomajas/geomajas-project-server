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
package org.geomajas.extension.printing.configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * In memory representation for stored print template.
 *
 * @author Jan De Moerloose
 */
public class InMemoryPrintTemplateDao implements PrintTemplateDao {

	private Map<String, PrintTemplate> templates = Collections.synchronizedMap(new HashMap<String, PrintTemplate>());

	public List<String> findAllNames() throws IOException {
		Set<String> set = templates.keySet();
		List<String> names = new ArrayList<String>(set);
		Collections.sort(names);
		return names;
	}

	public PrintTemplate findPrintByName(String name) throws IOException {
		PrintTemplate match = templates.get(name);
		if (match == null || match.isTemplate()) {
			return null;
		} else {
			return match;
		}
	}

	public List<PrintTemplate> findAll() throws IOException {
		return new ArrayList<PrintTemplate>(templates.values());
	}

	public List<PrintTemplate> findByExample(PrintTemplate exampleInstance, String... excludeProperty)
			throws IOException {
		throw new IOException("Operation not supported");
	}

	public PrintTemplate findById(Long id, boolean lock) throws IOException {
		throw new IOException("Operation not supported");
	}

	public PrintTemplate findUniqueByExample(PrintTemplate exampleInstance) throws IOException {
		throw new IOException("Operation not supported");
	}

	public PrintTemplate makePersistent(PrintTemplate entity) throws IOException {
		entity.setId(new Long(entity.getName().hashCode()));
		templates.put(entity.getName(), entity);
		return entity;
	}

	public void merge(PrintTemplate entity) throws IOException {
		entity.setId(new Long(entity.getName().hashCode()));
		templates.put(entity.getName(), entity);
	}

	public void makeTransient(PrintTemplate entity) throws IOException {
		templates.remove(entity.getName());
	}

}
