package org.milton.elastic.service;

import org.milton.elastic.model.Contact;
import org.milton.elastic.request.ContactRequest;
import org.milton.elastic.response.BulkInsertQueryResponse;
import org.milton.elastic.response.BulkUpdateQueryResponse;
import org.milton.elastic.response.CreateIndexQueryResponse;
import org.milton.elastic.response.DeleteQueryResponse;
import org.milton.elastic.response.ESSearchResponse;
import org.milton.elastic.response.ExistQueryResponse;
import org.milton.elastic.utils.PaginationInfo;
import org.milton.elastic.utils.SearchInfo;

public interface ContactService {

	CreateIndexQueryResponse createIndex(String indexName);

	ExistQueryResponse indexExists(String indexName);

	BulkInsertQueryResponse createContact(String indexName, ContactRequest request);
	
	BulkUpdateQueryResponse updateContact(String indexName, ContactRequest request);

	DeleteQueryResponse deleteByIndexId(String indexName, String indexId);

	ESSearchResponse<Contact> search(String indexName, SearchInfo searchInfo, PaginationInfo paginationInfo, Class<Contact> clazz);


}
