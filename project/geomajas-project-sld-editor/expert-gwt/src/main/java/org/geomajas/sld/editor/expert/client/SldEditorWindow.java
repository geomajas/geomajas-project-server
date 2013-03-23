/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.sld.editor.expert.client;

import com.smartgwt.client.widgets.Window;

/**
 * Wrapper for an SldEditorPanel, showing it as a window.
 * 
 * @author Kristof Heirwegh
 */
public class SldEditorWindow extends Window {

	public SldEditorWindow() {
		setAutoSize(true);
		setTitle("Expert SLD Editor");
		setAutoCenter(true);
		setCanDragReposition(true);
		setCanDragResize(false);
		setShowMinimizeButton(false);

		addItem(new SldEditorPanel());
	}

}
