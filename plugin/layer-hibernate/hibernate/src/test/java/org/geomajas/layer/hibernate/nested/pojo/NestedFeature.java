package org.geomajas.layer.hibernate.nested.pojo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Geometry;

@Entity
@Table(name = "nestedParentFeature")
public class NestedFeature {

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@ManyToOne(cascade = { CascadeType.ALL })
	private NestedOne one;

	@OneToMany(cascade = { CascadeType.ALL })
	private List<NestedMany> many = new ArrayList<NestedMany>();

	@Type(type = "org.hibernatespatial.GeometryUserType")
	private Geometry geometry;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public NestedOne getOne() {
		return one;
	}

	public void setOne(NestedOne one) {
		this.one = one;
	}

	public List<NestedMany> getMany() {
		return many;
	}

	public void setMany(List<NestedMany> many) {
		this.many = many;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}
	
	public void updateOne(String oldText, String newText){
		if(getOne().getTextAttr().equals(oldText)){
			getOne().setTextAttr(newText);
		} else {
			
		}
	}

	public static NestedFeature getDefaultInstance1() {
		NestedFeature feature = new NestedFeature();
		NestedMany many1 = new NestedMany();
		NestedMany many2 = new NestedMany();
		NestedOne one = new NestedOne();
		feature.getMany().add(many1);
		feature.getMany().add(many2);
		feature.setOne(one);

		NestedManyInMany manyInMany1 = new NestedManyInMany();
		NestedManyInMany manyInMany2 = new NestedManyInMany();
		many1.getManyInMany().add(manyInMany1);
		many1.getManyInMany().add(manyInMany2);

		NestedManyInOne manyInOne1 = new NestedManyInOne();
		NestedManyInOne manyInOne2 = new NestedManyInOne();
		one.getManyInOne().add(manyInOne1);
		one.getManyInOne().add(manyInOne2);

		NestedOneInOne oneInOne = new NestedOneInOne();
		one.setOneInOne(oneInOne);

		NestedOneInMany oneInMany = new NestedOneInMany();
		many2.setOneInMany(oneInMany);
		return feature;

	}

}
