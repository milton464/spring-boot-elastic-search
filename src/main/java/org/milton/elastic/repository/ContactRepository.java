package org.milton.elastic.repository;

import java.util.List;

import org.milton.elastic.model.Contact;
import org.milton.elastic.response.BulkInsertQueryResponse;
import org.milton.elastic.response.BulkUpdateQueryResponse;
import org.milton.elastic.response.CreateIndexQueryResponse;
import org.milton.elastic.response.DeleteQueryResponse;
import org.milton.elastic.response.ESSearchResponse;
import org.milton.elastic.response.ESUpdateRequest;
import org.milton.elastic.response.ExistQueryResponse;
import org.milton.elastic.service.ESAddRequest;
import org.milton.elastic.service.ESHelper;
import org.milton.elastic.utils.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;

/**
 * @author MILTON
 */
@Repository
public class ContactRepository {

	@Autowired
	private ESHelper esHelper;
	
	
	public CreateIndexQueryResponse createIndex(String indexName) {
		return esHelper.createIndex(indexName);
	}

	public ExistQueryResponse indexExists(String indexName) {
		return esHelper.indexExists(indexName);
	}

	public DeleteQueryResponse deleteByIndexId(String indexName, String indexId) {
		return esHelper.deleteByIndexId(indexName, indexId);
	}

	public DeleteQueryResponse deleteByQuery(String indexName, Query query) {
		return esHelper.deleteByQuery(indexName, query);
	}

	public BulkInsertQueryResponse createContact(List<ESAddRequest> requests) {
		return esHelper.bulkInsert(requests);
	}

	public BulkUpdateQueryResponse updateContact(List<ESUpdateRequest> requests) {
		return esHelper.bulkUpsert(requests);
	}

	public ESSearchResponse<Contact> search(String indexName, Query query, SortOptions sorts,PaginationInfo paginationInfo, Class<Contact> clazz) {
		return esHelper.search(indexName, query, sorts, paginationInfo, clazz);
	}

	
}
