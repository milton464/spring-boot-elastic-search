package org.milton.elastic.response;

import org.milton.elastic.utils.Action;

import lombok.Getter;
import lombok.Setter;

/**
 * @author MILTON
 */
@Setter
@Getter
public class ExistQueryResponse extends ElasticQueryResponse{

	private boolean exist;
	
	public ExistQueryResponse() {
		super(Action.EXIST);
	}
}
