package org.milton.elastic.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.milton.elastic.utils.DateRangeInfo;
import org.milton.elastic.utils.SearchInfo;
import org.milton.elastic.utils.TextSearchQuery;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.JsonData;
import io.micrometer.common.util.StringUtils;
import lombok.Getter;
import lombok.Setter;
/**
 * @author MILTON
 */
@Getter
@Setter
public abstract class GenericESQueryBuilder {

	private TextSearchQuery textSearchQuery;

	private SearchInfo searchInfo;

	
	private List<Query> matchQueryList;
	
	private List<Query> wildQueryList;
	
	private List<Query> matchPrefixQueryList;
	
	private List<Query> filterQueryList;
	
	private Map<String, Object> filterMap;
	
	private DateRangeInfo dateRangeInfo;
	
	private List<Query> dateRangeQueryList;
	
	
	public GenericESQueryBuilder(SearchInfo searchInfo) {
		this.searchInfo = searchInfo;
	}

	/**
	 * Builds a text search query based on the provided search information.
	 * The generated query includes match, wildcard, and match-prefix queries
	 * for each specified search field.
	 */
	public void buildTextSearchQuery() {
		textSearchQuery = new TextSearchQuery();
		
		if (searchInfo != null && StringUtils.isNotBlank(searchInfo.getSearchText())
				&& searchInfo.getSearchFields() != null && !searchInfo.getSearchFields().isEmpty()) {
			
			matchQueryList = new ArrayList<>(searchInfo.getSearchFields().size());
			wildQueryList = new ArrayList<>(searchInfo.getSearchFields().size());
			matchPrefixQueryList = new ArrayList<>(searchInfo.getSearchFields().size());

			searchInfo.getSearchFields().forEach(field-> matchQueryList.add(ESQueryBuilder.getMatchQuery(field, searchInfo.getSearchText())));
			searchInfo.getSearchFields().forEach(field-> wildQueryList.add(ESQueryBuilder.getWildCardQuery(field, searchInfo.getSearchText())));
			searchInfo.getSearchFields().forEach(field-> matchPrefixQueryList.add(ESQueryBuilder.getMatchPrefixQuery(field, searchInfo.getSearchText())));
			
			textSearchQuery.setMatchQuery(matchQueryList);
			textSearchQuery.setWildQuery(wildQueryList);
			textSearchQuery.setMatchPrefixQuery(matchPrefixQueryList);
		}
	}
	
	public void buildFilterQuery() {
		if (filterMap != null && !filterMap.isEmpty()) {
			matchPrefixQueryList = new ArrayList<>(filterMap.size());
			matchQueryList = new ArrayList<>(filterMap.size());
			
			for (Map.Entry<String, Object> mapEntry : filterMap.entrySet()) {
				if(mapEntry.getValue() instanceof String) {
					matchPrefixQueryList.add(ESQueryBuilder.getMatchPrefixQuery(mapEntry.getKey(), String.valueOf(mapEntry.getValue())));
				}else {
					matchQueryList.add(ESQueryBuilder.getMatchQuery(mapEntry.getKey(), String.valueOf(mapEntry.getValue())));
				}
			}
		}
	}
	
	public void buildDateRangeQuery() {
		if (dateRangeInfo != null && dateRangeInfo.getFromDate() != null && dateRangeInfo.getToDate() != null
				&& dateRangeInfo.getDateRangeFields() != null && !dateRangeInfo.getDateRangeFields().isEmpty()) {

			dateRangeInfo.getDateRangeFields().forEach(field -> 
						dateRangeQueryList.add(ESQueryBuilder.getRangeQuery(field, JsonData.of(dateRangeInfo.getFromDate()), 
								JsonData.of(dateRangeInfo.getToDate()))));
		}
	}
	
}
