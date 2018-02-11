package com.wf.data.controller.admin.business;

import com.alibaba.fastjson.JSONObject;
import com.wf.core.persistence.Page;
import com.wf.core.utils.type.NumberUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.common.utils.DateUtils;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shihui
 * @date 2018/2/9
 */

@RestController
@RequestMapping("/data/admin/business/transFragmentChange")
public class TransFragmentChangeController extends ExtJsController {

    @Autowired
    private TransFragmentChangeLogService fragmentChangeLogService;

    @Autowired
    private ActivityInfoService activityInfoService;

    @Autowired
    private UicUserService mycatUicUserService;

    @Autowired
    private TransFragmentService transFragmentService;

    @RequestMapping("list")
    public Object list() {

        JSONObject json = getRequestJson();

        Long parentId = null;
        Long channelId = null;
        Long userId = null;
        Integer changeType = null;
        Integer businessType = null;
        String beginDate = null;
        String endDate = null;
        Long activityType = null;
        Long fragmentType = null;

        JSONObject data = json.getJSONObject("data");
        if (data != null) {
            parentId = data.getLong("parentId");
            channelId = data.getLong("channelId");
            userId = data.getLong("userId");
            changeType = data.getInteger("changeType");
            businessType = data.getInteger("businessType");
            beginDate = data.getString("beginDate");
            endDate = data.getString("endDate");
            activityType = data.getLong("activityType");
            fragmentType = data.getLong("fragmentType");
        }


        //设置默认搜索时间为昨天
        if (StringUtils.isBlank(beginDate) || StringUtils.isBlank(endDate)) {
            beginDate = DateUtils.getYesterdayDate() + " 00:00:00";
            endDate = DateUtils.getYesterdayDate() + " 23:59:59";
        }

        String sStart = json.getString("start");
        String sLength = json.getString("limit");
        Integer start = 0;
        Integer length = 0;
        if (sStart != null) {
            start = Integer.parseInt(sStart);
        }
        if (sLength != null) {
            length = Integer.parseInt(sLength);
        }

        TransFragmentChangeLog paramBean = new TransFragmentChangeLog();
        paramBean.setParentId(parentId);
        paramBean.setChannelId(channelId);
        paramBean.setUserId(userId);
        paramBean.setChangeType(changeType);
        paramBean.setBusinessType(businessType);
        paramBean.setBeginDate(beginDate);
        paramBean.setEndDate(endDate);
        paramBean.setActivityId(activityType);
        paramBean.setFragmentType(fragmentType);

        //根据活动类别和活动渠道找到活动的id
        Long channelIdParam = paramBean.getParentId();
        Integer activityTypeParam = activityType == null ? null : activityType.intValue();
        List<Long> activityIds = activityInfoService.getListByActivityTypeAndChannelId(activityTypeParam, channelIdParam);
        Page<TransFragmentChangeLog> changeLogPage = new Page<TransFragmentChangeLog>(paramBean, start, length);

        //不存在活动id时，返回空白页面
        if (CollectionUtils.isEmpty(activityIds)) {
            changeLogPage.setCount(0);
            changeLogPage.setData(new ArrayList<TransFragmentChangeLog>());
            return dataGrid(changeLogPage);
        }

        paramBean.setActivityIds(activityIds);
        changeLogPage = fragmentChangeLogService.findPage(changeLogPage);

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

        List<TransFragmentChangeLog> changeLogList = fragmentChangeLogService.findList(changeLog, 99999999);
        for (TransFragmentChangeLog temp : changeLogList) {
            if (temp.getFragmentId() != null) {
                TransFragment awardsInfo = transFragmentService.get(temp.getFragmentId());
                rmbAmount += (awardsInfo.getRmbAmount() * temp.getChangeNum());
            }
        }
        return rmbAmount;
    }

    /**
     * 获取所有碎片类型
     * @return
     */
    @RequestMapping("getAllFragmentType")
    public Object getAllFragmentType(){
        JSONObject json = getRequestJson();

        String keyword = null;
        JSONObject data = json.getJSONObject("data");
        if (data != null) {
            keyword = data.getString("data");
        }
        TransFragment dto = new TransFragment();
        if (StringUtils.isNotBlank(keyword)) {
            keyword = keyword.trim();
            if (NumberUtils.isDigits(keyword)) {
                dto.setId(NumberUtils.toLong(keyword));
            } else {
                dto.setName(keyword);
            }
        }
        return transFragmentService.findList(dto,1000);
    }

}
