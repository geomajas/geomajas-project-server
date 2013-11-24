package org.geomajas.plugin.staticsecurity.ldap;

import org.geomajas.plugin.staticsecurity.security.dto.AbstractUserFilter;
import org.geomajas.plugin.staticsecurity.security.dto.UserFilterVisitor;


public class CustomUserFilter extends AbstractUserFilter {
	
	private String searchText;

	public CustomUserFilter(String searchText) {
		this.searchText = searchText;
	}


	@Override
	public Object accept(UserFilterVisitor filterVisitor, Object extraData) {
		return filterVisitor.visit(this, extraData);
	}

	
	public String getSearchText() {
		return searchText;
	}

	
	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}
	
	

}
