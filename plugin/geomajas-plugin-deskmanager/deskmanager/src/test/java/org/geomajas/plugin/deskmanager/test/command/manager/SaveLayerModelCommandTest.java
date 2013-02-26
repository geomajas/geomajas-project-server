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
package org.geomajas.plugin.deskmanager.test.command.manager;

import junit.framework.Assert;

import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.CommandResponse;
import org.geomajas.configuration.client.ClientWidgetInfo;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetLayerModelRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetLayerModelsRequest;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetLayerModelsResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.LayerModelResponse;
import org.geomajas.plugin.deskmanager.command.manager.dto.SaveLayerModelRequest;
import org.geomajas.plugin.deskmanager.command.security.dto.RetrieveRolesRequest;
import org.geomajas.plugin.deskmanager.domain.dto.LayerModelDto;
import org.geomajas.plugin.deskmanager.security.DeskmanagerSecurityService;
import org.geomajas.plugin.deskmanager.security.ProfileService;
import org.geomajas.plugin.deskmanager.test.general.MyClientWidgetInfo;
import org.geomajas.security.GeomajasSecurityException;
import org.geomajas.security.SecurityManager;
import org.geomajas.security.SecurityService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Oliver May
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/deskmanager/spring/**/*.xml", "/applicationContext.xml" })
@Transactional
public class SaveLayerModelCommandTest {

	@Autowired
	private SecurityService securityService;

	@Autowired
	private ProfileService profileService;

	@Autowired
	private SecurityManager securityManager;

	@Autowired
	private CommandDispatcher dispatcher;

	private String userToken;

	private String guestToken;

	@Before
	public void setup() throws Exception {
		// First profile in list is admin
		userToken = ((DeskmanagerSecurityService) securityService).registerRole(
				RetrieveRolesRequest.MANAGER_ID, profileService.getProfiles().get(0));
		guestToken = ((DeskmanagerSecurityService) securityService).registerRole(
				RetrieveRolesRequest.MANAGER_ID, DeskmanagerSecurityService.createGuestProfile());

		// Log in
		securityManager.createSecurityContext(userToken);
	}

	/**
	 * Test non dynamic layers.
	 */
	@Test
	public void testSaveSimpleSettings() {
		GetLayerModelsResponse glmsresponse = (GetLayerModelsResponse) dispatcher.execute(GetLayerModelsRequest.COMMAND,
				new GetLayerModelsRequest(), userToken, "en");
		Assert.assertTrue(glmsresponse.getExceptions().isEmpty());
		
		GetLayerModelRequest glmrequest = new GetLayerModelRequest();
		glmrequest.setId(glmsresponse.getLayerModels().get(0).getId());
		LayerModelResponse glmresponse = (LayerModelResponse) dispatcher.execute(GetLayerModelRequest.COMMAND,
				glmrequest, userToken, "en");
		Assert.assertTrue(glmresponse.getExceptions().isEmpty());

		LayerModelDto layerModel = glmresponse.getLayerModel();

		
		ClientWidgetInfo originalCwi = layerModel.getWidgetInfo().get("TEST");
		boolean originalActive = layerModel.isActive();
		boolean originalDefaultVisible = layerModel.isDefaultVisible();
		boolean originalPublic = layerModel.isPublic();
		String originalName = layerModel.getName();
		
		
		layerModel.getWidgetInfo().put("TEST", new MyClientWidgetInfo("CWI1"));
		layerModel.setActive(!originalActive);
		layerModel.setDefaultVisible(!originalDefaultVisible);
		layerModel.setPublic(!originalPublic);
		layerModel.setName(originalName + "_NamE_");
		
		//Save the layermodel
		SaveLayerModelRequest request = new SaveLayerModelRequest();
		request.setLayerModel(layerModel);
		request.setSaveBitmask(SaveLayerModelRequest.SAVE_SETTINGS);
		dispatcher.execute(SaveLayerModelRequest.COMMAND, request, userToken, "en");
		
		//Load the layermodel again
		glmrequest.setId(glmsresponse.getLayerModels().get(0).getId());
		glmresponse = (LayerModelResponse) dispatcher.execute(GetLayerModelRequest.COMMAND,
				glmrequest, userToken, "en");
		layerModel = glmresponse.getLayerModel();
		
		Assert.assertEquals(originalCwi, layerModel.getWidgetInfo().get("TEST"));
		Assert.assertEquals(!originalActive, layerModel.isActive());
		Assert.assertEquals(!originalDefaultVisible, layerModel.isDefaultVisible());
		Assert.assertEquals(!originalPublic, layerModel.isPublic());
		Assert.assertEquals(originalName + "_NamE_", layerModel.getName());
	}

	@Test
	public void testSaveWidgets() {
		GetLayerModelsResponse glmsresponse = (GetLayerModelsResponse) dispatcher.execute(GetLayerModelsRequest.COMMAND,
				new GetLayerModelsRequest(), userToken, "en");
		Assert.assertTrue(glmsresponse.getExceptions().isEmpty());
		
		GetLayerModelRequest glmrequest = new GetLayerModelRequest();
		glmrequest.setId(glmsresponse.getLayerModels().get(0).getId());
		LayerModelResponse glmresponse = (LayerModelResponse) dispatcher.execute(GetLayerModelRequest.COMMAND,
				glmrequest, userToken, "en");
		Assert.assertTrue(glmresponse.getExceptions().isEmpty());

		LayerModelDto layerModel = glmresponse.getLayerModel();

		
		Assert.assertFalse(layerModel.getWidgetInfo().containsKey("TEST"));
		
		//These are all the settings that can be changed in the frontend.
		layerModel.getWidgetInfo().put("TEST", new MyClientWidgetInfo("CWI1"));
		
		//Save the layermodel
		SaveLayerModelRequest request = new SaveLayerModelRequest();
		request.setLayerModel(layerModel);
		request.setSaveBitmask(SaveLayerModelRequest.SAVE_CLIENTWIDGETINFO);
		dispatcher.execute(SaveLayerModelRequest.COMMAND, request, userToken, "en");
		
		//Load the layermodel again
		glmrequest.setId(glmsresponse.getLayerModels().get(0).getId());
		glmresponse = (LayerModelResponse) dispatcher.execute(GetLayerModelRequest.COMMAND,
				glmrequest, userToken, "en");
		layerModel = glmresponse.getLayerModel();
		
		//These are all the settings that can be changed in the frontend.
		Assert.assertEquals(layerModel.getWidgetInfo().get("TEST"), new MyClientWidgetInfo("CWI1"));
	}
	
	@Test
	public void testSaveEverything() {
		GetLayerModelsResponse glmsresponse = (GetLayerModelsResponse) dispatcher.execute(GetLayerModelsRequest.COMMAND,
				new GetLayerModelsRequest(), userToken, "en");
		Assert.assertTrue(glmsresponse.getExceptions().isEmpty());
		
		GetLayerModelRequest glmrequest = new GetLayerModelRequest();
		glmrequest.setId(glmsresponse.getLayerModels().get(0).getId());
		LayerModelResponse glmresponse = (LayerModelResponse) dispatcher.execute(GetLayerModelRequest.COMMAND,
				glmrequest, userToken, "en");
		Assert.assertTrue(glmresponse.getExceptions().isEmpty());

		LayerModelDto layerModel = glmresponse.getLayerModel();

		
		boolean originalActive = layerModel.isActive();
		boolean originalDefaultVisible = layerModel.isDefaultVisible();
		boolean originalPublic = layerModel.isPublic();
		String originalName = layerModel.getName();
		
		
		layerModel.getWidgetInfo().put("TEST", new MyClientWidgetInfo("CWI1"));
		layerModel.setActive(!originalActive);
		layerModel.setDefaultVisible(!originalDefaultVisible);
		layerModel.setPublic(!originalPublic);
		layerModel.setName(originalName + "_NamE_");
		
		//Save the layermodel
		SaveLayerModelRequest request = new SaveLayerModelRequest();
		request.setLayerModel(layerModel);
		request.setSaveBitmask(SaveLayerModelRequest.SAVE_CLIENTWIDGETINFO + SaveLayerModelRequest.SAVE_SETTINGS);
		dispatcher.execute(SaveLayerModelRequest.COMMAND, request, userToken, "en");
		
		//Load the layermodel again
		glmrequest.setId(glmsresponse.getLayerModels().get(0).getId());
		glmresponse = (LayerModelResponse) dispatcher.execute(GetLayerModelRequest.COMMAND,
				glmrequest, userToken, "en");
		layerModel = glmresponse.getLayerModel();
		
		Assert.assertEquals(new MyClientWidgetInfo("CWI1"), layerModel.getWidgetInfo().get("TEST"));
		Assert.assertEquals(!originalActive, layerModel.isActive());
		Assert.assertEquals(!originalDefaultVisible, layerModel.isDefaultVisible());
		Assert.assertEquals(!originalPublic, layerModel.isPublic());
		Assert.assertEquals(originalName + "_NamE_", layerModel.getName());
	}
	
	@Test
	public void testSaveNothing() {

		GetLayerModelsResponse glmsresponse = (GetLayerModelsResponse) dispatcher.execute(GetLayerModelsRequest.COMMAND,
				new GetLayerModelsRequest(), userToken, "en");
		Assert.assertTrue(glmsresponse.getExceptions().isEmpty());
		
		GetLayerModelRequest glmrequest = new GetLayerModelRequest();
		glmrequest.setId(glmsresponse.getLayerModels().get(0).getId());
		LayerModelResponse glmresponse = (LayerModelResponse) dispatcher.execute(GetLayerModelRequest.COMMAND,
				glmrequest, userToken, "en");
		Assert.assertTrue(glmresponse.getExceptions().isEmpty());

		LayerModelDto layerModel = glmresponse.getLayerModel();

		
		ClientWidgetInfo originalCwi = layerModel.getWidgetInfo().get("TEST");
		boolean originalActive = layerModel.isActive();
		boolean originalDefaultVisible = layerModel.isDefaultVisible();
		boolean originalPublic = layerModel.isPublic();
		String originalName = layerModel.getName();
		
		
		layerModel.getWidgetInfo().put("TEST", new MyClientWidgetInfo("CWI1"));
		layerModel.setActive(!originalActive);
		layerModel.setDefaultVisible(!originalDefaultVisible);
		layerModel.setPublic(!originalPublic);
		layerModel.setName(originalName + "_NamE_");
		
		//Save the layermodel
		SaveLayerModelRequest request = new SaveLayerModelRequest();
		request.setLayerModel(layerModel);
		dispatcher.execute(SaveLayerModelRequest.COMMAND, request, userToken, "en");
		
		//Load the layermodel again
		glmrequest.setId(glmsresponse.getLayerModels().get(0).getId());
		glmresponse = (LayerModelResponse) dispatcher.execute(GetLayerModelRequest.COMMAND,
				glmrequest, userToken, "en");
		layerModel = glmresponse.getLayerModel();
		
		Assert.assertEquals(originalCwi, layerModel.getWidgetInfo().get("TEST"));
		Assert.assertEquals(originalActive, layerModel.isActive());
		Assert.assertEquals(originalDefaultVisible, layerModel.isDefaultVisible());
		Assert.assertEquals(originalPublic, layerModel.isPublic());
		Assert.assertEquals(originalName, layerModel.getName());
		
	}

	@Test
	public void testSaveNew() {

	}

	@Test
	public void testNotAllowed() {
		GetLayerModelsResponse glmsresponse = (GetLayerModelsResponse) dispatcher.execute(GetLayerModelsRequest.COMMAND,
				new GetLayerModelsRequest(), userToken, "en");
		Assert.assertTrue(glmsresponse.getExceptions().isEmpty());
		
		GetLayerModelRequest glmrequest = new GetLayerModelRequest();
		glmrequest.setId(glmsresponse.getLayerModels().get(0).getId());
		LayerModelResponse glmresponse = (LayerModelResponse) dispatcher.execute(GetLayerModelRequest.COMMAND,
				glmrequest, userToken, "en");
		Assert.assertTrue(glmresponse.getExceptions().isEmpty());

		
		securityManager.createSecurityContext(guestToken);


		SaveLayerModelRequest request = new SaveLayerModelRequest();
		request.setLayerModel(glmresponse.getLayerModel());

		CommandResponse response = dispatcher.execute(SaveLayerModelRequest.COMMAND, request, guestToken, "en");
		
		Assert.assertFalse(response.getExceptions().isEmpty());
		Assert.assertEquals(response.getExceptions().get(0).getClassName(), GeomajasSecurityException.class.getName());
	}

}
