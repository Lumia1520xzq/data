package com.wf.uic.controller.request;

/**
 * 有来源的请求
 *
 * @author Fe 2016年9月19日
 */
public class VisitorRequest {
    private Integer source;
    private String visitorToken;

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getVisitorToken() {
        return visitorToken;
    }

    public void setVisitorToken(String visitorToken) {
        this.visitorToken = visitorToken;
    }
}
