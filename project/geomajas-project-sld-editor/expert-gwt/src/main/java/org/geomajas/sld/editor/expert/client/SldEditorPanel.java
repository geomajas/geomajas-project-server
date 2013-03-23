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

import org.geomajas.codemirror.client.widget.CodeMirrorPanel;

import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * The main editor panel containing toolbar and textarea.
 * 
 * @author Kristof Heirwegh
 */
public class SldEditorPanel extends VLayout {
	
	private CodeMirrorPanel codeArea;
	
	public SldEditorPanel() {
		super(0);
			
		HLayout buttonBar = new HLayout(10); // menubar or toolstrip
		buttonBar.setHeight(30);
		buttonBar.setWidth("650px");
		buttonBar.addMember(new Label("Hier komen buttons"));
		buttonBar.setBorder("thin solid #CCCCCC");
		codeArea = new CodeMirrorPanel();
		codeArea.setHeight("400px");
		codeArea.setWidth("650px");
		
		addMember(buttonBar);
		addMember(codeArea);
	}
	
}
