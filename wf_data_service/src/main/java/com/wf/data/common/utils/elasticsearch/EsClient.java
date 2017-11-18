package com.wf.data.common.utils.elasticsearch;

import org.elasticsearch.client.transport.TransportClient;

public class EsClient {
    public final TransportClient client;
    private final long createTime;

    public EsClient(TransportClient client) {
        this.client = client;
        this.createTime = System.currentTimeMillis();
    }

    public boolean isActive() {
        return System.currentTimeMillis() - createTime > 1000 * 60 * 60l;
    }
}
