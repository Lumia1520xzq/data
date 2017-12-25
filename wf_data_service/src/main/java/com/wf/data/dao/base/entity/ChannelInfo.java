package com.wf.data.dao.base.entity;


import com.wf.core.persistence.DataEntity;

public class ChannelInfo extends DataEntity {
    private static final long serialVersionUID = -1;
    private Long parentId;
    private String code;
    private String name;
    private String description;
    private Integer enable;
    private Long mainChannel;

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    public Long getMainChannel() {
        return mainChannel;
    }

    public void setMainChannel(Long mainChannel) {
        this.mainChannel = mainChannel;
    }


}