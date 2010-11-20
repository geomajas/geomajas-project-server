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

package org.geomajas.gwt.client.gfx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Visitor pattern implementation for painter.
 * </p>
 * 
 * @author Jan De Moerloose
 */
public class PainterVisitor {

	/**
	 * The context on which all painter registered here should be applied.
	 */
	private MapContext context;

	/**
	 * Dictionary of registered painter. It is possible that more then one painter is registered for a certain type of
	 * object.
	 */
	private Map<String, List<Painter>> painters;

	/**
	 * This class should always be initialized with a <code>GraphicsContext</code> object, since all painter registered
	 * here will apply their painting skills on it.
	 * 
	 * @param graphics
	 *            Implementation of the MapContext interface.
	 */
	public PainterVisitor(MapContext context) {
		this.context = context;
		this.painters = new HashMap<String, List<Painter>>();
	}

	/**
	 * Register a new painter to this visitor.
	 * 
	 * @param painter
	 *            The painter itself.
	 */
	public void registerPainter(Painter painter) {
		if (painters.containsKey(painter.getPaintableClassName())) {
			List<Painter> list = painters.remove(painter.getPaintableClassName());
			list.add(painter);
			this.painters.put(painter.getPaintableClassName(), list);
		} else {
			List<Painter> list = new ArrayList<Painter>();
			list.add(painter);
			this.painters.put(painter.getPaintableClassName(), list);
		}
	}

	/**
	 * Unregister an existing painter from this visitor.
	 * 
	 * @param painter
	 *            The painter itself.
	 */
	public void unregisterPainter(Painter painter) {
		String className = painter.getPaintableClassName();
		if (painters.containsKey(className)) {
			List<Painter> list = painters.remove(className);
			if (list.size() > 1) {
				list.remove(painter);
				painters.put(className, list);
			}
		}
	}

	/**
	 * The visitors visit function.
	 * 
	 * @param paintable
	 */
	public void visit(Paintable paintable, Object group) {
		String className = paintable.getClass().getName();
		if (painters.containsKey(className)) {
			List<Painter> list = painters.get(className);
			for (Painter painter : list) {
				if (context.isReady()) {
					painter.paint(paintable, group, context);
				}
			}
		}
	}

	/**
	 * Remove a paintable object from the graphics.
	 * 
	 * @param paintable
	 */
	public void remove(Paintable paintable, Object group) {
		String className = paintable.getClass().getName();
		if (painters.containsKey(className)) {
			List<Painter> list = painters.get(className);
			for (Painter painter : list) {
				painter.deleteShape(paintable, group, context);
			}
		}
	}

	/**
	 * Retrieve the full list of painter for a specific type of paintable object.
	 * 
	 * @param paintable
	 *            The type of object to retrieve painter for.
	 * @return Return a list of painter, or null.
	 */
	public List<Painter> getPaintersForObject(Paintable paintable) {
		String className = paintable.getClass().getName();
		if (painters.containsKey(className)) {
			return painters.get(className);
		}
		return null;
	}
}