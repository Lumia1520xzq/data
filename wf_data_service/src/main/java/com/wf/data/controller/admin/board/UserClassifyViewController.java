package com.wf.data.controller.admin.board;

import com.alibaba.fastjson.JSONObject;
import com.wf.core.utils.type.StringUtils;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.common.constants.GameDataConstants;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.datarepo.entity.DatawareFinalRechargeTagAnalysis;
import com.wf.data.service.data.DatawareFinalRechargeTagAnalysisService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 用户分层分析
 *
 * @author JoeH
 */
@RestController
@RequestMapping("/data/user/classify")
public class UserClassifyViewController extends ExtJsController {

    @Autowired
    private DatawareFinalRechargeTagAnalysisService rechargeTagAnalysisService;

    private String tmp = "-";

    /**
     * 用户分层数据
     */
    @RequestMapping("/getList")
    public Object getList() {
        Map<String, Object> map = new HashMap<>();
        JSONObject json = getRequestJson();
        Long parentId = null;
        String startTime = null;
        String endTime = null;
        JSONObject data = json.getJSONObject("data");
        if (data != null) {
            parentId = data.getLong(GameDataConstants.PARENTID);
            startTime = data.getString("startTime");
            endTime = data.getString("endTime");
        }
        if (StringUtils.isBlank(startTime)) {
            startTime = DateUtils.formatDate(DateUtils.getNextDate(new Date(), -14));
        }
        if (StringUtils.isBlank(endTime)) {
            endTime = DateUtils.getYesterdayDate();
        }

        Map<String, Object> params = new HashMap<>(3);
        if (null == parentId) {
            parentId = 1L;
        }
        params.put(GameDataConstants.PARENTID, parentId);

        params.put("beginDate", startTime);
        params.put("endDate", endTime);
        //获取日期list
        List<String> dateList = rechargeTagAnalysisService.getDateList(params);


        List<String> simpleDateList = new ArrayList<>();
        for (String date : dateList) {
            int index = date.indexOf(tmp);
            simpleDateList.add(date.substring(index + 1));
        }

        map.put("dateList", simpleDateList);
        //获取dateList最后一天的日期
        String lastDate = DateUtils.getYesterdayDate();
        if (CollectionUtils.isNotEmpty(dateList)) {
            lastDate = dateList.get(dateList.size() - 1);
        }
        //截取次日留存的日期
        List<String> dayRetentionDateList = dateList;
        if (!DateUtils.parseDate(lastDate).before(DateUtils.getYesterday()) && CollectionUtils.isNotEmpty(dateList)) {
            dayRetentionDateList = dateList.subList(0, dateList.size() - 1);
        }

        List<String> simpleDayRetentionDateList = new ArrayList<>();
        for (String date : dayRetentionDateList) {
            int index = date.indexOf(tmp);
            simpleDayRetentionDateList.add(date.substring(index + 1));
        }

        map.put("dayRetentionDateList", simpleDayRetentionDateList);


        map.put("weekRetentionDateList", getWeekRetentionDateList(dateList, lastDate));


        map.put("weekLostDateList", getWeekLostDateList(dateList, lastDate));

        // 循环取每个userTag对应的list0
        for (int index = 0; index <= 6; index++) {
            params.put("userTag", index);
            List<DatawareFinalRechargeTagAnalysis> list = rechargeTagAnalysisService.getListByTagAndDate(params);
            map.put(Integer.toString(index), list);
        }
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(map);
        return list;
    }

    private List<String> getWeekRetentionDateList(List<String> dateList, String lastDate) {
        //截取七日留存的日期
        List<String> weekRetentionDateList = dateList;
        if (!DateUtils.parseDate(lastDate).before(DateUtils.parseDate(DateUtils.getPrevDate(DateUtils.getYesterdayDate(), 6))) && CollectionUtils.isNotEmpty(dateList)) {
            if (dateList.size() >= 6) {
                weekRetentionDateList = dateList.subList(0, dateList.size() - 6);
            } else {
                weekRetentionDateList = new ArrayList<>();
            }
        }

        List<String> simpleWeekRetentionDateList = new ArrayList<>();
        for (String date : weekRetentionDateList) {
            int index = date.indexOf(tmp);
            simpleWeekRetentionDateList.add(date.substring(index + 1));
        }
        return simpleWeekRetentionDateList;

    }

    private List<String> getWeekLostDateList(List<String> dateList, String lastDate) {
        //七日流失的日期
        List<String> weekLostDateList = dateList;
        if (!DateUtils.parseDate(lastDate).before(DateUtils.parseDate(DateUtils.getPrevDate(DateUtils.getYesterdayDate(), 7))) && CollectionUtils.isNotEmpty(dateList)) {
            if (dateList.size() >= 7) {
                weekLostDateList = dateList.subList(0, dateList.size() - 7);
            } else {
                weekLostDateList = new ArrayList<>();
            }
        }

        List<String> simpleWeekLostDateList = new ArrayList<>();
        for (String date : weekLostDateList) {
            int index = date.indexOf(tmp);
            simpleWeekLostDateList.add(date.substring(index + 1));
        }

        return simpleWeekLostDateList;

    }


}
