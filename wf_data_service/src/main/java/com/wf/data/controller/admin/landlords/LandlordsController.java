package com.wf.data.controller.admin.landlords;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import com.wf.core.utils.type.BigDecimalUtil;
import com.wf.core.utils.type.DateUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.dto.LandlordsDto;
import com.wf.data.service.data.DatawareBuryingPointDayService;
import com.wf.data.service.landlords.LandlordsUserAmountLogService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 斗地主
 *
 * @author lcs
 */
@RestController
@RequestMapping("/data/admin/landlords")
public class LandlordsController extends ExtJsController {

    @Autowired
    private LandlordsUserAmountLogService landlordsUserAmountLogService;
    @Autowired
    private DatawareBuryingPointDayService datawareBuryingPointDayService;


    /**
     * 根据类型获取列表
     *
     * @return
     */
    @RequestMapping("/getList")
    public Object getList() {
        JSONObject json = getRequestJson();
        List<LandlordsDto> list = Lists.newArrayList();
        Long parentId = null;
        List<Integer> deskTypes = null;
        String startTime = null;
        String endTime = null;
        JSONObject data = json.getJSONObject("data");
        if (data != null) {

            JSONArray arr = data.getJSONArray("deskType");
            String js = JSONObject.toJSONString(arr, SerializerFeature.WriteClassName);
            deskTypes = JSONObject.parseArray(js, Integer.class);

            parentId = data.getLong("parentId");
            startTime = data.getString("startTime");
            endTime = data.getString("endTime");
        }

        if (StringUtils.isBlank(startTime) && StringUtils.isBlank(endTime)) {
            startTime = DateUtils.getYesterdayDate();
            endTime = startTime;
        } else if (StringUtils.isBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            startTime = endTime;
        } else if (StringUtils.isNotBlank(startTime) && StringUtils.isBlank(endTime)) {
            endTime = startTime;
        }

        if (CollectionUtils.isEmpty(deskTypes)) {
            deskTypes = Lists.newArrayList();
            deskTypes.add(0);
            deskTypes.add(1);
            deskTypes.add(2);
            deskTypes.add(3);
            deskTypes.add(4);
        }

        List<String> dateList = DateUtils.getDateList(startTime, endTime);

        if (deskTypes.contains(0)) {
            for (String searchDate : dateList) {
                LandlordsDto dto = new LandlordsDto();
                Map<String, Object> map = new HashMap<>();
                map.put("searchDate", searchDate);
                if (null != parentId) {
                    map.put("parentId", parentId);
                }
                map.put("gameType", 15);
                dto.setSearchDate(searchDate);
                dto.setDeskType(0);
                dto.setDeskTypeName("全部");
                Integer dau = datawareBuryingPointDayService.getGameDau(map);
                dto.setDauCount(dau);

                Map<String, Object> landMap = new HashMap<>();
                landMap.put("beginDate", DateUtils.formatDate(DateUtils.getDayStartTime(DateUtils.parseDate(searchDate, "yyyy-MM-dd")), "yyyy-MM-dd HH:mm:ss"));
                landMap.put("endDate", DateUtils.formatDate(DateUtils.getDayEndTime(DateUtils.parseDate(searchDate, "yyyy-MM-dd")), "yyyy-MM-dd HH:mm:ss"));
                landMap.put("parentId", parentId);


                Double tableAmount = landlordsUserAmountLogService.getTableAmount(landMap);
                dto.setTableAmount(tableAmount);

                LandlordsDto bettingDto = landlordsUserAmountLogService.getBettingInfo(landMap);
                if (null != bettingDto) {
                    dto.setUserCount(bettingDto.getUserCount());
                    dto.setBettingAmount(bettingDto.getBettingAmount());
                    dto.setResultAmount(bettingDto.getResultAmount());
                    dto.setToolsAmount(bettingDto.getToolsAmount());
                    if (dto.getDauCount() != null && dto.getDauCount() != 0) {
                        dto.setConversionRate(BigDecimalUtil.div(bettingDto.getUserCount() * 100, dto.getDauCount(), 2) + "%");
                    }

                    if (null != dto.getBettingAmount() && dto.getBettingAmount() != 0) {
                        dto.setReturnRate(BigDecimalUtil.div(dto.getResultAmount() * 100, dto.getBettingAmount(), 2) + "%");
                    }

                    if (null != dto.getUserCount() && dto.getUserCount() != 0) {
                        dto.setBettingArpu(BigDecimalUtil.div(dto.getBettingAmount(), dto.getUserCount(), 2));
                        Double amount = dto.getBettingAmount() - dto.getResultAmount();
                        dto.setAmountDiffArpu(BigDecimalUtil.div(amount, dto.getUserCount(), 2));
                    }

                }


                Double toolsAmount = landlordsUserAmountLogService.getToolsAmount(landMap);

                dto.setToolsAmount(toolsAmount);

                Integer gameTimes = landlordsUserAmountLogService.getGameTimes(landMap);
                dto.setGameTimes(gameTimes);

                if (null != dto.getUserCount() && dto.getUserCount() != 0) {
                    dto.setAvgGameTimes(BigDecimalUtil.div(dto.getGameTimes(), dto.getUserCount(), 2));
                }

                if (null != dto.getGameTimes() && dto.getGameTimes() != 0) {
                    dto.setBettingAsp(BigDecimalUtil.div(dto.getBettingAmount(), dto.getGameTimes(), 2));
                }

                list.add(dto);
            }
        }

        if (deskTypes.contains(1) || deskTypes.contains(2) || deskTypes.contains(3) || deskTypes.contains(4)) {
            List<LandlordsDto> deskList = Lists.newArrayList();
            Map<String, Object> map = new HashMap<>();
            if (null != parentId) {
                map.put("parentId", parentId);
            }
            map.put("deskTypes", deskTypes);
            for (String searchDate : dateList) {
                map.put("beginDate", DateUtils.formatDate(DateUtils.getDayStartTime(DateUtils.parseDate(searchDate, "yyyy-MM-dd")), "yyyy-MM-dd HH:mm:ss"));
                map.put("endDate", DateUtils.formatDate(DateUtils.getDayEndTime(DateUtils.parseDate(searchDate, "yyyy-MM-dd")), "yyyy-MM-dd HH:mm:ss"));
                List<LandlordsDto> dtoList = landlordsUserAmountLogService.getLandlordList(map);
                deskList.addAll(dtoList);
            }

            for (LandlordsDto landlordsDto : deskList) {
                if (landlordsDto.getDeskType() == 1) {
                    landlordsDto.setDeskTypeName("新手场");
                } else if (landlordsDto.getDeskType() == 2) {
                    landlordsDto.setDeskTypeName("初级场");
                } else if (landlordsDto.getDeskType() == 3) {
                    landlordsDto.setDeskTypeName("中级场");
                } else if (landlordsDto.getDeskType() == 4) {
                    landlordsDto.setDeskTypeName("高级场");
                } else {
                    landlordsDto.setDeskTypeName(String.valueOf(landlordsDto.getDeskType()));
                }

                if (landlordsDto.getDauCount() != null && landlordsDto.getDauCount() != 0) {
                    landlordsDto.setConversionRate(BigDecimalUtil.div(landlordsDto.getUserCount(), landlordsDto.getDauCount(), 2) + "%");
                }

                if (null != landlordsDto.getBettingAmount() && landlordsDto.getBettingAmount() > 0) {
                    landlordsDto.setReturnRate(BigDecimalUtil.div(landlordsDto.getResultAmount() * 100, landlordsDto.getBettingAmount(), 2) + "%");
                }

                if (null != landlordsDto.getUserCount() && landlordsDto.getUserCount() != 0) {
                    landlordsDto.setBettingArpu(BigDecimalUtil.div(landlordsDto.getBettingAmount(), landlordsDto.getUserCount(), 2));
                    Double amount = landlordsDto.getBettingAmount() - landlordsDto.getResultAmount();
                    landlordsDto.setAmountDiffArpu(BigDecimalUtil.div(amount, landlordsDto.getUserCount(), 2));
                    landlordsDto.setAvgGameTimes(BigDecimalUtil.div(landlordsDto.getGameTimes(), landlordsDto.getUserCount(), 2));
                }

                if (null != landlordsDto.getGameTimes() && landlordsDto.getGameTimes() != 0) {
                    landlordsDto.setBettingAsp(BigDecimalUtil.div(landlordsDto.getBettingAmount(), landlordsDto.getGameTimes(), 2));
                }
            }
            list.addAll(deskList);


        }
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                LandlordsDto dto1 = (LandlordsDto) o1;
                LandlordsDto dto2 = (LandlordsDto) o2;
                if (DateUtils.parseDate(dto1.getSearchDate()).getTime() > DateUtils.parseDate(dto2.getSearchDate()).getTime()) {
                    return 1;
                } else if (DateUtils.parseDate(dto1.getSearchDate()).getTime() == DateUtils.parseDate(dto2.getSearchDate()).getTime()) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });

        return list;
    }


}
