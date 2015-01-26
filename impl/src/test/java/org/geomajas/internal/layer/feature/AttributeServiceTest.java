/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.internal.layer.feature;

import com.vividsolutions.jts.geom.Envelope;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.internal.layer.vector.lazy.LazyAttribute;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.VectorLayerLazyFeatureConversionSupport;
import org.geomajas.layer.bean.FeatureBean;
import org.geomajas.layer.bean.ManyToOneAttributeBean;
import org.geomajas.layer.bean.OneToManyAttributeBean;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.Feature;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.feature.attribute.ManyToOneAttribute;
import org.geomajas.layer.feature.attribute.OneToManyAttribute;
import org.geomajas.service.DtoConverterService;
import org.geomajas.spring.ThreadScopeContextHolder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Test for AttributeService.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/beanContext.xml", "/org/geomajas/testdata/layerBeans.xml" })
public class AttributeServiceTest {

	private static final long TEST_ID = 17;
	private static final String TEST_STRING = "test";
	private static final int TEST_INTEGER = 37;
	private static final String TEST_STRING2 = "m2oLinked";
	private static final String TEST_STRING3 = "o2mLinked";

	private FeatureBean featureBean;

	@Autowired
	@Qualifier("beans")
	private VectorLayer layerBeans;

	private VectorLayer lazyLayerBeans;

	@Autowired
	private AttributeService attributeService;

	@Autowired
	private DtoConverterService dtoConverter;

	@Autowired
	private org.geomajas.security.SecurityManager securityManager;

	@Before
	public void init() {
		featureBean = new FeatureBean();
		featureBean.setId(TEST_ID);
		featureBean.setStringAttr(TEST_STRING);
		featureBean.setIntegerAttr(TEST_INTEGER);
		featureBean.setBooleanAttr(true);
		ManyToOneAttributeBean manyToOne = new ManyToOneAttributeBean();
		manyToOne.setStringAttr(TEST_STRING2);
		featureBean.setManyToOneAttr(manyToOne);
		OneToManyAttributeBean oneToMany = new OneToManyAttributeBean();
		oneToMany.setStringAttr(TEST_STRING3);
		List<OneToManyAttributeBean> list = new ArrayList<OneToManyAttributeBean>();
		list.add(oneToMany);
		featureBean.setOneToManyAttr(list);

		securityManager.createSecurityContext(""); // log in

		lazyLayerBeans = new LazyVectorLayer(layerBeans);
	}

	@After
	public void fixSideEffects() {
		ThreadScopeContextHolder.clear();
	}

	@Test
	public void testNormal() throws Exception {
		InternalFeature feature = new InternalFeatureImpl();
		Assert.assertNotNull(attributeService.getAttributes(layerBeans, feature, featureBean));
		Attribute attribute;
		Attribute linked;
		attribute = feature.getAttributes().get("stringAttr");
		assertThat(attribute.getValue()).isEqualTo(TEST_STRING);
		assertThat(attribute.isEditable()).isTrue(); // AllowAll -> all editable
		attribute = feature.getAttributes().get("integerAttr");
		assertThat(attribute.getValue()).isEqualTo(TEST_INTEGER);
		assertThat(attribute.isEditable()).isTrue(); // AllowAll -> all editable
		attribute = feature.getAttributes().get("booleanAttr");
		assertThat(attribute.getValue()).isEqualTo(Boolean.TRUE);
		assertThat(attribute.isEditable()).isTrue(); // AllowAll -> all editable

		assertThat(containsLazy(feature.getAttributes())).isFalse();
		Feature dto = dtoConverter.toDto(feature);
		assertThat(containsLazy(dto.getAttributes())).isFalse();

		// verify rights on many-to-one attributes
		attribute = feature.getAttributes().get("manyToOneAttr");
		assertThat(attribute.isEditable()).isTrue(); // AllowAll -> all editable
		linked = ((ManyToOneAttribute) attribute).getValue().getAllAttributes().get("stringAttr");
		assertThat(linked.getValue()).isEqualTo(TEST_STRING2);
		assertThat(linked.isEditable()).isTrue(); // AllowAll -> all editable

		// verify rights on one-to-many attributes
		attribute = feature.getAttributes().get("oneToManyAttr");
		assertThat(attribute.isEditable()).isTrue(); // AllowAll -> all editable
		linked = ((OneToManyAttribute) attribute).getValue().get(0).getAllAttributes().get("stringAttr");
		assertThat(linked.getValue()).isEqualTo(TEST_STRING3);
		assertThat(linked.isEditable()).isTrue(); // AllowAll -> all editable
	}

	@Test
	@DirtiesContext // @todo ?? don't know why this is needed, it affects SecurityContextAttributeAuthorizationTest
	public void testLazy() throws Exception {
		InternalFeature feature = new InternalFeatureImpl();
		Assert.assertNotNull(attributeService.getAttributes(lazyLayerBeans, feature, featureBean));
		Attribute attribute;
		Attribute linked;
		attribute = feature.getAttributes().get("stringAttr");
		assertThat(attribute.getValue()).isEqualTo(TEST_STRING);
		assertThat(attribute.isEditable()).isTrue(); // AllowAll -> all editable
		attribute = feature.getAttributes().get("integerAttr");
		assertThat(attribute.getValue()).isEqualTo(TEST_INTEGER);
		assertThat(attribute.isEditable()).isTrue(); // AllowAll -> all editable
		attribute = feature.getAttributes().get("booleanAttr");
		assertThat(attribute.getValue()).isEqualTo(Boolean.TRUE);
		assertThat(attribute.isEditable()).isTrue(); // AllowAll -> all editable

		assertThat(containsLazy(feature.getAttributes())).isTrue();
		Feature dto = dtoConverter.toDto(feature);
		assertThat(containsLazy(dto.getAttributes())).isFalse();

		// verify rights on many-to-one attributes
		attribute = feature.getAttributes().get("manyToOneAttr");
		assertThat(attribute.isEditable()).isTrue(); // AllowAll -> all editable
		linked = ((ManyToOneAttribute) attribute).getValue().getAllAttributes().get("stringAttr");
		assertThat(linked.getValue()).isEqualTo(TEST_STRING2);
		assertThat(linked.isEditable()).isTrue(); // AllowAll -> all editable

		// verify rights on one-to-many attributes
		attribute = feature.getAttributes().get("oneToManyAttr");
		assertThat(attribute.isEditable()).isTrue(); // AllowAll -> all editable
		linked = ((OneToManyAttribute) attribute).getValue().get(0).getAllAttributes().get("stringAttr");
		assertThat(linked.getValue()).isEqualTo(TEST_STRING3);
		assertThat(linked.isEditable()).isTrue(); // AllowAll -> all editable
	}

	private boolean containsLazy(Map<String, Attribute> attributes) {
		for (Attribute attribute : attributes.values()) {
			if (attribute instanceof LazyAttribute) {
				return true;
			}
		}
		return false;
	}

	private class LazyVectorLayer implements VectorLayer, VectorLayerLazyFeatureConversionSupport {
		VectorLayer delegate;

		public LazyVectorLayer(VectorLayer delegate) {
			this.delegate = delegate;
		}

		public boolean useLazyFeatureConversion() {
			return true;
		}

		public boolean isCreateCapable() {
			return delegate.isCreateCapable();
		}

		public boolean isUpdateCapable() {
			return delegate.isUpdateCapable();
		}

		public boolean isDeleteCapable() {
			return delegate.isDeleteCapable();
		}

		public FeatureModel getFeatureModel() {
			return delegate.getFeatureModel();
		}

		public Object create(Object feature) throws LayerException {
			return delegate.create(feature);
		}

		public Object saveOrUpdate(Object feature) throws LayerException {
			return delegate.saveOrUpdate(feature);
		}

		public Object read(String featureId) throws LayerException {
			return delegate.read(featureId);
		}

		public void delete(String featureId) throws LayerException {
			delegate.delete(featureId);
		}

		public Iterator<?> getElements(Filter filter, int offset, int maxResultSize) throws LayerException {
			return delegate.getElements(filter, offset, maxResultSize);
		}

		public Envelope getBounds(Filter filter) throws LayerException {
			return delegate.getBounds(filter);
		}

		public Envelope getBounds() throws LayerException {
			return delegate.getBounds();
		}

		public VectorLayerInfo getLayerInfo() {
			return delegate.getLayerInfo();
		}

		public CoordinateReferenceSystem getCrs() {
			return delegate.getCrs();
		}

		public String getId() {
			return delegate.getId();
		}
	}
}

