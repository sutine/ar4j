package org.ar4j;

public class QueryRequest {
    private Class<?> clazz;
    private int offset = 0;
    private int size = 20;
    private String[] fields;
    private String[] orderBy;
    private Boolean desc;

    public Class<?> getClazz() {
        return clazz;
    }

    public QueryRequest setClazz(Class<?> clazz) {
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

    public String[] getOrderBy() {
        return orderBy;
    }

    public QueryRequest setOrderBy(String[] orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    public Boolean getDesc() {
        return desc;
    }

    public QueryRequest setDesc(Boolean desc) {
        this.desc = desc;
        return this;
    }
}
