package org.milton.elastic.response;

import org.milton.elastic.utils.Action;

import lombok.Setter;

/**
 * 
 * @author MILTON
 *
 */
@Setter
public class CreateIndexQueryResponse extends ElasticQueryResponse {

	public CreateIndexQueryResponse(String indexName) {
		super(Action.CREATE_INDEX, indexName);
	}
}
