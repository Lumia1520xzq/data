package com.wf.uic.common.hander;

import com.wf.core.utils.Global;
import com.wf.uic.controller.request.callback.CheetahClearBean;
import com.wf.uic.controller.request.callback.CheetahClearLoginRspBean;
import com.wf.uic.controller.request.callback.CheetahClearRspBean;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

@Service
public class CheetahClearHander implements InitializingBean {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private static final String LOGIN_URI = "/api/v1/accounts/getUser/";
	private String baseUri;

	public CheetahClearLoginRspBean login(CheetahClearBean bean){
		JSONObject result = request(LOGIN_URI, bean);
		
		CheetahClearLoginRspBean rsp = initRsp(new CheetahClearLoginRspBean(), result);
		if (rsp.success()) {
			JSONObject data = result.getJSONObject("data").getJSONObject("userinfo");
			String mobile = data.getString("phone");
			String nickName = data.getString("nickname");
			if (mobile != null && mobile.length() == 11 && (StringUtils.isBlank(nickName) || nickName.endsWith("****"))){
				nickName = mobile.substring(0, 4) + "***" + mobile.substring(7, 11);
			}
			rsp.setHeadImg(data.getString("avatar"));
			rsp.setNickName(nickName);
			rsp.setUserId(data.getString("accountid"));
			if(mobile != null && mobile.length() == 11){
				rsp.setPhone(data.getString("phone"));
			}
		}
		return rsp;
	}
	
	/**
     * 请求
     * @param uri
     * @param bean
     * @return
     */
    private JSONObject request(String uri, CheetahClearBean bean) {
        try {
            bean.getBody().put("token", bean.getToken());
            bean.getBody().put("accountid", bean.getUserId());

            logger.info("request:" + bean.getBody());

            JsonNode object = Unirest.post(baseUri + uri)
                    .header("Content-Type", "application/json")
                    .body(bean.getBody())
                    .asJson().getBody();
            
            if (logger.isInfoEnabled()) {
				logger.info("response:" + object.toString());
			}
            return object.getObject();
        } catch (Exception e) {
            throw new RuntimeException("请求猎豹清理失败：" + uri, e);
        }
    }
    
    /**
     * 初始化响应
     * @param rsp
     * @param result
     * @return
     */
    private <T extends CheetahClearRspBean> T initRsp(T rsp, JSONObject result) {
        rsp.setCode(result.getInt("code"));
        if (!rsp.success()) {
			rsp.setMessage(result.getString("msg"));
		}
        return rsp;
    }

	@Override
	public void afterPropertiesSet() throws Exception {
		baseUri = Global.getConfig("cheetah.clear.uri");
	}
}
