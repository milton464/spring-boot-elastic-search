package org.milton.elastic.response;

import org.milton.elastic.utils.Action;

import lombok.Getter;
import lombok.Setter;
/**
 * @author MILTON
 */
@Getter
@Setter
public class CountQueryResponse extends ElasticQueryResponse {

	private long totalCount;

	public CountQueryResponse() {
		super(Action.COUNT);
	}
}
