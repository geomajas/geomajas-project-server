package org.geomajas.internal.layer.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.apache.commons.beanutils.BeanUtils;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.bean.FeatureBean;
import org.geomajas.layer.bean.ManyToOneAttributeBean;
import org.geomajas.layer.bean.OneToManyAttributeBean;
import org.geomajas.layer.entity.Entity;
import org.geomajas.layer.entity.EntityCollection;
import org.geomajas.layer.entity.EntityMapper;
import org.geomajas.layer.entity.EntityAttributeService;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.attribute.AssociationValue;
import org.geomajas.layer.feature.attribute.DoubleAttribute;
import org.geomajas.layer.feature.attribute.FloatAttribute;
import org.geomajas.layer.feature.attribute.LongAttribute;
import org.geomajas.layer.feature.attribute.ManyToOneAttribute;
import org.geomajas.layer.feature.attribute.PrimitiveAttribute;
import org.geomajas.layer.feature.attribute.ShortAttribute;
import org.geomajas.layer.feature.attribute.StringAttribute;
import org.geomajas.layer.feature.attribute.UrlAttribute;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.filter.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * <p>
 * Test class for {@link org.geomajas.service.FilterService} service.
 * </p>
 * 
 * @author Jan De Moerloose
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/testdata/layerBeans.xml","/org/geomajas/internal/layer/entity/layerNonEditableBeans.xml" })
public class EntityAttributeServiceTest {

	@Autowired
	@Qualifier("beans")
	private VectorLayer layerBeans;

	@Autowired
	@Qualifier("layerNonEditableBeans")
	private VectorLayer layerNonEditableBeans;

	@Autowired
	private EntityAttributeService service;

	@Test
	public void testNoAttributes() throws LayerException {
		Map<String, Attribute<?>> attributes = new HashMap<String, Attribute<?>>();
		FeatureBean bean = new FeatureBean();
		service.setAttributes(bean, layerBeans.getLayerInfo().getFeatureInfo(), new DummyMapper(), attributes);
	}

	@Test
	public void testPrimitiveAttributes() throws LayerException {
		Map<String, Attribute<?>> attributes = new HashMap<String, Attribute<?>>();
		FeatureBean bean = new FeatureBean();
		attributes.put("stringAttr", new StringAttribute("s1"));
		attributes.put("doubleAttr", new DoubleAttribute(1.23));
		attributes.put("longAttr", new LongAttribute(12L));
		attributes.put("floatAttr", new FloatAttribute(1.67F));
		attributes.put("shortAttr", new ShortAttribute((short) 6));
		attributes.put("urlAttr", new UrlAttribute("http://haha"));
		service.setAttributes(bean, layerBeans.getLayerInfo().getFeatureInfo(), new DummyMapper(), attributes);
		Assert.assertEquals("s1", bean.getStringAttr());
		Assert.assertEquals(1.23, bean.getDoubleAttr(), 0.0001);
		Assert.assertEquals(12L, bean.getLongAttr().longValue());
		Assert.assertEquals(1.67F, bean.getFloatAttr(), 0.0001);
		Assert.assertEquals(6, bean.getShortAttr().shortValue());
		Assert.assertEquals("http://haha", bean.getUrlAttr());
	}

	@Test
	public void testManyToOneAttribute() throws LayerException {
		Map<String, Attribute<?>> attributes = new HashMap<String, Attribute<?>>();
		FeatureBean bean = new FeatureBean();
		AssociationValue value = new AssociationValue(new LongAttribute(),new HashMap<String, Attribute<?>>(), false);
		value.getAllAttributes().put("stringAttr",new StringAttribute("mto"));
		attributes.put("manyToOneAttr", new ManyToOneAttribute(value));
		service.setAttributes(bean, layerBeans.getLayerInfo().getFeatureInfo(), new DummyMapper(), attributes);
		Assert.assertNotNull(bean.getManyToOneAttr());
		Assert.assertEquals("mto",bean.getManyToOneAttr().getStringAttr());
		// test replacing
		ManyToOneAttributeBean original = new ManyToOneAttributeBean();
		original.setId(5L);
		original.setStringAttr("original");
		bean.setManyToOneAttr(original);
		service.setAttributes(bean, layerBeans.getLayerInfo().getFeatureInfo(), new DummyMapper(), attributes);
		Assert.assertNotNull(bean.getManyToOneAttr());
		// should be replaced
		Assert.assertNotSame(original,bean.getManyToOneAttr());
		Assert.assertEquals(null,bean.getManyToOneAttr().getId());
	}
	
	@Test
	public void testNonEditable() throws LayerException {
		Map<String, Attribute<?>> attributes = new HashMap<String, Attribute<?>>();
		FeatureBean bean = new FeatureBean();
		AssociationValue value = new AssociationValue(new LongAttribute(),new HashMap<String, Attribute<?>>(), false);
		value.getAllAttributes().put("stringAttr",new StringAttribute("mto"));
		attributes.put("manyToOneAttr", new ManyToOneAttribute(value));
		attributes.put("stringAttr", new StringAttribute("top"));
		service.setAttributes(bean, layerNonEditableBeans.getLayerInfo().getFeatureInfo(), new DummyMapper(), attributes);
		Assert.assertNotNull(bean.getManyToOneAttr());
		Assert.assertNull(bean.getManyToOneAttr().getStringAttr());
		Assert.assertNull(bean.getStringAttr());
	}
	
	@Test
	public void testGetters() throws LayerException {
		Iterator it = layerBeans.getElements(Filter.INCLUDE, 0, 0);
		while(it.hasNext()){
			FeatureBean bean = (FeatureBean)it.next();
			Assert.assertEquals(bean.getStringAttr(), service.getAttribute(bean, layerBeans.getLayerInfo()
					.getFeatureInfo(), new DummyMapper(), "stringAttr").getValue());
			Assert.assertEquals(bean.getCurrencyAttr(), service.getAttribute(bean, layerBeans.getLayerInfo()
					.getFeatureInfo(), new DummyMapper(), "currencyAttr").getValue());
			Assert.assertEquals(bean.getImageUrlAttr(), service.getAttribute(bean, layerBeans.getLayerInfo()
					.getFeatureInfo(), new DummyMapper(), "imageUrlAttr").getValue());
			if(bean.getManyToOneAttr() != null) {
				Assert.assertEquals(bean.getManyToOneAttr().getId(), service.getAttribute(bean, layerBeans.getLayerInfo()
						.getFeatureInfo(), new DummyMapper(), "manyToOneAttr/@id").getValue());
			}
		}
	}
	
	

	class DummyMapper implements EntityMapper {

		public Entity asEntity(Object object) throws LayerException {
			return new FeatureEntity((FeatureBean) object);
		}

		public Entity findOrCreateEntity(String dataSourceName, Object id) throws LayerException {
			if (dataSourceName.equals(FeatureBean.class.getName())) {
				return new FeatureEntity(new FeatureBean());
			}
			if (dataSourceName.equals(ManyToOneAttributeBean.class.getName())) {
				return new ManyToOneEntity(new ManyToOneAttributeBean());
			}
			throw new LayerException();
		}

	}

	class FeatureEntity implements Entity {

		private FeatureBean bean;

		public FeatureEntity(FeatureBean bean) {
			this.bean = bean;
		}

		public Object getId(String name) throws LayerException {
			return bean.getId();
		}

		public Entity getChild(String name) throws LayerException {
			if(bean.getManyToOneAttr()!=null){
				return new ManyToOneEntity(bean.getManyToOneAttr());
			} else {
				return null;
			}
		}

		public void setChild(String name, Entity entity) throws LayerException {
			if (entity != null) {
				bean.setManyToOneAttr(((ManyToOneEntity) entity).getBean());
			}
		}

		public EntityCollection getChildCollection(String name) throws LayerException {
			return new OneToManyCollection(bean.getOneToManyAttr());
		}

		public void setAttribute(String name, Object value) throws LayerException {
			try {
				BeanUtils.setProperty(bean, name, value);
			} catch (Exception e) {
				throw new LayerException();
			}
		}

		public Object getAttribute(String name) throws LayerException {
			try {
				return BeanUtils.getProperty(bean, name);
			} catch (Exception e) {
				throw new LayerException();
			}
		}

	}

	public class ManyToOneEntity implements Entity {

		private ManyToOneAttributeBean bean;

		public ManyToOneEntity(ManyToOneAttributeBean bean) {
			this.bean = bean;
		}

		public Object getId(String name) throws LayerException {
			return bean.getId();
		}

		public Entity getChild(String name) throws LayerException {
			if(bean.getManyToOneAttr()!=null){
				return new ManyToOneEntity(bean.getManyToOneAttr());
			} else {
				return null;
			}
		}

		public void setChild(String name, Entity entity) throws LayerException {
			if (entity != null) {
				bean.setManyToOneAttr(((ManyToOneEntity) entity).getBean());
			}
		}

		public EntityCollection getChildCollection(String name) throws LayerException {
			return new OneToManyCollection(bean.getOneToManyAttr());
		}

		public void setAttribute(String name, Object value) throws LayerException {
			try {
				BeanUtils.setProperty(bean, name, value);
			} catch (Exception e) {
				throw new LayerException();
			}
		}

		public ManyToOneAttributeBean getBean() {
			return bean;
		}

		public Object getAttribute(String name) throws LayerException {
			try {
				return BeanUtils.getProperty(bean, name);
			} catch (Exception e) {
				throw new LayerException();
			}
		}

	}

	public class OneToManyCollection implements EntityCollection {

		private List<OneToManyAttributeBean> beans;

		public OneToManyCollection(List<OneToManyAttributeBean> beans) {
			this.beans = beans;
		}

		public Iterator<Entity> iterator() {
			List<Entity> entities = new ArrayList<Entity>();
			for (OneToManyAttributeBean bean : beans) {
				entities.add(new OneToManyEntity(bean));
			}
			return entities.iterator();
		}

		public void addEntity(Entity entity) throws LayerException {
			beans.add(((OneToManyEntity) entity).getBean());
		}

		public void removeEntity(Entity entity) throws LayerException {
			beans.remove(((OneToManyEntity) entity).getBean());
		}

	}

	public class OneToManyEntity implements Entity {

		private OneToManyAttributeBean bean;

		public OneToManyEntity(OneToManyAttributeBean bean) {
			this.bean = bean;
		}

		public OneToManyAttributeBean getBean() {
			return bean;
		}

		public Object getId(String name) throws LayerException {
			return bean.getId();
		}

		public Entity getChild(String name) throws LayerException {
			if(bean.getManyToOneAttr()!=null){
				return new ManyToOneEntity(bean.getManyToOneAttr());
			} else {
				return null;
			}
		}

		public void setChild(String name, Entity entity) throws LayerException {
			if (entity != null) {
				bean.setManyToOneAttr(((ManyToOneEntity) entity).getBean());
			}
		}

		public EntityCollection getChildCollection(String name) throws LayerException {
			return new OneToManyCollection(bean.getOneToManyAttr());
		}

		public void setAttribute(String name, Object value) throws LayerException {
			try {
				BeanUtils.setProperty(bean, name, value);
			} catch (Exception e) {
				throw new LayerException();
			}
		}

		public Object getAttribute(String name) throws LayerException {
			try {
				return BeanUtils.getProperty(bean, name);
			} catch (Exception e) {
				throw new LayerException();
			}
		}
	}

}
