package com.wf.data.dao.data.entity;

import com.wf.core.persistence.DataEntity;
import com.wf.core.utils.excel.ExcelField;

import java.util.Date;

public class GrowTest extends DataEntity {
	private static final long serialVersionUID = -1;
	@ExcelField(title="测试点", align = 1, sort = 10)
	private String point;
	private Long testTypeId;
	@ExcelField(title="测试类型", align = 1, sort = 20)
	private String testTypeName;
	private String docUrl;
	@ExcelField(title="测试渠道", align = 1, sort = 30)
	private String testChannel;
	@ExcelField(title="测试目标", align = 1, sort = 40)
	private String target;
	@ExcelField(title="用户群(ID尾号)", align = 1, sort = 50)
	private String userIds;
	@ExcelField(title="测试结果", align = 1, sort = 60)
	private String result;
	@ExcelField(title="维护人", align = 1, sort = 100)
	private String operationUsername;
	@ExcelField(title="开始时间", align = 1, sort = 70)
	private Date startTime;
	@ExcelField(title="结束时间", align = 1, sort = 80)
	private Date endTime;
	private String beginDate;
	private String docShowUrl;
	@ExcelField(title="维护日期", align = 1, sort = 90)
	private String showUpdateTime;



	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public Long getTestTypeId() {
		return testTypeId;
	}

	public void setTestTypeId(Long testTypeId) {
		this.testTypeId = testTypeId;
	}

	public String getTestTypeName() {
		return testTypeName;
	}

	public void setTestTypeName(String testTypeName) {
		this.testTypeName = testTypeName;
	}

	public String getDocUrl() {
		return docUrl;
	}

	public void setDocUrl(String docUrl) {
		this.docUrl = docUrl;
	}

	public String getTestChannel() {
		return testChannel;
	}

	public void setTestChannel(String testChannel) {
		this.testChannel = testChannel;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getOperationUsername() {
		return operationUsername;
	}

	public void setOperationUsername(String operationUsername) {
		this.operationUsername = operationUsername;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getDocShowUrl() {
		return docShowUrl;
	}

	public void setDocShowUrl(String docShowUrl) {
		this.docShowUrl = docShowUrl;
	}



	public String getShowUpdateTime() {
		return showUpdateTime;
	}

	public void setShowUpdateTime(String showUpdateTime) {
		this.showUpdateTime = showUpdateTime;
	}
}