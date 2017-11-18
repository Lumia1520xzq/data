package com.wf.data.common.utils.elasticsearch;

public class EsBean {
	
	private String ips;
	private String clusterName;
	private String port;
	private String nodeName;
	



	public String getNodeName() {
		return nodeName;
	}



	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}



	public String getIps() {
		return ips;
	}


	public void setIps(String ips) {
		this.ips = ips;
	}




	public String getClusterName() {
		return clusterName;
	}


	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}


	public String getPort() {
		return port;
	}


	public void setPort(String port) {
		this.port = port;
	}
	
	public EsBean(String ips, String clusterName,String port, String nodeName) {
		this.ips = ips;
		this.clusterName = clusterName;
		this.port = port;
		this.nodeName = nodeName;
	}
	
}
