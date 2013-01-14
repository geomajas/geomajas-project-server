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

package org.geomajas.command.feature;

import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.dto.PersistTransactionRequest;
import org.geomajas.command.dto.PersistTransactionResponse;
import org.geomajas.geometry.Geometry;
import org.geomajas.global.ExceptionCode;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.feature.Feature;
import org.geomajas.layer.feature.FeatureTransaction;
import org.geomajas.security.*;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.geomajas.testdata.ReloadContext;
import org.geomajas.testdata.ReloadContextTestExecutionListener;
import org.geomajas.testdata.rule.SecurityRule;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Test for {@link PersistTransactionCommand}.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(listeners = { ReloadContextTestExecutionListener.class,
		DependencyInjectionTestExecutionListener.class })
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/layerCountries.xml", "/org/geomajas/testdata/simplevectorsContext.xml" })
@ReloadContext
public class PersistTransactionCommandTest {

	private static final double DOUBLE_TOLERANCE = .0000000001;
	private static final String LAYER_ID = "countries";
	private static final String CRS = "EPSG:4326";

	@Autowired
	private CommandDispatcher dispatcher;

	@Autowired
	private GeoService geoService;

	@Autowired
	private DtoConverterService dtoConverter;

	@Autowired
	@Rule
	public SecurityRule securityRule;

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

	@Test
	@DirtiesContext // adding a bean
	public void testPersistAddFeatureTransaction() throws Exception {
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

	// Test for creating a feature without geometry. This should put an empty geometry in the feature.
	@Test
	@DirtiesContext // adding a bean with empty context if the test fails
	public void testCreateWithoutGeometry() throws Exception {
		PersistTransactionRequest request = new PersistTransactionRequest();
		request.setCrs(CRS);
		FeatureTransaction featureTransaction = new FeatureTransaction();
		featureTransaction.setLayerId(LAYER_ID);
		Feature feature = new Feature();
		featureTransaction.setNewFeatures(new Feature[] {feature});
		request.setFeatureTransaction(featureTransaction);
		PersistTransactionResponse response = (PersistTransactionResponse) dispatcher.execute(
				PersistTransactionRequest.COMMAND, request, null, "en");
		assertThat(response.isError()).isTrue();
		Throwable error = response.getErrors().get(0);
		assertThat(error).isInstanceOf(LayerException.class);
		assertThat(((LayerException) error).getExceptionCode()).isEqualTo(
				ExceptionCode.LAYER_EMPTY_GEOMETRY_NOT_ALLOWED);
		/*
		if (response.isError()) {
			response.getErrors().get(0).printStackTrace();
		}
		Assert.assertFalse(response.isError());
		Assert.assertNotNull(response.getFeatureTransaction());
		Feature[] newFeatures = response.getFeatureTransaction().getNewFeatures();
		assertThat(newFeatures.length).isEqualTo(1);
		assertThat(newFeatures[0].getId()).isNotNull();
		assertThat(newFeatures[0].getGeometry()).isNotNull();
		*/
	}

}
