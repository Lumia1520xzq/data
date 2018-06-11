package com.wf.data.controller.admin.thirdChannel;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wf.core.cache.CacheHander;
import com.wf.core.cache.CacheKey;
import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.excel.ExportExcel;
import com.wf.core.utils.http.HttpClientUtils;
import com.wf.core.utils.type.BigDecimalUtil;
import com.wf.core.utils.type.StringUtils;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.common.constants.DataCacheKey;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.appuic.entity.AppUicChannelInfo;
import com.wf.data.dto.FishDto;
import com.wf.data.dto.FishInOut;
import com.wf.data.service.appuic.AppUicChannelInfoService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/data/admin/appfish")
public class AppFishController extends ExtJsController {

    @Autowired
    protected CacheHander cacheHander;
    @Autowired
    private AppUicChannelInfoService appUicChannelInfoService;

    /**
     * 实物成本查询
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("list")
    public Object listData() {
        JSONObject json = getRequestJson();
        JSONObject data = json.getJSONObject("data");
        Long channelId = null;
        Long parentId = null;
        String beginDate = null;
        String endDate = null;

        if (data != null) {
            channelId = data.getLong("channelId");
            parentId = data.getLong("parentId");
            beginDate = data.getString("beginDate");
            endDate = data.getString("endDate");
        }
        //设置默认搜索时间为昨天
        if (StringUtils.isBlank(beginDate) || StringUtils.isBlank(endDate)) {
            beginDate = DateUtils.formatDate(DateUtils.getNextDate(new Date(), -6));
            endDate = DateUtils.getDate();
        }
        return getFishRecord(channelId, parentId, beginDate, endDate);
    }


    /**
     * 导出用户数据
     *
     * @return
     */
    @RequestMapping(value = "export")
    public void exportFile(@RequestParam String parentId, @RequestParam String channelId, @RequestParam String beginDate,
                           @RequestParam String endDate, HttpServletResponse response) {

        if (StringUtils.isNotBlank(beginDate)) {
            beginDate = DateUtils.formatGTMDate(beginDate, "yyyy-MM-dd");
        }


        if (StringUtils.isNotBlank(endDate)) {
            endDate = DateUtils.formatGTMDate(endDate, "yyyy-MM-dd");
        }
        //设置默认搜索时间为昨天
        if (StringUtils.isBlank(beginDate) || StringUtils.isBlank(endDate)) {
            beginDate = DateUtils.formatDate(DateUtils.getNextDate(new Date(), -7));
            endDate = DateUtils.getYesterdayDate();
        } else {

        }
        Long cId = null;
        Long pId = null;

        if (StringUtils.isNotBlank(parentId)) {
            pId = new Long(parentId);
        }
        if (StringUtils.isNotBlank(channelId)) {
            cId = new Long(channelId);
        }

        try {
            List<FishDto> resultList = getFishRecord(cId, pId, beginDate, endDate);
            String fileName = "捕鱼数据报表" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel("捕鱼数据报表", FishDto.class).setDataList(resultList).write(response, fileName).dispose();
        } catch (Exception e) {
            logger.error("导出失败：" + e.getMessage());
        }


    }


    /**
     * 导出用户数据
     *
     * @return
     */
    @RequestMapping(value = "channelExportFile")
    public void channelExportFile(@RequestParam String parentId, @RequestParam String channelId, @RequestParam String beginDate,
                                  @RequestParam String endDate, HttpServletResponse response) {

        if (StringUtils.isNotBlank(beginDate)) {
            beginDate = DateUtils.formatGTMDate(beginDate, "yyyy-MM-dd");
        }


        if (StringUtils.isNotBlank(endDate)) {
            endDate = DateUtils.formatGTMDate(endDate, "yyyy-MM-dd");
        }
        //设置默认搜索时间为昨天
        if (StringUtils.isBlank(beginDate) || StringUtils.isBlank(endDate)) {
            beginDate = DateUtils.formatDate(DateUtils.getNextDate(new Date(), -7));
            endDate = DateUtils.getYesterdayDate();
        } else {

        }
        Long cId = null;
        Long pId = null;

        if (StringUtils.isNotBlank(parentId)) {
            pId = new Long(parentId);
        }
        if (StringUtils.isNotBlank(channelId)) {
            cId = new Long(channelId);
        }

        try {
            List<FishDto> resultList = getFishRecord(cId, pId, beginDate, endDate);
            String fileName = "捕鱼数据报表" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel("捕鱼数据报表", FishInOut.class).setDataList(resultList).write(response, fileName).dispose();
        } catch (Exception e) {
            logger.error("导出失败：" + e.getMessage());
        }


    }


    private List<FishDto> getFishRecord(Long channelId, Long parentId, String beginDate, String endDate) {
        List<FishDto> resultList = Lists.newArrayList();

        List<FishDto> list = request();


        for (FishDto dto : list) {
            if (StringUtils.isNotBlank(beginDate)) {
                if (DateUtils.parseDate(dto.getDates()).getTime() < DateUtils.parseDate(beginDate).getTime()) {
                    continue;
                }
            }
            if (StringUtils.isNotBlank(endDate)) {
                if (DateUtils.parseDate(dto.getDates()).getTime() > DateUtils.parseDate(endDate).getTime()) {
                    continue;
                }
            }
            if (channelId != null) {
                if (channelId.longValue() == dto.getChannel().longValue()) {

                    resultList.add(calculate(dto));
                }
            } else {
                if (parentId != null) {
                    if (dto.getChannel().toString().startsWith(parentId.toString())) {
                        resultList.add(calculate(dto));
                    }
                } else {
                    resultList.add(calculate(dto));
                }
            }

        }

        Collections.sort(resultList, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                FishDto dto1 = (FishDto) o1;
                FishDto dto2 = (FishDto) o2;
                if (DateUtils.parseDate(dto1.getDates()).getTime() > DateUtils.parseDate(dto2.getDates()).getTime()) {
                    return 1;
                } else if (DateUtils.parseDate(dto1.getDates()).getTime() == DateUtils.parseDate(dto2.getDates()).getTime()) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });
        return resultList;
    }

    private FishDto calculate(FishDto dto) {
        if (dto.getChannel() != null) {
            AppUicChannelInfo channelInfo = appUicChannelInfoService.get(dto.getChannel());
            if (channelInfo != null) {
                dto.setChannelName(channelInfo.getName() + "(" + channelInfo.getId() + ")");
            } else {
                dto.setChannelName(dto.getChannel().toString());
            }
        }


        //投注转化率=投注人数/登陆人数
        if (dto.getLognum() == 0) {
            dto.setBettingRate(0.00);
        } else {
            dto.setBettingRate(BigDecimalUtil.div(dto.getBetmem() * 100, dto.getLognum(), 2));
        }
        //ARPU=充值金额/DAU
        if (dto.getLognum() == 0) {
            dto.setBettingArpu(0.00);
        } else {
            dto.setBettingArpu(BigDecimalUtil.div(dto.getPayamount().doubleValue(), dto.getLognum(), 2));

        }

        //返奖率 = 返奖流水/投注流水
        if (dto.getBetamt() == 0) {
            dto.setResultRate(0.00);
        } else {
            dto.setResultRate(BigDecimalUtil.div(dto.getResultamt() * 100, dto.getBetamt(), 2));

        }

        //流水差=投注流水-返奖金额
        Long bettingDiff = dto.getBetamt() - dto.getResultamt();
        dto.setBettingDiff(bettingDiff);

        return dto;
    }


    /**
     * 请求
     *
     * @return
     */
    private List<FishDto> request() {
        String uri = "http://10.103.5.201:8088/appfish/record";
        List<FishDto> list = Lists.newArrayList();
        try {
            List<FishDto> fishList = cacheHander.get(DataCacheKey.DATA_APP_FISH_LIST.key(DateUtils.formatCurrentDateYMD()));
            if (CollectionUtils.isEmpty(fishList)) {
                String body = HttpClientUtils.post(uri, logger);
                if (StringUtils.isBlank(body)) {
                    throw new RuntimeException("send HTTP Request to get menu data failed. request:" + uri);
                }

                list = GfJsonUtil.parseArray(body, FishDto.class);
                cacheHander.set(DataCacheKey.DATA_APP_FISH_LIST.key(DateUtils.formatCurrentDateYMD()), list, CacheKey.MINUTE_10);

            } else {
                list.addAll(fishList);
            }


        } catch (Exception e) {
            logger.error("请求数据异常:uri={}, ex={}, traceId={}", GfJsonUtil.toJSONString(uri), LogExceptionStackTrace.erroStackTrace(e), TraceIdUtils.getTraceId());
        }
        return list;
    }


}
