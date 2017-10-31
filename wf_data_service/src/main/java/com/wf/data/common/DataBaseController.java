package com.wf.data.common;

import com.wf.base.rpc.ChannelRpcService;
import com.wf.base.rpc.dto.ChannelInfoDto;
import com.wf.core.cache.ehcache.EhcacheManager;
import com.wf.core.utils.type.NumberUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.core.web.base.BaseController;
import com.wf.data.common.constants.ChannelConstants;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.common.constants.UicCacheKey;
import com.wf.uic.rpc.UserLoginRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * @author zk
 */
public abstract class DataBaseController extends BaseController {
    @Resource
    private ChannelRpcService channelRpcService;
    @Resource
    private UserLoginRpcService userLoginRpcService;
    @Autowired
    private EhcacheManager ehcacheManager;

    /**
     * 获取渠道ID
     * @return 渠道ID
     */
    public Long getChannelId() {
        String channel = super.getAppChannel();
        if (StringUtils.isBlank(channel)) {
            //默认多多互娱
            return ChannelConstants.JDD_CHANNEL;
        }
        channel = channel.split("#")[0];
        if (!NumberUtils.isDigits(channel)) {
            throw new ChannelErrorException();
        }
        ChannelInfoDto channelInfo = channelRpcService.get(Long.parseLong(channel));
        if (channelInfo == null || channelInfo.getEnable() == DataConstants.NO) {
            throw new ChannelErrorException();
        }
        return Long.parseLong(channel);
    }

    /**
     * 获取父级渠道Id,没有父级，显示自己Id
     * @return 渠道ID
     */
    protected Long getParentChannelId() {
        return this.getParentChannelId(this.getChannelId());
    }

    /**
     * 获取父级渠道Id,没有父级，显示自己Id
     * @return 渠道ID
     */
    protected Long getParentChannelId(final Long channelId) {
        if (channelId == null) {
            return null;
        }
        ChannelInfoDto channelInfo = channelRpcService.getParentChannel(channelId);;
        if (channelInfo == null) {
            throw new ChannelErrorException();
        }
        return channelInfo.getParentId() == null ? channelId : channelInfo.getParentId();
    }

    /**
     * 获取str user
     * @return
     */
    protected Long getUserId() {
        String token = super.getToken();
        Long userId;
        Object ehcacheVal = ehcacheManager.get(UicCacheKey.OAUTH2_TOKEN_INFO.key(token));
        if(ehcacheVal != null){
            userId = (Long)ehcacheVal;
        }else{
            userId =  userLoginRpcService.getUserId(token);
            if(userId != null){
                ehcacheManager.set(UicCacheKey.OAUTH2_TOKEN_INFO.key(token),userId,3600);
            }
        }
        if (userId == null) {
            throw new LbmOAuthException();
        }
        return userId;
    }

    /**
     * 获取用户id，不抛出未登录异常
     * @return
     */
    protected Long getUserIdNoError() {
        String token = super.getTokenNoError();
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        return this.getUserInfoNoError(token);
    }

    /**
     * 获取用户信息，不抛出未登录异常
     * @param
     * @return
     */
    private Long getUserInfoNoError(String token) {
        Long userId = userLoginRpcService.getUserId(token);
        return userId;
    }

}
