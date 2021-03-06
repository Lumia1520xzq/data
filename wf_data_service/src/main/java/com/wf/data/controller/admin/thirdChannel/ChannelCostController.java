package com.wf.data.controller.admin.thirdChannel;

import com.alibaba.fastjson.JSONObject;
import com.wf.core.persistence.Page;
import com.wf.core.utils.excel.ExportExcel;
import com.wf.core.utils.type.StringUtils;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.mall.entity.AwardsSendLogInOut;
import com.wf.data.dao.mall.entity.InventoryPhyAwardsInfo;
import com.wf.data.dao.mall.entity.InventoryPhyAwardsSendlog;
import com.wf.data.dao.mycatuic.entity.UicUser;
import com.wf.data.service.UicUserService;
import com.wf.data.service.mall.ActivityInfoService;
import com.wf.data.service.mall.InventoryPhyAwardsInfoService;
import com.wf.data.service.mall.InventoryPhyAwardsSendlogService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/data/admin/channel/cost")
public class ChannelCostController extends ExtJsController {

    @Autowired
    private InventoryPhyAwardsSendlogService inventoryPhyAwardsSendlogService;
    @Autowired
    private InventoryPhyAwardsInfoService inventoryPhyAwardsInfoService;
    @Autowired
    private ActivityInfoService activityInfoService;
    @Autowired
    private UicUserService mycatUicUserService;

    /**
     * 实物成本查询
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("list")
    public Object listData() {
        JSONObject json = getRequestJson();
        Long channelId = null;
        Long parentId = null;
        Long userId = null;
        Integer activityType = null;
        String beginDate = null;
        String endDate = null;
        Long start = null;
        Long length = null;
        Long phyAwardsId = null;

        JSONObject data = json.getJSONObject("data");
        if (data != null) {
            channelId = data.getLong("channelId");
            parentId = data.getLong("parentId");
            userId = data.getLong("userId");
            activityType = data.getInteger("activityType");
            phyAwardsId = data.getLong("phyAwardsId");
            beginDate = data.getString("beginDate");
            endDate = data.getString("endDate");
            start = json.getLongValue("start");
            length = json.getLongValue("limit");
        }

        //设置默认搜索时间为昨天
        if (StringUtils.isBlank(beginDate) || StringUtils.isBlank(endDate)) {
            beginDate = DateUtils.getYesterdayDate() + " 00:00:00";
            endDate = DateUtils.getYesterdayDate() + " 23:59:59";
        }

        InventoryPhyAwardsSendlog awardsSendLog = new InventoryPhyAwardsSendlog();
        awardsSendLog.setChannelId(channelId);
        awardsSendLog.setParentId(parentId);
        awardsSendLog.setUserId(userId);
        awardsSendLog.setActivityType(activityType);
        awardsSendLog.setBeginDate(beginDate);
        awardsSendLog.setEndDate(endDate);
        awardsSendLog.setReceiveStatus(3);
        awardsSendLog.setPhyAwardsId(phyAwardsId);

        //根据活动类别和活动渠道找到活动的id
        Page<InventoryPhyAwardsSendlog> awardsSendLogPage = new Page<>(awardsSendLog, start, length);

        /*Long channelId = awardsSendLog.getParentId();
        List<Long> activityIds = activityInfoService.getListByActivityTypeAndChannelId(activityType, channelId);
        if (CollectionUtils.isEmpty(activityIds)) {
            awardsSendLogPage.setCount(0);
            awardsSendLogPage.setData(new ArrayList<>());
            return dataGrid(awardsSendLogPage);
        }
        awardsSendLog.setActivityIds(activityIds);*/
        awardsSendLogPage = inventoryPhyAwardsSendlogService.findPage(awardsSendLogPage);
        List<InventoryPhyAwardsSendlog> sendlogList = awardsSendLogPage.getData();
        for (InventoryPhyAwardsSendlog temp : sendlogList) {
            if (temp.getUserId() != null) {
                UicUser user = mycatUicUserService.get(temp.getUserId());
                if (user != null) {
                    temp.setUserName(user.getNickname());
                    temp.setThirdId(user.getThirdId());
                }
            }
            temp.setActivityName(activityInfoService.get(temp.getActivityId()).getName());
            if (temp.getPhyAwardsId() != null) {
                InventoryPhyAwardsInfo awardsInfo = inventoryPhyAwardsInfoService.get(temp.getPhyAwardsId());
                temp.setPhyAwardsName(awardsInfo.getName());
                temp.setRmbAmount(awardsInfo.getRmbAmount());
                temp.setGoldAmount(awardsInfo.getGoldAmount());
            }
        }
        return dataGrid(awardsSendLogPage);
    }

    @RequestMapping("sumData")
    public Object countData(@RequestBody InventoryPhyAwardsSendlog awardsSendLog) {
        Double rmbAmount = 0.0;
        Long goodsAmount = 0L;

        //根据活动类别和活动渠道找到活动的id
        Long channelId = awardsSendLog.getParentId();
        List<Long> activityIds = activityInfoService.getListByActivityTypeAndChannelId(awardsSendLog.getActivityType(), channelId);
        if (CollectionUtils.isEmpty(activityIds)) {
            return 0D;
        }
        /*awardsSendLog.setActivityIds(activityIds);*/
        List<InventoryPhyAwardsSendlog> sendlogList = inventoryPhyAwardsSendlogService.findList(awardsSendLog, 99999999);
        for (InventoryPhyAwardsSendlog temp : sendlogList) {
            if (temp.getPhyAwardsId() != null) {
                InventoryPhyAwardsInfo awardsInfo = inventoryPhyAwardsInfoService.get(temp.getPhyAwardsId());
                rmbAmount += awardsInfo.getRmbAmount();
                goodsAmount += awardsInfo.getGoldAmount();
            }
        }
        return rmbAmount;
    }

    /**
     * 导出用户数据
     *
     * @return
     */
    @RequestMapping(value = "export")
    public void exportFile(@RequestParam String parentId, @RequestParam String userId,
                           @RequestParam String activityType, @RequestParam String beginDate,
                           @RequestParam String endDate, HttpServletResponse response) {

        InventoryPhyAwardsSendlog awardsSendLog = new InventoryPhyAwardsSendlog();
        if (StringUtils.isNotBlank(parentId)) {
            awardsSendLog.setParentId(Long.parseLong(parentId));
        }
        if (StringUtils.isNotBlank(userId)) {
            awardsSendLog.setUserId(Long.parseLong(userId));
        }
        if (StringUtils.isNotBlank(activityType)) {
            awardsSendLog.setActivityType(Integer.parseInt(activityType));
        }
        if (StringUtils.isNotBlank(beginDate)) {
            awardsSendLog.setBeginDate(com.wf.data.common.utils.DateUtils.formatGTMDate(beginDate, "yyyy-MM-dd HH:mm:ss"));
        }
        if (StringUtils.isNotBlank(endDate)) {
            awardsSendLog.setEndDate(com.wf.data.common.utils.DateUtils.formatGTMDate(beginDate, "yyyy-MM-dd HH:mm:ss"));
        }
        awardsSendLog.setReceiveStatus(3);
        try {
            List<AwardsSendLogInOut> list = new ArrayList<AwardsSendLogInOut>();
            //根据活动类别和活动渠道找到活动的id
            Integer activityTypeIn = awardsSendLog.getActivityType();
            Long channelId = awardsSendLog.getParentId();
            List<InventoryPhyAwardsSendlog> sendlogList = inventoryPhyAwardsSendlogService.findList(awardsSendLog, 99999999);
            /*awardsSendLog.setActivityIds(activityIds);*/
            for (InventoryPhyAwardsSendlog temp : sendlogList) {
                AwardsSendLogInOut out = new AwardsSendLogInOut();
                if (temp.getUserId() != null) {
                    UicUser user = mycatUicUserService.get(temp.getUserId());
                    if (user != null) {
                        out.setUserName(user.getNickname());
                    }
                }
                temp.setActivityName(activityInfoService.get(temp.getActivityId()).getName());
                out.setActivityName(activityInfoService.get(temp.getActivityId()).getName());
                if (temp.getPhyAwardsId() != null) {
                    InventoryPhyAwardsInfo awardsInfo = inventoryPhyAwardsInfoService.get(temp.getPhyAwardsId());
                    out.setPhyAwardsName(awardsInfo.getName());
                    out.setRmbAmount(awardsInfo.getRmbAmount());
                    out.setGoldAmount(awardsInfo.getGoldAmount());

                }
                out.setUpdateTime(temp.getSendTime());
                out.setUserId(temp.getUserId());

                list.add(out);
            }
            String fileName = "用户实物成本" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel("用户实物成本", AwardsSendLogInOut.class).setDataList(list).write(response, fileName).dispose();
        } catch (Exception e) {
            logger.error("导出失败：" + e.getMessage());
        }
    }

}
