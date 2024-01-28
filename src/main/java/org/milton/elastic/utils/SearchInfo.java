package org.milton.elastic.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * @author MILTON
 */
@Getter
@Setter
@ToString
public class SearchInfo {

	private String searchText;
	
	private List<String> searchFields;
	
	public SearchInfo() {
		this.searchFields = new ArrayList<>();
	}
	
	public List<String> getSearchFields(){
		return searchFields == null ? Collections.emptyList() : searchFields; 
	}
}
