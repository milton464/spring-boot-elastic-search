package org.milton.elastic.response;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * @author MILTON
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ESUpdateRequest {

	private String indexName;

	private String indexPK;
	
	private Object data;

	private BigInteger priority;
}
