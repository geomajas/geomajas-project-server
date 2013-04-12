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

import java.util.LinkedHashMap;
import java.util.List;

import org.geomajas.codemirror.client.widget.CodeMirrorPanel;
import org.geomajas.sld.editor.common.client.i18n.SldEditorMessages;
import org.geomajas.sld.editor.expert.client.domain.SldInfo;
import org.geomajas.sld.editor.expert.client.i18n.SldEditorExpertMessages;
import org.geomajas.sld.editor.expert.client.presenter.SldEditorExpertPresenter;
import org.geomajas.sld.editor.expert.client.presenter.event.SldCancelEvent;
import org.geomajas.sld.editor.expert.client.presenter.event.SldValidateEvent;
import org.geomajas.sld.editor.expert.client.presenter.event.TemplateSelectEvent;

import com.google.gwt.core.shared.GWT;
import com.google.web.bindery.event.shared.EventBus;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * The main editor panel containing toolbar and textarea.
 * 
 * @author Kristof Heirwegh
 */
public class SldEditorPanel extends VLayout {
	
	private static final SldEditorMessages MSG = GWT.create(SldEditorMessages.class);
	private static final SldEditorExpertMessages EXP_MSG = GWT.create(SldEditorExpertMessages.class);
	
	private CodeMirrorPanel codeArea;
	private ToolStrip buttonBar;
	private SelectItem templateSelect;
	private SldEditorExpertPresenter.MyView view;
	private String oldTemplate;

	public SldEditorPanel(EventBus eventBus, SldEditorExpertPresenter.MyView view) {
		super(0);
		this.view = view;

		buttonBar = new ToolStrip();
		buttonBar.setWidth("650px");
		codeArea = new CodeMirrorPanel();
		codeArea.setHeight("400px");
		codeArea.setWidth("650px");

		addMember(buttonBar);
		addMember(codeArea);

		createActions();
		// TODO make configurable
	}

	public void setTemplates(List<SldInfo> templates) {
		LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();
		for (SldInfo info : templates) {
			data.put(info.getName(), info.getTitle() != null ? info.getTitle() : info.getName());
		}
		templateSelect.clearValue();
		templateSelect.setValueMap(data);
	}
	
	public void setData(String xml) {
		// because of codemirrorFoefelare we can't guarantee that the widget already exists, so deferring it if not.
		if (codeArea.getEditor() != null) {
			codeArea.getEditor().setContent(xml == null ? "" : xml);
			codeArea.getEditor().clearHistory();
		} else {
			codeArea.setInitialData(xml);
		}
	}

	public void selectTemplateCancelled() {
		templateSelect.setValue(oldTemplate);
	}
	
	public String getData() {
		return codeArea.getEditor().getContent();
	}

	/**
	 * Please note that you cannot actually set dirty == true, only to false.
	 * @param dirty
	 */
	public void setDataDirty(boolean dirty) {
		if (!dirty && codeArea.getEditor() != null) {
			codeArea.getEditor().markClean();
		}
	}
	
	public boolean isDataDirty() {
		return !codeArea.getEditor().isClean();
	}
	
	public void clearValues() {
		codeArea.getEditor().setContent("");
		codeArea.getEditor().markClean();
		templateSelect.clearValue();
		// templateSelect.setValueMap(new LinkedHashMap<String, String>());
	}
	
	// ---------------------------------------------------------------

	private void createActions() {
		ToolStripButton saveBtn = new ToolStripButton();
		saveBtn.setIcon("[ISOMORPHIC]/" + "icons/silk/disk.png");
		saveBtn.setTitle(MSG.saveButtonTitle());
		saveBtn.setTooltip(MSG.saveButtonTooltip());
		saveBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SldValidateEvent.fire(view, new SldValidateEvent(true));
			}
		});
		
		ToolStripButton cancelBtn = new ToolStripButton();
		cancelBtn.setIcon("[ISOMORPHIC]/" + "icons/silk/cancel.png");
		cancelBtn.setTitle(MSG.cancelButtonTitle());
		cancelBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SldCancelEvent.fire(view);
			}
		});

		ToolStripButton validateBtn = new ToolStripButton();
		validateBtn.setIcon("[ISOMORPHIC]/" + "icons/silk/tick.png");
		validateBtn.setTitle(EXP_MSG.validateButtonTitle());
		validateBtn.setTooltip(EXP_MSG.validateButtonTooltip());
		validateBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SldValidateEvent.fire(view);
			}
		});
		
		templateSelect = new SelectItem();
		templateSelect.setTitle(EXP_MSG.templateSelectTitle());
		templateSelect.setTooltip(EXP_MSG.templateSelectTooltip());
		templateSelect.setWidth(250);
		templateSelect.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				oldTemplate = (String) event.getOldValue();
				TemplateSelectEvent.fire(view, (String) event.getValue());
			}
		});
		
		buttonBar.addFormItem(templateSelect);
		buttonBar.addButton(validateBtn);
		buttonBar.addSeparator();
		buttonBar.addButton(saveBtn);
		buttonBar.addFill();  
		buttonBar.addButton(cancelBtn);
	}
}
