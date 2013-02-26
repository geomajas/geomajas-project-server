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
package org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.steps;

import java.util.Map;

import org.geomajas.geometry.Bbox;
import org.geomajas.gwt.client.util.Notify;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.NewLayerModelWizardWindow;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.Wizard;
import org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.WizardStepPanel;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DiscoveryCommService;
import org.geomajas.plugin.deskmanager.command.manager.dto.RasterCapabilitiesInfo;
import org.geomajas.plugin.deskmanager.command.manager.dto.DynamicRasterLayerConfiguration;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.ImageStyle;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;

/**
 * 
 * @author Jan De Moerloose
 *
 */
public class WmsPreviewLayerStep extends WizardStepPanel {

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);

	private Map<String, String> connectionProps;

	private DynamicRasterLayerConfiguration layerConfiguration;
	
	private RasterCapabilitiesInfo info;

	private boolean loading;

	private Label warnings;

	private Img img;

	public WmsPreviewLayerStep(Wizard parent) {
		super(NewLayerModelWizardWindow.STEP_WMS_PREVIEW_LAYER, MESSAGES.wmsPreviewLayerStepNumbering() + " " +
				MESSAGES.wmsPreviewLayerStepTitle(), false, parent);
		setWindowTitle(MESSAGES.wmsPreviewLayerStepTitle());
		img = new Img();
		img.setWidth(300);
		img.setHeight(200);
		img.setImageType(ImageStyle.CENTER);
		img.setLayoutAlign(VerticalAlignment.CENTER);
		addMember(img);
		warnings = new Label();
		warnings.setWidth100();
		warnings.setAutoHeight();
		warnings.setPadding(3);
		warnings.setOverflow(Overflow.VISIBLE);
		warnings.setVisible(false);
		warnings.setBackgroundColor("#FFCCCC");
		addMember(warnings);

	}

	@Override
	public String getNextStep() {
		return NewLayerModelWizardWindow.STEP_EDIT_LAYER_SETTINGS;
	}

	@Override
	public String getPreviousStep() {
		return NewLayerModelWizardWindow.STEP_WMS_CHOOSE_LAYER;
	}

	@Override
	public void reset() {
	}

	@Override
	public void initialize() {
		reset();
		if (connectionProps != null && info != null) {
			Bbox extent = info.getExtent();
			double w2h = extent.getWidth() / extent.getHeight();
			int width = img.getWidth();
			int height = img.getHeight();
			if (w2h * height < width) {
				img.setSrc(info.getPreviewUrl() + "&height=" + height + "&width=" + (int) (w2h * height));
			} else {
				img.setSrc(info.getPreviewUrl() + "&width=" + width + "&height=" + (int) (width / w2h));
			}
			loading = true;
			DiscoveryCommService.getRasterLayerConfiguration(connectionProps, info,
					new DataCallback<DynamicRasterLayerConfiguration>() {

						public void execute(DynamicRasterLayerConfiguration result) {
							loading = false;
							layerConfiguration = result;
							fireChangedEvent();
						}
					}, new DataCallback<String>() {

						public void execute(String result) {
							reset();
							warnings.setVisible(true);
							warnings.setContents("<b><i>" + result + "</i></b>");
							loading = false;
							fireChangedEvent();
						}
					});
		}
	}

	@Override
	public void stepFinished() {
		EditLayerSettingsStep nextStep = (EditLayerSettingsStep) parent
				.getStep(NewLayerModelWizardWindow.STEP_EDIT_LAYER_SETTINGS);
		if (nextStep != null) {
			nextStep.setData(layerConfiguration, NewLayerModelWizardWindow.STEP_WMS_PREVIEW_LAYER);
		} else {
			Notify.error(MESSAGES.wmsPreviewLayerStepNextStepNotFound());
		}
	}

	@Override
	public boolean isValid() {
		return !loading & layerConfiguration != null;
	}

	public void setData(Map<String, String> connectionProps, RasterCapabilitiesInfo info) {
		this.connectionProps = connectionProps;
		this.info = info;
	}

}
