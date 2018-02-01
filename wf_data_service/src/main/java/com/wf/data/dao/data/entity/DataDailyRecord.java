package com.wf.data.dao.data.entity;

import com.wf.core.persistence.DataEntity;
import com.wf.core.utils.excel.ExcelField;

import java.util.Date;


public class DataDailyRecord extends DataEntity {
    private static final long serialVersionUID = -1;

    private Long channelId;//渠道
    private Date dataTime;//数据日期
    private Integer indicatorType;//指标
    private String phenomenon;//现象
    private String analysisSummary;//分析总结
    private String followUp;//待跟进
    private String followUpUser;//跟进人
    private String creater;//录入人
    private String updater;//修改人

    private String beginDate;
    private String endDate;
    private String dataTimeStr;
    private String indicatorTypeName;


    @ExcelField(title="渠道", type=0, align=1, sort=10)
    public Long getChannelId() {
        return channelId;
    }

    public Date getDataTime() {
        return dataTime;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    @ExcelField(title="数据日期", type=0, align=1, sort=20)
    public String getDataTimeStr() {
        return dataTimeStr;
    }

    public void setDataTimeStr(String dataTimeStr) {
        this.dataTimeStr = dataTimeStr;
    }


    public void setDataTime(Date dataTime) {
        this.dataTime = dataTime;
    }

    @ExcelField(title="指标", type=0, align=1, sort=30)
    public String getIndicatorTypeName() {
        return indicatorTypeName;
    }

    public void setIndicatorTypeName(String indicatorTypeName) {
        this.indicatorTypeName = indicatorTypeName;
    }

    public Integer getIndicatorType() {
        return indicatorType;
    }

    public void setIndicatorType(Integer indicatorType) {
        this.indicatorType = indicatorType;
    }

    @ExcelField(title="现象", type=0, align=1, sort=40)
    public String getPhenomenon() {
        return phenomenon;
    }

    public void setPhenomenon(String phenomenon) {
        this.phenomenon = phenomenon;
    }

    @ExcelField(title="分析总结", type=0, align=1, sort=50)
    public String getAnalysisSummary() {
        return analysisSummary;
    }

    public void setAnalysisSummary(String analysisSummary) {
        this.analysisSummary = analysisSummary;
    }

    @ExcelField(title="待跟进", type=0, align=1, sort=60)
    public String getFollowUp() {
        return followUp;
    }

    public void setFollowUp(String followUp) {
        this.followUp = followUp;
    }

    @ExcelField(title="跟进人", type=0, align=1, sort=70)
    public String getFollowUpUser() {
        return followUpUser;
    }

    public void setFollowUpUser(String followUpUser) {
        this.followUpUser = followUpUser;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
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
}

