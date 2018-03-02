package com.wf.data.dao.data.entity;

import com.wf.core.persistence.DataEntity;
import com.wf.core.utils.excel.ExcelField;

public class DataEvent extends DataEntity {
    private static final long serialVersionUID = -1;

    private Long channelId;
    private String beginDate;
    private String endDate;
    private Integer eventType;
    private String title;
    private String content;
    private String creater;
    private String updater;

    private String channelName;
    private String createrName;
    private String updaterName;
    private String eventTypeName;

    @ExcelField(title = "渠道", type = 0, align = 1, sort = 10)
    public Long getChannelId() {
        return this.channelId;
    }

    public void setChannelId(Long value) {
        this.channelId = value;
    }

    @ExcelField(title = "开始日期", type = 0, align = 1, sort = 20)
    public String getBeginDate() {
        return this.beginDate;
    }

    public void setBeginDate(String value) {
        this.beginDate = value;
    }

    @ExcelField(title = "结束日期", type = 0, align = 1, sort = 30)
    public String getEndDate() {
        return this.endDate;
    }

    public void setEndDate(String value) {
        this.endDate = value;
    }

    public void setEventType(Integer value) {
        this.eventType = value;
    }

    public Integer getEventType() {
        return this.eventType;
    }

    @ExcelField(title = "类别", align = 1, sort = 40)
    public String getEventTypeName() {
        return eventTypeName;
    }

    public void setEventTypeName(String eventTypeName) {
        this.eventTypeName = eventTypeName;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String value) {
        this.title = value;
    }

    @ExcelField(title = "事记", type = 0, align = 1, sort = 50)
    public String getContent() {
        return this.content;
    }

    public void setContent(String value) {
        this.content = value;
    }

    public String getCreater() {
        return this.creater;
    }

    public void setCreater(String value) {
        this.creater = value;
    }

    public String getUpdater() {
        return this.updater;
    }

    public void setUpdater(String value) {
        this.updater = value;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getCreaterName() {
        return createrName;
    }

    public void setCreaterName(String createrName) {
        this.createrName = createrName;
    }

    public String getUpdaterName() {
        return updaterName;
    }

    public void setUpdaterName(String updaterName) {
        this.updaterName = updaterName;
    }
}

