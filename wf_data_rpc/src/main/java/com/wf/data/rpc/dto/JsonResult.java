package com.wf.data.rpc.dto;

import java.io.Serializable;

/**
 * 统一格式的请求相应格式
 * 
 * @author 
 * @date 2014-7-5下午3:26:41
 */
public class JsonResult<T> implements Serializable {
	private static final long serialVersionUID = 1905122041950251207L;

	/**
	 * 请求处理是否成功
	 */
	protected String success = "true";
	
	/**
     * 请求处理是否成功
     */
    protected boolean hasSuccess = true;

	/**
	 * 请求处理的提示信息 <br>
	 * 当success = true 是服务告知用户的提示信息 <br>
	 * 当success = false 是服务告知用户的错误信息<br>
	 */
	protected String resultInfo;

	/**
	 * 返回给前端的数据
	 */
	protected T aoData;

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public T getData() {
		return aoData;
	}

	public void setData(T data) {
		this.aoData = data;
	}

	public String getResultInfo() {
		return resultInfo;
	}

	public void setResultInfo(String resultInfo) {
		this.resultInfo = resultInfo;
	}

    public boolean isHasSuccess() {
        return hasSuccess;
    }

    public void setHasSuccess(boolean hasSuccess) {
        this.hasSuccess = hasSuccess;
    }
}
