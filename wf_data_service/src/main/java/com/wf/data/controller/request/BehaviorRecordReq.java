package com.wf.data.controller.request;

/**
 * @author shihui
 * @date 2018/1/23
 */
public class BehaviorRecordReq {
    private Long parentId;
    private Long channelId;
    private Long parentEventId;
    private Long eventId;
    private String beginDate;
    private String endDate;
    private Integer userType;

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

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
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

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Long getParentEventId() {
        return parentEventId;
    }

    public void setParentEventId(Long parentEventId) {
        this.parentEventId = parentEventId;
    }

    @Override
    public String toString() {
        return "BehaviorRecordReq [parentId=" + parentId + ", channelId=" + channelId + ", parentEventId="
                + parentEventId + ", eventId=" + eventId + ", beginDate=" + beginDate + ", endDate=" + endDate
                + ", userType=" + userType + "]";
    }
}
