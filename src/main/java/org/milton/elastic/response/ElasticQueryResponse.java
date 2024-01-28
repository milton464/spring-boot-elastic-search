package org.milton.elastic.response;

import org.milton.elastic.utils.Action;

import lombok.Getter;
import lombok.Setter;

/**
 * @author MILTON
 */
@Setter
@Getter
public abstract class ElasticQueryResponse {

	private String indexName;

	private boolean success;

	private Action action;

	private String errorMessage;

	protected ElasticQueryResponse(Action action, String indexName) {
		this.action = action;
		this.indexName = indexName;
	}

	protected ElasticQueryResponse(Action action) {
		this.action = action;
	}

}
