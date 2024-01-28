package org.milton.elastic.controller;

import java.util.List;

import org.milton.elastic.model.Contact;
import org.milton.elastic.request.ContactRequest;
import org.milton.elastic.response.BulkInsertQueryResponse;
import org.milton.elastic.response.BulkUpdateQueryResponse;
import org.milton.elastic.response.CreateIndexQueryResponse;
import org.milton.elastic.response.DeleteQueryResponse;
import org.milton.elastic.response.ESSearchResponse;
import org.milton.elastic.response.ExistQueryResponse;
import org.milton.elastic.service.ContactService;
import org.milton.elastic.utils.PaginationInfo;
import org.milton.elastic.utils.SearchInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author MILTON
 *
 */
@RestController
@RequestMapping("contacts")
public class ContactController {

	@Autowired
	private ContactService contactService;
	
	@PostMapping("/create-index/{indexName}")
	public ResponseEntity<Boolean> createIndex(@PathVariable String indexName) {
		CreateIndexQueryResponse createIndexQueryResponse = contactService.createIndex(indexName);
		return new ResponseEntity<>(createIndexQueryResponse.isSuccess(), HttpStatus.OK);
	}

	@GetMapping("/index-exists/{indexName}")
	public ResponseEntity<ExistQueryResponse> indexExists(@PathVariable String indexName) {
		ExistQueryResponse existQueryResponse = contactService.indexExists(indexName);
		return new ResponseEntity<>(existQueryResponse, HttpStatus.OK);
	}
	
	@PostMapping("/create-contact/{indexName}")
	public ResponseEntity<BulkInsertQueryResponse> createContact(@PathVariable String indexName, @RequestBody ContactRequest request){
		BulkInsertQueryResponse bulkInsertQueryResponse = contactService.createContact(indexName, request);
		return new ResponseEntity<>(bulkInsertQueryResponse, HttpStatus.OK);
	}
	
	@PostMapping("/update-contact/{indexName}")
	public ResponseEntity<BulkUpdateQueryResponse> updateContact(@PathVariable String indexName, @RequestBody ContactRequest request){
		BulkUpdateQueryResponse bulkUpdateQueryResponse = contactService.updateContact(indexName, request);
		return new ResponseEntity<>(bulkUpdateQueryResponse, HttpStatus.OK);
	}
	
	@DeleteMapping("delete-by-name/{indexName}/{id}")
	public ResponseEntity<DeleteQueryResponse> deleteIndexByName(@PathVariable String indexName, @PathVariable String indexId){
		DeleteQueryResponse deleteQueryResponse = contactService.deleteByIndexId(indexName, indexId);
		return new ResponseEntity<>(deleteQueryResponse, HttpStatus.OK);
	}
	
	@GetMapping("/search/{indexName}")
	public ResponseEntity<ESSearchResponse<Contact>> search(@PathVariable String indexName, 
			@RequestParam(name = "search") String searchText,
			@RequestParam(name = "page", defaultValue = "1") int page, 
			@RequestParam(name = "size", defaultValue = "20") int pageSize){

		
		
		PaginationInfo paginationInfo = PaginationInfo.builder().pageNo(page).pageSize(pageSize).build();
		SearchInfo searchInfo = new SearchInfo();
		searchInfo.setSearchFields(List.of("first_name", "last_name", "email"));
		if(searchText !=null) {
			searchInfo.setSearchText(searchText);
		}
		
		
		ESSearchResponse<Contact> searchResponse = contactService.search(indexName, searchInfo, paginationInfo, Contact.class);
		return new ResponseEntity<> (searchResponse, HttpStatus.OK);
	}
	
}
