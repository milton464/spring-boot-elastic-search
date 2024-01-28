package org.milton.elastic.response;

import org.milton.elastic.utils.Action;

import lombok.Getter;
import lombok.Setter;
/**
 * @author MILTON
 */
@Setter
@Getter
public class DeleteQueryResponse extends ElasticQueryResponse {

	private long totalDeleted;

	public DeleteQueryResponse() {
		super(Action.DELETE);
	}
}
