package com.wf.data.dao.mall.entity;


import com.wf.core.utils.excel.ExcelField;

import java.util.Date;
import java.util.List;

public class AwardsSendLogInOut {

    private String userName;
    private Long userId;
    private String activityName;
    private Double rmbAmount;
    private Long activityId;
    private List<Long> activityIds;
    private Long parentId; //主渠道
    private String beginDate;
    private String endDate;
    private Long goldAmount;
    private  Date updateTime;
    private String phyAwardsName;

    @ExcelField(title = "奖品名称", type = 1, align = 2, sort = 4)
    public String getPhyAwardsName() {
        return phyAwardsName;
    }

    public void setPhyAwardsName(String phyAwardsName) {
        this.phyAwardsName = phyAwardsName;
    }

    @ExcelField(title = "用户昵称", type = 1, align = 2, sort = 1)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    @ExcelField(title = "用户ID", type = 1, align = 2, sort = 2)
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    @ExcelField(title = "出口类型", type = 1, align = 2, sort = 3)
    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
    @ExcelField(title = "金额", type = 1, align = 2, sort = 5)
    public Double getRmbAmount() {
        return rmbAmount;
    }

    public void setRmbAmount(Double rmbAmount) {
        this.rmbAmount = rmbAmount;
    }
    @ExcelField(title = "变更时间", type = 1, align = 2, sort = 6)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public List<Long> getActivityIds() {
        return activityIds;
    }

    public void setActivityIds(List<Long> activityIds) {
        this.activityIds = activityIds;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
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

    public Long getGoldAmount() {
        return goldAmount;
    }

    public void setGoldAmount(Long goldAmount) {
        this.goldAmount = goldAmount;
    }
}