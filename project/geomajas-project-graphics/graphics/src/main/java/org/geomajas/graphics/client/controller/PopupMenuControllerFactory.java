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
package org.geomajas.graphics.client.controller;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.graphics.client.action.Action;
import org.geomajas.graphics.client.editor.Editor;
import org.geomajas.graphics.client.object.GraphicsObject;
import org.geomajas.graphics.client.service.GraphicsController;
import org.geomajas.graphics.client.service.GraphicsControllerFactory;
import org.geomajas.graphics.client.service.GraphicsService;
import org.geomajas.graphics.client.util.Interruptible;

/**
 * Factory for the {@link PopupMenuController}.
 * 
 * @author Jan De Moerloose
 * 
 */
public class PopupMenuControllerFactory implements GraphicsControllerFactory {

	private List<Action> actions = new ArrayList<Action>();
	
	// anchor X offset for the popupMenu icon, relative to the left top border of the Bbox of the resizable
	private double offsetX;
	
	// anchor Y offset for the popupMenu icon, relative to the left top border of the Bbox of the resizable
	private double offsetY;
	
	// URL for the PopupMenu icon
	protected String iconUrl;
	
	private PopupMenuFactory popupMenuFactory;

	public PopupMenuControllerFactory(PopupMenuFactory popupMenuFactory) {
		this(popupMenuFactory, 2, 2);
	}
	
	public PopupMenuControllerFactory(PopupMenuFactory popupMenuFactory, double offsetX, double offsetY) {
		this(popupMenuFactory, offsetX, offsetY, null);
	}
	
	public PopupMenuControllerFactory(PopupMenuFactory popupMenuFactory,
			double offsetX, double offsetY, String iconUrl) {
		super();
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.iconUrl = iconUrl;
		this.popupMenuFactory = popupMenuFactory;
	}
	
	@Override
	public boolean supports(GraphicsObject object) {
		return true;
	}

	@Override
	public GraphicsController createController(GraphicsService graphicsService, GraphicsObject object) {
		for (Action action : actions) {
			action.setService(graphicsService);
		}
		PopupMenuController controller = new PopupMenuController(actions, object, graphicsService, offsetX, offsetY,
				iconUrl, popupMenuFactory);
		return controller;
	}

	/**
	 * Register an action with the popup menu. Actions/editors appear top-to-bottom in the order in which they have been
	 * registered.
	 * 
	 * @param action
	 */
	public void registerAction(Action action) {
		actions.add(action);
	}

	/**
	 * Register an editor with the popup menu. Actions/editors appear top-to-bottom in the order in which they have been
	 * registered.
	 * 
	 * @param action
	 */

	public void registerEditor(Editor editor) {
		actions.add(new EditorAction(editor));
	}
	
	public List<Action> getActions() {
		return actions;
	}
	
	/**
	 * Cancels all {@link Interruptible} {@link Action} that are in progress.
	 * 
	 */
	public void cancelActions() {
		for (Interruptible action : getInterruptibleActions()) {
			if (action.isInProgress()) {
				action.cancel();
			}
		}
	}

	/**
	 * Saves and stops all {@link Interruptible} {@link Action} that are in progress.
	 * 
	 */
	public void saveAndStopActions() {
		for (Interruptible action : getInterruptibleActions()) {
			if (action.isInProgress()) {
				action.save();
				action.stop();
			}
		}
	}

	/**
	 * Checks whether it contains at least one {@link Interruptible} {@link Action} that is in progress.
	 * 
	 */
	public boolean isActionInProgress() {
		for (Interruptible action : getInterruptibleActions()) {
			if (action.isInProgress()) {
				return true;
			}
		}
		return false;
	}
	
	private List<Interruptible> getInterruptibleActions() {
		List<Interruptible> interruptibles = new ArrayList<Interruptible>();
		for (Action action : getActions()) {
			if (action instanceof Interruptible) {
				interruptibles.add((Interruptible) action);
			}
		}
		return interruptibles;
	}

	/**
	 * Wraps editor invocation in an {@link Action}.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	public class EditorAction implements Action, Interruptible {

		private Editor editor;

		private PopupMenuEditorDialog box;
		
		private String iconUrl;

		public EditorAction(Editor editor) {
			this.editor = editor;
			box = new PopupMenuEditorDialog(editor);
			box.addHandler(new PopupMenuEditorDialogHandler());
		}

		@Override
		public boolean supports(GraphicsObject object) {
			return editor.supports(object);
		}

		@Override
		public void setService(GraphicsService service) {
			editor.setService(service);
		}

		@Override
		public void execute(GraphicsObject object) {
			editor.setObject(object);
			if (editor instanceof Interruptible) {
				((Interruptible) editor).start();
			}
			box.center();
		}

		@Override
		public String getLabel() {
			return editor.getLabel();
		}

		@Override
		public void setIconUrl(String url) {
			this.iconUrl = url;
		}

		@Override
		public String getIconUrl() {
			return iconUrl;
		}

		@Override
		public void cancel() {
			box.hide();
			if (editor instanceof Interruptible) {
				((Interruptible) editor).cancel();
			}
		}

		@Override
		public void stop() {
			if (editor instanceof Interruptible) {
				box.hide();
				((Interruptible) editor).stop();
			}
		}

		@Override
		public void save() {
			if (editor instanceof Interruptible) {
				((Interruptible) editor).save();
			}
		}

		@Override
		public void pause() {
			if (editor instanceof Interruptible) {
				((Interruptible) editor).pause();
			}
		}

		@Override
		public void resume() {
			if (editor instanceof Interruptible) {
				((Interruptible) editor).resume();
			}
		}

		@Override
		public boolean isInterrupted() {
			if (editor instanceof Interruptible) {
				return ((Interruptible) editor).isInterrupted();
			}
			return false;
		}

		@Override
		public boolean isInProgress() {
			if (editor instanceof Interruptible) {
				return ((Interruptible) editor).isInProgress();
			}
			return false;
		}

		@Override
		public void start() {
			// do nothing
			
		}

		/**
		 * Handler for the {@link PopupMenuEditorDialog}.
		 * 
		 * @author Jan Venstermans
		 * 
		 */
		class PopupMenuEditorDialogHandler {

			public void onOk() {
				if (editor.validate()) {
					editor.onOk();
					box.hide();
				}
			}
			
			public void onApply() {
				if (editor.validate()) {
					editor.onOk();
				}
			}

			public void onUndo() {
				editor.undo();
			}

			public void onCancel() {
				cancel();
			}
		}
	}

	

}
