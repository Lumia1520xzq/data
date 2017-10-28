package com.wf.uic.common.hander;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.wf.uic.common.mapper.JsonMapper;
import com.wf.uic.controller.request.callback.OkoooLoginBean;
import com.wf.uic.controller.request.callback.OkoooResBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@Service
public class OkoooHander{
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Value("${request.okooo.app_key}")
	private String appKey;
	@Value("${request.okooo.app_secret}")
	private String appSecret;
	@Value("${request.okooo.login}")
	private String login;

	public OkoooLoginBean login(OkoooResBean bean){
		bean.setAppKey(appKey);
		bean.setAppSecret(appSecret);
		
		JSONObject json = loginRequest(login, bean);
		if(json == null){
			logger.error("获取澳客用户信息失败。。。");
			return null;
		}else{
			try {
				String data = json.getJSONObject("object").toString();;
				OkoooLoginBean loginBean = (OkoooLoginBean) JsonMapper.fromJsonString(data, OkoooLoginBean.class);
				return loginBean;
			} catch (Exception e) {
				logger.error("json转换失败", e);
				return null;
			}
			
		}
	}

	/**
	 * 服务端校验用户信息
	 * 请求
	 * @param uri
	 * @param bean
	 * @return
	 */
	public JSONObject loginRequest(String uri, OkoooResBean bean) {
		Map<String, Object> parms = new HashMap<>();
		parms.put("token", bean.getToken());
		parms.put("app_key", bean.getAppKey());
		parms.put("app_secret", bean.getAppSecret());
		long index = Index.nextId();
		try {
			uri +="&app_key=" +bean.getAppKey();
			uri +="&app_secret=" +bean.getAppSecret();
			uri +="&token=" +bean.getToken();

			logger.info(index + "\trequest:" + uri);
			JsonNode response = Unirest.get(uri).asJson().getBody();
			if(response.getObject() == null){
				logger.warn(index + "\tresponse为空");
				return null;
			}else{
				JSONObject object = new JSONObject(response);
				logger.info(index + "\tresponse is ok!");
				return object;
			}
		} catch (Exception e) {
			logger.info("请求数据异常", e);
			return null;
		}
	}

	private static class Index {
		private static long index;

		public static long nextId() {
			return ++index;
		}
	}


}
