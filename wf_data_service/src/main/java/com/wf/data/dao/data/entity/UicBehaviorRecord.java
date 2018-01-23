package com.wf.data.dao.data.entity;


import com.wf.core.persistence.DataEntity;

public class UicBehaviorRecord extends DataEntity {
    private static final long serialVersionUID = -1;
    private Long userId;
    private Long behaviorEventId;
    private Long channelId;
    private Long parentChannelId;
    private String behaviorName;
    private String nickname;
    private String eventName;
    private String channelName;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBehaviorEventId() {
        return behaviorEventId;
    }

    public void setBehaviorEventId(Long behaviorEventId) {
        this.behaviorEventId = behaviorEventId;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public Long getParentChannelId() {
        return parentChannelId;
    }

    public void setParentChannelId(Long parentChannelId) {
        this.parentChannelId = parentChannelId;
    }

    public String getBehaviorName() {
        return behaviorName;
    }

    public void setBehaviorName(String behaviorName) {
        this.behaviorName = behaviorName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    @Override
    public String toString() {
        return "UicBehaviorRecord [userId=" + userId + ", behaviorEventId=" + behaviorEventId + ", channelId="
                + channelId + ", parentChannelId=" + parentChannelId + ", behaviorName=" + behaviorName + ", nickname="
                + nickname + ", eventName=" + eventName + ", channelName=" + channelName + "]";
    }
}