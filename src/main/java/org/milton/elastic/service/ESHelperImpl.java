package org.milton.elastic.service;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.milton.elastic.response.BulkInsertQueryResponse;
import org.milton.elastic.response.BulkUpdateQueryResponse;
import org.milton.elastic.response.CountQueryResponse;
import org.milton.elastic.response.CreateIndexQueryResponse;
import org.milton.elastic.response.DeleteQueryResponse;
import org.milton.elastic.response.ESSearchResponse;
import org.milton.elastic.response.ESUpdateRequest;
import org.milton.elastic.response.ExistQueryResponse;
import org.milton.elastic.utils.ESConfig;
import org.milton.elastic.utils.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch._types.Result;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.CountRequest;
import co.elastic.clients.elasticsearch.core.CountResponse;
import co.elastic.clients.elasticsearch.core.DeleteByQueryRequest;
import co.elastic.clients.elasticsearch.core.DeleteByQueryResponse;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author MILTON
 */
@Slf4j
@Service
public class ESHelperImpl implements ESHelper {

	private static final String ERROR = "ERROR:{}";
	
	@Autowired
	private ElasticsearchClient elasticsearchClient;
	
	@Override
	public CreateIndexQueryResponse createIndex(String indexName) {
		CreateIndexQueryResponse response = new CreateIndexQueryResponse(indexName);
		
		try {
			
			CreateIndexRequest createIndexRequest = CreateIndexRequest.of(c -> c.index(indexName).settings(s-> s.numberOfShards(ESConfig.NUMBER_OF_SHARDS+"").numberOfReplicas(ESConfig.NUMBER_OF_SHARD_REPLICAS+"")));
			CreateIndexResponse createIndexResponse = elasticsearchClient.indices().create(createIndexRequest);
			response.setSuccess(createIndexResponse.acknowledged());

		} catch (Exception e) {
			log.error(ERROR, e);
		}
		return response;
	}
	
	@Override
	public ExistQueryResponse indexExists(String indexName) {
		ExistQueryResponse response = new ExistQueryResponse();

		try {

			ExistsRequest existsRequest = ExistsRequest.of(e -> e.index(indexName));
			BooleanResponse booleanResponse = elasticsearchClient.indices().exists(existsRequest);
			response.setSuccess(booleanResponse.value());
			response.setExist(booleanResponse.value());

		} catch (Exception e) {
			log.error(ERROR, e);
		}

		return response;
	}
	
	@Override
	public CountQueryResponse exist(String indexName, BigInteger id) {
		CountQueryResponse response = new CountQueryResponse();
		try {

			Query query = new Query.Builder().match(m -> m.field("id").query(id.longValue())).build();
			CountRequest countRequest = CountRequest.of(c -> c.index(indexName).query(query));
			CountResponse countResponse = elasticsearchClient.count(countRequest);
			response.setSuccess(countResponse.count() > 0);
			response.setTotalCount(countResponse.count());

		} catch (Exception e) {
			log.error(ERROR, e);
		}

		return response;
	}
	
	@Override
	public BulkInsertQueryResponse bulkInsert(List<ESAddRequest> requests) {
		BulkInsertQueryResponse response = new BulkInsertQueryResponse();
		if (requests.isEmpty()) {
			return response;
		}
		
		log.info("requests :"+requests.size());
		
		try {
			List<BulkOperation> listOperations = new ArrayList<>(requests.size());
			
			requests.forEach(request->
				listOperations.add(BulkOperation.of(blk -> blk.create(c -> c.index(request.getIndexName()).document(request.getData()))))
			);
			
			BulkRequest bulkRequest = BulkRequest.of(c -> c.operations(listOperations).refresh(Refresh.WaitFor));
			BulkResponse bulkResponse = elasticsearchClient.bulk(bulkRequest);

			Set<String> insertedIds = new HashSet<>(listOperations.size());
			
			bulkResponse.items().forEach(item -> {
				insertedIds.add(item.id());
				if (null != item.error() && null != item.error().causedBy()) {
					log.info("error :" + item.error().causedBy().reason());
				}
			});
			
			response.setSuccess(!bulkResponse.errors());
			response.setIndexIdList(insertedIds);

		} catch (IOException e) {
			log.error(ERROR, e);
		} 
		return response;
	}
	
	
	@Override
	public BulkUpdateQueryResponse bulkUpsert(List<ESUpdateRequest> requests) {
		
		BulkUpdateQueryResponse response = new BulkUpdateQueryResponse();
		if (requests.isEmpty()) {
			return response;
		}
		try {
			
			List<BulkOperation> listOperations = new ArrayList<>(requests.size());
			requests.forEach(request -> {
				String idValue = request.getIndexPK();
				if (StringUtils.isNotBlank(idValue)) {
					listOperations.add(BulkOperation.of(blk -> blk.update(c -> c.index(request.getIndexName())
							.id(idValue).action(ac -> ac.docAsUpsert(true).doc(request.getData()).upsert(request.getData())))));
				}
			});

			
			BulkRequest bulkRequest = BulkRequest.of(c -> c.operations(listOperations).refresh(Refresh.WaitFor));

			BulkResponse bulkResponse = elasticsearchClient.bulk(bulkRequest);

			Set<String> insertedIds = new HashSet<>(listOperations.size());
			
			bulkResponse.items().forEach(item -> {
				insertedIds.add(item.id());
				if (null != item.error() && null != item.error().causedBy()) {
					log.debug("error :" + item.error().causedBy().reason());
				}
			});
			
			response.setIndexIdList(insertedIds);

		} catch (IOException e) {
			log.error(ERROR, e);
		}
		
		return response;
	}
	
	@Override
	public DeleteQueryResponse deleteByQuery(String indexName, Query query) {
		DeleteQueryResponse response = new DeleteQueryResponse();

		try {

			DeleteByQueryRequest deleteByQueryRequest = DeleteByQueryRequest.of(d -> d.index(indexName).query(query));
			DeleteByQueryResponse deleteByQueryResponse = elasticsearchClient.deleteByQuery(deleteByQueryRequest);
			
			response.setSuccess(deleteByQueryResponse.total() > 0);
			response.setTotalDeleted(deleteByQueryResponse.total());

		} catch (Exception e) {
			log.error(ERROR, e);
		}

		return response;
	}
	
	@Override
	public DeleteQueryResponse deleteByIndexId(String indexName, String id) {
		DeleteQueryResponse response = new DeleteQueryResponse();
		try {

			DeleteRequest deleteRequest = DeleteRequest.of(d -> d.index(indexName).id(id));
			DeleteResponse deleteResponse = elasticsearchClient.delete(deleteRequest);
			response.setSuccess(deleteResponse.result().equals(Result.Deleted));
			response.setTotalDeleted(deleteResponse.result().equals(Result.Deleted) ? 1 : 0);
			
		} catch (Exception e) {
			log.error(ERROR, e);
		}

		return response;
	}
	
	@Override
	public <T> ESSearchResponse<T> search(String indexName, Query query, Class<T> clazz) {
		ESSearchResponse<T> response = new ESSearchResponse<>();

		try {

			SearchRequest searchRequest = SearchRequest.of(s -> s.index(indexName).query(query));
			SearchResponse<T> searchResponse = elasticsearchClient.search(searchRequest, clazz);
			Optional.ofNullable(searchResponse).ifPresent(data->
				data.hits().hits().forEach(hit-> response.getDataList().add(hit.source())));
			
		} catch (Exception e) {
			log.error(ERROR, e);
		}

		return response;
	}
	
	@Override
	public <T> ESSearchResponse<T> search(String indexName, Query query, SortOptions sorts, Class<T> clazz) {
		ESSearchResponse<T> response = new ESSearchResponse<>();
		
		try {
			SearchRequest searchRequest = SearchRequest.of(s -> s.index(indexName).query(query).sort(sorts));
			SearchResponse<T> searchResponse = elasticsearchClient.search(searchRequest, clazz);
			
			Optional.ofNullable(searchResponse).ifPresent(data -> 
				data.hits().hits().forEach(hit-> response.getDataList().add(hit.source())));
			
		} catch (Exception e) {
			log.error(ERROR, e);
		}
		
		return response;
	}
	
	@Override
	public <T> ESSearchResponse<T> search(String indexName, Query query, SortOptions sorts, PaginationInfo paginationInfo, Class<T> clazz) {
		ESSearchResponse<T> response = new ESSearchResponse<>();
		
		try {
			if(paginationInfo == null) {
				paginationInfo = PaginationInfo.builder().pageNo(1).pageSize(20).build();
			}
			int offSet = (paginationInfo.getPageNo() -1 ) * paginationInfo.getPageSize();
			int dataSize = paginationInfo.getPageSize();
			
			SearchRequest searchRequest = SearchRequest.of(s-> s.index(indexName).query(query).sort(sorts).from(offSet).size(dataSize));
			SearchResponse<T> searchResponse = elasticsearchClient.search(searchRequest, clazz);
			Optional.ofNullable(searchResponse).ifPresent(data -> 
								data.hits().hits().forEach(hit -> response.getDataList().add(hit.source())));
			
			CountRequest countRequest = CountRequest.of(c -> c.index(indexName).query(query));
			CountResponse countResponse = elasticsearchClient.count(countRequest);
			response.setTotal(countResponse != null ? countResponse.count() : 0);
			
		} catch (Exception e) {
			log.error(ERROR, e);
		}
		
		return response;
	}
	
}
