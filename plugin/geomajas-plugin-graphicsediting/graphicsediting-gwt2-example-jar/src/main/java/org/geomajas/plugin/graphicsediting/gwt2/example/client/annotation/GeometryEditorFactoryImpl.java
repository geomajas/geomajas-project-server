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

package org.geomajas.plugin.graphicsediting.gwt2.example.client.annotation;

import org.geomajas.plugin.editing.gwt.client.GeometryEditor;
import org.geomajas.plugin.editing.gwt.client.GeometryEditorFactory;
import org.geomajas.plugin.editing.gwt.client.GeometryEditorImpl;
import org.geomajas.gwt2.client.gfx.GfxUtil;
import org.geomajas.gwt2.client.map.MapPresenter;

/**
 * GeometryEditorFactoryImpl.
 * 
 * @author Jan De Moerloose
 */
public class GeometryEditorFactoryImpl implements GeometryEditorFactory {

	private GfxUtil gfxUtil;
	
	public GeometryEditorFactoryImpl(GfxUtil gfxUtil) {
		this.gfxUtil = gfxUtil;
	}
	
	@Override
	public GeometryEditor create(MapPresenter mapPresenter) {

		return new GeometryEditorImpl(mapPresenter, gfxUtil);
	}
	
}
