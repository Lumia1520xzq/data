package com.wf.data.controller.request;

/**
 * 用户访问路径请求对象
 *
 * @author huangjianjian
 */
public class UicBehaviorReq {
    private Long parentId;
    private Long channelId;
    private Long userId;
    private String beginDate;
    private String endDate;
    private Long parentEventId;
    private Long eventId;

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Long getParentEventId() {
        return parentEventId;
    }

    public void setParentEventId(Long parentEventId) {
        this.parentEventId = parentEventId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    @Override
    public String toString() {
        return "UicBehaviorReq [parentId=" + parentId + ", channelId=" + channelId + ", userId=" + userId
                + ", beginDate=" + beginDate + ", endDate=" + endDate + ", parentEventId=" + parentEventId
                + ", eventId=" + eventId + "]";
    }

}
