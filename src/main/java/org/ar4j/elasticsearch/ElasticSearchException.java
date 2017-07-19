package org.ar4j.elasticsearch;

public class ElasticSearchException extends Exception {

    public ElasticSearchException(String message) {
        super(message);
    }

    public ElasticSearchException(Throwable cause) {
        super(cause);
    }
}
