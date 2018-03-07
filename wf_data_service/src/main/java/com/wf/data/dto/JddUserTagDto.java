package com.wf.data.dto;

import java.io.Serializable;

/**
 * @author: lcs
 * @date: 2018/03/05
 */
public class JddUserTagDto implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * 必须项
     * 用户id列表，用户“,”逗号隔开，每次最多上传5000个用户
     */
    private String userIds;
    /**
     * 必须项
     * 批次id，32位的唯一字符串，
     * 同一标签下同一批次的标签用户分批上传时使用相同的batchId，
     * 建议使用uuid作为唯一标识
     */
    private String batchId;
    /**
     * 必须项
     *本批次所有用户上传完成标记 1：完成  0：未完成
     */
    private String batchEndFlag;
    /**
     * 必须项
     * 数据来源方
     * 可选值（小写字母）
     * game：游戏
     */
    private String from;
    /**
     * 必须项
     * 标签id
     */
    private String tagId;

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getBatchEndFlag() {
        return batchEndFlag;
    }

    public void setBatchEndFlag(String batchEndFlag) {
        this.batchEndFlag = batchEndFlag;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }
}
