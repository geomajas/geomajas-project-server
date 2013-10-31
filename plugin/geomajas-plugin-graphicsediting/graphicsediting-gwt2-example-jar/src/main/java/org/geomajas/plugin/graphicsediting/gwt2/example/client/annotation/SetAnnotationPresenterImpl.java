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
package org.geomajas.plugin.graphicsediting.gwt2.example.client.annotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.geomajas.graphics.client.action.AddTextAsAnchorAction;
import org.geomajas.graphics.client.action.DeleteAction;
import org.geomajas.graphics.client.action.ToggleLabelAction;
import org.geomajas.graphics.client.controller.AnchorControllerFactory;
import org.geomajas.graphics.client.controller.CreateAnchoredIconController;
import org.geomajas.graphics.client.controller.CreateAnchoredTextController;
import org.geomajas.graphics.client.controller.CreateIconController;
import org.geomajas.graphics.client.controller.CreatePathController;
import org.geomajas.graphics.client.controller.CreateRectangleController;
import org.geomajas.graphics.client.controller.CreateTextController;
import org.geomajas.graphics.client.controller.DeleteControllerFactory;
import org.geomajas.graphics.client.controller.DragControllerFactory;
import org.geomajas.graphics.client.controller.PopupMenuControllerFactory;
import org.geomajas.graphics.client.controller.PopupMenuFactory;
import org.geomajas.graphics.client.controller.ResizeControllerFactory;
import org.geomajas.graphics.client.editor.AnchorStyleEditor;
import org.geomajas.graphics.client.editor.LabelEditor;
import org.geomajas.graphics.client.editor.StrokeFillEditor;
import org.geomajas.graphics.client.event.GraphicsObjectContainerEvent;
import org.geomajas.graphics.client.service.GraphicsController;
import org.geomajas.graphics.client.service.GraphicsService;
import org.geomajas.graphics.client.service.GraphicsServiceImpl;
import org.geomajas.graphics.client.service.MetaControllerFactory;
import org.geomajas.plugin.graphicsediting.gwt2.client.action.EditAction;
import org.geomajas.plugin.graphicsediting.gwt2.client.controller.CreatePolygonController;
import org.geomajas.gwt2.client.event.MapInitializationEvent;
import org.geomajas.gwt2.client.event.MapInitializationHandler;
import org.geomajas.gwt2.client.gfx.GfxUtil;
import org.geomajas.gwt2.client.map.MapPresenter;

import com.google.web.bindery.event.shared.EventBus;

/**
 * SetAnnotationPresenterImpl.
 * 
 * @author Jan De Moerloose
 */
public class SetAnnotationPresenterImpl implements SetAnnotationPresenter, SetAnnotationPresenter.Handler,
		MapInitializationHandler, GraphicsObjectContainerEvent.Handler {

	private GraphicsService graphicsService;

	private EventBus eventBus;

	private View view;

	private MapPresenter mapPresenter;

	private AnnotationContainer annotationContainer;

	private Map<Action, GraphicsController> controllerMap = new HashMap<Action, GraphicsController>();

	private GfxUtil gfxUtil;
	
	public SetAnnotationPresenterImpl(final View view, final EventBus eventBus, final MapPresenter mapPresenter,
			final GfxUtil gfxUtil) {
		this.view = view;
		this.eventBus = eventBus;
		this.mapPresenter = mapPresenter;
		this.gfxUtil = gfxUtil;
		graphicsService = new GraphicsServiceImpl(eventBus, true, false);
		bind(eventBus);
	}

	private void bind(final EventBus eventBus) {
		mapPresenter.getEventBus().addMapInitializationHandler(this);
		view.setHandler(this);
	}

	@Override
	public void onMapInitialized(MapInitializationEvent event) {		
		annotationContainer = new AnnotationContainer(mapPresenter, eventBus);
		annotationContainer.setRootContainer(mapPresenter.addWorldContainer());
		annotationContainer.addGraphicsObjectContainerHandler(this);
		graphicsService.setMetaControllerFactory(new MetaControllerFactory() {

			@Override
			public GraphicsController createController(GraphicsService graphicsService) {
				return new NavigationMetaController(graphicsService, mapPresenter);
			}
		});
		graphicsService.setObjectContainer(annotationContainer);
		PopupMenuControllerFactory popupFactory = new PopupMenuControllerFactory(new PopupMenuFactory(), 1.3, 1.3);
		popupFactory.registerAction(new EditAction(mapPresenter, new GeometryEditorFactoryImpl(gfxUtil)));
		popupFactory.registerAction(new DeleteAction());
		popupFactory.registerEditor(new LabelEditor());
		popupFactory.registerEditor(new StrokeFillEditor());
		popupFactory.registerEditor(new AnchorStyleEditor());
		popupFactory.registerAction(new ToggleLabelAction());
		popupFactory.registerAction(new AddTextAsAnchorAction());
		graphicsService.registerControllerFactory(new ResizeControllerFactory());
		graphicsService.registerControllerFactory(new DragControllerFactory());
		graphicsService.registerControllerFactory(new AnchorControllerFactory());
		graphicsService.registerControllerFactory(new DeleteControllerFactory());
//		graphicsService.registerControllerFactory(new PropertyEditControllerFactory());
		graphicsService.registerControllerFactory(popupFactory);
		controllerMap.put(Action.CREATE_LINE, new CreatePathController(graphicsService, false));
		controllerMap.put(Action.CREATE_POLYGON, new CreatePolygonController(graphicsService,
				new GeometryEditorFactoryImpl(gfxUtil), mapPresenter));
		controllerMap.put(Action.CREATE_RECTANGLE, new CreateRectangleController(graphicsService));
		controllerMap.put(Action.CREATE_TEXT, new CreateTextController(graphicsService));
		controllerMap.put(Action.CREATE_ANCHORED_TEXT, new CreateAnchoredTextController(graphicsService));
		controllerMap.put(Action.CREATE_ICON, new CreateIconController(graphicsService,
				34, 34, new ArrayList<String>()));
		controllerMap.put(Action.CREATE_ANCHORED_ICON,
				new CreateAnchoredIconController(graphicsService, 34, 34, new ArrayList<String>() ));
	}

	@Override
	public void onAction(GraphicsObjectContainerEvent event) {
		for (GraphicsController c : controllerMap.values()) {
			c.setActive(false);
		}
		// deactivate all
		for (Action action : Action.values()) {
			view.setActive(action, false);
		}
		graphicsService.getMetaController().setActive(true);
	}

	@Override
	public void onAction(Action action) {
		graphicsService.start();
		for (Action a : controllerMap.keySet()) {
			if (a != action) {
				controllerMap.get(a).setActive(false);
				view.setActive(a, false);
			}
		}
		GraphicsController c = controllerMap.get(action);
		graphicsService.getMetaController().setActive(c.isActive());
		c.setActive(!c.isActive());
		view.setActive(action, c.isActive());
	}

	@Override
	public View getView() {
		return view;
	}

	@Override
	public void onDuplicateWhenDragging(boolean duplicate) {
		graphicsService.setShowOriginalObjectWhileDragging(duplicate);
	}
}
