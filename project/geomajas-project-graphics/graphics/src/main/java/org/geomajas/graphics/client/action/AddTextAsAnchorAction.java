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
package org.geomajas.graphics.client.action;

import org.geomajas.graphics.client.controller.CreateTextController;
import org.geomajas.graphics.client.object.GText;
import org.geomajas.graphics.client.object.GraphicsObject;
import org.geomajas.graphics.client.object.Resizable;
import org.geomajas.graphics.client.object.anchor.Anchorable;
import org.geomajas.graphics.client.object.anchor.AnchoredToResizable;
import org.geomajas.graphics.client.service.GraphicsService;

/**
 * Action to add a {@link GText} object anchored to an existing {@link GraphicsObject}.
 * 
 * @author Jan Venstermans
 * 
 */
public class AddTextAsAnchorAction implements Action {

	private GraphicsService service;
	
	private GraphicsObject object;
	
	private String iconUrl;

	public boolean supports(GraphicsObject object) {
		return object.hasRole(Anchorable.TYPE) && object instanceof Resizable;
	}

	public void execute(GraphicsObject object) {
		this.object = object;
		//deactivate all controllers
		service.getMetaController().setActive(false);
		
		//create and activate a custom textcretor controller
		CreateAnchoredOnTextController textCreator = new CreateAnchoredOnTextController(service);
		textCreator.setActive(true);
	}

	@Override
	public void setService(GraphicsService service) {
		this.service = service;
	}

	@Override
	public String getLabel() {
		return "anchor text";
	}
	
	@Override
	public void setIconUrl(String url) {
		this.iconUrl = url;
	}

	@Override
	public String getIconUrl() {
		return iconUrl;
	}
	
	protected void anchorTextOnObject(GText result){
		
	}
	
	/**
	 * This inner class creates a {@link GText} object, with role {@link AnchoredTo} the master
	 * {@link GraphicsObject}.
	 * 
	 * @author Jan Venstermans
	 * 
	 */
	public class CreateAnchoredOnTextController extends CreateTextController {

		public CreateAnchoredOnTextController(GraphicsService graphicsService) {
			super(graphicsService);
		}
		
		@Override
		protected GText createText(String text) {
			setActive(false);
			GText result = super.createText(text);
			result.addRole(new AnchoredToResizable((Resizable) object));
			return result;
		}
	}

}
