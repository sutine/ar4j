package org.ar4j.elasticsearch;

import org.ar4j.QueryResponse;
import org.ar4j.elasticsearch.annotation.*;
import org.ar4j.utils.AnnotationUtils;
import org.ar4j.utils.BeanUtils;
import org.ar4j.utils.JsonUtils;
import org.ar4j.utils.StringUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.*;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class ElasticSearchUtils {
    private static Logger logger = LoggerFactory.getLogger(ElasticSearchUtils.class);
    private static long timeout = 5000;

    public static <T> QueryResponse<T> search(ElasticSearchRequest query) {
        Class<T> clazz = query.getClazz();
        SearchRequestBuilder request = ConnectionFactory.getConnection().prepareSearch(getIndex(clazz)).setTypes(getType(clazz))
//                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
//                .setPostFilter(QueryBuilders.rangeQuery("age").from(12).to(18))     // Filter
                .setFrom(query.getOffset())
                .setSize(query.getSize())
                .setExplain(true);

        if (query.getFields() != null)
            request.storedFields(query.getFields());

        if (query.getQuery() != null)
            request.setQuery(query.getQuery());

        if (query.getSorts() != null) {
            for (SortBuilder sort : query.getSorts()) {
                request.addSort(sort);
            }
        }

        SearchResponse resp = request.execute().actionGet();

        List<T> items = new LinkedList<>();
        SearchHits hits = resp.getHits();
        if (hits != null) {
            for (SearchHit hit : hits) {
                String source = hit.getSourceAsString();
                items.add(JsonUtils.fromJson(source, clazz));
            }
        }

        return new QueryResponse<T>()
                .setItems(items)
                .setTotal(resp.getHits().getTotalHits())
                .setElapse(resp.getTookInMillis());
    }

    public static <T> T get(Class<T> clazz, String id) throws ElasticSearchException {
        if (StringUtils.isBlank(id))
            return null;
        GetRequest getRequest = buildGetRequest(clazz, id);
        String source = ConnectionFactory.getConnection().get(getRequest).actionGet(timeout).getSourceAsString();
        return JsonUtils.fromJson(source, clazz);
    }

    /**
     * insert data, if the data id is exists, then update it(if field is null, it will be set to null)
     * @param data
     * @param isRefresh
     * @param <T>
     * @return
     * @throws ElasticSearchException
     */
    public static <T> Integer insert(T data, Boolean isRefresh) throws ElasticSearchException {
        if (data == null)
            throw new ElasticSearchException("data can not be null！");
        String id = getDocId(data);
        if (StringUtils.isBlank(id))
            throw new ElasticSearchException("data id can not be empty！");

        IndexRequest indexRequest = buildIndexRequest(data, isRefresh);

        long version = 0;
        try {
            version = ConnectionFactory.getConnection().index(indexRequest).get().getVersion();
        } catch (InterruptedException | ExecutionException e) {
            throw new ElasticSearchException(e);
        }

        if (version > 0)
            return 1;
        else
            return 0;
    }

    /**
     * update data, if field is null, skip the field to update
     * if the data is not exists, it will be fail.
     * @param data
     * @param isRefresh
     * @param <T>
     * @return
     * @throws ElasticSearchException
     */
    public static <T> Integer update(T data, Boolean isRefresh) throws ElasticSearchException {
        if (data == null)
            throw new ElasticSearchException("data can not be null！");
        String id = getDocId(data);
        if (StringUtils.isBlank(id))
            throw new ElasticSearchException("data id can not be empty！");

        UpdateRequest updateRequest = buildUpdateRequest(data, isRefresh);

        long version = 0;
        try {
            version = ConnectionFactory.getConnection().update(updateRequest).get().getVersion();
        } catch (InterruptedException | ExecutionException e) {
            throw new ElasticSearchException(e);
        }

        if (version > 0)
            return 1;
        else
            return 0;
    }

    /**
     * upsert data, if the data id is exists, then update it(if field is null, skip the field to update)
     * if the data is not exists, then insert it.
     * @param data
     * @param isRefresh
     * @param <T>
     * @return
     * @throws ElasticSearchException
     */
    public static <T> Integer upsert(T data, Boolean isRefresh) throws ElasticSearchException {
        if (data == null)
            throw new ElasticSearchException("data can not be null！");
        String id = getDocId(data);
        if (StringUtils.isBlank(id))
            throw new ElasticSearchException("data id can not be empty！");

        String json = JsonUtils.toJson(data);
        IndexRequest indexRequest = buildIndexRequest(data, isRefresh);

        UpdateRequest updateRequest = new UpdateRequest(getIndex(data), getType(data), getDocId(data))
                .doc(json, XContentFactory.xContentType(json))
                .upsert(indexRequest);

        long version = 0;
        try {
            version = ConnectionFactory.getConnection().update(updateRequest).get().getVersion();
        } catch (InterruptedException | ExecutionException e) {
            throw new ElasticSearchException(e);
        }

        if (version > 0)
            return 1;
        else
            return 0;
    }

    public static <T> int delete(Class<T> clazz, String id, Boolean isRefresh) throws ElasticSearchException {
        DeleteRequest deleteRequest = buildDeleteRequest(clazz, id, isRefresh);
        long version = ConnectionFactory.getConnection().delete(deleteRequest).actionGet(timeout).getVersion();
        if (version > 0)
            return 1;
        else
            return 0;
    }

    public static <T> List<T> get(Class<T> clazz, List<String> list) throws ElasticSearchException {
        List<T> result = new LinkedList<>();
        if (list == null || list.isEmpty())
            return result;

        MultiGetRequestBuilder multiGetRequestBuilder = ConnectionFactory.getConnection().prepareMultiGet();
        for (String id : list) {
            multiGetRequestBuilder.add(getIndex(clazz), getType(clazz), id);
        }

        MultiGetResponse multiGetResponse = multiGetRequestBuilder.get(TimeValue.timeValueMillis(timeout));
        for (MultiGetItemResponse itemResponse : multiGetResponse) {
            GetResponse response = itemResponse.getResponse();
            if (response.isExists()) {
                String source = response.getSourceAsString();
                T item = null;
                if (source != null && !source.isEmpty())
                    item = JsonUtils.fromJson(source, clazz);

                result.add(item);
            }
        }

        return result;
    }

    public static <T> int insert(List<T> list, Boolean isRefresh) throws ElasticSearchException {
        if (list == null || list.isEmpty())
            return 0;

        BulkRequestBuilder bulkRequest = ConnectionFactory.getConnection().prepareBulk();
        for (T data: list) {
            IndexRequest indexRequest = buildIndexRequest(data, isRefresh);
            bulkRequest.add(indexRequest);
        }

        BulkResponse bulkResponse = bulkRequest.get(TimeValue.timeValueMillis(timeout));
        if (bulkResponse.hasFailures()) {
            throw new ElasticSearchException("batch insert failed! error: " + bulkResponse.buildFailureMessage());
        }

        return bulkResponse.getItems().length;
    }

    /**
     * update a data list, if a data is not exists, the data update operation will fail, but others will success.
     * @param list
     * @param isRefresh
     * @param <T>
     * @return
     * @throws ElasticSearchException
     */
    public static <T> int update(List<T> list, Boolean isRefresh) throws ElasticSearchException {
        if (list == null || list.isEmpty())
            return 0;

        BulkRequestBuilder bulkRequest = ConnectionFactory.getConnection().prepareBulk();
        for (T data: list) {
            UpdateRequest updateRequest = buildUpdateRequest(data, isRefresh);
            bulkRequest.add(updateRequest);
        }

        BulkResponse bulkResponse = bulkRequest.get(TimeValue.timeValueMillis(timeout));
        if (bulkResponse.hasFailures()) {
            throw new ElasticSearchException("batch update failed! error: " + bulkResponse.buildFailureMessage());
        }

        return bulkResponse.getItems().length;
    }

    /**
     * if the data id is exists, then delete it.
     * @param clazz
     * @param list
     * @param isRefresh
     * @param <T>
     * @return
     * @throws ElasticSearchException
     */
    public static <T> int delete(Class<T> clazz, List<String> list, Boolean isRefresh) throws ElasticSearchException {
        if (list == null || list.isEmpty())
            return 0;

        BulkRequestBuilder bulkRequest = ConnectionFactory.getConnection().prepareBulk();
        for (String id: list) {
            DeleteRequest deleteRequest = buildDeleteRequest(clazz, id, isRefresh);
            bulkRequest.add(deleteRequest);
        }

        BulkResponse bulkResponse = bulkRequest.get(TimeValue.timeValueMillis(timeout));
        if (bulkResponse.hasFailures()) {
            throw new ElasticSearchException("batch delete failed! error: " + bulkResponse.buildFailureMessage());
        }

        return bulkResponse.getItems().length;
    }

    public static <T> int upsert(List<T> list, Boolean isRefresh) throws ElasticSearchException {
        if (list == null || list.isEmpty())
            return 0;

        BulkRequestBuilder bulkRequest = ConnectionFactory.getConnection().prepareBulk();
        for (T data: list) {
            IndexRequest indexRequest = buildIndexRequest(data, isRefresh);
            bulkRequest.add(indexRequest);
        }

        BulkResponse bulkResponse = bulkRequest.get(TimeValue.timeValueMillis(timeout));
        if (bulkResponse.hasFailures()) {
            throw new ElasticSearchException("batch upsert failed! error: " + bulkResponse.buildFailureMessage());
        }

        return bulkResponse.getItems().length;
    }

    private static <T> GetRequest buildGetRequest(Class<T> clazz, String id) throws ElasticSearchException {
        if (StringUtils.isBlank(id))
            throw new ElasticSearchException("data id can not be empty！");

        return new GetRequest(getIndex(clazz), getType(clazz), id);
    }

    private static <T> DeleteRequest buildDeleteRequest(Class<T> clazz, String id, Boolean isRefresh) throws ElasticSearchException {
        if (StringUtils.isBlank(id))
            throw new ElasticSearchException("data id can not be empty！");

        DeleteRequest deleteRequest = new DeleteRequest(getIndex(clazz), getType(clazz), id);
        if (isRefresh) deleteRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        if (StringUtils.isNotBlank(id)) deleteRequest.id(id);
        return deleteRequest;
    }

    private static <T> IndexRequest buildIndexRequest(T data, Boolean isRefresh) throws ElasticSearchException {
        if (data == null)
            throw new ElasticSearchException("data can not be null！");
        String id = getDocId(data);
        if (StringUtils.isBlank(id))
            throw new ElasticSearchException("data id can not be empty！");

        String json = JsonUtils.toJson(data);
        String parentId = getDocParentId(data);

        IndexRequest indexRequest = new IndexRequest(getIndex(data), getType(data))
                .source(json, XContentFactory.xContentType(json));
        if (isRefresh) indexRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        if (StringUtils.isNotBlank(parentId)) indexRequest.parent(parentId);
        if (StringUtils.isNotBlank(id)) indexRequest.id(id);
        return indexRequest;
    }

    private static <T> UpdateRequest buildUpdateRequest(T data, Boolean isRefresh) throws ElasticSearchException {
        if (data == null)
            throw new ElasticSearchException("data can not be null！");
        String id = getDocId(data);
        if (StringUtils.isBlank(id))
            throw new ElasticSearchException("data id can not be empty！");

        String json = JsonUtils.toJson(data);
        String parentId = getDocParentId(data);

        UpdateRequest updateRequest = new UpdateRequest(getIndex(data), getType(data), id)
                .doc(json, XContentFactory.xContentType(json));
        if (isRefresh) updateRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        if (StringUtils.isNotBlank(parentId)) updateRequest.parent(parentId);
        return updateRequest;
    }

    /************************************/
    public static void create(Class clazz) {
        CreateIndexRequestBuilder createIndexRequestBuilder = ConnectionFactory.getConnection().admin().indices().prepareCreate(getIndex(clazz));
        Object[] mappings = getMappings(clazz);
        createIndexRequestBuilder.addMapping(getType(clazz), mappings);
//        createIndexRequestBuilder.setSettings(getSettings(clazz));
        createIndexRequestBuilder.execute().actionGet();
    }

    public static Object[] getSettings(Class clazz) {
        String settings = AnnotationUtils.getAnnotationValue(clazz, TableSettings.class, "value");
        return new Object[]{settings};
    }

    public static Object[] getMappings(Class clazz) {
        List<Object> mappings = new ArrayList<>();
        Field[] fields = BeanUtils.getDeclaredFields(clazz);
        for (Field field : fields) {
            String fieldSettings = AnnotationUtils.getAnnotationValue(field, TableField.class, "value");
            if (StringUtils.isNotBlank(fieldSettings)) {
                mappings.add(field.getName());
                mappings.add(fieldSettings);
            }
        }
        return mappings.toArray();
    }

    public static String getIndex(Class clazz) {
        return AnnotationUtils.getAnnotationValue(clazz, Index.class, "value");
    }

    public static <T> String getIndex(T data) {
        if (data == null)
            return "";

        Class clazz = data.getClass();
        return getIndex(clazz);
    }

    public static <T> String getType(Class clazz) {
        return AnnotationUtils.getAnnotationValue(clazz, Type.class, "value");
    }

    public static <T> String getType(T data) {
        if (data == null)
            return "";

        Class clazz = data.getClass();
        return getType(clazz);
    }

    private static <T> String getDocId(T data) throws ElasticSearchException {
        if (data == null)
            return "";

        Field[] fields = BeanUtils.getDeclaredFields(data.getClass());
        for (Field field : fields) {
            DocId annotation = field.getAnnotation(DocId.class);
            if (annotation != null)
                try {
                    return BeanUtils.getProperty(data, field.getName());
                } catch (Exception e) {
                    throw new ElasticSearchException("get data id failed! error: " + e.getMessage());
                }
        }

        return "";
    }

    private static <T> String getDocParentId(T data) {
        if (data == null)
            return null;

        Field[] fields = data.getClass().getDeclaredFields();
        for (Field field : fields) {
            DocParentId annotation = field.getAnnotation(DocParentId.class);
            if (annotation != null)
                return annotation.value();
        }

        return null;
    }
}

