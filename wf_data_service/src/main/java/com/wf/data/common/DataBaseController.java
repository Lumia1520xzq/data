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
import com.wf.uic.rpc.UserRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * @author zk
 */
public class DataBaseController extends BaseController {

    @Resource
    private ChannelRpcService channelRpcService;
    @Resource
    private UserRpcService userRpcService;
    @Autowired
    private EhcacheManager ehcacheManager;
    /**
     * 删除过期数据间隔，单位秒
     */
    private static final int EXPIRE_INTERVAL = 300;

    /**
     * 获取当前渠道
     *
     * @return
     */
    protected Long getChannelId() {
        String shopChannel = getAppChannel();
        if (StringUtils.isBlank(shopChannel)) {
            //默认多多互娱
            return ChannelConstants.JDD_CHANNEL;
        }
        shopChannel = shopChannel.split("#")[0];
        if (!NumberUtils.isDigits(shopChannel)) {
            throw new ChannelErrorException(getAppChannel());
        }
        ChannelInfoDto channelInfoDto = getChannelById(Long.parseLong(shopChannel));
        return channelInfoDto.getId();
    }

    /**
     * 获取上级渠道
     *
     * @param channelId
     * @return
     */
    protected Long getParentChannelId(final Long channelId) {
        if (channelId == null) {
            return null;
        }
        ChannelInfoDto channelInfo = getChannelById(channelId);
        return channelInfo.getParentId() == null ? channelId : channelInfo.getParentId();
    }

    /**
     * 获取上级渠道
     * @return
     */
    protected Long getParentChannelId() {
        return getParentChannelId(getChannelId());
    }

    private ChannelInfoDto getChannelById(Long channelId) {
        //判断渠道是否存在
        ChannelInfoDto channelInfoDto = (ChannelInfoDto) ehcacheManager.get(DataCacheKey.CHANNEL_INFO.key(channelId));
        if (channelInfoDto == null) {
            channelInfoDto = channelRpcService.get(channelId);
            if (channelInfoDto == null || channelInfoDto.getEnable() == DataConstants.NO) {
                throw new ChannelErrorException(channelId.toString());
            }
            ehcacheManager.set(DataCacheKey.CHANNEL_INFO.key(channelId), channelInfoDto, EXPIRE_INTERVAL);
        }
        return channelInfoDto;
    }

    /**
     * 获取str user
     * @return
     */
    protected Long getUserId() {
        String token = super.getToken();
        Long userId = (Long) ehcacheManager.get(UicCacheKey.OAUTH2_TOKEN_INFO.key(token));
        if (userId == null) {
            userId = userRpcService.getUserId(token);
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
            userId = userRpcService.getUserId(token);
            if (userId != null) {
                ehcacheManager.set(UicCacheKey.OAUTH2_TOKEN_INFO.key(token), userId, EXPIRE_INTERVAL);
            }
        }
        return userId;
    }

}
