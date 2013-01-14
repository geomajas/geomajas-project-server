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

package org.geomajas.plugin.jsapi.example.client.example;

/**
 * Meta-data for an example in the showcase.
 * 
 * @author Pieter De Graef
 */
public class Example {

	private String title;

	private String explanation;

	private String link;

	public Example(String title, String explanation, String link) {
		this.title = title;
		this.explanation = explanation;
		this.link = link;
	}

	public String getTitle() {
		return title;
	}

	public String getExplanation() {
		return explanation;
	}

	public String getLink() {
		return link;
	}
}
