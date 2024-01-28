package org.milton.elastic.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.milton.elastic.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
/**
 * @author MILTON
 */
@Repository
public class ProductRepository {

	private static final String PRODUCTS = "products";
	
	@Autowired
	private ElasticsearchClient elasticsearchClient;

	public String createOrUpdateDocument(Product product) throws IOException{
		IndexResponse indexResponse = elasticsearchClient
				.index(i -> i.index(PRODUCTS).id(product.getId()).document("product"));
		
		Map<String, String> responseMessages = Map.of("created", " Document has created", "updated",
				"document has updated");

		return responseMessages.getOrDefault(indexResponse.result().name(), "Error occured");
	}

	
	public Product getDocument(String productId) throws IOException{
		return elasticsearchClient.get(g->g.index(PRODUCTS).id(productId), Product.class).source();
	}
	
	public String deleteDocument(String productId) throws IOException{
		DeleteRequest deleteRequest = DeleteRequest.of(d-> d.index(PRODUCTS).id(productId));
		DeleteResponse deleteResponse = elasticsearchClient.delete(deleteRequest);
		return new StringBuilder().append(deleteResponse.result().name().equals("NOT_FOUND")? "Document not found with id: "+productId : "document is deleted").toString();
	}
	
	
	public List<Product> findAll() throws IOException{
		SearchRequest searchRequest = SearchRequest.of(s->s.index(PRODUCTS).query(Query.of(null)));
		SearchResponse<Product>  searchResponse = elasticsearchClient.search(searchRequest, Product.class);
		List<Product> dataList = new ArrayList<>();
		
		searchResponse.hits().hits().stream().forEach(p -> {
			dataList.add(p.source());
		});
		
		return dataList;
	}
	
	public String bulkSave(List<Product> products) throws IOException{
		BulkRequest.Builder bulkRequest = new BulkRequest.Builder();
		
		products.stream().forEach(product-> bulkRequest
				.operations(operation-> operation
						.index(i-> i
								.index(PRODUCTS)
								.id(product.getId())
								.document(product))));
		
		BulkResponse bulkResponse = elasticsearchClient.bulk(bulkRequest.build());
		if (bulkResponse.errors()) {
			return new StringBuilder().append("Bulk has errors").toString();
		} else {
			return new StringBuffer().append("Bulk addedd successfully!").toString();
		}
		
	}
}