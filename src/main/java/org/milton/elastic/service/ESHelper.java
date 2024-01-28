package org.milton.elastic.service;

import java.math.BigInteger;
import java.util.List;

import org.milton.elastic.response.BulkInsertQueryResponse;
import org.milton.elastic.response.BulkUpdateQueryResponse;
import org.milton.elastic.response.CountQueryResponse;
import org.milton.elastic.response.CreateIndexQueryResponse;
import org.milton.elastic.response.DeleteQueryResponse;
import org.milton.elastic.response.ESSearchResponse;
import org.milton.elastic.response.ESUpdateRequest;
import org.milton.elastic.response.ExistQueryResponse;
import org.milton.elastic.utils.PaginationInfo;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;

public interface ESHelper {

	CreateIndexQueryResponse createIndex(String indexName);

	ExistQueryResponse indexExists(String indexName);
	
	CountQueryResponse exist(String indexName, BigInteger id);

	BulkInsertQueryResponse bulkInsert(List<ESAddRequest> requests);
	
	BulkUpdateQueryResponse bulkUpsert(List<ESUpdateRequest> requests);
	
	DeleteQueryResponse deleteByQuery(String indexName, Query query);

	DeleteQueryResponse deleteByIndexId(String indexName, String id);
	
	<T> ESSearchResponse<T> search(String indexName, Query query, Class<T> clazz);

	<T> ESSearchResponse<T> search(String indexName, Query query, SortOptions sorts, Class<T> clazz);

	<T> ESSearchResponse<T> search(String indexName, Query query, SortOptions sorts, PaginationInfo paginationInfo, Class<T> clazz);
	
}
