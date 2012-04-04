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
package org.geomajas.plugin.printing.client.widget;

import java.util.LinkedHashMap;

import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.printing.client.PrintingMessages;
import org.geomajas.plugin.printing.client.template.DefaultTemplateBuilder;
import org.geomajas.plugin.printing.client.template.PageSize;
import org.geomajas.plugin.printing.client.util.PrintingLayout;
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
 */
public class PrintPreferencesCanvas extends Canvas {

	private static final PrintingMessages MESSAGES = GWT.create(PrintingMessages.class);
	private static final String ORIENTATION = "orientation";
	private static final String LANDSCAPE = "landscape";
	private static final String PORTRAIT = "portrait";
	private static final String TITLE = "title";
	private static final String SIZE = "size";
	private static final String DOWNLOAD_TYPE = "downloadType";
	private static final String SAVE = "save";
	private static final String OPEN = "open";
	private static final String FILENAME = "filename";
	private static final String EXTENSION = ".pdf";
	private static final String URL_PATH = "d/printing";
	private static final String URL_DOCUMENT_ID = "documentId";
	private static final String URL_NAME = "name";
	private static final String URL_TOKEN = "userToken";
	private static final String URL_DOWNLOAD = "download";
	private static final String URL_DOWNLOAD_YES = "1";
	private static final String URL_DOWNLOAD_NO = "0";

	private TextItem titleItem;

	private TextItem fileNameItem;

	private SelectItem sizeItem;

	private RadioGroupItem orientationGroup;

	private SliderItem rasterDpiSlider;

	private CheckboxItem arrowCheckbox;

	private CheckboxItem scaleBarCheckbox;

	private RadioGroupItem downloadTypeGroup;

	private FormItemIcon barIcon;

	private MapWidget mapWidget;

	public PrintPreferencesCanvas(MapWidget mapWidget) {
		this.mapWidget = mapWidget;
		// tab set
		TabSet tabs = new TabSet();
		tabs.setWidth(PrintingLayout.printPreferencesWidth);
		tabs.setHeight(PrintingLayout.printPreferencesHeight);

		// create the one and only tab pane
		Tab mainPreferences = new Tab();
		mainPreferences.setTitle(MESSAGES.printPrefsChoose());

		// create the form
		DynamicForm form = new DynamicForm();
		// title
		titleItem = new TextItem();
		titleItem.setName(TITLE);
		titleItem.setTitle(MESSAGES.printPrefsTitleText());
		// size
		sizeItem = new SelectItem();
		sizeItem.setName(SIZE);
		sizeItem.setTitle(MESSAGES.printPrefsSize());
		sizeItem.setValueMap(PageSize.getAllNames());
		sizeItem.setValue(PageSize.A4.getName());
		// orientation
		orientationGroup = new RadioGroupItem();
		orientationGroup.setName(ORIENTATION);
		orientationGroup.setTitle(MESSAGES.printPrefsOrientation());
		LinkedHashMap<String, String> orientations = new LinkedHashMap<String, String>();
		orientations.put(LANDSCAPE, MESSAGES.printPrefsLandscape());
		orientations.put(PORTRAIT, MESSAGES.printPrefsPortrait());
		orientationGroup.setValueMap(orientations);
		orientationGroup.setVertical(false);
		orientationGroup.setValue(LANDSCAPE);
		// raster dpi slider
		rasterDpiSlider = new SliderItem();
		rasterDpiSlider.setTitle(MESSAGES.printPrefsRasterDPI());
		rasterDpiSlider.setWidth(PrintingLayout.printPreferencesResolutionWidth);
		rasterDpiSlider.setHeight(PrintingLayout.printPreferencesResolutionHeight);
		rasterDpiSlider.setMinValue(72);
		rasterDpiSlider.setMaxValue(600);
		rasterDpiSlider.setNumValues(5);
		// north arrow
		arrowCheckbox = new CheckboxItem();
		arrowCheckbox.setValue(true);
		arrowCheckbox.setTitle(MESSAGES.printPrefsWithArrow());
		// scale bar
		scaleBarCheckbox = new CheckboxItem();
		scaleBarCheckbox.setValue(true);
		scaleBarCheckbox.setTitle(MESSAGES.printPrefsWithScaleBar());
		// filename
		fileNameItem = new TextItem();
		fileNameItem.setName(FILENAME);
		fileNameItem.setTitle(MESSAGES.printPrefsFileName());
		fileNameItem.setValue(mapWidget.getMapModel().getMapInfo().getId() + EXTENSION);

		// progress indicator
		barIcon = new FormItemIcon();
		barIcon.setHeight(PrintingLayout.iconWaitHeight);
		barIcon.setWidth(PrintingLayout.iconWaitWidth);
		StaticTextItem statusText = new StaticTextItem(MESSAGES.printPrefsStatus());
		statusText.setIcons(barIcon);
		barIcon.setSrc(PrintingLayout.iconWaitBlank);
		// download type
		downloadTypeGroup = new RadioGroupItem();
		downloadTypeGroup.setName(DOWNLOAD_TYPE);
		downloadTypeGroup.setTitle(MESSAGES.printPrefsDownloadType());
		LinkedHashMap<String, String> types = new LinkedHashMap<String, String>();
		types.put(SAVE, MESSAGES.printPrefsSaveAsFile());
		types.put(OPEN, MESSAGES.printPrefsOpenInBrowserWindow());
		downloadTypeGroup.setValueMap(types);
		downloadTypeGroup.setVertical(false);
		downloadTypeGroup.setValue(SAVE);

		form.setFields(titleItem, sizeItem, orientationGroup, arrowCheckbox, scaleBarCheckbox, rasterDpiSlider,
				fileNameItem, downloadTypeGroup, statusText);
		mainPreferences.setPane(form);
		tabs.setTabs(mainPreferences);

		IButton printButton = new IButton();
		printButton.setTitle(MESSAGES.printPrefsPrint());
		printButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				print();
			}
		});

		VLayout vLayout = new VLayout();
		vLayout.setMembersMargin(WidgetLayout.marginLarge);
		vLayout.addMember(tabs);
		vLayout.addMember(printButton);
		addChild(vLayout);
	}

	private void stopProgress() {
		barIcon.setSrc(PrintingLayout.iconWaitBlank);
		redraw();
	}

	private void startProgress() {
		barIcon.setSrc(PrintingLayout.iconWaitMoving);
		redraw();
	}

	protected void print() {
		startProgress();
		PrintGetTemplateRequest request = new PrintGetTemplateRequest();
		DefaultTemplateBuilder builder = new DefaultTemplateBuilder();
		builder.setApplicationId(mapWidget.getApplicationId());
		builder.setMapModel(mapWidget.getMapModel());
		builder.setMarginX((int) PrintingLayout.templateMarginX);
		builder.setMarginY((int) PrintingLayout.templateMarginY);
		PageSize size = PageSize.getByName((String) sizeItem.getValue());
		if (LANDSCAPE.equals(orientationGroup.getValue())) {
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
		final GwtCommand command = new GwtCommand(PrintGetTemplateRequest.COMMAND);
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command, new AbstractCommandCallback<PrintGetTemplateResponse>() {

			public void execute(PrintGetTemplateResponse response) {
				stopProgress();
				UrlBuilder url = new UrlBuilder(GWT.getHostPageBaseURL());
				url.addPath(URL_PATH);
				url.addParameter(URL_DOCUMENT_ID, response.getDocumentId());
				url.addParameter(URL_NAME, (String) fileNameItem.getValue());
				url.addParameter(URL_TOKEN, command.getUserToken());
				if (SAVE.equals(downloadTypeGroup.getValue())) {
					url.addParameter(URL_DOWNLOAD, URL_DOWNLOAD_YES);
					String encodedUrl = url.toString();
					// create a hidden iframe to avoid popups ???
					HTMLPanel hiddenFrame = new HTMLPanel("<iframe src='" + encodedUrl
							+ "'+style='position:absolute;width:0;height:0;border:0'>");
					hiddenFrame.setVisible(false);
					addChild(hiddenFrame);
				} else {
					url.addParameter(URL_DOWNLOAD, URL_DOWNLOAD_NO);
					String encodedUrl = url.toString();
					com.google.gwt.user.client.Window.open(encodedUrl, "_blank", null);
				}
			}
		});
	}

}
