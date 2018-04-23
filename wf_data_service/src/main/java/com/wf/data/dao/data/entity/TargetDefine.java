package com.wf.data.dao.data.entity;

import com.wf.core.persistence.DataEntity;
import com.wf.core.utils.excel.ExcelField;

public class TargetDefine extends DataEntity {
	private static final long serialVersionUID = -1;
	private Integer targetType;
	@ExcelField(title="指标类型", align = 1, sort = 10)
	private String targetTypeName;
	@ExcelField(title="指标名称", align = 1, sort = 20)
	private String targetName;
	@ExcelField(title="指标定义", align = 1, sort = 30)
	private String targetDefine;

	public Integer getTargetType() {
		return targetType;
	}

	public void setTargetType(Integer targetType) {
		this.targetType = targetType;
	}

	public String getTargetTypeName() {
		return targetTypeName;
	}

	public void setTargetTypeName(String targetTypeName) {
		this.targetTypeName = targetTypeName;
	}

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	public String getTargetDefine() {
		return targetDefine;
	}

	public void setTargetDefine(String targetDefine) {
		this.targetDefine = targetDefine;
	}
}