/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.widget.featureinfo.configuration.client;

import org.geomajas.configuration.client.ClientWidgetInfo;

/**
 * Configuration properties for custom widgets.
 * 
 * @author Kristof Heirwegh
 */
public class WidgetBuilderInfo implements ClientWidgetInfo {

	private static final long serialVersionUID = 180L;

	/**
	 * The name (key) of a builder for the custom Widget.
	 * (the type will depend on the face, eg. a Canvas builder when using GWT)
	 * <p>Do not forget to add your builder to the widgetfactory (in EntryPoint.onModuleLoad).</p>
	 */
	private String builderName;

	public String getBuilderName() {
		return builderName;
	}

	public void setBuilderName(String builderName) {
		this.builderName = builderName;
	}

}
