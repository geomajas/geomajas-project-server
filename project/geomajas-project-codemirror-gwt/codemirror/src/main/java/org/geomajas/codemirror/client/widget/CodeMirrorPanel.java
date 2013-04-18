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
package org.geomajas.codemirror.client.widget;

import org.geomajas.codemirror.client.CodeMirrorWrapper;
import org.geomajas.codemirror.client.Config;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;

/**
 * A code editing Panel. 
 * 
 * @author Kristof Heirwegh
 */
public class CodeMirrorPanel extends SimplePanel {
	
	private CodeMirrorWrapper editor;
	private TextArea textArea;
	private Config config;
	
	/**
	 * Create an new codemirrorpanel with the default configuration.
	 */
	public CodeMirrorPanel() {
		this(Config.forXml());
	}
	
	/**
	 * Create an new codemirrorpanel with the default configuration.
	 * @param initialData the initial content.
	 */
	public CodeMirrorPanel(String initialData) {
		this(Config.forXml());
		setInitialData(initialData);
	}
	
	public CodeMirrorPanel(Config config) {
		super();
		this.config = config;
		setWidth("100%");
		setHeight("100%");
		textArea = new TextArea();
		setWidget(textArea);
	}
	
	public CodeMirrorWrapper getEditor() {
		return editor;
	}

	public void setInitialData(String initialData) {
		textArea.setValue(initialData);
	}

	// ------------------------------------------------------------------

	@Override
	protected void onLoad() {
		super.onLoad();
		attachtCodeMirror();
	}

	private void attachtCodeMirror() {
		Scheduler.get().scheduleDeferred(new Command() {
			public void execute() {
				editor = CodeMirrorWrapper.fromTextArea(textArea, config == null ? Config.getDefault() : config);
			}
		});
	}
}
