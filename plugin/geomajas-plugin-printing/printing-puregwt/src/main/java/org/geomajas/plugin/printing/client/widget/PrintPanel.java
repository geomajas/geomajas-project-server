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
package org.geomajas.plugin.printing.client.widget;

import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.plugin.printing.client.PrintingMessages;
import org.geomajas.plugin.printing.client.template.DefaultTemplateBuilder;
import org.geomajas.plugin.printing.client.template.PageSize;
import org.geomajas.plugin.printing.client.util.PrintingLayout;
import org.geomajas.plugin.printing.client.util.UrlBuilder;
import org.geomajas.plugin.printing.command.dto.PrintGetTemplateRequest;
import org.geomajas.plugin.printing.command.dto.PrintGetTemplateResponse;
import org.geomajas.plugin.printing.command.dto.PrintTemplateInfo;
import org.geomajas.puregwt.client.map.MapPresenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
//TODO: import org.geomajas.plugin.rasterizing.client.image.ImageUrlService;
//TODO: import org.geomajas.plugin.rasterizing.client.image.ImageUrlServiceImpl;


/**
 * Widget for choosing print preferences and printing.
 * 
 * @author An Buyle
 * @author Jan De Moerloose
 */
public class PrintPanel  extends Composite {

	private static final PrintingMessages MESSAGES = GWT.create(PrintingMessages.class);
	
	private static final String SAVE = "save";
	private static final String OPEN = "open";

//	private static final String EXTENSION = ".pdf";
	private static final String URL_PATH = "d/printing";
	private static final String URL_DOCUMENT_ID = "documentId";
	private static final String URL_NAME = "name";
	private static final String URL_TOKEN = "userToken";
	private static final String URL_DOWNLOAD = "download";
	private static final String URL_DOWNLOAD_YES = "1";
	private static final String URL_DOWNLOAD_NO = "0";
	
	private PrintableMapBuilder mapBuilder = new PrintableMapBuilder();
	
	/**
	 * UI binder definition for the {@link } widget.
	 * 
	 * @author An Buyle
	 */
	interface PrintPanelUIBinder extends UiBinder<Widget, PrintPanel> {
	}

	private static final PrintPanelUIBinder UI_BINDER = GWT
			.create(PrintPanelUIBinder.class);
	

	@UiField
	protected Button printButton;
	
	@UiField
	protected TextBox titleTextBox;
//
//	private TextItem fileNameItem;
//
//	private SelectItem sizeItem;
//
//	private RadioGroupItem orientationGroup;
	@UiField
	protected RadioButton optionLandscapeOrientation;
	@UiField
	protected RadioButton optionPortraitOrientation;
//
//	private SliderItem rasterDpiSlider;
//
//	private CheckboxItem arrowCheckbox;
//
//	private CheckboxItem scaleBarCheckbox;
//
//	private RadioGroupItem downloadTypeGroup;
//
//	private FormItemIcon barIcon;
//
	private MapPresenter mapPresenter;
	private String applicationId;
	

	//TODO: gwt : private ImageUrlService imageUrlService = new ImageUrlServiceImpl();


	public PrintPanel(MapPresenter mapPresenter, String applicationId) {
		assert (mapPresenter != null) : "mapPresenter must be specified when constructing PrintPanel";
		assert (applicationId != null) : "applicationId must be specified when constructing PrintPanel";
		
		initWidget(UI_BINDER.createAndBindUi(this));
		
		this.mapPresenter = mapPresenter;
		this.applicationId = applicationId; 
		printButton.setEnabled(true);

//		// tab set
//		TabSet tabs = new TabSet();
//		tabs.setWidth(PrintingLayout.printPreferencesWidth);
//		tabs.setHeight(PrintingLayout.printPreferencesHeight);
//
//		// create the one and only tab pane
//		Tab mainPreferences = new Tab();
//		mainPreferences.setTitle(MESSAGES.printPrefsChoose());
//
//		// create the form
//		DynamicForm form = new DynamicForm();
//		// title
//		titleItem = new TextItem();
//		titleItem.setName(TITLE);
//		titleItem.setTitle(MESSAGES.printPrefsTitleText());
		
		titleTextBox.setTitle(MESSAGES.printPrefsTitleTooltip());
		titleTextBox.getElement().setAttribute("placeholder", MESSAGES.printPrefsTitlePlaceholder());
//		// size
//		sizeItem = new SelectItem();
//		sizeItem.setName(SIZE);
//		sizeItem.setTitle(MESSAGES.printPrefsSize());
//		sizeItem.setValueMap(PageSize.getAllNames());
//		sizeItem.setValue(PageSize.A4.getName());
//		// orientation
//		orientationGroup = new RadioGroupItem();
//		orientationGroup.setName(ORIENTATION);
//		orientationGroup.setTitle(MESSAGES.printPrefsOrientation());
//		LinkedHashMap<String, String> orientations = new LinkedHashMap<String, String>();
//		orientations.put(LANDSCAPE, MESSAGES.printPrefsLandscape());
//		orientations.put(PORTRAIT, MESSAGES.printPrefsPortrait());
//		orientationGroup.setValueMap(orientations);
//		orientationGroup.setVertical(false);
//		orientationGroup.setValue(LANDSCAPE);
		final ClickHandler orientationOptionClickedHandler = new ClickHandler() {

			public void onClick(ClickEvent event) {
				if (event != null) {
					optionLandscapeOrientation.setValue(event.getSource()
							.equals(optionLandscapeOrientation));
					optionPortraitOrientation.setValue(event.getSource()
							.equals(optionPortraitOrientation));
				}
			}
		};
	
		optionLandscapeOrientation.addClickHandler(orientationOptionClickedHandler);
		optionPortraitOrientation.addClickHandler(orientationOptionClickedHandler);  
		
		// Defayult = Landscape
		optionLandscapeOrientation.setValue(true);
		optionPortraitOrientation.setValue(false);
		
//		// raster dpi slider
//		rasterDpiSlider = new SliderItem();
//		rasterDpiSlider.setTitle(MESSAGES.printPrefsRasterDPI());
//		rasterDpiSlider.setWidth(PrintingLayout.printPreferencesResolutionWidth);
//		rasterDpiSlider.setHeight(PrintingLayout.printPreferencesResolutionHeight);
//		rasterDpiSlider.setMinValue(72);
//		rasterDpiSlider.setMaxValue(600);
//		rasterDpiSlider.setNumValues(5);
//		// north arrow
//		arrowCheckbox = new CheckboxItem();
//		arrowCheckbox.setValue(true);
//		arrowCheckbox.setTitle(MESSAGES.printPrefsWithArrow());
//		// scale bar
//		scaleBarCheckbox = new CheckboxItem();
//		scaleBarCheckbox.setValue(true);
//		scaleBarCheckbox.setTitle(MESSAGES.printPrefsWithScaleBar());
//		// filename
//		fileNameItem = new TextItem();
//		fileNameItem.setName(FILENAME);
//		fileNameItem.setTitle(MESSAGES.printPrefsFileName());
//		fileNameItem.setValue(mapWidget.getMapModel().getMapInfo().getId() + EXTENSION);
//
//		// progress indicator
//		barIcon = new FormItemIcon();
//		barIcon.setHeight(PrintingLayout.iconWaitHeight);
//		barIcon.setWidth(PrintingLayout.iconWaitWidth);
//		StaticTextItem statusText = new StaticTextItem(MESSAGES.printPrefsStatus());
//		statusText.setIcons(barIcon);
//		barIcon.setSrc(PrintingLayout.iconWaitBlank);
//		// download type
//		downloadTypeGroup = new RadioGroupItem();
//		downloadTypeGroup.setName(DOWNLOAD_TYPE);
//		downloadTypeGroup.setTitle(MESSAGES.printPrefsDownloadType());
//		LinkedHashMap<String, String> types = new LinkedHashMap<String, String>();
//		types.put(SAVE, MESSAGES.printPrefsSaveAsFile());
//		types.put(OPEN, MESSAGES.printPrefsOpenInBrowserWindow());
//		downloadTypeGroup.setValueMap(types);
//		downloadTypeGroup.setVertical(false);
//		downloadTypeGroup.setValue(SAVE);
//
//		form.setFields(titleItem, sizeItem, orientationGroup, arrowCheckbox, scaleBarCheckbox, rasterDpiSlider,
//				fileNameItem, downloadTypeGroup, statusText);
//		mainPreferences.setPane(form);
//		tabs.setTabs(mainPreferences);
//
//		IButton printButton = new IButton();
//		printButton.setTitle(MESSAGES.printPrefsPrint());
		
//
//		VLayout vLayout = new VLayout();
//		vLayout.setMembersMargin(WidgetLayout.marginLarge);
//		vLayout.addMember(tabs);
//		vLayout.addMember(printButton);
//		addChild(vLayout);
		
		
		
	}
	
	@UiHandler("printButton")
	public void onClick(ClickEvent event) {
		print();
	}
	

//TODO convert to pureGWT:
//	private void stopProgress() {
//		barIcon.setSrc(PrintingLayout.iconWaitBlank);
//		redraw();
//	}

//TODO convert to pureGWT:
//	private void startProgress() {
//		barIcon.setSrc(PrintingLayout.iconWaitMoving);
//		redraw();
//	}

	protected void print() {
		if (this.mapPresenter == null) {
			return; // ABORT
		}
		
		//		startProgress();
		PrintGetTemplateRequest request = new PrintGetTemplateRequest();
		mapBuilder.build(mapPresenter);
		//TODO GWT: imageUrlService.makeRasterizable(mapWidget); //Only needed for vector layers
		DefaultTemplateBuilder builder = new DefaultTemplateBuilder();

		builder.setApplicationId(this.applicationId);
		builder.setMapPresenter(mapPresenter);
		builder.setMarginX((int) PrintingLayout.templateMarginX);
		builder.setMarginY((int) PrintingLayout.templateMarginY);
		PageSize size = PageSize.A4;
				// TODO: PageSize.getByName((String) sizeItem.getValue());
		
		
		if (optionLandscapeOrientation.getValue()) {
			builder.setPageHeight(size.getWidth());
			builder.setPageWidth(size.getHeight());
		} else {
			builder.setPageHeight(size.getHeight());
			builder.setPageWidth(size.getWidth());
		}
		String title = titleTextBox.getText().trim();
		if (title.length() == 0) {
			title = MESSAGES.defaultPrintTitle();
		}
		builder.setTitleText(title);
		//TODO: builder.setWithArrow((Boolean) arrowCheckbox.getValue());
		builder.setWithArrow(true);
		//TODO: builder.setWithScaleBar((Boolean) scaleBarCheckbox.getValue());
		builder.setWithScaleBar(true);
		//builder.setRasterDpi((Integer) rasterDpiSlider.getValue());
		builder.setRasterDpi(72);
		PrintTemplateInfo template = builder.buildTemplate();
		request.setTemplate(template);
		final GwtCommand command = new GwtCommand(PrintGetTemplateRequest.COMMAND);
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command, new AbstractCommandCallback<PrintGetTemplateResponse>() {

			public void execute(PrintGetTemplateResponse response) {
				//TODO: stopProgress();
				UrlBuilder url = new UrlBuilder(GWT.getHostPageBaseURL());
				url.addPath(URL_PATH);
				url.addParameter(URL_DOCUMENT_ID, response.getDocumentId());
				//url.addParameter(URL_NAME, (String) fileNameItem.getValue());
				url.addParameter(URL_NAME, "mapPrinting.pdf");
				url.addParameter(URL_TOKEN, command.getUserToken());
				//TODO String downloadType = downloadTypeGroup.getValue()
				String downloadType = OPEN;
				if (SAVE.equals(downloadType)) {
					url.addParameter(URL_DOWNLOAD, URL_DOWNLOAD_YES);
					// TODO Converted to pureGWT 
//					String encodedUrl = url.toString();
//					// create a hidden iframe to avoid popups ???
// 					HTMLPanel hiddenFrame = new HTMLPanel("<iframe src='" + encodedUrl
//							+ "'+style='position:absolute;width:0;height:0;border:0'>");
//					hiddenFrame.setVisible(false);
//					
//					addChild(hiddenFrame);
				} else {
					url.addParameter(URL_DOWNLOAD, URL_DOWNLOAD_NO);
					String encodedUrl = url.toString();
					com.google.gwt.user.client.Window.open(encodedUrl, "_blank", null);
				}
			}
		});
	}
	
	public PrintableMapBuilder getMapBuilder() {
		return mapBuilder;
	}
	
	

}
