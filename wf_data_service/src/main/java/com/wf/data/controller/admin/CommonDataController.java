package com.wf.data.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.wf.core.persistence.Page;
import com.wf.core.utils.file.FastDFSUtils;
import com.wf.core.utils.type.MapUtils;
import com.wf.core.utils.type.NumberUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.data.entity.DataDict;
import com.wf.data.dao.mall.entity.InventoryPhyAwardsInfo;
import com.wf.data.dao.mycatuic.entity.UicUser;
import com.wf.data.dao.trans.entity.PayAgentMerchant;
import com.wf.data.service.ChannelInfoService;
import com.wf.data.service.DataConfigService;
import com.wf.data.service.DataDictService;
import com.wf.data.service.UicUserService;
import com.wf.data.service.mall.InventoryPhyAwardsInfoService;
import com.wf.data.service.trans.PayAgentMerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 通用数据接口
 *
 * @author zwf
 */
@RestController
@RequestMapping("/data/admin/common/data")
public class CommonDataController extends ExtJsController {

    @Autowired
    private ChannelInfoService channelInfoService;
    @Autowired
    private UicUserService uicUserService;
    @Autowired
    private DataConfigService dataConfigService;
    @Autowired
    private DataDictService dataDictService;
    @Autowired
    private PayAgentMerchantService payAgentMerchantService;
    @Autowired
    private InventoryPhyAwardsInfoService inventoryPhyAwardsInfoService;

    /**
     * 获取所有的渠道
     *
     * @return
     */
    @RequestMapping("/getAllChannels")
    public Object getAllChannels() {
        JSONObject json = getRequestJson();

        String keyword = null;
        JSONObject data = json.getJSONObject("data");
        if (data != null) {
            keyword = data.getString("data");
        }
        ChannelInfo dto = new ChannelInfo();
        if (StringUtils.isNotBlank(keyword)) {
            keyword = keyword.trim();
            if (NumberUtils.isDigits(keyword)) {
                dto.setId(NumberUtils.toLong(keyword));
            } else {
                dto.setName(keyword);
            }
        }

        List<ChannelInfo> list = channelInfoService.findList(dto, 1000);

        for (ChannelInfo cInfo : list) {
            cInfo.setName(cInfo.getName() + "(" + cInfo.getId() + ")");
        }
        return list;
    }

    /**
     * 获取主渠道
     *
     * @return
     */
    @RequestMapping("/getParentChannels")
    public Object getParentChannels() {
        JSONObject json = getRequestJson();
        String keyword = null;
        JSONObject data = json.getJSONObject("data");
        if (data != null) {
            keyword = data.getString("data");
        }
        ChannelInfo dto = new ChannelInfo();
        if (StringUtils.isNotBlank(keyword)) {
            keyword = keyword.trim();
            if (NumberUtils.isDigits(keyword)) {
                dto.setId(NumberUtils.toLong(keyword));
            } else {
                dto.setName(keyword);
            }
        }
        dto.setEnable(1);
        dto.setMainChannel(1L);
        List<ChannelInfo> list = channelInfoService.findList(dto, 1000);

        for (ChannelInfo cInfo : list) {
            cInfo.setName(cInfo.getName() + "(" + cInfo.getId() + ")");
        }
        return list;
    }

    /**
     * 获取子渠道
     *
     * @return
     */
    @RequestMapping("/getChildChannels")
    public Object getChildChannels() {
        JSONObject json = getRequestJson();

        String keyword = null;
        JSONObject data = json.getJSONObject("data");
        if (data != null) {
            keyword = data.getString("parentId");
        }
        ChannelInfo dto = new ChannelInfo();
        if (StringUtils.isNotBlank(keyword)) {
            keyword = keyword.trim();
            if (NumberUtils.isDigits(keyword)) {
                dto.setParentId(Long.parseLong(keyword));
            } else {
                dto.setName(keyword);
            }
        }
        if (data != null) {
            String channelName = data.getString("name");
            if(StringUtils.isNotBlank(channelName)){
                dto.setName(channelName);
            }
        }
        dto.setEnable(1);
        List<ChannelInfo> list = channelInfoService.findList(dto, 1000);
        if (null != dto.getParentId()) {
            ChannelInfo info = channelInfoService.get(dto.getParentId());
            list.add(0, info);
        }
        for (ChannelInfo cInfo : list) {
            cInfo.setName(cInfo.getName() + "(" + cInfo.getId() + ")");
        }

        return list;
    }

    /**
     * 获取渠道注册用户列表
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"channelUserList"})
    public Object channelUserList(HttpServletRequest request) {
        UicUser user = new UicUser();
        JSONObject json = getRequestJson();

        String keyword = null;
        Long parentId = null;
        JSONObject data = json.getJSONObject("data");
        if (data != null && data.size() > 0) {
            keyword = data.getString("data");
            parentId = data.getLong("parentId");
        }

        if (null != parentId) {
            user.setRegParentChannel(parentId);
        }

        if (StringUtils.isNotBlank(keyword)) {
            keyword = keyword.trim();
            if (NumberUtils.isDigits(keyword)) {
                user.setId(NumberUtils.toLong(keyword));
            } else {
                user.setNickname(keyword);
            }
        }
        Page<UicUser> page = uicUserService.findPage(new Page<UicUser>(user));
        return page;
    }

    /**
     * 获取用户列表
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"userList"})
    public Object findUserList(HttpServletRequest request) {
        UicUser user = new UicUser();
        JSONObject json = getRequestJson();

        String keyword = null;
        JSONObject data = json.getJSONObject("data");
        if (data != null && data.size() > 0) {
            keyword = data.getString("data");
        }

        if (StringUtils.isNotBlank(keyword)) {
            keyword = keyword.trim();
            if (NumberUtils.isDigits(keyword)) {
                user.setId(NumberUtils.toLong(keyword));
            } else {
                user.setNickname(keyword);
            }
        }
        Page<UicUser> page = uicUserService.findPage(new Page<>(user));
        return page;
    }

    @RequestMapping("/getViewChannels")
    public Object getViewChannels() {
        JSONObject json = getRequestJson();
        String keyword = null;
        JSONObject data = json.getJSONObject("data");
        if (data != null) {
            keyword = data.getString("data");
        }
        ChannelInfo dto = new ChannelInfo();
        List<ChannelInfo> list = new ArrayList<>();
        if (StringUtils.isNotBlank(keyword)) {
            keyword = keyword.trim();
            if (NumberUtils.isDigits(keyword)) {
                dto.setId(NumberUtils.toLong(keyword));
            } else {
                dto.setName(keyword);
            }
            dto.setEnable(1);
            dto.setMainChannel(1L);
            list = channelInfoService.findList(dto, 1000);
        } else {
            String channelIdList = dataConfigService.getStringValueByName(DataConstants.DATA_DESTINATION_COLLECTING_CHANNEL);
            if (StringUtils.isNotBlank(channelIdList)) {
                List<String> channels = Arrays.asList(channelIdList.split(","));
                for (String channel : channels) {
                    ChannelInfo info = channelInfoService.get(Long.parseLong(channel));
                    list.add(info);
                }
                ChannelInfo special = new ChannelInfo();
                special.setId(0L);
                special.setName("其他");
                list.add(special);
            }
        }
        for (ChannelInfo cInfo : list) {
            cInfo.setName(cInfo.getName() + "(" + cInfo.getId() + ")");
        }
        return list;
    }

    @RequestMapping("/getFilterChannels")
    public Object getFilterChannels() {
        JSONObject json = getRequestJson();
        String keyword = null;
        JSONObject data = json.getJSONObject("data");
        if (data != null) {
            keyword = data.getString("data");
        }
        ChannelInfo dto = new ChannelInfo();
        List<ChannelInfo> list = new ArrayList<>();
        if (StringUtils.isNotBlank(keyword)) {
            keyword = keyword.trim();
            if (NumberUtils.isDigits(keyword)) {
                dto.setId(NumberUtils.toLong(keyword));
            } else {
                dto.setName(keyword);
            }
            dto.setEnable(1);
            dto.setMainChannel(1L);
            list = channelInfoService.findList(dto, 1000);
        } else {
            String channelIdList = dataConfigService.getStringValueByName(DataConstants.DATA_DESTINATION_COLLECTING_CHANNEL);
            if (StringUtils.isNotBlank(channelIdList)) {
                List<String> channels = Arrays.asList(channelIdList.split(","));
                for (String channel : channels) {
                    ChannelInfo info = channelInfoService.get(Long.parseLong(channel));
                    list.add(info);
                }
            }
        }
        for (ChannelInfo cInfo : list) {
            cInfo.setName(cInfo.getName() + "(" + cInfo.getId() + ")");
        }
        return list;
    }

    @RequestMapping("/getUserType")
    public Object getUserType(HttpServletRequest request) {
        List<DataDict> dictList = new ArrayList<>();
        String type = request.getParameter("type");
        JSONObject json = getRequestJson();
        Integer signal = null;
        JSONObject data = json.getJSONObject("data");
        if (data != null && data.size() > 0) {
            signal = data.getInteger("signal");
        }
        if (signal != null && signal > 0) {
            DataDict dict = dataDictService.getDictByValue(type, 0);
            dictList.add(dict);
            return dictList;
        }
        dictList = dataDictService.findListByType(type);
        return dictList;
    }

    @RequestMapping("/getPayAgentMerchants")
    public Object getPayAgentMerchants() {
        PayAgentMerchant payAgentMerchant = new PayAgentMerchant();
        JSONObject json = getRequestJson();

        String keyword = null;
        JSONObject data = json.getJSONObject("data");
        if (data != null && data.size() > 0) {
            keyword = data.getString("data");
        }

        if (StringUtils.isNotBlank(keyword)) {
            keyword = keyword.trim();
            payAgentMerchant.setMerchantCode(keyword);
        }
        return payAgentMerchantService.findPage(new Page<>(payAgentMerchant));
    }

    @RequestMapping("/awardsInfoList")
    public Object awardsInfoList() {
        InventoryPhyAwardsInfo inventoryPhyAwardsInfo = new InventoryPhyAwardsInfo();
        JSONObject json = getRequestJson();

        String keyword = null;
        JSONObject data = json.getJSONObject("data");
        if (data != null && data.size() > 0) {
            keyword = data.getString("data");
        }

        if (StringUtils.isNotBlank(keyword)) {
            keyword = keyword.trim();
            if (NumberUtils.isDigits(keyword)) {
                inventoryPhyAwardsInfo.setId(NumberUtils.toLong(keyword));
            } else {
                inventoryPhyAwardsInfo.setName(keyword);
            }
        }
        return inventoryPhyAwardsInfoService.findPage(new Page<>(inventoryPhyAwardsInfo));
    }

    @RequestMapping("/getValueByType")
    public Object getValueByType() {
        JSONObject json = getRequestJson();
        String param = null;
        JSONObject data = json.getJSONObject("data");
        if (data != null) {
            param = data.getString("type");
        }
        List<DataDict> dataDictDtoList = dataDictService.getDictList(param);
        return dataDictDtoList;
    }



    @RequestMapping("/uploadWord")
    public Object uploadWord(MultipartFile wordFile) {
        String path = FastDFSUtils.uploadFile(wordFile);
        String domainUri = FastDFSUtils.getDomainUri(path);
        System.out.println(path + "------------------" + domainUri);
        return success(MapUtils.toMap("data", path, "url", FastDFSUtils.getDomainUri(path)));
    }


}
