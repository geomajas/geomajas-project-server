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
@Table(name = "nestedOne")
public class NestedOne {
	
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@ManyToOne(cascade = { CascadeType.ALL })
	private NestedOneInOne oneInOne;

	
	@OneToMany(cascade = { CascadeType.ALL })
	private List<NestedManyInOne> manyInOne = new ArrayList<NestedManyInOne>();

	@Column(name = "textAttr")
	private String textAttr;

	
	public Long getId() {
		return id;
	}


	
	public void setId(Long id) {
		this.id = id;
	}


	
	public NestedOneInOne getOneInOne() {
		return oneInOne;
	}


	
	public void setOneInOne(NestedOneInOne oneInOne) {
		this.oneInOne = oneInOne;
	}


	
	public List<NestedManyInOne> getManyInOne() {
		return manyInOne;
	}


	
	public void setManyInOne(List<NestedManyInOne> manyInOne) {
		this.manyInOne = manyInOne;
	}



	
	public String getTextAttr() {
		return textAttr;
	}



	
	public void setTextAttr(String textAttr) {
		this.textAttr = textAttr;
	}



}
