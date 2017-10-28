package com.wf.uic.common.utils;

import com.wf.uic.rpc.dto.JsonResult;
import org.springframework.validation.Errors;

public class JsonResultUtils {

    public static final String IS_SUCCESS = "true";
    public static final String IS_ERROR = "false";

    public static JsonResult markSuccessResult() {
        return markJSONResult(IS_SUCCESS, null, "");
    }

    public static JsonResult markSuccessResult(Object data, String resultInfo) {
        return markJSONResult(IS_SUCCESS, data, resultInfo);
    }

    public static JsonResult markErrorResult(String errorCoder) {
        String errorMessgae = errorCoder;
        return markJSONResult(IS_ERROR, null, errorCoder);
    }
    
    public static JsonResult markErrorResult(Object data,String errorCoder) {
        String errorMessgae = "";
        return markJSONResult(IS_ERROR, data, errorCoder);
    }

    public static JsonResult markErrorResult(Errors errors) {
        StringBuffer message = new StringBuffer();
        return markJSONResult(IS_ERROR, null, message.toString());
    }

    private static <T> JsonResult<T> markJSONResult(String isSuccess, T data, String resultInfo) {
        JsonResult<T> jsonResult = new JsonResult<T>();
        jsonResult.setSuccess(isSuccess);
        jsonResult.setHasSuccess(IS_SUCCESS.endsWith(isSuccess));
        jsonResult.setData(data);
        jsonResult.setResultInfo(resultInfo);
        return jsonResult;
    }

}