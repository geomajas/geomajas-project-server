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
package org.geomajas.plugin.printing;

import org.geomajas.plugin.printing.document.AbstractDocument;

import java.util.HashMap;
import java.util.NoSuchElementException;

/**
 * <p>
 * Singleton class that temporarily stores requested pdf documents.
 * </p>
 *
 * @author Oliver May
 */
// TODO: this class should be made less static and stored in the usercontext!
// @todo JVDA I rather expect this should be converted to a Spring service (which is a singleton)	
public class PdfContainer {

	public static final PdfContainer INSTANCE = new PdfContainer();

	protected PdfContainer() {
	}

	private HashMap<Integer, AbstractDocument> documents = new HashMap<Integer, AbstractDocument>();

	/**
	 * Get a PdfContainer instance.
	 *
	 * @return (only) instance of PDFContainer.
	 */
	public static PdfContainer getInstance() {
		return INSTANCE;
	}

	/**
	 * Add document.
	 *
	 * @param doc document to be added to container.
	 * @return document identifier.
	 */
	public int addDocument(AbstractDocument doc) {
		documents.put(doc.hashCode(), doc);
		return doc.hashCode();
	}

	/**
	 * Return the document requested by the identifier, after return the
	 * document is deleted.
	 *
	 * @param id document identifier
	 * @return the requested document in memory
	 * @throws NoSuchElementException
	 */
	public AbstractDocument getDocument(int id) throws NoSuchElementException {

		if (!documents.containsKey(id)) {
			throw new NoSuchElementException();
		}

		AbstractDocument doc = documents.get(id);
		documents.remove(id);
		return doc;
	}
}