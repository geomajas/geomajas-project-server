package org.geomajas.plugin.admin.command.admin;

import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.CommandResponse;
import org.geomajas.command.dto.GetRasterTilesRequest;
import org.geomajas.configuration.RasterLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.plugin.admin.command.dto.SaveOrUpdateParameterBeanRequest;
import org.geomajas.plugin.admin.service.BeanFactory;
import org.geomajas.plugin.admin.service.factory.ClientRasterLayerBeanFactory;
import org.geomajas.plugin.admin.service.factory.ClientVectorLayerBeanFactory;
import org.geomajas.plugin.admin.service.factory.WmsLayerBeanFactory;
import org.geomajas.security.SecurityManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:org/geomajas/spring/geomajasContext.xml","/org/geomajas/plugin/admin/command/admin/admin.xml",
		"/org/geomajas/plugin/admin/command/admin/appAdmin.xml",
		"/org/geomajas/plugin/admin/command/admin/mapAdmin.xml" })
public class SaveOrUpdateBeanParameterCommandTest {

	@Autowired
	private CommandDispatcher commandDispatcher;

	@Autowired
	private SecurityManager manager;

	@Autowired
	private ApplicationContext applicationContext;

	@Before
	public void login() {
		manager.createSecurityContext(null);
	}

	@After
	public void logout() {
		manager.clearSecurityContext();
	}

	@Test
	public void testCreateShapeLayer() {
		SaveOrUpdateParameterBeanRequest request = new SaveOrUpdateParameterBeanRequest();
		request.addStringParameter(BeanFactory.CLASS_NAME, "org.geomajas.layer.geotools.GeoToolsLayer");
		request.addStringParameter(BeanFactory.BEAN_NAME, "adminCountries");
		request.addStringParameter("location", "adminCountries.shp");
		CommandResponse response = commandDispatcher.execute(SaveOrUpdateParameterBeanRequest.COMMAND, request, null,
				null);
		if (response.isError()) {
			for (Throwable throwable : response.getErrors()) {
				throwable.printStackTrace();
			}
		}

		request = new SaveOrUpdateParameterBeanRequest();
		request.addStringParameter(ClientVectorLayerBeanFactory.CLASS_NAME,
				"org.geomajas.configuration.client.ClientVectorLayerInfo");
		request.addStringParameter(ClientVectorLayerBeanFactory.BEAN_NAME, "clientAdminCountries");
		request.addStringParameter(ClientVectorLayerBeanFactory.LABEL, "Countries");
		request.addStringParameter(ClientVectorLayerBeanFactory.SERVER_LAYER_ID, "adminCountries");
		request.addStringParameter(ClientVectorLayerBeanFactory.MAP_ID, "mapAdmin");
		response = commandDispatcher.execute(SaveOrUpdateParameterBeanRequest.COMMAND, request, null, null);
		if (response.isError()) {
			for (Throwable throwable : response.getErrors()) {
				throwable.printStackTrace();
			}
		}
	}

	@Test
	public void testCreateWmsLayer() {
		Assert.assertNotNull(applicationContext.getBean(GetRasterTilesRequest.COMMAND));
		SaveOrUpdateParameterBeanRequest request = new SaveOrUpdateParameterBeanRequest();
		request.addStringParameter(WmsLayerBeanFactory.CLASS_NAME, "org.geomajas.layer.wms.WmsLayer");
		RasterLayerInfo info = new RasterLayerInfo();
		info.setDataSourceName("topp:gemeenten");
		info.setMaxExtent(new Bbox(20026376.393709917, 20026376.393709917, 40052752.787419834, 40052752.787419834));
		info.setCrs("EPSG:900913");
		info.setTileWidth(512);
		info.setTileHeight(512);	
		request.addStringParameter(WmsLayerBeanFactory.BEAN_NAME, "wmsLayer");
		request.addStringParameter(WmsLayerBeanFactory.BASE_WMS_URL, "http://apps.geomajas.org/geoserver/wms");
		request.addObjectParameter(WmsLayerBeanFactory.LAYER_INFO, info);
		CommandResponse response = commandDispatcher.execute(SaveOrUpdateParameterBeanRequest.COMMAND, request, null,
				null);
		if (response.isError()) {
			for (Throwable throwable : response.getErrors()) {
				throwable.printStackTrace();
			}
		}

		request = new SaveOrUpdateParameterBeanRequest();
		request.addStringParameter(ClientRasterLayerBeanFactory.CLASS_NAME,
				"org.geomajas.configuration.client.ClientRasterLayerInfo");
		request.addStringParameter(ClientRasterLayerBeanFactory.BEAN_NAME, "clientWmsLayer");
		request.addStringParameter(ClientRasterLayerBeanFactory.LABEL, "WMS");
		request.addStringParameter(ClientRasterLayerBeanFactory.SERVER_LAYER_ID, "wmsLayer");
		request.addStringParameter(ClientRasterLayerBeanFactory.MAP_ID, "mapAdmin");
		response = commandDispatcher.execute(SaveOrUpdateParameterBeanRequest.COMMAND, request, null, null);
		if (response.isError()) {
			for (Throwable throwable : response.getErrors()) {
				throwable.printStackTrace();
			}
		}
		Assert.assertNotNull(applicationContext.getBean(GetRasterTilesRequest.COMMAND));
	}

}
