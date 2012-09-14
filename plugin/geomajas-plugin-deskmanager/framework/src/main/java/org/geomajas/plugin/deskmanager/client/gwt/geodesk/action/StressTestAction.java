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
package org.geomajas.plugin.deskmanager.client.gwt.geodesk.action;

import java.util.List;

import org.geomajas.configuration.client.ScaleInfo;
import org.geomajas.gwt.client.action.ToolbarAction;
import org.geomajas.gwt.client.map.MapView;
import org.geomajas.gwt.client.map.MapView.ZoomOption;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.widget.MapWidget;

import com.google.gwt.user.client.Timer;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SpinnerItem;

/**
 * 
 * @author Oliver May
 *
 */
public class StressTestAction extends ToolbarAction {

	private MapWidget mapWidget;

	private StressTester tester;

	// fps
	private double speed = 2;

	// percentage moved each step
	private double moveRatio = 0.5;

	private boolean selected;

	public StressTestAction(MapWidget mapWidget) {
		super("[ISOMORPHIC]/images/osgeo/stresstest.png", "Stress test!", "Test that stress !!");
		this.setMapWidget(mapWidget);
	}

	public void setMapWidget(MapWidget mapWidget) {
		this.mapWidget = mapWidget;
	}

	public MapWidget getMapWidget() {
		return mapWidget;
	}

	public void onClick(ClickEvent event) {
		if (selected) {
			tester.stop();
		} else {
			tester = new StressTester();
			showModalWindow(tester);
		}
		selected = !selected;
	}

	private void showModalWindow(final StressTester tester) {
		final Window winModal = new Window();
		winModal.setWidth(300);
		winModal.setHeight(150);
		winModal.setTitle("Parameters");
		winModal.setIsModal(true);
		winModal.setShowCloseButton(false);

		DynamicForm form = new DynamicForm();

		final SpinnerItem speedSliderItem = new SpinnerItem();
		speedSliderItem.setTitle("Speed");
		speedSliderItem.setValue(speed);
		speedSliderItem.setMin(0.1);
		speedSliderItem.setMax(10);
		speedSliderItem.setStep(0.5);

		final SpinnerItem progressSliderItem = new SpinnerItem();
		progressSliderItem.setTitle("Progress");
		progressSliderItem.setValue(moveRatio);
		progressSliderItem.setMin(0.001);
		progressSliderItem.setMax(1);
		progressSliderItem.setStep(0.1);

		IButton button = new IButton("Start!");
		button.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				speed = Double.parseDouble(speedSliderItem.getValueAsString());
				moveRatio = Double.parseDouble(progressSliderItem.getValueAsString());
				winModal.destroy();
				tester.run();
			}
		});

		form.setFields(speedSliderItem, progressSliderItem);

		winModal.addItem(form);
		winModal.addItem(button);
		winModal.show();
	}

	/**
	 * 
	 * @author Oliver May
	 *
	 */
	private class StressTester {

		private List<ScaleInfo> scaleInfos;

		private ScaleInfo currentScaleInfo;

		private int scaleInfoIndex;

		private Timer timer;

		private MapView mapView;

		public StressTester() {
			mapView = mapWidget.getMapModel().getMapView();

			scaleInfos = mapWidget.getMapModel().getMapInfo().getScaleConfiguration().getZoomLevels();
			Double scale = mapWidget.getMapModel().getMapView().getCurrentScale();

			while (scale > scaleInfos.get(scaleInfoIndex).getPixelPerUnit()) {
				scaleInfoIndex++;
			}
			currentScaleInfo = scaleInfos.get(scaleInfoIndex);

			// move to top bottom corner
			mapView.setCurrentScale(currentScaleInfo.getPixelPerUnit(), ZoomOption.LEVEL_CLOSEST);
			mapView.translate(-mapView.getMaxBounds().getWidth(), -mapView.getMaxBounds().getHeight());
		}

		public void run() {
			timer = new Timer() {

				@Override
				public void run() {
					tick();
				}
			};
			timer.scheduleRepeating((int) (1000 / speed));
		}

		public void stop() {
			timer.cancel();
		}

		private void tick() {
			MapView mapView = mapWidget.getMapModel().getMapView();
			Bbox maxBounds = mapView.getMaxBounds();

			if (currentScaleInfo == null) {
				stop();
			} else if (mapView.getBounds().getMaxX() >= maxBounds.getMaxX() - 1) {
				if (mapView.getBounds().getMaxY() >= maxBounds.getMaxY() - 1) {
					nextLevel();
				} else {
					returnLeft();
				}
			} else {
				moveRight();
			}

		}

		private void moveRight() {
			mapView.translate(mapView.getBounds().getWidth() * moveRatio, 0);
		}

		private void returnLeft() {
			mapView.translate(-mapView.getMaxBounds().getWidth(), mapView.getBounds().getHeight() * moveRatio);
		}

		private void nextLevel() {
			if (scaleInfoIndex >= scaleInfos.size()) {
				currentScaleInfo = null;
				return;
			}

			currentScaleInfo = scaleInfos.get(scaleInfoIndex++);

			mapView.setCurrentScale(currentScaleInfo.getPixelPerUnit(), ZoomOption.LEVEL_CLOSEST);
			mapView.translate(-mapView.getMaxBounds().getWidth(), -mapView.getMaxBounds().getHeight());
		}

	}

}
