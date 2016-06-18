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

package org.geomajas.command.feature;

import org.geomajas.command.CommandDispatcher;
import org.geomajas.command.dto.PersistTransactionRequest;
import org.geomajas.command.dto.PersistTransactionResponse;
import org.geomajas.layer.feature.Feature;
import org.geomajas.layer.feature.FeatureTransaction;
import org.geomajas.testdata.ReloadContext;
import org.geomajas.testdata.ReloadContextTestExecutionListener;
import org.geomajas.testdata.rule.SecurityRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Test for {@link org.geomajas.command.feature.PersistTransactionCommand}.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(listeners = {ReloadContextTestExecutionListener.class,
		DependencyInjectionTestExecutionListener.class})
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml", "/org/geomajas/testdata/allowAll.xml",
		"/org/geomajas/command/emptyGeometryTest.xml"})
@ReloadContext
public class PersistTransactionCommandEmptyGeometryTest {

	private static final String LAYER_EMPTY_POINT_ID = "layerEmptyPoint";
	private static final String LAYER_EMPTY_POLYGON_ID = "layerEmptyGeometry";
	private static final String CRS = "EPSG:4326";

	@Autowired
	private CommandDispatcher dispatcher;

	@Autowired
	@Rule
	public SecurityRule securityRule;

	// Test for creating a feature with null (point) geometry. This should fail as empty points don't exist.
	@Test
	@DirtiesContext // adding a bean with empty context if the test fails
	public void testCreateEmptyPoint() throws Exception {
		PersistTransactionRequest request = new PersistTransactionRequest();
		request.setCrs(CRS);
		FeatureTransaction featureTransaction = new FeatureTransaction();
		featureTransaction.setLayerId(LAYER_EMPTY_POINT_ID);
		Feature feature = new Feature();
		featureTransaction.setNewFeatures(new Feature[] {feature});
		request.setFeatureTransaction(featureTransaction);
		PersistTransactionResponse response = (PersistTransactionResponse) dispatcher.execute(
				PersistTransactionRequest.COMMAND, request, null, "en");
		if (response.isError()) {
			response.getErrors().get(0).printStackTrace();
		}
		assertThat(response.isError()).isFalse();
		assertThat(response.getFeatureTransaction()).isNotNull();
		Feature[] newFeatures = response.getFeatureTransaction().getNewFeatures();
		assertThat(newFeatures.length).isEqualTo(1);
		assertThat(newFeatures[0].getId()).isNotNull();
		assertThat(newFeatures[0].getGeometry()).isNotNull();
/*
		assertThat(response.isError()).isTrue();
		Throwable error = response.getErrors().get(0);
		assertThat(error).isInstanceOf(LayerException.class);
		assertThat(((LayerException) error).getExceptionCode()).isEqualTo(
				ExceptionCode.LAYER_EMPTY_GEOMETRY_POINT);
*/
	}

	// Test for creating a feature with null (polygon) geometry. This should add a feature in the layer.
	@Test
	@DirtiesContext // adding a bean
	public void testCreateEmptyPolygon() throws Exception {
		PersistTransactionRequest request = new PersistTransactionRequest();
		request.setCrs(CRS);
		FeatureTransaction featureTransaction = new FeatureTransaction();
		featureTransaction.setLayerId(LAYER_EMPTY_POLYGON_ID);
		Feature feature = new Feature();
		featureTransaction.setNewFeatures(new Feature[] {feature});
		request.setFeatureTransaction(featureTransaction);
		PersistTransactionResponse response = (PersistTransactionResponse) dispatcher.execute(
				PersistTransactionRequest.COMMAND, request, null, "en");
		if (response.isError()) {
			response.getErrors().get(0).printStackTrace();
		}
		assertThat(response.isError()).isFalse();
		assertThat(response.getFeatureTransaction()).isNotNull();
		Feature[] newFeatures = response.getFeatureTransaction().getNewFeatures();
		assertThat(newFeatures.length).isEqualTo(1);
		assertThat(newFeatures[0].getId()).isNotNull();
		assertThat(newFeatures[0].getGeometry()).isNotNull();
	}

}
