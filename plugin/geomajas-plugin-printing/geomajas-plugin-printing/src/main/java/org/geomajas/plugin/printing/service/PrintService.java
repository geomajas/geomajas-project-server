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
package org.geomajas.plugin.printing.service;

import java.util.List;

import org.geomajas.plugin.printing.PrintingException;
import org.geomajas.plugin.printing.configuration.PrintTemplate;
import org.geomajas.plugin.printing.document.Document;

/**
 * Service for printing and template handling.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface PrintService {

	/**
	 * Retrieves all available templates.
	 * 
	 * @return a list of templates ordered by name
	 * @throws PrintingException
	 *             if the templates could not be fetched from db
	 */
	List<PrintTemplate> getAllTemplates() throws PrintingException;

	/**
	 * Saves a new template or updates an existing one.
	 * 
	 * @param template
	 *            the template
	 * @throws PrintingException
	 *             if persistence failed
	 */
	void saveOrUpdateTemplate(PrintTemplate template) throws PrintingException;

	/**
	 * Creates a new default template.
	 * 
	 * @param pagesize
	 *            the size of the page (A1,A2,A3,A4,..)
	 * @param landscape
	 *            true for landscape orientation
	 * @return the template
	 * @throws PrintingException
	 */
	PrintTemplate createDefaultTemplate(String pagesize, boolean landscape) throws PrintingException;

	/**
	 * Puts a new document in the service.
	 * 
	 * @param document
	 * @return key unique key to reference the document
	 */
	String putDocument(Document document);

	/**
	 * Removes a document from the service.
	 * 
	 * @param key
	 *            unique key to reference the document
	 * @return the document or null if no such document
	 * 
	 * @throws GeomajasException
	 *             if document does not exist
	 */
	Document removeDocument(String key) throws PrintingException;
}
