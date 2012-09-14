/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.client.gwt.manager.events;

import java.util.LinkedHashSet;
import java.util.Set;

import org.geomajas.gwt.client.util.Log;

/**
 * @author Kristof Heirwegh
 */
public final class Whiteboard {

	// private I say
	private Whiteboard() {
	}

	private static final Set<BlueprintHandler> BLUEPRINT_HANDLERS = new LinkedHashSet<BlueprintHandler>();

	private static final Set<BlueprintSelectionHandler> BLUEPRINT_SELECTION_HANDLERS = 
		new LinkedHashSet<BlueprintSelectionHandler>();

	private static final Set<GeodeskHandler> LOKET_HANDLERS = new LinkedHashSet<GeodeskHandler>();

	private static final Set<LayerModelHandler> LAYER_MODEL_HANDLERS = new LinkedHashSet<LayerModelHandler>();

	private static final Set<EditSessionHandler> EDIT_SESSION_HANDLERS = new LinkedHashSet<EditSessionHandler>();

	// ----------------------------------------------------------

	public static void registerHandler(BlueprintSelectionHandler bph) {
		BLUEPRINT_SELECTION_HANDLERS.add(bph);
	}

	public static void unregisterHandler(BlueprintSelectionHandler bph) {
		BLUEPRINT_SELECTION_HANDLERS.remove(bph);
	}

	public static void registerHandler(BlueprintHandler bph) {
		BLUEPRINT_HANDLERS.add(bph);
	}

	public static void unregisterHandler(BlueprintHandler bph) {
		BLUEPRINT_HANDLERS.remove(bph);
	}

	public static void registerHandler(GeodeskHandler lh) {
		LOKET_HANDLERS.add(lh);
	}

	public static void unregisterHandler(GeodeskHandler lh) {
		LOKET_HANDLERS.remove(lh);
	}

	public static void registerHandler(LayerModelHandler lme) {
		LAYER_MODEL_HANDLERS.add(lme);
	}

	public static void unregisterHandler(LayerModelHandler lme) {
		LAYER_MODEL_HANDLERS.remove(lme);
	}

	public static void registerHandler(EditSessionHandler esh) {
		EDIT_SESSION_HANDLERS.add(esh);
	}

	public static void unregisterHandler(EditSessionHandler esh) {
		EDIT_SESSION_HANDLERS.remove(esh);
	}

	// ----------------------------------------------------------

	public static void fireEvent(BlueprintEvent e) {
		for (BlueprintHandler bph : BLUEPRINT_HANDLERS) {
			try {
				bph.onBlueprintChange(e);
			} catch (Exception e2) {
				Log.logWarn("EventHandlerException (BlueprintEvent)", e2);
			}
		}
	}

	public static void fireChangeEvent(BlueprintEvent e) {
		for (BlueprintSelectionHandler bph : BLUEPRINT_SELECTION_HANDLERS) {
			try {
				bph.onBlueprintSelectionChange(e);
			} catch (Exception e2) {
				Log.logWarn("EventHandlerException (BlueprintEvent)", e2);
			}
		}
	}

	public static void fireEvent(GeodeskEvent e) {
		for (GeodeskHandler le : LOKET_HANDLERS) {
			try {
				le.onGeodeskChange(e);
			} catch (Exception e2) {
				Log.logWarn("EventHandlerException (GeodeskEvent)", e2);
			}
		}
	}

	public static void fireEvent(LayerModelEvent e) {
		for (LayerModelHandler lme : LAYER_MODEL_HANDLERS) {
			try {
				lme.onLayerModelChange(e);
			} catch (Exception e2) {
				Log.logWarn("EventHandlerException (LayerModelEvent)", e2);
			}
		}
	}

	public static void fireEvent(EditSessionEvent e) {
		for (EditSessionHandler esh : EDIT_SESSION_HANDLERS) {
			try {
				esh.onEditSessionChange(e);
			} catch (Exception e2) {
				Log.logWarn("EventHandlerException (EditSessionEvent)", e2);
			}
		}
	}

}
