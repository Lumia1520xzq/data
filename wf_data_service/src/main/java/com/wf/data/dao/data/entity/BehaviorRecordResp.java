package com.wf.data.dao.data.entity;

/**
 * @author shihui
 * @date 2018/1/23
 */
public class BehaviorRecordResp {
    private Long id; //id
    private String behaviorName;//事件名称
    private Long behaviorEventId;//事件id
    private Long behaviorCount;//触发数量
    private Integer behaviorUserCount;//触发用户数
    private String channelName;//渠道名称

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBehaviorName() {
        return behaviorName;
    }

    public void setBehaviorName(String behaviorName) {
        this.behaviorName = behaviorName;
    }

    public Long getBehaviorEventId() {
        return behaviorEventId;
    }

    public void setBehaviorEventId(Long behaviorEventId) {
        this.behaviorEventId = behaviorEventId;
    }

    public Long getBehaviorCount() {
        return behaviorCount;
    }

    public void setBehaviorCount(Long behaviorCount) {
        this.behaviorCount = behaviorCount;
    }

    public Integer getBehaviorUserCount() {
        return behaviorUserCount;
    }

    public void setBehaviorUserCount(Integer behaviorUserCount) {
        this.behaviorUserCount = behaviorUserCount;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    @Override
    public String toString() {
        return "BehaviorRecordResp [id=" + id + ", behaviorName=" + behaviorName + ", behaviorEventId="
                + behaviorEventId + ", behaviorCount=" + behaviorCount + ", behaviorUserCount=" + behaviorUserCount
                + ", channelName=" + channelName + "]";
    }
}
