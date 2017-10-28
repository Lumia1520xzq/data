package com.wf.uic.common;

import com.wf.base.rpc.ChannelRpcService;
import com.wf.base.rpc.dto.ChannelInfoDto;
import com.wf.core.utils.type.StringUtils;
import com.wf.core.web.base.BaseController;
import com.wf.uic.common.bean.UserInfo;
import com.wf.uic.common.constants.ChannelConstants;
import com.wf.uic.common.hander.LoginHander;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;

/**
 * 基础 Controller
 * @author fxy
 */
@Component
public class UicBaseController extends BaseController {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private ChannelRpcService channelRpcService;
    @Autowired
    protected LoginHander userLoginHander;

    /**
     * 获取用户信息
     * @param
     * @return
     */
    protected UserInfo getUserInfo(String token) {
        UserInfo userInfo = userLoginHander.getUserInfo(token);
        if (userInfo == null) {
            throw new LbmOAuthException();
        }
        return userInfo;
    }

    /**
     * 获取用户信息
     * @param
     * @return
     */
    protected UserInfo getUserInfo() {
        return getUserInfo(getToken());
    }

    /**
     * 获取userId
     *
     * @return
     */
    protected Long getUserId() {
        return userLoginHander.getUserId(getToken());
    }

    /**
     * 获取渠道ID
     *
     * @return
     */
    protected Long getChannelId() {
        String channel = getAppChannel();
        if (StringUtils.isBlank(channel)) {
            //默认多多互娱
            return ChannelConstants.JDD_CHANNEL;
        } else {
            channel = channel.split("#")[0];
            ChannelInfoDto channelInfoDto = channelRpcService.get(Long.parseLong(channel));
            if (channelInfoDto == null || channelInfoDto.getEnable() == 0) {
                throw new ChannelErrorException();
            }
        }
        return Long.parseLong(channel);
    }

    /**
     * 获取父级渠道Id,没有父级，显示自己Id
     *
     * @return
     */
    protected Long getParentChannelId() {
        return getParentChannelId(getChannelId());
    }

    /**
     * 获取父级渠道Id,没有父级，显示自己Id
     *
     * @return
     */
    protected Long getParentChannelId(final Long channelId) {
        ChannelInfoDto channelInfoDto = channelRpcService.get(getChannelId());
        return channelInfoDto.getParentId() == null ? channelId : channelInfoDto.getParentId();
    }

}