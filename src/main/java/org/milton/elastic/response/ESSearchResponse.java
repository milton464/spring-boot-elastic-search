package org.milton.elastic.response;

import java.util.Collections;
import java.util.List;

import org.milton.elastic.utils.Action;

import lombok.Getter;
import lombok.Setter;
/**
 * @author MILTON
 */
@Getter
@Setter
public class ESSearchResponse <T> extends ElasticQueryResponse{

	private List<T> dataList;
 	
	private long total;
	
	private int nextPage;
	
	private int previousPage;
	
	private int currentPage;
	
	private int pageSize;
	
	public ESSearchResponse() {
		super(Action.SEARCH);
	}
	
	
	public int getTotalPages() {
		return (int) Math.ceil((float) total / pageSize);
	}
	
	public List<T> getDataList() {
		return dataList == null ? Collections.emptyList() : dataList;
	}
	
}
