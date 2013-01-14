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

package org.geomajas.plugin.editing.gwt.client.handler;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.plugin.editing.client.handler.AbstractGeometryIndexMapHandler;
import org.geomajas.plugin.editing.client.handler.GeometryIndexDragSelectionHandler;
import org.geomajas.plugin.editing.client.handler.GeometryIndexHighlightHandler;
import org.geomajas.plugin.editing.client.handler.GeometryIndexInsertHandler;
import org.geomajas.plugin.editing.client.handler.GeometryIndexSelectHandler;
import org.geomajas.plugin.editing.client.handler.GeometryIndexSnapToDeleteHandler;
import org.geomajas.plugin.editing.client.handler.GeometryIndexStopInsertingHandler;

/**
 * ...
 * 
 * @author Pieter De Graef
 */
public final class EditingHandlerRegistry {

	/**
	 * ...
	 * 
	 * @author Pieter De Graef
	 */
	public interface VertexHandlerFactory {

		AbstractGeometryIndexMapHandler create();
	}

	/**
	 * ...
	 * 
	 * @author Pieter De Graef
	 */
	public interface EdgeHandlerFactory {

		AbstractGeometryIndexMapHandler create();
	}

	/**
	 * ...
	 * 
	 * @author Pieter De Graef
	 */
	public interface GeometryHandlerFactory {

		AbstractGeometryIndexMapHandler create();
	}

	private static final List<VertexHandlerFactory> VERTEX_FACTORIES = new ArrayList<VertexHandlerFactory>();

	private static final List<EdgeHandlerFactory> EDGE_FACTORIES = new ArrayList<EdgeHandlerFactory>();

	private static final List<GeometryHandlerFactory> GEOMETRY_FACTORIES = new ArrayList<GeometryHandlerFactory>();

	private EditingHandlerRegistry() {
		// Utility class: private constructor.
	}

	static {
		// Create all the default vertex handler factories:
		VERTEX_FACTORIES.add(new VertexHandlerFactory() {

			public AbstractGeometryIndexMapHandler create() {
				return new GeometryIndexHighlightHandler();
			}
		});
		VERTEX_FACTORIES.add(new VertexHandlerFactory() {

			public AbstractGeometryIndexMapHandler create() {
				return new GeometryIndexSelectHandler();
			}
		});
		VERTEX_FACTORIES.add(new VertexHandlerFactory() {

			public AbstractGeometryIndexMapHandler create() {
				return new GeometryIndexDragSelectionHandler();
			}
		});
		VERTEX_FACTORIES.add(new VertexHandlerFactory() {

			public AbstractGeometryIndexMapHandler create() {
				return new GeometryIndexSnapToDeleteHandler();
			}
		});
		VERTEX_FACTORIES.add(new VertexHandlerFactory() {

			public AbstractGeometryIndexMapHandler create() {
				return new GeometryIndexStopInsertingHandler();
			}
		});

		// Create all the default edge handler factories:
		EDGE_FACTORIES.add(new EdgeHandlerFactory() {

			public AbstractGeometryIndexMapHandler create() {
				return new GeometryIndexHighlightHandler();
			}
		});
		EDGE_FACTORIES.add(new EdgeHandlerFactory() {

			public AbstractGeometryIndexMapHandler create() {
				return new GeometryIndexInsertHandler();
			}
		});
		EDGE_FACTORIES.add(new EdgeHandlerFactory() {

			public AbstractGeometryIndexMapHandler create() {
				return new GeometryIndexSnapToDeleteHandler();
			}
		});

		// Create all the default geometry handler factories:
		// ....
	};

	public static void addGeometryHandlerFactory(GeometryHandlerFactory factory) {
		if (!GEOMETRY_FACTORIES.contains(factory)) {
			GEOMETRY_FACTORIES.add(factory);
		}
	}

	public static void removeGeometryHandlerFactory(GeometryHandlerFactory factory) {
		if (GEOMETRY_FACTORIES.contains(factory)) {
			GEOMETRY_FACTORIES.remove(factory);
		}
	}

	public static List<AbstractGeometryIndexMapHandler> getVertexHandlers() {
		List<AbstractGeometryIndexMapHandler> handlers = new ArrayList<AbstractGeometryIndexMapHandler>();
		for (VertexHandlerFactory factory : VERTEX_FACTORIES) {
			handlers.add(factory.create());
		}
		return handlers;
	}

	public static List<AbstractGeometryIndexMapHandler> getEdgeHandlers() {
		List<AbstractGeometryIndexMapHandler> handlers = new ArrayList<AbstractGeometryIndexMapHandler>();
		for (EdgeHandlerFactory factory : EDGE_FACTORIES) {
			handlers.add(factory.create());
		}
		return handlers;
	}

	public static List<AbstractGeometryIndexMapHandler> getGeometryHandlers() {
		List<AbstractGeometryIndexMapHandler> handlers = new ArrayList<AbstractGeometryIndexMapHandler>();
		for (GeometryHandlerFactory factory : GEOMETRY_FACTORIES) {
			handlers.add(factory.create());
		}
		return handlers;
	}
}