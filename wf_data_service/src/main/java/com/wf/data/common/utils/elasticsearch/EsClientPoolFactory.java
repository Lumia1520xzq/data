package com.wf.data.common.utils.elasticsearch;

import org.apache.commons.pool.PoolableObjectFactory;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

@Component
public class EsClientPoolFactory implements PoolableObjectFactory {

	@Autowired
	private EsBean esBean;
	
	@Override
	public void activateObject(Object obj) throws Exception {
		//TODO
	}

	@Override
	public void destroyObject(Object obj) throws Exception {
		toClient(obj).client.close();
	}

	@Override
	public Object makeObject() throws Exception {
		Settings settings = Settings.builder().put("cluster.name", esBean.getClusterName()).put("node.name", esBean.getNodeName()).build();
		@SuppressWarnings("resource")
        Client client = new PreBuiltTransportClient(settings);
		for (String ip : esBean.getIps().split(",")) {
			try {
				((TransportClient) client).addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ip), Integer.valueOf(esBean.getPort())));
			} catch (Exception e) {
				throw new RuntimeException("连接es请求失败：", e);
			}
		}
		return new EsClient(client);
	}

	@Override
	public void passivateObject(Object obj) throws Exception {
		//TODO
	}

	@Override
	public boolean validateObject(Object obj) {
		return toClient(obj).isActive();
	}
	
	private EsClient toClient(Object obj) {
		return (EsClient) obj;
	}
}
