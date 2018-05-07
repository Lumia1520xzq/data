package com.wf.data.controller.admin.thirdChannel;

import com.alibaba.fastjson.JSONObject;
import com.wf.core.persistence.Page;
import com.wf.core.utils.excel.ExportExcel;
import com.wf.core.utils.type.StringUtils;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.mall.entity.AwardsSendLogInOut;
import com.wf.data.dao.mycatuic.entity.UicUser;
import com.wf.data.dao.trans.entity.TransFragment;
import com.wf.data.dao.trans.entity.TransFragmentChangeLog;
import com.wf.data.service.UicUserService;
import com.wf.data.service.mall.ActivityInfoService;
import com.wf.data.service.trans.TransFragmentChangeLogService;
import com.wf.data.service.trans.TransFragmentService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/data/admin/channel/fragment")
public class ChannelFragmentController extends ExtJsController {

    @Autowired
    private ActivityInfoService activityInfoService;
    @Autowired
    private UicUserService mycatUicUserService;
    @Autowired
    private TransFragmentChangeLogService transFragmentChangeLogService;
    @Autowired
    private TransFragmentService transFragmentService;

    /**
     * 奖品发放列表
     *
     * @return
     */
    @RequestMapping("list")
    public Object listData() {
        JSONObject json = getRequestJson();
        Long parentId = null;
        Long userId = null;
        Integer activityType = null;
        String beginDate = null;
        String endDate = null;
        Long start = null;
        Long length = null;

        JSONObject data = json.getJSONObject("data");
        if (data != null) {
            parentId = data.getLong("parentId");
            userId = data.getLong("userId");
            activityType = data.getInteger("activityType");
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

        TransFragmentChangeLog fragmentChangeLog = new TransFragmentChangeLog();
        fragmentChangeLog.setParentId(parentId);
        fragmentChangeLog.setUserId(userId);
        fragmentChangeLog.setActivityType(activityType);
        fragmentChangeLog.setBeginDate(beginDate);
        fragmentChangeLog.setEndDate(endDate);

        //根据活动类别和活动渠道找到活动的id
        Long channelId = fragmentChangeLog.getParentId();
        List<Long> activityIds = activityInfoService.getListByActivityTypeAndChannelId(activityType, channelId);
        Page<TransFragmentChangeLog> changeLogPage = new Page<TransFragmentChangeLog>(fragmentChangeLog, start, length);
        //不存在活动id时，返回空白页面
        if (CollectionUtils.isEmpty(activityIds)) {
            changeLogPage.setCount(0);
            changeLogPage.setData(new ArrayList<TransFragmentChangeLog>());
            return dataGrid(changeLogPage);
        }
        fragmentChangeLog.setActivityIds(activityIds);
        fragmentChangeLog.setChangeType(1);
        fragmentChangeLog.setBusinessType(1);//平台签到
        changeLogPage = transFragmentChangeLogService.findPage(changeLogPage);
        List<TransFragmentChangeLog> sendlogList = changeLogPage.getData();
        for (TransFragmentChangeLog temp : sendlogList) {
            if (temp.getUserId() != null) {
                UicUser user = mycatUicUserService.get(temp.getUserId());
                if (user != null) {
                    temp.setUserName(user.getNickname());
                    temp.setThirdId(user.getThirdId());
                }
            }
            temp.setActivityName(activityInfoService.get(temp.getActivityId()).getName());
            if (temp.getFragmentId() != null) {
                TransFragment awardsInfo = transFragmentService.get(temp.getFragmentId());
                temp.setPhyAwardsName(awardsInfo.getName());
                temp.setRmbAmount(awardsInfo.getRmbAmount() * temp.getChangeNum());
                temp.setGoldAmount(awardsInfo.getGoldAmount() * temp.getChangeNum());
            }
        }
        return dataGrid(changeLogPage);
    }

    @RequestMapping("sumData")
    public Object countData(@RequestBody TransFragmentChangeLog changeLog) {
        Double rmbAmount = 0.0;
        //根据活动类别和活动渠道找到活动的id
        Long channelId = changeLog.getParentId();
        List<Long> activityIds = activityInfoService.getListByActivityTypeAndChannelId(changeLog.getActivityType(), channelId);

        if (CollectionUtils.isEmpty(activityIds)) {
            return 0D;
        }
        changeLog.setActivityIds(activityIds);
        changeLog.setChangeType(1);
        changeLog.setBusinessType(1);

        List<TransFragmentChangeLog> changeLogList = transFragmentChangeLogService.findList(changeLog, 99999999);
        for (TransFragmentChangeLog temp : changeLogList) {
            if (temp.getFragmentId() != null) {
                TransFragment awardsInfo = transFragmentService.get(temp.getFragmentId());
                rmbAmount += (awardsInfo.getRmbAmount() * temp.getChangeNum());
            }
        }
       /* tempMap.put("businessAmount", BigDecimalUtil.round(rmbAmount,2));
        renderString(response, tempMap);*/
        return rmbAmount;
    }


    /**
     * 导出用户数据
     *
     * @param response
     * @return
     */
    @RequestMapping(value = "export")
    public void exportFile(@RequestParam String parentId, @RequestParam String userId,
                           @RequestParam String activityType, @RequestParam String beginDate,
                           @RequestParam String endDate, HttpServletResponse response) {

        TransFragmentChangeLog changeLog = new TransFragmentChangeLog();
        if (StringUtils.isNotBlank(parentId)) {
            changeLog.setParentId(Long.parseLong(parentId));
        }
        if (StringUtils.isNotBlank(userId)) {
            changeLog.setUserId(Long.parseLong(userId));
        }
        if (StringUtils.isNotBlank(activityType)) {
            changeLog.setActivityType(Integer.parseInt(activityType));
        }
        if (StringUtils.isNotBlank(beginDate)) {
            changeLog.setBeginDate(com.wf.data.common.utils.DateUtils.formatGTMDate(beginDate, "yyyy-MM-dd HH:mm:ss"));
        }
        if (StringUtils.isNotBlank(endDate)) {
            changeLog.setEndDate(com.wf.data.common.utils.DateUtils.formatGTMDate(beginDate, "yyyy-MM-dd HH:mm:ss"));
        }
        try {
            List<AwardsSendLogInOut> list = new ArrayList<AwardsSendLogInOut>();
            //根据活动类别和活动渠道找到活动的id
            Integer activityTypeIn = changeLog.getActivityType();
            Long channelId = changeLog.getParentId();
            List<Long> activityIds = activityInfoService.getListByActivityTypeAndChannelId(activityTypeIn, channelId);
            List<TransFragmentChangeLog> sendlogList;
            changeLog.setActivityIds(activityIds);
            changeLog.setChangeType(1);
            changeLog.setBusinessType(1);
            sendlogList = transFragmentChangeLogService.findList(changeLog, 99999999);
            for (TransFragmentChangeLog temp : sendlogList) {
                AwardsSendLogInOut out = new AwardsSendLogInOut();
                if (temp.getUserId() != null) {
                    UicUser user = mycatUicUserService.get(temp.getUserId());
                    if (user != null) {
                        out.setUserName(user.getNickname());
                    }
                }
                temp.setActivityName(activityInfoService.get(temp.getActivityId()).getName());
                out.setActivityName(activityInfoService.get(temp.getActivityId()).getName());
                if (temp.getFragmentId() != null) {
                    TransFragment awardsInfo = transFragmentService.get(temp.getFragmentId());
                    out.setPhyAwardsName(awardsInfo.getName());
                    out.setRmbAmount(awardsInfo.getRmbAmount() * temp.getChangeNum());
                    out.setGoldAmount(awardsInfo.getGoldAmount() * temp.getChangeNum());

                }
                out.setUpdateTime(temp.getUpdateTime());
                out.setUserId(temp.getUserId());

                list.add(out);
            }
            String fileName = "用户碎片成本" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel("用户碎片成本", AwardsSendLogInOut.class).setDataList(list).write(response, fileName).dispose();
        } catch (Exception e) {
            logger.error("导出失败：" + e.getMessage());
        }
    }
}
