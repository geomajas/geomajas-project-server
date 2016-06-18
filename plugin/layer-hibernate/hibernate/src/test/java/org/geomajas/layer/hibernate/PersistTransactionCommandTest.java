/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.layer.hibernate;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.dto.PersistTransactionRequest;
import org.geomajas.command.dto.PersistTransactionResponse;
import org.geomajas.geometry.Geometry;
import org.geomajas.layer.feature.Feature;
import org.geomajas.layer.feature.FeatureTransaction;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test for {@link org.geomajas.command.feature.PersistTransactionCommand} on a Hibernate layer
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/testContext.xml" })
@Transactional(rollbackFor = {org.geomajas.global.GeomajasException.class})
public class PersistTransactionCommandTest {

	private static final String LAYER_ID = "layer";
	private static final String CRS = "EPSG:4326";

	@Autowired
	private CommandDispatcher dispatcher;

	@Autowired
	private GeoService geoService;

	@Autowired
	private DtoConverterService dtoConverter;

	@Test
	public void testPersistEmptyTransaction() throws Exception {
		PersistTransactionRequest request = new PersistTransactionRequest();
		request.setCrs(CRS);
		FeatureTransaction featureTransaction = new FeatureTransaction();
		featureTransaction.setLayerId(LAYER_ID);
		request.setFeatureTransaction(featureTransaction);
		PersistTransactionResponse response = (PersistTransactionResponse) dispatcher.execute(
				PersistTransactionRequest.COMMAND, request, null, "en");
		if (response.isError()) {
			response.getErrors().get(0).printStackTrace();
		}
		Assert.assertFalse(response.isError());
		Assert.assertNotNull(response.getFeatureTransaction());
	}

	/**
	 * Check that the generated id is actually returned.
	 *
	 * @throws Exception oops
	 */
	@Test
	@DirtiesContext // adding a bean
	public void testPersistVerifyGeneratedIdSet() throws Exception {
		PersistTransactionRequest request = new PersistTransactionRequest();
		request.setCrs(CRS);
		FeatureTransaction featureTransaction = new FeatureTransaction();
		featureTransaction.setLayerId(LAYER_ID);
		Feature feature = new Feature();
		GeometryFactory factory = new GeometryFactory();
		Geometry circle =
				dtoConverter.toDto(geoService.createCircle(factory.createPoint(new Coordinate(0, 0)), 10, 10));
		feature.setGeometry(circle);
		featureTransaction.setNewFeatures(new Feature[] {feature});
		request.setFeatureTransaction(featureTransaction);
		PersistTransactionResponse response = (PersistTransactionResponse) dispatcher.execute(
				PersistTransactionRequest.COMMAND, request, null, "en");
		if (response.isError()) {
			response.getErrors().get(0).printStackTrace();
		}
		Assert.assertFalse(response.isError());
		Assert.assertNotNull(response.getFeatureTransaction());
		Feature[] newFeatures = response.getFeatureTransaction().getNewFeatures();
		Assert.assertEquals(1, newFeatures.length);
		Assert.assertNotNull(newFeatures[0].getId());
	}

}
