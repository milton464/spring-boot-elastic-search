package org.milton.elastic.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
/**
 * @author MILTON
 */
@Getter
@Setter
public class DateRangeInfo {

	private Date fromDate;

	private Date toDate;

	private List<String> dateRangeFields;

	public DateRangeInfo() {
		this.dateRangeFields = new ArrayList<>();
	}
}
