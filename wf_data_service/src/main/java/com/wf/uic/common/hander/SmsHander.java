package com.wf.uic.common.hander;

import com.wf.core.cache.CacheHander;
import com.wf.core.utils.APIUtils;
import com.wf.core.utils.Global;
import com.wf.core.utils.http.HttpClientUtils;
import com.wf.core.utils.type.DateUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.uic.common.constants.UicCacheKey;
import com.wf.uic.common.exception.SmsServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 发送短信服务
 * @author fxy
 */
@Component
public class SmsHander implements InitializingBean {
    private Logger logger = LoggerFactory.getLogger(SmsHander.class);
    private Logger smsLogger = LoggerFactory.getLogger("smsLogger");
    private static final int SMS_CODE_NUMBER = 6;   //手机验证码位数

    @Autowired
    private CacheHander cacheHander;

    private String url;

    private String username;

    private String password;

    private String signature;

    private String isSendNote;

    private static Map<String, String> codeMap = new HashMap();

    static {
        codeMap.put("101", "无此用户");
        codeMap.put("102", "密码错误");
        codeMap.put("103", "提交过快（提交速度超过流速限制）");
        codeMap.put("104", "系统忙（因平台侧原因，暂时无法处理提交的短信）");
        codeMap.put("105", "敏感短信（短信内容包含敏感词）");
        codeMap.put("106", "消息长度错误（>536或<=0）");
        codeMap.put("107", "包含错误的手机号码");
        codeMap.put("108", "手机号码个数错（群发>50000或<=0）");
        codeMap.put("109", "无发送额度（该用户可用短信数已使用完）");
        codeMap.put("110", "不在发送时间内");
        codeMap.put("113", "extno格式错（非数字或者长度不对）");
        codeMap.put("116", "签名不合法或未带签名（用户必须带签名的前提下）");
        codeMap.put("117", "IP地址认证错,请求调用的IP地址不是系统登记的IP地址");
        codeMap.put("118", "用户没有相应的发送权限（账号被禁止发送）");
        codeMap.put("119", "用户已过期");
        codeMap.put("120", "违反放盗用策略(日发限制) --自定义添加");
        codeMap.put("121", "必填参数。是否需要状态报告，取值true或false");
        codeMap.put("122", "5分钟内相同账号提交相同 消息内容过多");
        codeMap.put("123", "发送类型错误");
        codeMap.put("124", "白模板匹配错误");
        codeMap.put("125", "匹配驳回模板，提交失败");
        codeMap.put("126", "审核通过模板匹配错误");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        url = Global.getConfig("send.url");
        username = Global.getConfig("send.user");
        password = Global.getConfig("send.password");
        signature = Global.getConfig("send.signature");
        isSendNote = Global.getConfig("send.isSendNote");
    }

    /**
     * 验证验证码是否正确
     *
     * @param type     短信的类型 SmsType中定义
     * @param phone    手机号
     * @param smsCode  验证码
     * @return
     */
    public void checkVerifyCode(Integer type, String phone, String smsCode) {
        String cacheCode = cacheHander.get(UicCacheKey.SMS_VERIFY_CODE.key(type, phone));
        //验证码是否正确
        if(smsCode == null || !smsCode.equals(cacheCode)){
            throw new SmsServiceException("验证码不正确");
        }
    }

    /**
     * 发送验证码短信
     *
     * @param type        短信的类型 SmsType中定义
     * @param phone       手机号
     */
    public void sendVerifyCode(Integer type, String phone) {
        if (cacheHander.setNX(UicCacheKey.SMS_VERIFY_CODE_LOCK.key(phone), UicCacheKey.MINUTE_1)) {
            String smsCode = APIUtils.createRandom(true, SMS_CODE_NUMBER);
            cacheHander.set(UicCacheKey.SMS_VERIFY_CODE.key(type, phone), smsCode,UicCacheKey.MINUTE_5 * 6);
            sendSms(phone, "您的验证码是：" + smsCode);
        }else{
            throw new SmsServiceException("操作过于频繁");
        }
    }

    /**
     * 发送消息
     *
     * @param phone 手机号码
     * @param msg   短信内容
     */
    public void sendSms(String phone, String msg) {
        smsLogger.info(phone + ":" + msg);
        String key = UicCacheKey.SMS_DATE_MAX.key(phone, DateUtils.formatCurrentDate(DateUtils.DATE_PATTERN));
        long dateSend = cacheHander.incr(key, UicCacheKey.DAY_2);
        if (dateSend > 10) {
            throw new SmsServiceException("今日发送消息过于频繁");
        }
        if (!"1".equals(isSendNote)) {
            smsLogger.info("短信未下发");
            return;
        }
        // 拼接请求的url
        StringBuilder sb = new StringBuilder(url);
        sb.append("?un=").append(username);
        sb.append("&pw=").append(password);
        sb.append("&phone=").append(phone);
        try {
            sb.append("&msg=").append(URLEncoder.encode(signature + msg, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            smsLogger.error("转码异常", e);
            throw new SmsServiceException("转码异常");
        }
        sb.append("&rd=").append(1);
        // 发起get请求
        String ret = HttpClientUtils.post(sb.toString(), logger);
        if (StringUtils.isNotBlank(ret)) {
            String[] retArr = ret.split(",");
            String content = retArr[1];
            if (StringUtils.isNotBlank(ret)) {
                String[] codeArr = content.split("\n");
                String code = codeArr[0];
                if (!"0".equals(code)) {
                    cacheHander.incrBy(key, -1);
                    throw new SmsServiceException("短信发送失败，原因为:" + codeMap.get(code));
                }
            }
        }
    }

}