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

package org.geomajas.plugin.jsapi.example.client.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Static list of all examples in the showcase.
 * 
 * @author Pieter De Graef
 */
public final class Examples {

	private Examples() {
	}

	public static final List<Example> EXAMPLES = new ArrayList<Example>();

	static {
		EXAMPLES.add(new Example("Navigation", "Demonstrates how to use the ViewPort to navigate around the map.",
				"examples/navigation.html"));
		EXAMPLES.add(new Example("Map & Layers", "Demonstrates the methods of the LayersModel behind a map: hHow to "
				+ "get the available layers, and how to manipulate them.", "examples/layers.html"));
		EXAMPLES.add(new Example("Default Controllers", "Demonstrates how to use existing controllers from the "
				+ "Geomajas GWT face on a JavaScript map.", "examples/default_controllers.html"));
		EXAMPLES.add(new Example("Custom Controllers", "Demonstrates how to create custom controllers and apply them"
				+ " on the map. The controller in this sample prints out the mouse coordinates on the map.",
				"examples/custom_controller.html"));
		EXAMPLES.add(new Example("Search features", "Demonstrates how to search for features by ID and by using the"
				+ " maps bounding box.", "examples/search_features.html"));
		EXAMPLES.add(new Example("Busy events", "Demonstrates how to register to the Geomajas 'busy' events. These"
				+ " events display when the client is busy talking to the server.", "examples/busy_state.html"));
		EXAMPLES.add(new Example("Complex GWT app", "Demonstrates how a more complex GWT application can be used "
				+ "within a JavaScript environment.", "examples/gwt_app.html"));
		EXAMPLES.add(new Example("Cursors", "Demonstrates how change the cursor on the map. This sample includes a "
				+ "custom cursor.", "examples/cursors.html"));
	};
}