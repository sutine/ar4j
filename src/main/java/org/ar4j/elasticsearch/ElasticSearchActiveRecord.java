package org.ar4j.elasticsearch;

import org.ar4j.ActiveRecord;
import org.ar4j.QueryResponse;
import org.elasticsearch.ElasticsearchException;

import java.util.List;

public class ElasticSearchActiveRecord implements ActiveRecord {

    @Override
    public <T> QueryResponse query(Class<T> clazz, ElasticSearchRequest query) throws ElasticSearchException {
        QueryResponse respnse = ElasticSearchUtils.search(clazz, query);
        return respnse;
    }

    @Override
    public <T, PK> T get(Class<T> clazz, PK id) throws ElasticSearchException {
        if (!(id instanceof String))
            throw new ElasticsearchException("id type is not String!");

        return ElasticSearchUtils.get(clazz, (String) id);
    }

    @Override
    public <T, PK> int insert(T t) throws ElasticSearchException {
        return ElasticSearchUtils.insert(t, false);
    }

    @Override
    public <T> int update(T t) throws ElasticSearchException {
        return ElasticSearchUtils.update(t, false);
    }

    @Override
    public <T, PK> int delete(Class<T> clazz, PK id) throws ElasticSearchException {
        return ElasticSearchUtils.delete(clazz, (String) id, false);
    }

    @Override
    public <T, PK> int upsert(T t) throws ElasticSearchException {
        return ElasticSearchUtils.upsert(t, false);
    }

    @Override
    public <T> List<T> get(Class<T> clazz, List<String> ids) throws ElasticSearchException {
        return ElasticSearchUtils.get(clazz, ids);
    }

    @Override
    public <T> int insert(List<T> list) throws ElasticSearchException {
        return ElasticSearchUtils.insert(list, false);
    }

    @Override
    public <T> int update(List<T> list) throws ElasticSearchException {
        return ElasticSearchUtils.update(list, false);
    }

    @Override
    public <T> int delete(Class<T> clazz, List<String> ids) throws ElasticSearchException {
        return ElasticSearchUtils.delete(clazz, ids, false);
    }

    @Override
    public <T> int upsert(List<T> list) throws ElasticSearchException {
        return ElasticSearchUtils.upsert(list, false);
    }

}
