package org.ar4j;

import org.ar4j.elasticsearch.ElasticSearchException;
import org.ar4j.elasticsearch.ElasticSearchRequest;

import java.util.List;

public interface ActiveRecord {
    <T> QueryResponse query(Class<T> clazz, ElasticSearchRequest query) throws ElasticSearchException;
    <T, PK> T get(Class<T> clazz, PK id) throws ElasticSearchException;
    <T, PK> int insert(T t) throws ElasticSearchException;
    <T> int update(T t) throws ElasticSearchException;
    <T, PK> int delete(Class<T> clazz, PK id) throws ElasticSearchException;
    <T, PK> int upsert(T t) throws ElasticSearchException;

    <T> List<T> get(Class<T> clazz, List<String> ids) throws ElasticSearchException;
    <T> int insert(List<T> list) throws ElasticSearchException;
    <T> int update(List<T> list) throws ElasticSearchException;
    <T> int delete(Class<T> clazz, List<String> ids) throws ElasticSearchException;
    <T> int upsert(List<T> list) throws ElasticSearchException;
}
