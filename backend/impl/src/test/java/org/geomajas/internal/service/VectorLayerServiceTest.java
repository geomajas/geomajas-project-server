/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.internal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.geometry.Crs;
import org.geomajas.geometry.service.WktService;
import org.geomajas.internal.layer.tile.TileMetadataImpl;
import org.geomajas.layer.VectorLayerService;
import org.geomajas.layer.bean.BeanLayer;
import org.geomajas.layer.bean.FeatureBean;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.Feature;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.feature.attribute.StringAttribute;
import org.geomajas.layer.tile.InternalTile;
import org.geomajas.layer.tile.TileCode;
import org.geomajas.layer.tile.TileMetadata;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.FilterService;
import org.geomajas.service.GeoService;
import org.geomajas.testdata.Country;
import org.geomajas.testdata.rule.SecurityRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.PrecisionModel;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Tests for VectorLayerService.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/beanContext.xml", "/org/geomajas/testdata/layerBeans.xml",
		"/org/geomajas/internal/service/countriesNotAllEditable.xml"})
public class VectorLayerServiceTest {

	private static final String LAYER_ID = "beans";
	private static final String STRING_ATTR = "stringAttr";
	private static final String LAYER_NOT_ALL_EDITABLE_ID = "layerCountriesNotAllEditable";
	private static final String COUNTRY_CODE_ATTR = "country";
	private static final String COUNTRY_NAME_ATTR = "name";

	private static final double ALLOWANCE = .00000001;

	@Autowired
	private VectorLayerService layerService;

	@Autowired
	@Qualifier(LAYER_ID)
	private BeanLayer beanLayer;

	@Autowired
	@Qualifier(LAYER_NOT_ALL_EDITABLE_ID)
	private BeanLayer notAllEditableLayer;

	@Autowired
	private FilterService filterService;

	@Autowired
	private DtoConverterService converterService;

	@Autowired
	private GeoService geoService;

	@Autowired
	@Rule
	public SecurityRule securityRule;

	@Test
	public void testGetFeaturesLazy() throws Exception {
		List<InternalFeature> features = layerService.getFeatures(LAYER_ID,
				geoService.getCrs(beanLayer.getLayerInfo().getCrs()), null, null,
				VectorLayerService.FEATURE_INCLUDE_NONE);
		Assert.assertEquals(3, features.size());
		InternalFeature feature = features.get(0);
		Assert.assertNotNull(feature.getId());
		Assert.assertNull(feature.getGeometry());
		Assert.assertNull(feature.getAttributes());
		Assert.assertNull(feature.getLabel());
		Assert.assertNull(feature.getStyleInfo());
	}
	
	@Test
	@DirtiesContext
	@SuppressWarnings("unchecked")
	public void testUpdate() throws Exception {
		Filter filter = filterService.createFidFilter(new String[]{"3"});
		CoordinateReferenceSystem crs = geoService.getCrs(beanLayer.getLayerInfo().getCrs());
		List<InternalFeature> oldFeatures = layerService.getFeatures(LAYER_ID,
				crs, filter, null, VectorLayerService.FEATURE_INCLUDE_ATTRIBUTES);
		Assert.assertEquals(1, oldFeatures.size());
		InternalFeature feature = oldFeatures.get(0);
		List<InternalFeature> newFeatures = new ArrayList<InternalFeature>();
		feature = feature.clone();
		feature.getAttributes().put(STRING_ATTR, new StringAttribute("changed"));
		newFeatures.add(feature);
		layerService.saveOrUpdate(LAYER_ID, crs, oldFeatures, newFeatures);

		Iterator<FeatureBean> iterator =
				(Iterator<FeatureBean>) beanLayer.getElements(filterService.createTrueFilter(), 0, 0);
		int count = 0;
		int check = 0;
		while (iterator.hasNext()) {
			FeatureBean featureBean = iterator.next();
			count++;
			if (3 == featureBean.getId()) {
				Assert.assertEquals("changed", featureBean.getStringAttr());
			}
			check |= 1 << (featureBean.getId() - 1);
		}
		Assert.assertEquals(3, count);
		Assert.assertEquals(7, check);
	}

	@Test
	@DirtiesContext
	@SuppressWarnings("unchecked")
	public void testCreateDelete() throws Exception {
		// done in one test to assure the state is back to what is expected,
		// the spring context is not rebuilt between test methods
		
		// Create first
		CoordinateReferenceSystem crs = geoService.getCrs(beanLayer.getLayerInfo().getCrs());
		List<InternalFeature> oldFeatures = new ArrayList<InternalFeature>();
		List<InternalFeature> newFeatures = new ArrayList<InternalFeature>();
		InternalFeature feature = converterService.toInternal(new Feature());
		feature.setId("4");
		feature.setLayer(beanLayer);
		// feature needs a geometry or exceptions all over
		GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel());
		Geometry geometry = geometryFactory.createPoint(new Coordinate(1.5, 1.5));
		feature.setGeometry(geometry);
		newFeatures.add(feature);
		layerService.saveOrUpdate(LAYER_ID, crs, oldFeatures, newFeatures);

		Iterator<FeatureBean> iterator =
				(Iterator<FeatureBean>) beanLayer.getElements(filterService.createTrueFilter(), 0, 0);
		int count = 0;
		int check = 0;
		while (iterator.hasNext()) {
			FeatureBean featureBean = iterator.next();
			count++;
			check |= 1 << (featureBean.getId() - 1);
		}
		Assert.assertEquals(4, count);
		Assert.assertEquals(15, check);

		// now delete again
		Filter filter = filterService.createFidFilter(new String[]{"4"});
		oldFeatures = layerService.getFeatures(LAYER_ID,
				crs, filter, null, VectorLayerService.FEATURE_INCLUDE_ATTRIBUTES);
		Assert.assertEquals(1, oldFeatures.size());
		newFeatures = new ArrayList<InternalFeature>();
		layerService.saveOrUpdate(LAYER_ID, crs, oldFeatures, newFeatures);

		iterator = (Iterator<FeatureBean>) beanLayer.getElements(filterService.createTrueFilter(), 0, 0);
		count = 0;
		check = 0;
		while (iterator.hasNext()) {
			FeatureBean featureBean = iterator.next();
			count++;
			check |= 1 << (featureBean.getId() - 1);
		}
		Assert.assertEquals(3, count);
		Assert.assertEquals(7, check);
	}

	@Test
	public void testGetFeaturesAllFiltered() throws Exception {
		Filter filter = filterService.createFidFilter(new String[]{"3"});
		List<InternalFeature> features = layerService.getFeatures(LAYER_ID,
				geoService.getCrs2(beanLayer.getLayerInfo().getCrs()), filter, null,
				VectorLayerService.FEATURE_INCLUDE_ALL);
		Assert.assertEquals(1, features.size());
		InternalFeature feature = features.get(0);
		Assert.assertEquals("3", feature.getId());
		Assert.assertNotNull(feature.getGeometry());
		Assert.assertNotNull(feature.getAttributes().get(STRING_ATTR));
	}

	@Test
	public void testGetBoundsAll() throws Exception {
		Envelope bounds = layerService.getBounds(LAYER_ID, geoService.getCrs2(beanLayer.getLayerInfo().getCrs()), null);
		Assert.assertEquals(0, bounds.getMinX(), ALLOWANCE);
		Assert.assertEquals(0, bounds.getMinY(), ALLOWANCE);
		Assert.assertEquals(7, bounds.getMaxX(), ALLOWANCE);
		Assert.assertEquals(3, bounds.getMaxY(), ALLOWANCE);
	}

	@Test
	public void testGetBoundsFidFiltered() throws Exception {
		Filter filter = filterService.createFidFilter(new String[]{"2", "3"});
		Envelope bounds = layerService.getBounds(LAYER_ID,
				geoService.getCrs2(beanLayer.getLayerInfo().getCrs()), filter);
		Assert.assertEquals(2, bounds.getMinX(), ALLOWANCE);
		Assert.assertEquals(0, bounds.getMinY(), ALLOWANCE);
		Assert.assertEquals(7, bounds.getMaxX(), ALLOWANCE);
		Assert.assertEquals(3, bounds.getMaxY(), ALLOWANCE);
	}
	
	@Test
	public void testGetBoundsIntersectsFiltered() throws Exception {
		GeometryFactory factory = new GeometryFactory();
		LineString equator = factory.createLineString(new Coordinate[] { new Coordinate(0, 0),
				new Coordinate(-180, 180) });
		Filter filter = filterService.createIntersectsFilter(equator,beanLayer.getLayerInfo().getFeatureInfo().getGeometryType().getName());
		Envelope bounds = layerService.getBounds(LAYER_ID,
				geoService.getCrs2(beanLayer.getLayerInfo().getCrs()), filter);
		Assert.assertEquals(0, bounds.getMinX(), ALLOWANCE);
		Assert.assertEquals(0, bounds.getMinY(), ALLOWANCE);
		Assert.assertEquals(1, bounds.getMaxX(), ALLOWANCE);
		Assert.assertEquals(1, bounds.getMaxY(), ALLOWANCE);
	}


	@Test
	public void getTile()  throws Exception {
		TileMetadata tileMetadata = getTileMetadata();
		InternalTile tile = layerService.getTile(tileMetadata);
		Assert.assertNotNull(tile);
		Assert.assertNotNull(tile.getFeatures());
		Assert.assertEquals(3, tile.getFeatures().size());
	}

	@Test
	public void getFilteredTile()  throws Exception {
		TileMetadata tileMetadata = getTileMetadata();
		tileMetadata.setFilter("doubleAttr is null");
		InternalTile tile = layerService.getTile(tileMetadata);
		Assert.assertNotNull(tile);
		Assert.assertNotNull(tile.getFeatures());
		Assert.assertEquals(2, tile.getFeatures().size());

	}

	private TileMetadata getTileMetadata() {
		TileMetadata tileMetadata = new TileMetadataImpl();
		tileMetadata.setCrs("EPSG:4326");
		tileMetadata.setCode(new TileCode(0,0,0));
		tileMetadata.setRenderer(TileMetadata.PARAM_SVG_RENDERER);
		tileMetadata.setLayerId(LAYER_ID);
		tileMetadata.setPaintGeometries(true);
		tileMetadata.setScale(1.0);
		NamedStyleInfo styleInfo = new NamedStyleInfo();
		styleInfo.setName("beansStyleInfo");
		tileMetadata.setStyleInfo(styleInfo);
		tileMetadata.setPanOrigin(new org.geomajas.geometry.Coordinate(0, 0));
		return tileMetadata;
	}

	@Test
	@DirtiesContext
	@SuppressWarnings("unchecked")
	public void testUpdateEditable() throws Exception {
		String newGeometryWkt = "MULTIPOLYGON (((0 -1,1 -1,1 0,0 0,0 -1)))";
		Filter filter = filterService.createFidFilter(new String[]{"1"});
		Crs crs = geoService.getCrs2(notAllEditableLayer.getLayerInfo().getCrs());
		List<InternalFeature> oldFeatures = layerService.getFeatures(LAYER_NOT_ALL_EDITABLE_ID,
				crs, filter, null, VectorLayerService.FEATURE_INCLUDE_ATTRIBUTES);
		Assert.assertEquals(1, oldFeatures.size());
		InternalFeature feature = oldFeatures.get(0);
		List<InternalFeature> newFeatures = new ArrayList<InternalFeature>();
		feature = feature.clone();
		feature.getAttributes().put(COUNTRY_CODE_ATTR, new StringAttribute("newCode"));
		feature.getAttributes().put(COUNTRY_NAME_ATTR, new StringAttribute("newName"));
		feature.setGeometry(converterService.toInternal(WktService.toGeometry(newGeometryWkt)));
		newFeatures.add(feature);
		layerService.saveOrUpdate(LAYER_NOT_ALL_EDITABLE_ID, crs, oldFeatures, newFeatures);

		Iterator<Country> iterator =
				(Iterator<Country>) notAllEditableLayer.getElements(filterService.createTrueFilter(), 0, 0);
		int count = 0;
		while (iterator.hasNext()) {
			Country country = iterator.next();
			count++;
			if (1 == country.getId()) {
				assertThat(country.getCountry()).isEqualTo("CODE"); // not overwritten
				assertThat(country.getName()).isEqualTo("newName"); // overwritten
				assertThat(country.getGeometry()).isEqualTo(
						"MULTIPOLYGON(((0 0,1 0,1 1,0 1,0 0)))"); // not overwritten
			}
		}
		Assert.assertEquals(1, count);
	}

	@Test
	@DirtiesContext
	@SuppressWarnings("unchecked")
	public void testCreateEditable() throws Exception {
		String newGeometryWkt = "MULTIPOLYGON (((0 -1, 1 -1, 1 0, 0 0, 0 -1)))";
		Crs crs = geoService.getCrs2(notAllEditableLayer.getLayerInfo().getCrs());
		List<InternalFeature> oldFeatures = new ArrayList<InternalFeature>();
		List<InternalFeature> newFeatures = new ArrayList<InternalFeature>();
		InternalFeature feature = converterService.toInternal(new Feature());
		Map<String, Attribute> attributes = new HashMap<String, Attribute>();
		feature.setAttributes(attributes);
		feature.setId("2");
		feature.setLayer(notAllEditableLayer);
		// feature needs a geometry or exceptions all over
		feature.setGeometry(converterService.toInternal(WktService.toGeometry(newGeometryWkt)));
		attributes.put(COUNTRY_CODE_ATTR, new StringAttribute("newCode"));
		attributes.put(COUNTRY_NAME_ATTR, new StringAttribute("newName"));
		newFeatures.add(feature);
		layerService.saveOrUpdate(LAYER_NOT_ALL_EDITABLE_ID, crs, oldFeatures, newFeatures);

		Iterator<Country> iterator =
				(Iterator<Country>) notAllEditableLayer.getElements(filterService.createTrueFilter(), 0, 0);
		int count = 0;
		while (iterator.hasNext()) {
			Country country = iterator.next();
			if (2 == country.getId()) {
				assertThat(country.getCountry()).isEqualTo("newCode"); // written
				assertThat(country.getName()).isEqualTo("newName"); // written
				assertThat(country.getGeometry()).isEqualTo(newGeometryWkt); // written
			}
			count++;
		}
		Assert.assertEquals(2, count);
	}

	// @todo should also test the getObjects() method.

}
