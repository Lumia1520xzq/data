package com.wf.data.rpc.dto;

import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * @author chris
 */
public class DataDictDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer value;    // 数据值
    private String label;    // 标签名
    private String type;    // 类型
    private String description;// 描述
    private String remarks;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "BaseDictDTO{" +
                "value='" + value + '\'' +
                ", label='" + label + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }

    public static DataDictDto toDto(Object object) {
        if (object == null) {
            return null;
        }
        DataDictDto dto = new DataDictDto();
        BeanUtils.copyProperties(object, dto);
        return dto;
    }
}
