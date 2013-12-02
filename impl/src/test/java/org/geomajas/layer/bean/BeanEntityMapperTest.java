package org.geomajas.layer.bean;

import junit.framework.Assert;

import org.geomajas.layer.LayerException;
import org.geomajas.layer.bean.BeanEntityMapper.BeanEntity;
import org.geomajas.layer.entity.Entity;
import org.junit.Test;


public class BeanEntityMapperTest {
	@Test
	public void testAsEntity() throws LayerException {
		BeanEntityMapper mapper = new BeanEntityMapper();
		FeatureBean bean = new FeatureBean();
		bean.setId(3L);
		ManyToOneAttributeBean many = new ManyToOneAttributeBean();
		many.setId(4L);
		bean.setManyToOneAttr(many);
		Entity entity = mapper.asEntity(bean);
		Assert.assertEquals(new Long(3L), entity.getId("id"));
		Assert.assertEquals(new Long(4L), entity.getChild("manyToOneAttr").getId("id"));
	}
	
	@Test
	public void testFindOrCreateEntity() throws LayerException {
		BeanEntityMapper mapper = new BeanEntityMapper();
		Entity entity = mapper.findOrCreateEntity("org.geomajas.layer.bean.FeatureBean", null);
		entity.setAttribute("longAttr", 6L);
		Object object = ((BeanEntity)entity).getBean(); 
		Assert.assertTrue(object instanceof FeatureBean);
		Assert.assertEquals(new Long(6L), ((FeatureBean)object).getLongAttr());
	}
}
