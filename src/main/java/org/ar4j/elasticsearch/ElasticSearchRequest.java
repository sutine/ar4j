package org.ar4j.elasticsearch;

import org.ar4j.QueryRequest;
import org.elasticsearch.index.query.QueryBuilder;

public class ElasticSearchRequest extends QueryRequest {
    public QueryBuilder getQuery() {
        return query;
    }

    public ElasticSearchRequest setQuery(QueryBuilder query) {
        this.query = query;
        return this;
    }

    private QueryBuilder query;
}
