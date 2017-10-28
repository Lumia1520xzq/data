package com.wf.uic.common.oauth;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import com.qq.connect.QQConnectException;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.oauth.Oauth;
import com.qq.connect.utils.QQConnectConfig;
import com.qq.connect.utils.RandomStatusGenerator;
import com.qq.connect.utils.http.PostParameter;
import com.wf.core.cache.CacheHander;
import com.wf.uic.common.constants.UicCacheKey;

public class WapQQOauth extends Oauth {
	private static final long serialVersionUID = 1L;
	private CacheHander cacheHander;

	public WapQQOauth(CacheHander cacheHander) {
		this.cacheHander = cacheHander;
	}

	public String getAuthorizeURL(Long channelId, String source) throws QQConnectException {
		String state = RandomStatusGenerator.getUniqueState();
		cacheHander.set(UicCacheKey.QQ_OAUTH.key(state), CacheHander.Y);
		cacheHander.set(UicCacheKey.QQ_OAUTH_CHANNEL.key(state), channelId);
		cacheHander.set(UicCacheKey.QQ_OAUTH_SOURCE.key(state), source);
		String scope = QQConnectConfig.getValue("scope");
		if ((scope != null) && (!"".equals(scope))) {
			return getAuthorizeURL("code", state, scope);
		}
		return QQConnectConfig.getValue("authorizeURL").trim() + "?client_id="
				+ QQConnectConfig.getValue("app_ID").trim() + "&redirect_uri="
				+ QQConnectConfig.getValue("redirect_URI").trim() + "&response_type="
				+ "code" + "&state=" + state;
	}

	public Map<String,Object> getAccessToken(ServletRequest request) throws QQConnectException {
		Map<String,Object> result = new HashMap<>();
		String queryString = ((HttpServletRequest) request).getQueryString();
		if (queryString == null) {
			result.put("accessToken",  new AccessToken());
			return result;
		}
		String[] authCodeAndState = extractionAuthCodeFromUrl(queryString);
		String returnState = authCodeAndState[1];
		String returnAuthCode = authCodeAndState[0];
		if (("".equals(returnState)) || ("".equals(returnAuthCode))){
			result.put("accessToken",  new AccessToken());
			return result;
		}
		String userUUID = cacheHander.get(UicCacheKey.QQ_OAUTH.key(returnState));
		if (userUUID == null){
			result.put("accessToken",  new AccessToken());
			return result;
		}
		Long channelId = cacheHander.get(UicCacheKey.QQ_OAUTH_CHANNEL.key(returnState));
		String source = cacheHander.get(UicCacheKey.QQ_OAUTH_SOURCE.key(returnState));
		cacheHander.delete(UicCacheKey.QQ_OAUTH.key(returnState));
		cacheHander.delete(UicCacheKey.QQ_OAUTH_CHANNEL.key(returnState));
		cacheHander.delete(UicCacheKey.QQ_OAUTH_SOURCE.key(returnState));
		AccessToken accessTokenObj = new AccessToken(this.client.post(QQConnectConfig.getValue("accessTokenURL"),
				new PostParameter[] { new PostParameter("client_id", QQConnectConfig.getValue("app_ID")),
						new PostParameter("client_secret", QQConnectConfig.getValue("app_KEY")),
						new PostParameter("grant_type", "authorization_code"),
						new PostParameter("code", returnAuthCode),
						new PostParameter("redirect_uri", QQConnectConfig.getValue("redirect_URI")) },
				Boolean.valueOf(false)));
		result.put("accessToken", accessTokenObj);
		result.put("channelId", channelId);
		result.put("source", source);
		return result;
	}
	
	private String[] extractionAuthCodeFromUrl(String url) throws QQConnectException {
		if (url == null) {
			throw new QQConnectException("you pass a null String object");
		}
		Matcher m = Pattern.compile("code=(\\w+)&state=(\\w+)&?").matcher(url);
		String authCode = "";
		String state = "";
		if (m.find()) {
			authCode = m.group(1);
			state = m.group(2);
		}
		return new String[] { authCode, state };
	}
}
