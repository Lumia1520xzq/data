package com.wf.data.common;

import com.wf.base.rpc.ChannelRpcService;
import com.wf.base.rpc.dto.ChannelInfoDto;
import com.wf.core.cache.ehcache.EhcacheManager;
import com.wf.core.utils.type.NumberUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.core.web.base.BaseController;
import com.wf.data.common.constants.ChannelConstants;
import com.wf.data.common.constants.DataCacheKey;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.common.constants.UicCacheKey;
import com.wf.uic.rpc.UserLoginRpcService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zk
 */
public class DataBaseController extends BaseController {

    @Autowired
    private ChannelRpcService channelRpcService;
    @Autowired
    private UserLoginRpcService userLoginRpcService;
    @Autowired
    private EhcacheManager ehcacheManager;
    /**
     * 删除过期数据间隔，单位秒
     */
    private static final int EXPIRE_INTERVAL = 300;

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
        ChannelInfoDto channelInfo = (ChannelInfoDto) ehcacheManager.get(DataCacheKey.CHANNEL_INFO.key(channel));
        if (channelInfo == null) {
            channelInfo = channelRpcService.get(Long.parseLong(channel));
            if (channelInfo == null || channelInfo.getEnable() == DataConstants.NO) {
                throw new ChannelErrorException();
            }
            ehcacheManager.set(DataCacheKey.CHANNEL_INFO.key(channel), channelInfo, EXPIRE_INTERVAL);
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
        ChannelInfoDto channelInfo = (ChannelInfoDto) ehcacheManager.get(channelId.toString());
        if (channelInfo == null) {
            channelInfo = channelRpcService.getParentChannel(channelId);
            if (channelInfo == null) {
                return channelId;
            }
            ehcacheManager.set(channelId.toString(), channelInfo, EXPIRE_INTERVAL);
        }
        return channelInfo.getParentId() == null ? channelId : channelInfo.getParentId();
    }

    /**
     * 获取str user
     * @return
     */
    protected Long getUserId() {
        String token = super.getToken();
        Long userId = (Long) ehcacheManager.get(UicCacheKey.OAUTH2_TOKEN_INFO.key(token));
        if (userId == null) {
            userId = userLoginRpcService.getUserId(token);
            if (userId == null) {
                throw new LbmOAuthException();
            }
            ehcacheManager.set(UicCacheKey.OAUTH2_TOKEN_INFO.key(token), userId, EXPIRE_INTERVAL);
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
        Long userId = (Long) ehcacheManager.get(UicCacheKey.OAUTH2_TOKEN_INFO.key(token));
        if (userId == null) {
            userId = userLoginRpcService.getUserId(token);
            if (userId != null) {
                ehcacheManager.set(UicCacheKey.OAUTH2_TOKEN_INFO.key(token), userId, EXPIRE_INTERVAL);
            }
        }
        return userId;
    }

}
