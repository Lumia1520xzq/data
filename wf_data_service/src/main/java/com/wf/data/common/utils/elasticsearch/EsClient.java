package com.wf.data.common.utils.elasticsearch;

import org.elasticsearch.client.Client;

public class EsClient {
	public final Client client;
	private final long createTime;
	public EsClient(Client client) {
		this.client = client;
		this.createTime = System.currentTimeMillis();
	}
	public boolean isActive() {
		return System.currentTimeMillis() - createTime > 1000 * 60 * 60l;
	}
}
