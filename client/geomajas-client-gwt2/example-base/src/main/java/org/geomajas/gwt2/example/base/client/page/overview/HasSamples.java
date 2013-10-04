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

package org.geomajas.gwt2.example.base.client.page.overview;

import java.util.List;

import org.geomajas.gwt2.example.base.client.sample.ShowcaseSampleDefinition;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * Base definition for the sample overview GUI.
 * 
 * @author Pieter De Graef
 */
public interface HasSamples extends IsWidget {

	void setData(List<ShowcaseSampleDefinition> data);
}