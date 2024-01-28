package org.milton.elastic.utils;

import java.util.ArrayList;
import java.util.List;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import lombok.Getter;
import lombok.Setter;
/**
 * @author MILTON
 */
@Getter
@Setter
public class TextSearchQuery {

	private List<Query> matchQuery;

	private List<Query> wildQuery;

	private List<Query> matchPrefixQuery;

	public TextSearchQuery() {
		this.matchQuery = new ArrayList<>();
		this.wildQuery = new ArrayList<>();
		this.matchPrefixQuery = new ArrayList<>();
	}
}
