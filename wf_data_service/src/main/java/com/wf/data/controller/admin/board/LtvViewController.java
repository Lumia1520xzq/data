package com.wf.data.controller.admin.board;

import com.alibaba.fastjson.JSONObject;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.datarepo.entity.DatawareFinalChannelInfoAll;
import com.wf.data.service.ChannelInfoService;
import com.wf.data.service.data.DatawareFinalChannelInfoAllService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 用户ltv分析
 *
 * @author JoeH
 */
@RestController
@RequestMapping("/data/ltv/view")
public class LtvViewController extends ExtJsController {

    @Autowired
    private ChannelInfoService channelInfoService;
    @Autowired
    private DatawareFinalChannelInfoAllService channelInfoAllService;

    /**
     * 用户ltv分析
     */
    @RequestMapping("/getList")
    public Object getList() {
        JSONObject json = getRequestJson();
        String startTime = null;
        String endTime = null;
        String channelStr = null;
        JSONObject data = json.getJSONObject("data");
        if (data != null) {
            startTime = data.getString("startTime");
            endTime = data.getString("endTime");
            channelStr = data.getString("channels");
        }
        try {
            if (StringUtils.isBlank(startTime) && StringUtils.isBlank(endTime)) {
                startTime = DateUtils.formatDate(DateUtils.getNextDate(new Date(), -30));
                endTime = DateUtils.getYesterdayDate();
            } else if (StringUtils.isBlank(startTime) && StringUtils.isNotBlank(endTime)) {
                startTime = endTime;
            } else if (StringUtils.isNotBlank(startTime) && StringUtils.isBlank(endTime)) {
                endTime = startTime;
            }
        } catch (Exception e) {
            logger.error("查询条件转换失败: traceId={}, data={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(data));
        }
        Map<String, Object> params = new HashMap<>(3);
        params.put("beginDate",startTime);
        params.put("endDate",endTime);
        List<Long> channels;
        if(null == channelStr){
            channels = Arrays.asList(new Long[]{1L,100001L,100006L,100015L,100016L});
        }else {
            channels = JSONObject.parseArray(channelStr,long.class);
        }
        Map<String,Object> map = new HashMap<>();
        //遍历channels
        List<String> channelNames = new ArrayList<>();
        List<Double> ltvList = new ArrayList<>();
        String businessDate = "";
        for(Long channel:channels){
            //1、获取所有的渠道名称
            ChannelInfo channelInfo = channelInfoService.get(channel);
            if(null ==  channelInfo){
                channelNames.add("全平台");
            }else{
                channelNames.add(channelInfo.getName());
            }
            //2、获取几天的ltv值
            params.put("parentId", channel);
            List<DatawareFinalChannelInfoAll> infoAllList = channelInfoAllService.getListByChannelAndDate(params);
            map.put(""+channel,infoAllList);
            //3、获取最后一天的ltv值
            if(CollectionUtils.isNotEmpty(infoAllList)){
                DatawareFinalChannelInfoAll last = infoAllList.get(infoAllList.size()-1);
                businessDate = last.getBusinessDate();
                ltvList.add(last.getUserLtv());
            }else{
                ltvList.add(0.0);
            }
        }
        Collections.reverse(channelNames);
        Collections.reverse(ltvList);
        map.put("channelNames",channelNames);
        map.put("ltv",ltvList);
        map.put("endDate",businessDate);
        List<Map<String,Object>> list = new ArrayList<>();
        list.add(map);
        return list;
    }


}
