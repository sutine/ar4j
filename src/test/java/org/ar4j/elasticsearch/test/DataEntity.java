package org.ar4j.elasticsearch.test;

import org.ar4j.QueryResponse;
import org.ar4j.elasticsearch.ElasticSearchException;
import org.ar4j.elasticsearch.ElasticSearchRequest;
import org.ar4j.elasticsearch.EsBaseEntity;
import org.ar4j.elasticsearch.annotation.DocId;
import org.ar4j.elasticsearch.annotation.Index;
import org.ar4j.elasticsearch.annotation.Type;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

@Index("test_index")
@Type("test_type")
public class DataEntity extends EsBaseEntity {
    @DocId
    private String id;
    private String strField;
    private Integer integerField;
    private Long longField;
    private Float floatField;
    private Double doubleField;


    protected QueryBuilder buildQuery() {
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.must(QueryBuilders.rangeQuery("longField").gte(333332).lte(333335));
        query.must(QueryBuilders.termQuery("doubleField", "333.333333"));
        return query;
    }

    private SortBuilder[] buildSorts() {
        SortBuilder sort = SortBuilders.fieldSort("longField").order(SortOrder.DESC);
        return new SortBuilder[]{sort};
    }

    public QueryResponse<DataEntity> query() throws ElasticSearchException {
        ElasticSearchRequest query = new ElasticSearchRequest()
                .setQuery(buildQuery())
                .setSorts(buildSorts());

        query.setClazz(DataEntity.class);
        return query(query);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStrField() {
        return strField;
    }

    public void setStrField(String strField) {
        this.strField = strField;
    }

    public Integer getIntegerField() {
        return integerField;
    }

    public void setIntegerField(Integer integerField) {
        this.integerField = integerField;
    }

    public Long getLongField() {
        return longField;
    }

    public void setLongField(Long longField) {
        this.longField = longField;
    }

    public Float getFloatField() {
        return floatField;
    }

    public void setFloatField(Float floatField) {
        this.floatField = floatField;
    }

    public Double getDoubleField() {
        return doubleField;
    }

    public void setDoubleField(Double doubleField) {
        this.doubleField = doubleField;
    }
}
