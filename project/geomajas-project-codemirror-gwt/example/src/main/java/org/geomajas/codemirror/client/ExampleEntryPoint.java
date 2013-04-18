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
package org.geomajas.codemirror.client;

import org.geomajas.codemirror.client.widget.CodeMirrorPanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * Entry point of Example Codemirror GWT wrapper.
 * 
 * @author Kristof Heirwegh
 */
public class ExampleEntryPoint implements EntryPoint {

	public void onModuleLoad() {
		// -- show some background
		String contents = 
			"<div style='margin-left: 5px; font-size: 100pt; font-weight: bold; color:#DDFFDD'>GEOMAJAS</div>"
			+ "<div style='margin-left: 10px; margin-top:-70px; font-size: 50pt; color:#CCCCCC'>Codemirror</div>"
			+ "<div style='margin-left: 10px; margin-top:-15px; font-size: 28pt; color:#DDDDDD'>GWT wrapper</div>";
		HTMLPanel html = new HTMLPanel(contents);
		html.setSize("100%", "100%");
		
		RootLayoutPanel.get().add(html);
		
		// -- show a codemirror panel
		String initialContent = 
			"<html>\n\t<head>\n\t\t<title>Geomajas GWT Codemirror wrapper sample</title>\n\t</head>\n" +
			"\n\t<body>\n\t\tRead more here: <a href=\"http://www.geomajas.org/documentation/main\">" +
			"Geomajas</a><br />\n\t\tand here: <a href=\"http://codemirror.net/\">CodeMirror</a><br />\n\t" +
			"</body>\n</html>";

		final PopupPanel popup = new PopupPanel(false);
		popup.setSize("800px", "450px");
		popup.setWidget(new CodeMirrorPanel(initialContent));
		popup.center();
	}
}
