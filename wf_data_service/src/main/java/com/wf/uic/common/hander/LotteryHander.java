package com.wf.uic.common.hander;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.wf.core.utils.Global;
import com.wf.core.utils.encrypt.GameAesEncrypt;
import com.wf.core.utils.encrypt.MD5Util;
import com.wf.uic.common.bean.lottery.LotteryBean;
import com.wf.uic.common.bean.lottery.LotteryLoginRspBean;
import com.wf.uic.common.bean.lottery.LotteryRspBean;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
public class LotteryHander implements InitializingBean {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String LOGIN_URI = "/game/getUserInfo";
    private static String projectKey;
    private String baseUri;
    private String aesEncryptKey;

    /**
     * 登录
     *
     * @param bean
     * @return
     */
    public LotteryLoginRspBean login(LotteryBean bean) {
        bean.getBody().put("UserID", bean.getUserId());
        bean.getBody().put("Action", "");
        bean.getBody().put("PlatformCode", bean.getPlatformCode());
        bean.getBody().put("TimeStamp", bean.getTimeStamp());
        bean.getBody().put("AppVersion", bean.getAppVersion());

        JSONObject result = request(LOGIN_URI, bean);
        LotteryLoginRspBean rsp = initRsp(new LotteryLoginRspBean(), result);
        if (rsp.success()) {
            JSONObject data = result.getJSONObject("bean");
            String mobile = data.getString("UserMobile");
            String nickName = data.getString("NickName");
            if (mobile != null && mobile.length() == 11 && (StringUtils.isBlank(nickName) || nickName.endsWith("****"))) {
                nickName = mobile.substring(0, 4) + "***" + mobile.substring(7, 11);
            }
            rsp.setHeadImg(data.getString("UserFace"));
            rsp.setNickName(nickName);
            rsp.setUserId(data.getString("UserId"));
        }
        return rsp;
    }

    /**
     * 初始化响应
     *
     * @param rsp
     * @param result
     * @return
     */
    private <T extends LotteryRspBean> T initRsp(T rsp, JSONObject result) {
        rsp.setCode(result.getInt("code"));
        if (!rsp.success()) {
            rsp.setMessage(result.getString("msg"));
        }
        return rsp;
    }

    /**
     * 请求
     *
     * @param uri
     * @param bean
     * @return
     */
    private JSONObject request(String uri, LotteryBean bean) {
        try {
            bean.getBody().put("UserType", bean.getUserType());
            bean.getBody().put("Token", bean.getToken());

            logger.info("request:" + bean.getBody());
            String aesRequest = GameAesEncrypt.aesEncrypt(bean.getBody().toString(), aesEncryptKey, "UTF-8");

            JsonNode object = Unirest.post(baseUri + uri)
                    .header("Content-Type", "application/json")
                    .body("request=" + aesRequest)
                    .asJson().getBody();

            if (logger.isInfoEnabled()) {
                logger.info("response:" + object.toString());
            }
            return object.getObject();
        } catch (Exception e) {
            throw new RuntimeException("请求金山彩票失败：" + uri, e);
        }
    }

    public static String getProjectKey() {
        return projectKey;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        projectKey = MD5Util.getUpperMD5(Global.getConfig("js.lottery.project_key"));
        baseUri = Global.getConfig("js_lottery.uri");
        aesEncryptKey = Global.getConfig("js_lottery.aes.encrypt_key");
    }

}
