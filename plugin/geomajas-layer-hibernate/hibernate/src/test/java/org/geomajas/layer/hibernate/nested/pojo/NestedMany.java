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

@Entity
@Table(name = "nestedMany")
public class NestedMany {

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@OneToMany(cascade = { CascadeType.ALL })
	private List<NestedManyInMany> manyInMany = new ArrayList<NestedManyInMany>();

	@ManyToOne(cascade = { CascadeType.ALL })
	private NestedOneInMany oneInMany;

	@Column(name = "textAttr")
	private String textAttr;
	
	
	public String getTextAttr() {
		return textAttr;
	}


	
	public void setTextAttr(String textAttr) {
		this.textAttr = textAttr;
	}


	public Long getId() {
		return id;
	}

	
	public void setId(Long id) {
		this.id = id;
	}

	
	public List<NestedManyInMany> getManyInMany() {
		return manyInMany;
	}

	
	public void setManyInMany(List<NestedManyInMany> manyInMany) {
		this.manyInMany = manyInMany;
	}

	
	public NestedOneInMany getOneInMany() {
		return oneInMany;
	}

	
	public void setOneInMany(NestedOneInMany oneInMany) {
		this.oneInMany = oneInMany;
	}


}
