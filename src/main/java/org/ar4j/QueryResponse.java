package org.ar4j;

import java.util.List;

public class QueryResponse<T> {
    private long elapse;
    private long total;
    private List<T> items;

    public long getElapse() {
        return elapse;
    }

    public QueryResponse setElapse(long elapse) {
        this.elapse = elapse;
        return this;
    }

    public long getTotal() {
        return total;
    }

    public QueryResponse setTotal(long total) {
        this.total = total;
        return this;
    }

    public List<T> getItems() {
        return items;
    }

    public QueryResponse setItems(List<T> items) {
        this.items = items;
        return this;
    }
}
