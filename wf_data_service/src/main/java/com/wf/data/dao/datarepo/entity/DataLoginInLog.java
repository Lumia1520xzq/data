package com.wf.data.dao.datarepo.entity;


import com.wf.core.persistence.DataEntity;

/**
 * 数据系统登录log
 */
public class DataLoginInLog extends DataEntity {

    private static final long serialVersionUID = -1;
    /**
     * 用户名
     */
    private String loginName;
    /**
     * 请求类
     */
    private String requestClass;
    /**
     * 清洗方法
     */
    private String requestMethod;
    /**
     * 请求URL
     */
    private String requestUrl;
    /**
     * 请求ip
     */
    private String requestIp;
    /**
     * 请求耗时
     */
    private Integer executeTime;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getRequestClass() {
        return requestClass;
    }

    public void setRequestClass(String requestClass) {
        this.requestClass = requestClass;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestIp() {
        return requestIp;
    }

    public void setRequestIp(String requestIp) {
        this.requestIp = requestIp;
    }

    public Integer getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Integer executeTime) {
        this.executeTime = executeTime;
    }
}