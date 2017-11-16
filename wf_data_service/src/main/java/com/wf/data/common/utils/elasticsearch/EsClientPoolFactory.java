package com.wf.data.common.utils.elasticsearch;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

@Component
public class EsClientPoolFactory extends BasePooledObjectFactory<EsClient> {

    @Autowired
    private EsBean esBean;


    @Override
    public EsClient create() throws Exception {

        Settings settings = Settings.builder().put("cluster.name", esBean.getClusterName()).put("node.name", esBean.getNodeName()).put("client.transport.sniff", true).build();
        @SuppressWarnings("resource")
        TransportClient client = new PreBuiltTransportClient(settings);
        for (String ip : esBean.getIps().split(",")) {
            try {
                client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ip), Integer.valueOf(esBean.getPort())));
            } catch (Exception e) {
                throw new RuntimeException("连接es请求失败：", e);
            }
        }
        return new EsClient(client);
    }

    @Override
    public PooledObject<EsClient> wrap(EsClient obj) {
        return new DefaultPooledObject<EsClient>(obj);
    }

    @Override
    public void passivateObject(PooledObject<EsClient> p) throws Exception {
        super.passivateObject(p);
    }

}
