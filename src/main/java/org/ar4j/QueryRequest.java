package org.ar4j;

public class QueryRequest<T> {
    private Class<T> clazz;
    private int offset = 0;
    private int size = 20;
    private String[] fields;
    private Sort[] orderBy;

    public Class<T> getClazz() {
        return clazz;
    }

    public QueryRequest setClazz(Class<T> clazz) {
        this.clazz = clazz;
        return this;
    }

    public int getOffset() {
        return offset;
    }

    public QueryRequest setOffset(int offset) {
        this.offset = offset;
        return this;
    }

    public int getSize() {
        return size;
    }

    public QueryRequest setSize(int size) {
        this.size = size;
        return this;
    }

    public String[] getFields() {
        return fields;
    }

    public QueryRequest setFields(String[] fields) {
        this.fields = fields;
        return this;
    }

    public Sort[] getOrderBy() {
        return orderBy;
    }

    public QueryRequest setOrderBy(Sort[] orderBy) {
        this.orderBy = orderBy;
        return this;
    }
}

class Sort {
    public enum OrderType {
        NONE, ASC, DESC;
    }

    private String sortField;
    private OrderType orderType = OrderType.NONE;
    private String sortType = "field";

    public String getSortField() {
        return sortField;
    }

    public Sort setSortField(String sortField) {
        this.sortField = sortField;
        return this;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public Sort setOrderType(OrderType orderType) {
        this.orderType = orderType;
        return this;
    }

    public String getSortType() {
        return sortType;
    }

    public Sort setSortType(String sortType) {
        this.sortType = sortType;
        return this;
    }
}