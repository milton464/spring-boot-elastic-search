package org.milton.elastic.response;

import java.util.List;
import java.util.Set;

import org.milton.elastic.utils.Action;

import lombok.Getter;
import lombok.Setter;
/**
 * @author MILTON
 */
@Getter
@Setter
public class BulkInsertQueryResponse extends ElasticQueryResponse {

	private List<String> dataList;

	private Set<String> indexIdList;
	
	public BulkInsertQueryResponse() {
		super(Action.INSERT);
	}
}
