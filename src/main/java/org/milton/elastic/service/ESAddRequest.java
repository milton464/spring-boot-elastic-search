package org.milton.elastic.service;

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
public class ESAddRequest {

	private String indexName;

	private String indexPK;

	private Object data;
}
