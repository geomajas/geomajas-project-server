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
package org.geomajas.plugin.printing.client.widget;

import java.util.LinkedHashMap;

import org.geomajas.command.CommandResponse;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.printing.client.PrintingMessages;
import org.geomajas.plugin.printing.client.template.DefaultTemplateBuilder;
import org.geomajas.plugin.printing.client.template.PageSize;
import org.geomajas.plugin.printing.client.util.UrlBuilder;
import org.geomajas.plugin.printing.command.dto.PrintGetTemplateRequest;
import org.geomajas.plugin.printing.command.dto.PrintGetTemplateResponse;
import org.geomajas.plugin.printing.command.dto.PrintTemplateInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.FormItemIcon;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.SliderItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

/**
 * Canvas for choosing print preferences and printing.
 * 
 * @author Jan De Moerloose
 * 
 */
public class PrintPreferencesCanvas extends Canvas {

	private PrintingMessages messages = GWT.create(PrintingMessages.class);

	private TextItem titleItem;

	private TextItem fileNameItem;

	private SelectItem sizeItem;

	private RadioGroupItem orientationGroup;

	private SliderItem rasterDpiSlider;

	private CheckboxItem arrowCheckbox;

	private CheckboxItem scaleBarCheckbox;

	private RadioGroupItem downloadTypeGroup;

	private StaticTextItem statusText;

	private FormItemIcon barIcon;

	private MapWidget mapWidget;

	public PrintPreferencesCanvas(MapWidget mapWidget) {
		this.mapWidget = mapWidget;
		// tab set
		TabSet tabs = new TabSet();
		tabs.setWidth(400);
		tabs.setHeight(330);

		// create the one and only tab pane
		Tab mainPrefs = new Tab();
		mainPrefs.setTitle(messages.printPrefsChoose());

		// create the form
		DynamicForm form = new DynamicForm();
		// title
		titleItem = new TextItem();
		titleItem.setName("title");
		titleItem.setTitle(messages.printPrefsTitleText());
		// size
		sizeItem = new SelectItem();
		sizeItem.setName("size");
		sizeItem.setTitle(messages.printPrefsSize());
		sizeItem.setValueMap(PageSize.getAllNames());
		sizeItem.setValue(PageSize.A4.getName());
		// orientation
		orientationGroup = new RadioGroupItem();
		orientationGroup.setName("orientation");
		orientationGroup.setTitle(messages.printPrefsOrientation());
		LinkedHashMap<String, String> orientations = new LinkedHashMap<String, String>();
		orientations.put("landscape", messages.printPrefsLandscape());
		orientations.put("portrait", messages.printPrefsPortrait());
		orientationGroup.setValueMap(orientations);
		orientationGroup.setVertical(false);
		orientationGroup.setValue("landscape");
		// raster dpi slider
		rasterDpiSlider = new SliderItem();
		rasterDpiSlider.setTitle(messages.printPrefsRasterDPI());
		rasterDpiSlider.setWidth(250);
		rasterDpiSlider.setHeight(30);
		rasterDpiSlider.setMinValue(72);
		rasterDpiSlider.setMaxValue(500);
		rasterDpiSlider.setNumValues(5);
		// north arrow
		arrowCheckbox = new CheckboxItem();
		arrowCheckbox.setValue(true);
		arrowCheckbox.setTitle(messages.printPrefsWithArrow());
		// scale bar
		scaleBarCheckbox = new CheckboxItem();
		scaleBarCheckbox.setValue(true);
		scaleBarCheckbox.setTitle(messages.printPrefsWithScaleBar());
		// filename
		fileNameItem = new TextItem();
		fileNameItem.setName("filename");
		fileNameItem.setTitle(messages.printPrefsFileName());
		fileNameItem.setValue(mapWidget.getMapModel().getMapInfo().getId() + ".pdf");

		// progress indicator
		barIcon = new FormItemIcon();
		barIcon.setHeight(15);
		barIcon.setWidth(214);
		statusText = new StaticTextItem(messages.printPrefsStatus());
		statusText.setIcons(barIcon);
		barIcon.setSrc("[ISOMORPHIC]/geomajas/plugin/printing/pleasewait-blank.gif");
		// download type
		downloadTypeGroup = new RadioGroupItem();
		downloadTypeGroup.setName("downloadType");
		downloadTypeGroup.setTitle(messages.printPrefsDownloadType());
		LinkedHashMap<String, String> types = new LinkedHashMap<String, String>();
		types.put("save", messages.printPrefsSaveAsFile());
		types.put("open", messages.printPrefsOpenInBrowserWindow());
		downloadTypeGroup.setValueMap(types);
		downloadTypeGroup.setVertical(false);
		downloadTypeGroup.setValue("save");

		form.setFields(titleItem, sizeItem, orientationGroup, arrowCheckbox, scaleBarCheckbox, rasterDpiSlider,
				fileNameItem, downloadTypeGroup, statusText);
		mainPrefs.setPane(form);
		tabs.setTabs(mainPrefs);

		IButton printButton = new IButton();
		printButton.setTitle(messages.printPrefsPrint());
		printButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				print();
			}
		});

		VLayout vLayout = new VLayout();
		vLayout.setMembersMargin(10);
		vLayout.addMember(tabs);
		vLayout.addMember(printButton);
		addChild(vLayout);
	}

	private void stopProgress() {
		barIcon.setSrc("[ISOMORPHIC]/geomajas/plugin/printing/pleasewait-blank.gif");
		redraw();
	}

	private void startProgress() {
		barIcon.setSrc("[ISOMORPHIC]/geomajas/plugin/printing/pleasewait.gif");
		redraw();
	}

	protected void print() {
		startProgress();
		PrintGetTemplateRequest request = new PrintGetTemplateRequest();
		DefaultTemplateBuilder builder = new DefaultTemplateBuilder();
		builder.setApplicationId(mapWidget.getApplicationId());
		builder.setMapModel(mapWidget.getMapModel());
		builder.setMarginX(20);
		builder.setMarginY(20);
		PageSize size = PageSize.getByName((String) sizeItem.getValue());
		if ("landscape".equals(orientationGroup.getValue())) {
			builder.setPageHeight(size.getWidth());
			builder.setPageWidth(size.getHeight());
		} else {
			builder.setPageHeight(size.getHeight());
			builder.setPageWidth(size.getWidth());
		}
		builder.setTitleText((String) titleItem.getValue());
		builder.setWithArrow((Boolean) arrowCheckbox.getValue());
		builder.setWithScaleBar((Boolean) scaleBarCheckbox.getValue());
		builder.setRasterDpi((Integer) rasterDpiSlider.getValue());
		PrintTemplateInfo template = builder.buildTemplate();
		request.setTemplate(template);
		final GwtCommand command = new GwtCommand("command.print.GetTemplate");
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command, new CommandCallback() {

			public void execute(CommandResponse r) {
				stopProgress();
				if (r instanceof PrintGetTemplateResponse) {
					PrintGetTemplateResponse response = (PrintGetTemplateResponse) r;
					GWT.log("Downloading " + response.getDocumentId(), null);
					UrlBuilder url = new UrlBuilder(GWT.getHostPageBaseURL());
					url.addPath("d/printing").addParameter("documentId", response.getDocumentId());
					url.addParameter("name", (String) fileNameItem.getValue());
					url.addParameter("userToken", command.getUserToken());
					if ("save".equals(downloadTypeGroup.getValue())) {
						url.addParameter("download", "1");
						String encodedUrl = url.toString();
						// create a hidden iframe to avoid popups ???
						HTMLPanel hiddenFrame = new HTMLPanel("<iframe src='" + encodedUrl
								+ "'+style='position:absolute;width:0;height:0;border:0'>");
						hiddenFrame.setVisible(false);
						addChild(hiddenFrame);
					} else {
						url.addParameter("download", "0");
						String encodedUrl = url.toString();
						com.google.gwt.user.client.Window.open(encodedUrl, "_blank", null);
					}
				}
			}
		});
	}

}
