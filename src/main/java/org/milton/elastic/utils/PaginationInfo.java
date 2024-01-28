package org.milton.elastic.utils;

import lombok.Builder;
import lombok.Getter;
/**
 * @author MILTON
 */
@Builder
@Getter
public class PaginationInfo {

	private int pageNo;

	private int pageSize;

}
