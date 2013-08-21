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
import org.geomajas.gwt.client.map.MapPresenter;
import org.geomajas.plugin.printing.client.PrintingMessages;
import org.geomajas.plugin.printing.client.template.DefaultTemplateBuilder;
import org.geomajas.plugin.printing.client.template.PageSize;
import org.geomajas.plugin.printing.client.template.PrintableLayerBuilder;
import org.geomajas.plugin.printing.client.template.PrintableMapBuilder;
import org.geomajas.plugin.printing.client.util.PrintingLayout;
import org.geomajas.plugin.printing.client.util.UrlBuilder;
import org.geomajas.plugin.printing.command.dto.PrintGetTemplateRequest;
import org.geomajas.plugin.printing.command.dto.PrintGetTemplateResponse;
import org.geomajas.plugin.printing.command.dto.PrintTemplateInfo;

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

/**
 * Widget for choosing print preferences and printing.
 * 
 * @author An Buyle
 * @author Jan De Moerloose
 */
public class PrintPanel extends Composite {

	private static final PrintingMessages MESSAGES = GWT.create(PrintingMessages.class);

	private static final String SAVE = "save";

	private static final String OPEN = "open";

	// private static final String EXTENSION = ".pdf";
	private static final String URL_PATH = "d/printing";

	private static final String URL_DOCUMENT_ID = "documentId";

	private static final String URL_NAME = "name";

	private static final String URL_TOKEN = "userToken";

	private static final String URL_DOWNLOAD = "download";

	private static final String URL_DOWNLOAD_YES = "1";

	private static final String URL_DOWNLOAD_NO = "0";

	/**
	 * UI binder definition for the {@link } widget.
	 * 
	 * @author An Buyle
	 */
	interface PrintPanelUIBinder extends UiBinder<Widget, PrintPanel> {
	}

	private static final PrintPanelUIBinder UI_BINDER = GWT.create(PrintPanelUIBinder.class);

	@UiField
	protected Button printButton;

	@UiField
	protected TextBox titleTextBox;

	//
	// private TextItem fileNameItem;
	//
	// private SelectItem sizeItem;
	//
	// private RadioGroupItem orientationGroup;
	@UiField
	protected RadioButton optionLandscapeOrientation;

	@UiField
	protected RadioButton optionPortraitOrientation;

	private MapPresenter mapPresenter;

	private String applicationId;

	private PrintableMapBuilder mapBuilder = new PrintableMapBuilder();

	public void registerLayerBuilder(PrintableLayerBuilder layerBuilder) {
		mapBuilder.registerLayerBuilder(layerBuilder);
	}

	public PrintPanel(MapPresenter mapPresenter, String applicationId) {
		assert (mapPresenter != null) : "mapPresenter must be specified when constructing PrintPanel";
		assert (applicationId != null) : "applicationId must be specified when constructing PrintPanel";

		initWidget(UI_BINDER.createAndBindUi(this));

		this.mapPresenter = mapPresenter;
		this.applicationId = applicationId;
		printButton.setEnabled(true);

		titleTextBox.setTitle(MESSAGES.printPrefsTitleTooltip());
		titleTextBox.getElement().setAttribute("placeholder", MESSAGES.printPrefsTitlePlaceholder());

		final ClickHandler orientationOptionClickedHandler = new ClickHandler() {

			public void onClick(ClickEvent event) {
				if (event != null) {
					optionLandscapeOrientation.setValue(event.getSource().equals(optionLandscapeOrientation));
					optionPortraitOrientation.setValue(event.getSource().equals(optionPortraitOrientation));
				}
			}
		};

		optionLandscapeOrientation.addClickHandler(orientationOptionClickedHandler);
		optionPortraitOrientation.addClickHandler(orientationOptionClickedHandler);

		// Defayult = Landscape
		optionLandscapeOrientation.setValue(true);
		optionPortraitOrientation.setValue(false);
	}

	@UiHandler("printButton")
	public void onClick(ClickEvent event) {
		print();
	}

	public PrintableMapBuilder getMapBuilder() {
		return mapBuilder;
	}

	public void print() {
		if (this.mapPresenter == null) {
			return; // ABORT
		}
		PrintGetTemplateRequest request = new PrintGetTemplateRequest();

		DefaultTemplateBuilder builder = new DefaultTemplateBuilder(mapBuilder);

		builder.setApplicationId(this.applicationId);
		builder.setMapPresenter(mapPresenter);
		builder.setMarginX((int) PrintingLayout.templateMarginX);
		builder.setMarginY((int) PrintingLayout.templateMarginY);
		PageSize size = PageSize.A4;

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
		// TODO: builder.setWithArrow((Boolean) arrowCheckbox.getValue());
		builder.setWithArrow(true);
		// TODO: builder.setWithScaleBar((Boolean) scaleBarCheckbox.getValue());
		builder.setWithScaleBar(true);
		// builder.setRasterDpi((Integer) rasterDpiSlider.getValue());
		builder.setRasterDpi(200);
		PrintTemplateInfo template = builder.buildTemplate();
		request.setTemplate(template);
		final GwtCommand command = new GwtCommand(PrintGetTemplateRequest.COMMAND);
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command, new AbstractCommandCallback<PrintGetTemplateResponse>() {

			public void execute(PrintGetTemplateResponse response) {
				UrlBuilder url = new UrlBuilder(GWT.getHostPageBaseURL());
				url.addPath(URL_PATH);
				url.addParameter(URL_DOCUMENT_ID, response.getDocumentId());
				// url.addParameter(URL_NAME, (String) fileNameItem.getValue());
				url.addParameter(URL_NAME, "mapPrinting.pdf");
				url.addParameter(URL_TOKEN, command.getUserToken());
				// TODO String downloadType = downloadTypeGroup.getValue()
				String downloadType = OPEN;
				if (SAVE.equals(downloadType)) {
					url.addParameter(URL_DOWNLOAD, URL_DOWNLOAD_YES);
					// TODO Converted to pureGWT
					// String encodedUrl = url.toString();
					// // create a hidden iframe to avoid popups ???
					// HTMLPanel hiddenFrame = new HTMLPanel("<iframe src='" + encodedUrl
					// + "'+style='position:absolute;width:0;height:0;border:0'>");
					// hiddenFrame.setVisible(false);
					//
					// addChild(hiddenFrame);
				} else {
					url.addParameter(URL_DOWNLOAD, URL_DOWNLOAD_NO);
					String encodedUrl = url.toString();
					com.google.gwt.user.client.Window.open(encodedUrl, "_blank", null);
				}
			}
		});
	}

}
