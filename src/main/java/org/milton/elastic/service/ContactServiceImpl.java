package org.milton.elastic.service;

import java.util.ArrayList;
import java.util.List;

import org.milton.elastic.model.Contact;
import org.milton.elastic.repository.ContactRepository;
import org.milton.elastic.request.ContactRequest;
import org.milton.elastic.response.BulkInsertQueryResponse;
import org.milton.elastic.response.BulkUpdateQueryResponse;
import org.milton.elastic.response.CreateIndexQueryResponse;
import org.milton.elastic.response.DeleteQueryResponse;
import org.milton.elastic.response.ESSearchResponse;
import org.milton.elastic.response.ESUpdateRequest;
import org.milton.elastic.response.ExistQueryResponse;
import org.milton.elastic.utils.PaginationInfo;
import org.milton.elastic.utils.SearchInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
/**
 * @author MILTON
 */
@Service
public class ContactServiceImpl implements ContactService{

	@Autowired
	private ContactRepository contactRepository;
	
	@Override
	public CreateIndexQueryResponse createIndex(String indexName) {
		return contactRepository.createIndex(indexName);
	}

	@Override
	public ExistQueryResponse indexExists(String indexName) {
		return contactRepository.indexExists(indexName);
	}

	@Override
	public BulkInsertQueryResponse createContact(String indexName, ContactRequest request) {
		List<ESAddRequest> requests = new ArrayList<>();
		
		ESAddRequest esAddRequest = ESAddRequest.builder()
				.indexName(indexName)
				.data(request)
				.build();
		
		requests.add(esAddRequest);
		return contactRepository.createContact(requests);
	}

	@Override
	public BulkUpdateQueryResponse updateContact(String indexName, ContactRequest request) {
		List<ESUpdateRequest> requests = new ArrayList<>();
		ESUpdateRequest esUpdateRequest = ESUpdateRequest.builder()
				.indexName(indexName)
				.data(request)
				.build();
		requests.add(esUpdateRequest);
		return contactRepository.updateContact(requests);
	}
	
	@Override
	public DeleteQueryResponse deleteByIndexId(String indexName, String indexId) {
		return contactRepository.deleteByIndexId(indexName, indexId);
	}
	
	@Override
	public ESSearchResponse<Contact> search(String indexName, SearchInfo searchInfo, PaginationInfo paginationInfo, Class<Contact> classz) {
		
		Query query = buildSearchQuery(searchInfo);
		
		return contactRepository.search(indexName, query, ESQueryBuilder.getOrderBy(), paginationInfo, classz);
	}
	
	private Query buildSearchQuery(SearchInfo searchInfo) {
		ContactSearchHelper contactSearchHelper = new ContactSearchHelper(searchInfo);
		contactSearchHelper.buildTextSearchQuery();
		List<Query> matchPrefixQueryList = contactSearchHelper.getTextSearchQuery().getMatchPrefixQuery();
		
		return new Query.Builder()
				.bool(b-> b
				.should(matchPrefixQueryList)
				.minimumShouldMatch("1"))
				.build();
	}
}
