package com.wf.uic.common.oauth;

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

public class AppQQOauth extends Oauth {
	private static final long serialVersionUID = 1L;
	private CacheHander cacheHander;

	public AppQQOauth(CacheHander cacheHander) {
		this.cacheHander = cacheHander;
	}

	@Override
	public String getAuthorizeURL(ServletRequest request) throws QQConnectException {
		String state = RandomStatusGenerator.getUniqueState();
		cacheHander.set(UicCacheKey.APP_QQ_OAUTH.key(state), CacheHander.Y);
		String scope = QQConnectConfig.getValue("scope");
		if ((scope != null) && (!"".equals(scope))) {
			return getAuthorizeURL("code", state, scope);
		}
		return QQConnectConfig.getValue("authorizeURL").trim() + "?client_id="
		+ QQConnectConfig.getValue("app_ID").trim() + "&redirect_uri="
		+ QQConnectConfig.getValue("redirect_URI").trim()
		+ "&response_type=" + "code" + "&state=" + state;
	}

	@Override
	public AccessToken getAccessTokenByRequest(ServletRequest request) throws QQConnectException {
		String queryString = ((HttpServletRequest) request).getQueryString();
		if (queryString == null) {
			return new AccessToken();
		}
		String[] authCodeAndState = extractionAuthCodeFromUrl(queryString);
		String returnState = authCodeAndState[1];
		String returnAuthCode = authCodeAndState[0];
		if (("".equals(returnState)) || ("".equals(returnAuthCode))) {
			return new AccessToken();
		}
		String userUUID = cacheHander.get(UicCacheKey.APP_QQ_OAUTH.key(returnState));
		if (userUUID == null) {
			return new AccessToken();
		}
		cacheHander.delete(UicCacheKey.APP_QQ_OAUTH.key(returnState));
		AccessToken accessTokenObj = new AccessToken(this.client.post(
				QQConnectConfig.getValue("accessTokenURL"), 
				new PostParameter[]{
						new PostParameter("client_id", QQConnectConfig.getValue("app_ID")),
						new PostParameter("client_secret", QQConnectConfig.getValue("app_KEY")),
						new PostParameter("grant_type", "authorization_code"),
						new PostParameter("code", returnAuthCode),
						new PostParameter("redirect_uri", QQConnectConfig.getValue("redirect_URI"))},
				Boolean.valueOf(false)));
		return accessTokenObj;
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
