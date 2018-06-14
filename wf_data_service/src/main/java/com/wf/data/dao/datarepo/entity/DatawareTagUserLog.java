package com.wf.data.dao.datarepo.entity;

import com.wf.core.persistence.DataEntity;

public class DatawareTagUserLog extends DataEntity {
    private static final long serialVersionUID = -1;
    private String thirdId;
    private Long parentId;
    private String tagId;
    private String businessDate;

    public String getThirdId() {
        return thirdId;
    }

    public void setThirdId(String thirdId) {
        this.thirdId = thirdId;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(String businessDate) {
        this.businessDate = businessDate;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
