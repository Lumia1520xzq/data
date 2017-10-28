package com.wf.uic.common.hander;

import com.rabbitmq.client.LongString;
import com.wf.core.cache.CacheHander;
import com.wf.core.cache.CacheKey;
import com.wf.core.cache.ehcache.EhcacheManager;
import com.wf.core.utils.APIUtils;
import com.wf.core.utils.encrypt.Encodes;
import com.wf.core.utils.type.DateUtils;
import com.wf.uic.common.bean.ThirdUserInfo;
import com.wf.uic.common.bean.UserInfo;
import com.wf.uic.common.constants.UicCacheKey;
import com.wf.uic.common.security.Digests;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户登录相关服务
 * @author fxy
 */
@Service
public class LoginHander {
    private Logger logger = LoggerFactory.getLogger(LoginHander.class);
    @Autowired
    private CacheHander cacheHander;
    @Autowired
    private EhcacheManager ehcacheManager;

    /**
     * 获取用户信息
     * @param
     * @return
     */
    public UserInfo getUserInfo(String token) {
        String key = UicCacheKey.OAUTH2_TOKEN_INFO.key(token);
        UserInfo userInfo = (UserInfo)ehcacheManager.get(key);
        if(userInfo == null){
            userInfo = cacheHander.get(key);
            if(userInfo != null){
                ehcacheManager.set(key,userInfo,360);
            }
        }
        return userInfo;
    }

    /**
     * 获取第三方用户信息
     * @param
     * @return
     */
    public ThirdUserInfo getThirdUserInfo(Long userId) {
        String key = UicCacheKey.OAUTH2_THIRD_TOKEN.key(userId);
        ThirdUserInfo userInfo = (ThirdUserInfo)ehcacheManager.get(key);
        if(userInfo == null){
            userInfo = cacheHander.get(key);
            if(userInfo != null){
                ehcacheManager.set(key,userInfo,360);
            }
        }
        return userInfo;
    }

    /**
     * 获取userId
     * @param token
     * @return
     */
    public Long getUserId(String token) {
        UserInfo info = getUserInfo(token);
        return info == null ? null : info.getUserId();
    }

    /**
     * 用户token验证接口
     * @param token
     */
    public boolean tokenVerify(String token) {
        UserInfo vo = getUserInfo(token);
        if(vo == null){
            return false;
        }
        return true;
    }

    /**
     * 获取Oauth2第一步登录使用的token
     * @param userId
     * @return
     */
    public String getRequestToken(Long userId){
        String token = APIUtils.getUUID();
        cacheHander.set(UicCacheKey.OAUTH2_REQUEST_TOKEN.key(token), userId, CacheKey.MINUTE_5);
        return token;
    }

    /**
     * 登陆错误次数缓存KEY
     * @return
     */
    private String errorNumberKey(String username){
        return UicCacheKey.LOGIN_ERROR_NUMBER.key(username, DateUtils.formatCurrentDateYMD());
    }
    /**
     * 获取账号冻结时间缓存KEY
     * @return
     */
    private String errorCurrentTimeKey(String username){
        return UicCacheKey.LOGIN_ERROR_TIMES.key(username, DateUtils.formatCurrentDateYMD());
    }

    /**
     * 自增登陆错误次数并返回
     * @param username
     */
    public Long incrErrorNumber(String username){
        return cacheHander.incr(errorNumberKey(username), CacheKey.HOUR_12);
    }

    /**
     * 获取登陆错误次数
     * @return
     */
    public Long getErrorNumber(String username){
        String string = cacheHander.getString(errorNumberKey(username));
        return string == null ? null : Long.parseLong(string);
    }

    /**
     * 删除登陆错误次数
     * @param username
     */
    public void deleteErrorNumber(String username){
        cacheHander.delete(errorNumberKey(username));
    }

    /**
     * 设置账号冻结时间
     * @param username
     */
    public void setErrorCurrentTime(String username) {
        long currentTime = System.currentTimeMillis() + (12 * 60 * 60 * 1000);
        cacheHander.set(errorCurrentTimeKey(username), currentTime, CacheKey.HOUR_12);
    }

    /**
     * 获取账号冻结时间
     * @param username
     * @return
     */
    public Long getErrorCurrentTime(String username){
        return cacheHander.get(errorCurrentTimeKey(username));
    }

    /**
     * 删除登陆错误锁定时间
     * @param username
     */
    public void deleteErrorCurrentTime(String username){
        cacheHander.delete(errorCurrentTimeKey(username));
    }

}