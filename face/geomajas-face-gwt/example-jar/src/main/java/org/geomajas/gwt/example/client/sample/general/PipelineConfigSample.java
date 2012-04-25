package org.geomajas.gwt.example.client.sample.general;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.command.CommandResponse;
import org.geomajas.command.EmptyCommandRequest;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.gwt.client.widget.LayerTree;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.example.base.SamplePanelFactory;
import org.geomajas.gwt.example.client.sample.i18n.SampleMessages;

/**
 * Example of a layer with customized.
 *
 * @author Joachim Van der Auwera
 */
public class PipelineConfigSample extends SamplePanel {

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

	public static final String TITLE = "PipelineConfig";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new PipelineConfigSample();
		}
	};

	public Canvas getViewPanel() {
		VLayout mainLayout = new VLayout();
		mainLayout.setWidth100();
		mainLayout.setHeight100();

		// Build a map, and set a PanController:
		VLayout mapLayout = new VLayout();
		mapLayout.setShowEdges(true);
		final MapWidget map = new MapWidget("mapPipeline", "gwtExample");
		map.setController(new PanController(map));
		mapLayout.addMember(map);

		// Build the LayerTree:
		final LayerTree layerTree = new LayerTree(map);
		layerTree.setHeight(180);
		layerTree.setWidth100();
		layerTree.setShowEdges(true);
		layerTree.setShowResizeBar(true);
		layerTree.setMinHeight(100);

		// Add both to the main layout:
		mainLayout.addMember(layerTree);
		mainLayout.addMember(mapLayout);

		return mainLayout;
	}

	public String getDescription() {
		return MESSAGES.pipelineConfigDescription();
	}

	public String[] getConfigurationFiles() {
		return new String[] { "classpath:org/geomajas/gwt/example/context/mapPipeline.xml",
				"classpath:org/geomajas/gwt/example/context/layerPipeline.xml",
				"classpath:org/geomajas/gwt/example/server/samples/ProcessFeaturesPipelineStep.java",
				"classpath:org/geomajas/gwt/example/base/layerPopulatedPlaces110m.xml",
				"classpath:org/geomajas/gwt/example/base/layerWmsBluemarble.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
