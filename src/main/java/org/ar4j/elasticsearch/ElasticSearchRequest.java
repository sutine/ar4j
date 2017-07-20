package org.ar4j.elasticsearch;

import org.ar4j.QueryRequest;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.SortBuilder;

public class ElasticSearchRequest extends QueryRequest {
    private QueryBuilder query;

    private SortBuilder[] sorts;

    public SortBuilder[] getSorts() {
        return sorts;
    }

    public ElasticSearchRequest setSorts(SortBuilder[] sorts) {
        this.sorts = sorts;
        return this;
    }

    public QueryBuilder getQuery() {
        return query;
    }

    public ElasticSearchRequest setQuery(QueryBuilder query) {
        this.query = query;
        return this;
    }
}
