/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.test.service;

import java.util.Date;

import org.geomajas.configuration.client.ClientApplicationInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientWidgetInfo;
import org.geomajas.plugin.deskmanager.client.gwt.common.GdmLayout;
import org.geomajas.plugin.deskmanager.domain.Blueprint;
import org.geomajas.plugin.deskmanager.domain.ClientLayer;
import org.geomajas.plugin.deskmanager.domain.Geodesk;
import org.geomajas.plugin.deskmanager.domain.security.Territory;
import org.geomajas.plugin.deskmanager.security.DeskmanagerSecurityService;
import org.geomajas.plugin.deskmanager.security.ProfileService;
import org.geomajas.plugin.deskmanager.service.common.BlueprintService;
import org.geomajas.plugin.deskmanager.service.common.GeodeskConfigurationService;
import org.geomajas.plugin.deskmanager.service.common.GeodeskService;
import org.geomajas.plugin.deskmanager.service.common.LayerModelService;
import org.geomajas.plugin.deskmanager.service.common.TerritoryService;
import org.geomajas.plugin.deskmanager.test.SecurityContainingTestBase;
import org.geomajas.plugin.deskmanager.test.TestConst;
import org.geomajas.plugin.deskmanager.test.general.MyClientWidgetInfo;
import org.geomajas.plugin.deskmanager.test.security.StubProfileService;
import org.geomajas.security.SecurityManager;
import org.geomajas.security.SecurityService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Unit tests for the see trough overriding of geodesk/blueprint/layers.
 * 
 * @author Oliver May
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/deskmanager/spring/**/*.xml", "/applicationContext.xml" })
@Transactional
public class GeodeskConfigurationServiceTest extends SecurityContainingTestBase {

	private static final String KWI_KEY = "KWI_KEY";

	private static final String KWI_KEY2 = "KWI_KEY2";
	
	@Autowired
	private GeodeskService geodeskService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private ProfileService profileService;

	@Autowired
	private SecurityManager securityManager;

	@Autowired
	private BlueprintService blueprintService;
	
	@Autowired
	private LayerModelService layerModelService;

	@Autowired
	private GeodeskConfigurationService geodeskConfigurationService;

	@Autowired
	private TerritoryService territoryService;

	@Before
	public void setup() throws Exception {
		// First profile in list is admin
		String token = ((DeskmanagerSecurityService) securityService).registerRole("42",
				((StubProfileService) profileService).getPredefinedProfiles().get(0));
		// Log in
		securityManager.createSecurityContext(token);

		Blueprint blueprint = new Blueprint();
		blueprint.setUserApplicationKey(DeskmanagerExampleDatabaseProvisioningService.CLIENTAPPLICATION_ID);
		blueprint.setName("Blueprint 42");
		blueprint.setCreationDate(new Date());
		blueprint.setCreationBy("JUnit");
		blueprint.setLastEditDate(new Date());
		blueprint.setLastEditBy("JUnit");

		blueprintService.saveOrUpdateBlueprintInternal(blueprint);

		Territory territory = new Territory();
		territory.setName("territory1");
		territory.setCode("TERRITORY_1");
		territory.setCrs("EPSG:3857");
		territoryService.saveOrUpdateTerritory(territory);

		Geodesk geodesk = new Geodesk();
		geodesk.setGeodeskId("42");
		geodesk.setName("Fourty-two");
		geodesk.setBlueprint(blueprint);
		geodesk.setOwner(territory);

		geodeskService.saveOrUpdateGeodesk(geodesk);

	}

	@Test
	public void testLayerClientWidgetConfiguration() throws Exception {
		Geodesk geodesk = geodeskService.getGeodeskByPublicId("42");
		Blueprint blueprint = geodesk.getBlueprint();

		ClientApplicationInfo cai = geodeskConfigurationService.createClonedGeodeskConfiguration(geodesk, true);
		Assert.assertNull(getMainMap(cai).getLayers().get(0).getWidgetInfo(KWI_KEY));

		// Configure cwi on layer
		ClientWidgetInfo layermodelCwi = new MyClientWidgetInfo("layermodel");
		blueprint.getMainMapLayers().get(0).getLayerModel().getWidgetInfo().put(KWI_KEY, layermodelCwi);
		layerModelService.saveOrUpdateLayerModel(blueprint.getMainMapLayers().get(0).getLayerModel());
		cai = geodeskConfigurationService.createClonedGeodeskConfiguration(geodesk, true);
		Assert.assertEquals(getMainMap(cai).getLayers().get(0).getWidgetInfo(KWI_KEY), layermodelCwi);
		
		// Configure cwi on blueprint
		ClientWidgetInfo blueprintCwi = new MyClientWidgetInfo("blueprint");
		geodesk.getBlueprint().getMainMapLayers().get(0).getWidgetInfo().put(KWI_KEY, blueprintCwi);
		blueprintService.saveOrUpdateBlueprint(blueprint);
		cai = geodeskConfigurationService.createClonedGeodeskConfiguration(geodesk, true);
		Assert.assertEquals(getMainMap(cai).getLayers().get(0).getWidgetInfo(KWI_KEY), blueprintCwi);

		// Configure cwi2 on blueprint
		ClientWidgetInfo blueprintCwi2 = new MyClientWidgetInfo("blueprint2");
		geodesk.getBlueprint().getMainMapLayers().get(0).getWidgetInfo().put(KWI_KEY2, blueprintCwi2);
		blueprintService.saveOrUpdateBlueprint(blueprint);
		cai = geodeskConfigurationService.createClonedGeodeskConfiguration(geodesk, true);
		Assert.assertEquals(getMainMap(cai).getLayers().get(0).getWidgetInfo(KWI_KEY2), blueprintCwi2);
		Assert.assertEquals(getMainMap(cai).getLayers().get(0).getWidgetInfo(KWI_KEY), blueprintCwi);
		
		// Add layer to the geodesk
		ClientLayer clientLayer = new ClientLayer();
		clientLayer.setLayerModel(geodesk.getBlueprint().getMainMapLayers().get(0).getLayerModel());
		geodesk.getMainMapLayers().add(clientLayer);

		// Put cwi on geodesk
		ClientWidgetInfo geodeskCwi = new MyClientWidgetInfo("geodesk");
		geodesk.getMainMapLayers().get(0).getWidgetInfo().put(KWI_KEY, geodeskCwi);
		geodeskService.saveOrUpdateGeodesk(geodesk);
		cai = geodeskConfigurationService.createClonedGeodeskConfiguration(geodesk, true);
		Assert.assertEquals(getMainMap(cai).getLayers().get(0).getWidgetInfo(KWI_KEY), geodeskCwi);
		Assert.assertEquals(getMainMap(cai).getLayers().get(0).getWidgetInfo(KWI_KEY2), blueprintCwi2);

		// Remove cwi from geodesk
		geodesk.getMainMapLayers().get(0).getWidgetInfo().remove(KWI_KEY);
		geodeskService.saveOrUpdateGeodesk(geodesk);
		cai = geodeskConfigurationService.createClonedGeodeskConfiguration(geodesk, true);
		Assert.assertEquals(getMainMap(cai).getLayers().get(0).getWidgetInfo(KWI_KEY), blueprintCwi);
		Assert.assertEquals(getMainMap(cai).getLayers().get(0).getWidgetInfo(KWI_KEY2), blueprintCwi2);

		// Remove cwi from blueprint
		blueprint.getMainMapLayers().get(0).getWidgetInfo().remove(KWI_KEY);
		blueprintService.saveOrUpdateBlueprint(geodesk.getBlueprint());
		cai = geodeskConfigurationService.createClonedGeodeskConfiguration(geodesk, true);
		Assert.assertEquals(getMainMap(cai).getLayers().get(0).getWidgetInfo(KWI_KEY2), blueprintCwi2);
		Assert.assertEquals(getMainMap(cai).getLayers().get(0).getWidgetInfo(KWI_KEY), layermodelCwi);
		
		// Remove cwi from layermodel
		blueprint.getMainMapLayers().get(0).getLayerModel().getWidgetInfo().remove(KWI_KEY);
		layerModelService.saveOrUpdateLayerModel(blueprint.getMainMapLayers().get(0).getLayerModel());
		cai = geodeskConfigurationService.createClonedGeodeskConfiguration(geodesk, true);
		Assert.assertNull(getMainMap(cai).getLayers().get(0).getWidgetInfo(KWI_KEY));
	}
	
	@Test
	public void testApplicationWidgetInfo() throws Exception {
		Geodesk geodesk = geodeskService.getGeodeskByPublicId("42");
		Blueprint blueprint = geodesk.getBlueprint();

		ClientApplicationInfo cai = geodeskConfigurationService.createClonedGeodeskConfiguration(geodesk, true);
		Assert.assertNull(cai.getWidgetInfo(KWI_KEY));

		// Put cwi on blueprint
		ClientWidgetInfo blueprintCwi = new MyClientWidgetInfo("blueprint");
		blueprint.getApplicationClientWidgetInfos().put(KWI_KEY, blueprintCwi);
		blueprintService.saveOrUpdateBlueprint(blueprint);
		cai = geodeskConfigurationService.createClonedGeodeskConfiguration(geodesk, true);
		Assert.assertEquals(cai.getWidgetInfo(KWI_KEY), blueprintCwi);

		// Put cwi2 on blueprint
		ClientWidgetInfo blueprintCwi2 = new MyClientWidgetInfo("blueprint2");
		blueprint.getApplicationClientWidgetInfos().put(KWI_KEY2, blueprintCwi2);
		blueprintService.saveOrUpdateBlueprint(blueprint);
		cai = geodeskConfigurationService.createClonedGeodeskConfiguration(geodesk, true);
		Assert.assertEquals(cai.getWidgetInfo(KWI_KEY), blueprintCwi);
		Assert.assertEquals(cai.getWidgetInfo(KWI_KEY2), blueprintCwi2);

		// Put cwi on geodesk
		ClientWidgetInfo geodeskCwi = new MyClientWidgetInfo("geodesk");
		geodesk.getApplicationClientWidgetInfos().put(KWI_KEY, geodeskCwi);
		geodeskService.saveOrUpdateGeodesk(geodesk);
		cai = geodeskConfigurationService.createClonedGeodeskConfiguration(geodesk, true);
		Assert.assertEquals(cai.getWidgetInfo(KWI_KEY), geodeskCwi);

		// Remove cwi from geodesk
		geodesk.getApplicationClientWidgetInfos().remove(KWI_KEY);
		geodeskService.saveOrUpdateGeodesk(geodesk);
		cai = geodeskConfigurationService.createClonedGeodeskConfiguration(geodesk, true);
		Assert.assertEquals(cai.getWidgetInfo(KWI_KEY), blueprintCwi);

		// Remove cwi from blueprint
		blueprint.getApplicationClientWidgetInfos().remove(KWI_KEY);
		blueprintService.saveOrUpdateBlueprint(geodesk.getBlueprint());
		cai = geodeskConfigurationService.createClonedGeodeskConfiguration(geodesk, true);
		Assert.assertNull(cai.getWidgetInfo(KWI_KEY));
	}

	@Test
	public void testMainMapWidgetInfo() throws Exception {
		Geodesk geodesk = geodeskService.getGeodeskByPublicId("42");
		Blueprint blueprint = geodesk.getBlueprint();

		ClientApplicationInfo cai = geodeskConfigurationService.createClonedGeodeskConfiguration(geodesk, true);
		Assert.assertNull(getMainMap(cai).getWidgetInfo(KWI_KEY));

		// Put cwi on blueprint
		ClientWidgetInfo blueprintCwi = new MyClientWidgetInfo("blueprint");
		blueprint.getMainMapClientWidgetInfos().put(KWI_KEY, blueprintCwi);
		blueprintService.saveOrUpdateBlueprint(blueprint);
		cai = geodeskConfigurationService.createClonedGeodeskConfiguration(geodesk, true);
		Assert.assertEquals(getMainMap(cai).getWidgetInfo(KWI_KEY), blueprintCwi);

		// Put cwi2 on blueprint
		ClientWidgetInfo blueprintCwi2 = new MyClientWidgetInfo("blueprint2");
		blueprint.getMainMapClientWidgetInfos().put(KWI_KEY2, blueprintCwi2);
		blueprintService.saveOrUpdateBlueprint(blueprint);
		cai = geodeskConfigurationService.createClonedGeodeskConfiguration(geodesk, true);
		Assert.assertEquals(getMainMap(cai).getWidgetInfo(KWI_KEY), blueprintCwi);
		Assert.assertEquals(getMainMap(cai).getWidgetInfo(KWI_KEY2), blueprintCwi2);

		// Put cwi on geodesk
		ClientWidgetInfo geodeskCwi = new MyClientWidgetInfo("geodesk");
		geodesk.getMainMapClientWidgetInfos().put(KWI_KEY, geodeskCwi);
		geodeskService.saveOrUpdateGeodesk(geodesk);
		cai = geodeskConfigurationService.createClonedGeodeskConfiguration(geodesk, true);
		Assert.assertEquals(getMainMap(cai).getWidgetInfo(KWI_KEY), geodeskCwi);
		Assert.assertEquals(getMainMap(cai).getWidgetInfo(KWI_KEY2), blueprintCwi2);

		// Remove cwi from geodesk
		geodesk.getMainMapClientWidgetInfos().remove(KWI_KEY);
		geodeskService.saveOrUpdateGeodesk(geodesk);
		cai = geodeskConfigurationService.createClonedGeodeskConfiguration(geodesk, true);
		Assert.assertEquals(getMainMap(cai).getWidgetInfo(KWI_KEY), blueprintCwi);
		Assert.assertEquals(getMainMap(cai).getWidgetInfo(KWI_KEY2), blueprintCwi2);

		// Remove cwi from blueprint
		blueprint.getMainMapClientWidgetInfos().remove(KWI_KEY);
		blueprintService.saveOrUpdateBlueprint(geodesk.getBlueprint());
		cai = geodeskConfigurationService.createClonedGeodeskConfiguration(geodesk, true);
		Assert.assertNull(getMainMap(cai).getWidgetInfo(KWI_KEY));
		Assert.assertEquals(getMainMap(cai).getWidgetInfo(KWI_KEY2), blueprintCwi2);
	}

	@Test
	public void testOverviewMapWidgetInfo() throws Exception {
		Geodesk geodesk = geodeskService.getGeodeskByPublicId("42");
		Blueprint blueprint = geodesk.getBlueprint();

		ClientApplicationInfo cai = geodeskConfigurationService.createClonedGeodeskConfiguration(geodesk, true);
		Assert.assertNull(getOverviewMap(cai).getWidgetInfo(KWI_KEY));

		// Put cwi on blueprint
		ClientWidgetInfo blueprintCwi = new MyClientWidgetInfo("blueprint");
		blueprint.getOverviewMapClientWidgetInfos().put(KWI_KEY, blueprintCwi);
		blueprintService.saveOrUpdateBlueprint(blueprint);
		cai = geodeskConfigurationService.createClonedGeodeskConfiguration(geodesk, true);
		Assert.assertEquals(getOverviewMap(cai).getWidgetInfo(KWI_KEY), blueprintCwi);

		// Put cwi2 on blueprint
		ClientWidgetInfo blueprintCwi2 = new MyClientWidgetInfo("blueprint2");
		blueprint.getOverviewMapClientWidgetInfos().put(KWI_KEY2, blueprintCwi2);
		blueprintService.saveOrUpdateBlueprint(blueprint);
		cai = geodeskConfigurationService.createClonedGeodeskConfiguration(geodesk, true);
		Assert.assertEquals(getOverviewMap(cai).getWidgetInfo(KWI_KEY), blueprintCwi);
		Assert.assertEquals(getOverviewMap(cai).getWidgetInfo(KWI_KEY2), blueprintCwi2);


		// Put cwi on geodesk
		ClientWidgetInfo geodeskCwi = new MyClientWidgetInfo("geodesk");
		geodesk.getOverviewMapClientWidgetInfos().put(KWI_KEY, geodeskCwi);
		geodeskService.saveOrUpdateGeodesk(geodesk);
		cai = geodeskConfigurationService.createClonedGeodeskConfiguration(geodesk, true);
		Assert.assertEquals(getOverviewMap(cai).getWidgetInfo(KWI_KEY), geodeskCwi);
		Assert.assertEquals(getOverviewMap(cai).getWidgetInfo(KWI_KEY2), blueprintCwi2);

		// Remove cwi from geodesk
		geodesk.getOverviewMapClientWidgetInfos().remove(KWI_KEY);
		geodeskService.saveOrUpdateGeodesk(geodesk);
		cai = geodeskConfigurationService.createClonedGeodeskConfiguration(geodesk, true);
		Assert.assertEquals(getOverviewMap(cai).getWidgetInfo(KWI_KEY), blueprintCwi);
		Assert.assertEquals(getOverviewMap(cai).getWidgetInfo(KWI_KEY2), blueprintCwi2);

		// Remove cwi from blueprint
		blueprint.getOverviewMapClientWidgetInfos().remove(KWI_KEY);
		blueprintService.saveOrUpdateBlueprint(geodesk.getBlueprint());
		cai = geodeskConfigurationService.createClonedGeodeskConfiguration(geodesk, true);
		Assert.assertNull(getOverviewMap(cai).getWidgetInfo(KWI_KEY));
		Assert.assertEquals(getOverviewMap(cai).getWidgetInfo(KWI_KEY2), blueprintCwi2);
	}
	
	@Test
	public void testMainMapLayers() throws Exception {
		Geodesk geodesk = geodeskService.getGeodeskByPublicId("42");
		Blueprint blueprint = geodesk.getBlueprint();

		ClientApplicationInfo cai = geodeskConfigurationService.createClonedGeodeskConfiguration(geodesk, true);
		Assert.assertNotNull(getMainMap(cai).getLayers());
		Assert.assertTrue(getMainMap(cai).getLayers().size() == TestConst.DEFAULT_MAIN_LAYERS);

		// Remove all layers from blueprint, this copies the default configuration from the userapplication
		blueprint.getMainMapLayers().clear();
		blueprintService.saveOrUpdateBlueprint(blueprint);
		cai = geodeskConfigurationService.createClonedGeodeskConfiguration(geodesk, true);
		Assert.assertTrue(getMainMap(cai).getLayers().size() == TestConst.DEFAULT_MAIN_LAYERS);

		// Remove all and add two layers to blueprint
		ClientLayer clientLayer1 = new ClientLayer();
		clientLayer1.setLayerModel(geodesk.getBlueprint().getMainMapLayers().get(0).getLayerModel());
		
		ClientLayer clientLayer2 = new ClientLayer();
		clientLayer2.setLayerModel(geodesk.getBlueprint().getMainMapLayers().get(1).getLayerModel());
		
		blueprint.getMainMapLayers().clear();
		blueprint.getMainMapLayers().add(clientLayer1);
		blueprint.getMainMapLayers().add(clientLayer2);

		blueprintService.saveOrUpdateBlueprint(blueprint);
		cai = geodeskConfigurationService.createClonedGeodeskConfiguration(geodesk, true);
		Assert.assertTrue(getMainMap(cai).getLayers().size() == 2);
		Assert.assertTrue(getMainMap(cai).getLayers().get(0).getId().equals(
				clientLayer1.getLayerModel().getClientLayerId()));
		Assert.assertTrue(getMainMap(cai).getLayers().get(1).getId().equals(
				clientLayer2.getLayerModel().getClientLayerId()));

		// Configure one layer on geodesk, this overrides blueprint configuration
		ClientLayer clientLayer3 = new ClientLayer();
		clientLayer3.setLayerModel(geodesk.getBlueprint().getMainMapLayers().get(0).getLayerModel());

		geodesk.getMainMapLayers().add(clientLayer3);
		geodeskService.saveOrUpdateGeodesk(geodesk);
		cai = geodeskConfigurationService.createClonedGeodeskConfiguration(geodesk, true);
		Assert.assertTrue(getMainMap(cai).getLayers().size() == 1);
		Assert.assertTrue(getMainMap(cai).getLayers().get(0).getId().equals(
				clientLayer3.getLayerModel().getClientLayerId()));
		
		// Remove all layers from geodesk, this goes back to two layers on blueprint 
		geodesk.getMainMapLayers().clear();
		geodeskService.saveOrUpdateGeodesk(geodesk);
		cai = geodeskConfigurationService.createClonedGeodeskConfiguration(geodesk, true);
		Assert.assertTrue(getMainMap(cai).getLayers().size() == 2);
		Assert.assertTrue(getMainMap(cai).getLayers().get(0).getId().equals(
				clientLayer1.getLayerModel().getClientLayerId()));
		Assert.assertTrue(getMainMap(cai).getLayers().get(1).getId().equals(
				clientLayer2.getLayerModel().getClientLayerId()));
		
		
		// Remove all layers from blueprint, this copies the default configuration from the userapplication
		blueprint.getMainMapLayers().clear();
		blueprintService.saveOrUpdateBlueprint(blueprint);
		cai = geodeskConfigurationService.createClonedGeodeskConfiguration(geodesk, true);
		Assert.assertTrue(getMainMap(cai).getLayers().size() == TestConst.DEFAULT_MAIN_LAYERS);
	}

	@Test
	public void testOverviewMapLayers() throws Exception {
		Geodesk geodesk = geodeskService.getGeodeskByPublicId("42");
		Blueprint blueprint = geodesk.getBlueprint();

		ClientApplicationInfo cai = geodeskConfigurationService.createClonedGeodeskConfiguration(geodesk, true);
		Assert.assertNotNull(getOverviewMap(cai).getLayers());
		Assert.assertTrue(getOverviewMap(cai).getLayers().size() == TestConst.DEFAULT_OVERVIEW_LAYERS);

		// Remove all layers from blueprint, this copies the default configuration from the userapplication
		blueprint.getOverviewMapLayers().clear();
		blueprintService.saveOrUpdateBlueprint(blueprint);
		cai = geodeskConfigurationService.createClonedGeodeskConfiguration(geodesk, true);
		Assert.assertTrue(getOverviewMap(cai).getLayers().size() == TestConst.DEFAULT_OVERVIEW_LAYERS);

		// Remove all and add two layers to blueprint
		ClientLayer clientLayer1 = new ClientLayer();
		clientLayer1.setLayerModel(geodesk.getBlueprint().getMainMapLayers().get(0).getLayerModel());
		
		ClientLayer clientLayer2 = new ClientLayer();
		clientLayer2.setLayerModel(geodesk.getBlueprint().getMainMapLayers().get(1).getLayerModel());
		
		blueprint.getOverviewMapLayers().clear();
		blueprint.getOverviewMapLayers().add(clientLayer1);
		blueprint.getOverviewMapLayers().add(clientLayer2);

		blueprintService.saveOrUpdateBlueprint(blueprint);
		cai = geodeskConfigurationService.createClonedGeodeskConfiguration(geodesk, true);
		Assert.assertTrue(getOverviewMap(cai).getLayers().size() == 2);
		Assert.assertTrue(getOverviewMap(cai).getLayers().get(0).getId().equals(
				clientLayer1.getLayerModel().getClientLayerId()));
		Assert.assertTrue(getOverviewMap(cai).getLayers().get(1).getId().equals(
				clientLayer2.getLayerModel().getClientLayerId()));

		// Configure one layer on geodesk, this overrides blueprint configuration
		ClientLayer clientLayer3 = new ClientLayer();
		clientLayer3.setLayerModel(geodesk.getBlueprint().getMainMapLayers().get(0).getLayerModel());

		geodesk.getOverviewMapLayers().add(clientLayer3);
		geodeskService.saveOrUpdateGeodesk(geodesk);
		cai = geodeskConfigurationService.createClonedGeodeskConfiguration(geodesk, true);
		Assert.assertTrue(getOverviewMap(cai).getLayers().size() == 1);
		Assert.assertTrue(getOverviewMap(cai).getLayers().get(0).getId().equals(
				clientLayer3.getLayerModel().getClientLayerId()));
		
		// Remove all layers from geodesk, this goes back to two layers on blueprint 
		geodesk.getOverviewMapLayers().clear();
		geodeskService.saveOrUpdateGeodesk(geodesk);
		cai = geodeskConfigurationService.createClonedGeodeskConfiguration(geodesk, true);
		Assert.assertTrue(getOverviewMap(cai).getLayers().size() == 2);
		Assert.assertTrue(getOverviewMap(cai).getLayers().get(0).getId().equals(
				clientLayer1.getLayerModel().getClientLayerId()));
		Assert.assertTrue(getOverviewMap(cai).getLayers().get(1).getId().equals(
				clientLayer2.getLayerModel().getClientLayerId()));
		
		
		// Remove all layers from blueprint, this copies the default configuration from the userapplication
		blueprint.getOverviewMapLayers().clear();
		blueprintService.saveOrUpdateBlueprint(blueprint);
		cai = geodeskConfigurationService.createClonedGeodeskConfiguration(geodesk, true);
		Assert.assertTrue(getOverviewMap(cai).getLayers().size() == TestConst.DEFAULT_OVERVIEW_LAYERS);
	}

	private static ClientMapInfo getMainMap(ClientApplicationInfo cai) {
		for (ClientMapInfo mi : cai.getMaps()) {
			if (mi.getId().equals(GdmLayout.MAPMAIN_ID)) {
				return mi;
			}
		}
		return null;
	}

	private static ClientMapInfo getOverviewMap(ClientApplicationInfo cai) {
		for (ClientMapInfo mi : cai.getMaps()) {
			if (mi.getId().equals(GdmLayout.MAPOVERVIEW_ID)) {
				return mi;
			}
		}
		return null;
	}

}
