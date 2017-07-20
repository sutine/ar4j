package org.ar4j.elasticsearch;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

public class ConnectionFactory {
    static class SingletonHolder {
        private static Logger logger = LoggerFactory.getLogger(ElasticSearchUtils.class);
        static Client client = null;
        static {
            try {
                InputStream in = ConnectionFactory.class.getClassLoader().getResourceAsStream("elasticsearch.properties");
                Properties props = new Properties();
                props.load(in);
                String clusterName = props.getProperty("clusterName");
                String host = props.getProperty("host");
                int port = Integer.valueOf(props.getProperty("port", "9300"));
                Settings settings = Settings.builder()
                        .put("cluster.name", clusterName)
                        .put("client.transport.ping_timeout", "10s")
                        .put("transport.ping_schedule", "5s")
                        .build();
                client = new PreBuiltTransportClient(settings)
                        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));
            } catch (UnknownHostException e) {
                logger.error("connect to elasticsearch server failed!", e);
            } catch (Exception e) {
                logger.error("load elasticsearch config failed!", e);
            }
        }
    }

    public static Client getConnection(){
        return SingletonHolder.client;
    }
}
