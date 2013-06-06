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

package org.geomajas.puregwt.example.client.sample;


/**
 * Factory for creating sample panels.
 * 
 * @author Pieter De Graef
 */
public interface ShowcaseSampleDefinition {

	/** Create a new sample panel instance. */
	SamplePanel create();

	/** Get the title for the created sample. */
	String getTitle();
	
	/** Get a short description that is used in the sample overview. */
	String getShortDescription();

	/** Get the detailed description for this sample. */
	String getDescription();

	/** Get the category of samples this particular sample belongs to. */
	String getCategory();
}