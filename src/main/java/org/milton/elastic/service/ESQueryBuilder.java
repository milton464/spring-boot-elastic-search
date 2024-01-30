package org.milton.elastic.service;

import org.milton.elastic.utils.OrderInfo;
import org.milton.elastic.utils.SortOrderEnum;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.SumAggregation;
import co.elastic.clients.elasticsearch._types.aggregations.TermsAggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.JsonData;
/**
 * @author MILTON
 */
public class ESQueryBuilder {
	
	private ESQueryBuilder() {}

	public static SortOptions getOrderBy(OrderInfo orderInfo) {
		if(null == orderInfo) {
			return getOrderBy("id", SortOrder.Desc);
		}
		SortOrder sortOrder = orderInfo.getSortOrder() == SortOrderEnum.ASC ? SortOrder.Asc : SortOrder.Desc;
		return getOrderBy(orderInfo.getSortOrderFieldName(), sortOrder);
	}

	public static SortOptions getOrderBy() {
		return getOrderBy("id", SortOrder.Desc);
	}
	
	public static SortOptions getOrderBy(final String fieldName, final SortOrder order) {
		return SortOptions.of(s -> s.field(f -> f.field(fieldName).order(order)));
	}
	
	/**
	 * Generates a match query for Elasticsearch.
	 *
	 * @param fieldName The field to match against.
	 * @param fieldValue The value to match in the specified field.
	 * @return An Elasticsearch query that matches documents where the specified field contains the given value.
	 */
	public static Query getMatchQuery(String fieldName, String fieldVale) {
		return new Query.Builder().match(m -> m.field(fieldName).query(fieldVale)).build();
	}

	/**
	 * Constructs a wildcard query for a given field with a wildcard pattern.
	 *
	 * @param fieldName The name of the field to search.
	 * @param fieldValue The value to match using a wildcard pattern.
	 * @return A Query object representing the wildcard query.
	 */
	public static Query getWildCardQuery(String fieldName, String fieldVale) {
		return new Query.Builder().wildcard(w -> w.field(fieldName).value("*" + fieldVale + "*").caseInsensitive(true)).build();
	}
	
	/**
	 * Constructs and returns an Elasticsearch match_phrase_prefix query.
	 *
	 * This method generates a match_phrase_prefix query for a given field and value, allowing for
	 * efficient partial matching. The query is configured with a maximum expansion of 1000 terms.
	 *
	 * @param fieldName   The field on which the match_phrase_prefix query is applied.
	 * @param fieldValue  The value to match with a prefix in the specified field.
	 * @return            An Elasticsearch Query object representing the match_phrase_prefix query.
	 */
	public static Query getMatchPrefixQuery(String fieldName, String fieldValue) {
		return new Query.Builder().matchPhrasePrefix(m -> m.field(fieldName).query(fieldValue).maxExpansions(1000)).build();
	}

	public static Query getMatchPhraseQuery(String fieldName, String fieldValue) {
		return new Query.Builder().matchPhrase(m -> m.field(fieldName).query(fieldValue)).build();
	}
	
	
	/**
	 * Constructs and returns a term query for a specific field and value.
	 *
	 * @param fieldName  The name of the field for the term query.
	 * @param fieldValue The value to match in the specified field.
	 * @return A term query for the given field and value.
	 */
	public static Query getTermQuery(String fieldName, String fieldValue) {
		return new Query.Builder().term(t -> t.field(fieldName).value(fieldValue)).build();
	}
	
	/**
	 * Constructs and returns a range query based on the provided field name and range values.
	 *
	 * @param fieldName The name of the field to apply the range query on.
	 * @param fromValue The lower bound value for the range.
	 * @param toValue   The upper bound value for the range.
	 * @return A range query for the specified field and range values.
	 */
	public static Query getRangeQuery(String fieldName, JsonData fromValue, JsonData toValue) {
		return new Query.Builder().range(r->r.field(fieldName).gte(fromValue).lte(toValue)).build();
	}

	/**
	 * Generates a query for retrieving documents where the specified field has a value greater than or equal to the provided value.
	 *
	 * @param fieldName The name of the field to apply the greater than or equal condition.
	 * @param fromValue The inclusive starting value for the range.
	 * @return A Query object representing the condition for the specified range.
	 */
	public static Query getGreaterThanOrEqualQuery(String fieldName, JsonData fromValue) {
		return new Query.Builder().range(r->r.field(fieldName).gte(fromValue)).build();
	}

	
	/**
	 * Constructs a Elasticsearch Query for retrieving documents where the specified field
	 * is less than or equal to the provided value.
	 *
	 * @param fieldName The name of the field to filter on.
	 * @param fromValue The value representing the upper bound for the field.
	 * @return An Elasticsearch Query object for less than or equal to comparison.
	 */
	public static Query getLessThanOrEqualQuery(String fieldName, JsonData fromValue) {
		return new Query.Builder().range(r->r.field(fieldName).lte(fromValue)).build();
	}

	/**
	 * Generates a query for searching documents with a field value greater than the specified value.
	 * 
	 * @param fieldName The name of the field to perform the range query on.
	 * @param fromValue The value to compare against, documents with values greater than this will be retrieved.
	 * @return A Query object representing the range query.
	 */
	public static Query getGreaterThanrQuery(String fieldName, JsonData fromValue) {
		return new Query.Builder().range(r->r.field(fieldName).gt(fromValue)).build();
	}

	/**
	 * Generates a Elasticsearch Query for finding documents where a specified field's value is less than the given value.
	 *
	 * @param fieldName The name of the field to compare.
	 * @param fromValue The value to compare against. Documents with the specified field less than this value will be matched.
	 * @return A Query representing the less than condition for the specified field.
	 */
	public static Query getLessThanQuery(String fieldName, JsonData fromValue) {
		return new Query.Builder().range(r->r.field(fieldName).lt(fromValue)).build();
	}
	
	
	/**
	 * Creates a Terms Aggregation query for a given field name.
	 *
	 * Terms Aggregation is a bucketing type of aggregation that works on fields
	 * with string or numeric data by creating buckets and associating documents
	 * that share the same terms into each bucket.
	 *
	 * @param fieldName The name of the field for which the Terms Aggregation is to be created.
	 * @return A TermsAggregation object configured with the specified field.
	 */
	public static TermsAggregation getTermAggregateQyuery(String fieldName) {
		return new TermsAggregation.Builder().field(fieldName).build();
	}

	/**
	 * Constructs a SumAggregation query for the specified field.
	 *
	 * @param fieldName The name of the field for which the sum aggregation is calculated.
	 * @return SumAggregation object configured for the specified field.
	 */
	public static SumAggregation getSumAggregateQyuery(String fieldName) {
		return new SumAggregation.Builder().field(fieldName).build();
	}
}
