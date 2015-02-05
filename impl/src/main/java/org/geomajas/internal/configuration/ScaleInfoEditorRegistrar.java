/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.internal.configuration;

import org.geomajas.configuration.client.ScaleInfo;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;

/**
 * Custom {@link PropertyEditorRegistrar} implementation that registers {@link ScaleInfoEditor} any desired editors on a
 * given {@link PropertyEditorRegistry registry}.
 * 
 * @author Jan De Moerloose
 * 
 */
public class ScaleInfoEditorRegistrar implements PropertyEditorRegistrar {

	public void registerCustomEditors(PropertyEditorRegistry registry) {
		ScaleInfoEditor editor = new ScaleInfoEditor();
		registry.registerCustomEditor(ScaleInfo.class, editor);
	}

}
