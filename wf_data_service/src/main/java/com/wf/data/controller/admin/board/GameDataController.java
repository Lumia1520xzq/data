package com.wf.data.controller.admin.board;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import com.wf.core.utils.type.BigDecimalUtil;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.base.entity.ChannelInfo;
import com.wf.data.dao.data.entity.DataDict;
import com.wf.data.dao.datarepo.entity.DatawareFinalGameInfo;
import com.wf.data.service.ChannelInfoService;
import com.wf.data.service.DataDictService;
import com.wf.data.service.data.DatawareFinalGameInfoService;
import jodd.util.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lcs
 * 2018/04/02
 */
@RestController
@RequestMapping(value = "/data/game/data")
public class GameDataController extends ExtJsController {

    @Autowired
    private DatawareFinalGameInfoService datawareFinalGameInfoService;
    @Autowired
    private DataDictService dataDictService;
    @Autowired
    private ChannelInfoService channelInfoService;

    /**
     * 查询列表
     */
    @RequestMapping("/getList")
    public Object list() {
        JSONObject json = getRequestJson();
        String tabId = null;
        List<Long> parentIds = null;
        List<Integer> gameTypes = null;
        String startTime = null;
        String endTime = null;
        JSONObject data = json.getJSONObject("data");

        if (data != null) {
            tabId = data.getString("tabId");

            JSONArray arr = data.getJSONArray("gameType");
            String js = JSONObject.toJSONString(arr, SerializerFeature.WriteClassName);
            gameTypes = JSONObject.parseArray(js, Integer.class);

            JSONArray parentIdArr = data.getJSONArray("parentId");
            String parentIdJs = JSONObject.toJSONString(parentIdArr, SerializerFeature.WriteClassName);
            parentIds = JSONObject.parseArray(parentIdJs, Long.class);
            startTime = data.getString("startTime");
            endTime = data.getString("endTime");
        }

        if (CollectionUtils.isEmpty(parentIds)) {
            //默认显示全渠道
            parentIds.add(1L);
        }
        if (CollectionUtils.isEmpty(gameTypes)) {
            //默认显示捕鱼游戏
            gameTypes.add(10);
        }
        if (StringUtil.isBlank(startTime)) {
            return error("开始时间为空");
        }
        if (StringUtil.isBlank(endTime)) {
            return error("结束时间为空");
        }

        if (DateUtils.parseDate(startTime).getTime() >= DateUtils.parseDate(endTime).getTime()) {
            return error("开始时间大于等于结束时间");
        }
        Map<String, Object> resultMap = new HashMap<>();

        Map<String, Object> map = new HashMap<>();
        map.put("beginDate", startTime);
        map.put("endDate", endTime);
        List<String> datelist = DateUtils.getDateList(startTime, endTime);


        List<String> titles = Lists.newArrayList();
        resultMap.put("dateList", datelist);
        if ("allUsers".equals(tabId)) {
            titles.add("DAU");
            titles.add("投注人数");
            titles.add("投注转化率");
            titles.add("投注流水");
            titles.add("流水差");
            titles.add("返奖率");
            titles.add("投注笔数");
            titles.add("投注ARPU");
            titles.add("投注ASP");
            resultMap.put("titles", titles);
            resultMap.put("chartsData", getChartsData(parentIds, gameTypes, map, tabId));
        } else if ("newUsers".equals(tabId)) {
            titles.add("新增用户数");
            titles.add("新增投注用户数");
            titles.add("新增投注转化率");
            titles.add("投注流水");
            titles.add("流水差");
            titles.add("返奖率");
            titles.add("投注笔数");
            titles.add("投注ARPU");
            titles.add("投注ASP");
            resultMap.put("titles", titles);
            resultMap.put("chartsData", getChartsData(parentIds, gameTypes, map, tabId));
        } else if ("retention".equals(tabId)) {
            titles.add("新增次留");
            titles.add("新增三留");
            titles.add("新增七留");
            titles.add("全量次留");
            titles.add("全量三留");
            titles.add("全量七留");
            resultMap.put("titles", titles);
            resultMap.put("chartsData", getChartsData(parentIds, gameTypes, map, tabId));
        } else if ("other".equals(tabId)) {
            titles.add("导入率");
            titles.add("累计用户数");
            resultMap.put("titles", titles);
            resultMap.put("chartsData", getChartsData(parentIds, gameTypes, map, tabId));
        }


        return resultMap;
    }

    private Map<String, Object> getChartsData(List<Long> parentIds, List<Integer> gameTypes, Map<String, Object> map, String tabId) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> seriesMap = new HashMap<>();


        List<Object> dauList = Lists.newArrayList();
        List<Object> userCountList = Lists.newArrayList();
        List<Object> conversionList = Lists.newArrayList();
        List<Object> bettingAmountList = Lists.newArrayList();
        List<Object> diffAmountList = Lists.newArrayList();
        List<Object> returnRateList = Lists.newArrayList();
        List<Object> bettingCountList = Lists.newArrayList();
        List<Object> bettingArpuList = Lists.newArrayList();
        List<Object> bettingAspList = Lists.newArrayList();
        List<Object> oneDayRetentionList = Lists.newArrayList();
        List<Object> threeDayRetentionList = Lists.newArrayList();
        List<Object> sevenDayRetentionList = Lists.newArrayList();
        List<Object> newUserCountList = Lists.newArrayList();
        List<Object> newUserBettingUserCountList = Lists.newArrayList();
        List<Object> newUserBettingConversionList = Lists.newArrayList();
        List<Object> newUserBettingAmountList = Lists.newArrayList();
        List<Object> newUserDiffAmountList = Lists.newArrayList();
        List<Object> newUserReturnRateList = Lists.newArrayList();
        List<Object> newUserBettingCountList = Lists.newArrayList();
        List<Object> newUserBettingArpuList = Lists.newArrayList();
        List<Object> newUserBettingAspList = Lists.newArrayList();
        List<Object> newUserOneDayRetentionList = Lists.newArrayList();
        List<Object> newUserThreeDayRetentionList = Lists.newArrayList();
        List<Object> newUserSevenDayRetentionList = Lists.newArrayList();
        List<Object> importRateList = Lists.newArrayList();
        List<Object> totalUserCountList = Lists.newArrayList();


        List<String> legends = Lists.newArrayList();
        for (Long parentId : parentIds) {
            for (Integer gameType : gameTypes) {
                map.put("gameType", gameType);
                map.put("parentId", parentId);
                DataDict dictDto = dataDictService.getDictByValue("game_type", gameType);

                ChannelInfo channelDto = channelInfoService.get(parentId);
                if (null == channelDto) {
                    channelDto = new ChannelInfo();
                    channelDto.setName("全部");
                }
                legends.add(channelDto.getName() + "-" + dictDto.getLabel());
                List<DatawareFinalGameInfo> list = datawareFinalGameInfoService.findInfoByDate(map);
                if ("allUsers".equals(tabId)) {
                    dauList.add(getLinesData(list, tabId, "DAU"));
                    userCountList.add(getLinesData(list, tabId, "投注人数"));
                    conversionList.add(getLinesData(list, tabId, "投注转化率"));
                    bettingAmountList.add(getLinesData(list, tabId, "投注流水"));
                    diffAmountList.add(getLinesData(list, tabId, "流水差"));
                    returnRateList.add(getLinesData(list, tabId, "返奖率"));
                    bettingCountList.add(getLinesData(list, tabId, "投注笔数"));
                    bettingArpuList.add(getLinesData(list, tabId, "投注ARPU"));
                    bettingAspList.add(getLinesData(list, tabId, "投注ASP"));
                } else if ("newUsers".equals(tabId)) {
                    newUserCountList.add(getLinesData(list, tabId, "新增用户数"));
                    newUserBettingUserCountList.add(getLinesData(list, tabId, "新增投注用户数"));
                    newUserBettingConversionList.add(getLinesData(list, tabId, "新增投注转化率"));
                    newUserBettingAmountList.add(getLinesData(list, tabId, "投注流水"));
                    newUserDiffAmountList.add(getLinesData(list, tabId, "流水差"));
                    newUserReturnRateList.add(getLinesData(list, tabId, "返奖率"));
                    newUserBettingCountList.add(getLinesData(list, tabId, "投注笔数"));
                    newUserBettingArpuList.add(getLinesData(list, tabId, "投注ARPU"));
                    newUserBettingAspList.add(getLinesData(list, tabId, "投注ASP"));
                } else if ("retention".equals(tabId)) {
                    oneDayRetentionList.add(getLinesData(list, tabId, "全量次留"));
                    threeDayRetentionList.add(getLinesData(list, tabId, "全量三留"));
                    sevenDayRetentionList.add(getLinesData(list, tabId, "全量七留"));
                    newUserOneDayRetentionList.add(getLinesData(list, tabId, "新增次留"));
                    newUserThreeDayRetentionList.add(getLinesData(list, tabId, "新增三留"));
                    newUserSevenDayRetentionList.add(getLinesData(list, tabId, "新增七留"));
                } else if ("other".equals(tabId)) {
                    importRateList.add(getLinesData(list, tabId, "导入率"));
                    totalUserCountList.add(getLinesData(list, tabId, "累计用户数"));
                }

            }

        }

        if ("allUsers".equals(tabId)) {
            seriesMap.put("DAU", dauList);
            seriesMap.put("投注人数", userCountList);
            seriesMap.put("投注转化率", conversionList);
            seriesMap.put("投注流水", bettingAmountList);
            seriesMap.put("流水差", diffAmountList);
            seriesMap.put("返奖率", returnRateList);
            seriesMap.put("投注笔数", bettingCountList);
            seriesMap.put("投注ARPU", bettingArpuList);
            seriesMap.put("投注ASP", bettingAspList);
        } else if ("newUsers".equals(tabId)) {
            seriesMap.put("新增用户数", newUserCountList);
            seriesMap.put("新增投注用户数", newUserBettingUserCountList);
            seriesMap.put("新增投注转化率", newUserBettingConversionList);
            seriesMap.put("投注流水", newUserBettingAmountList);
            seriesMap.put("流水差", newUserDiffAmountList);
            seriesMap.put("返奖率", newUserReturnRateList);
            seriesMap.put("投注笔数", newUserBettingCountList);
            seriesMap.put("投注ARPU", newUserBettingArpuList);
            seriesMap.put("投注ASP", newUserBettingAspList);

        } else if ("retention".equals(tabId)) {
            seriesMap.put("全量次留", oneDayRetentionList);
            seriesMap.put("全量三留", threeDayRetentionList);
            seriesMap.put("全量七留", sevenDayRetentionList);
            seriesMap.put("新增次留", newUserOneDayRetentionList);
            seriesMap.put("新增三留", newUserThreeDayRetentionList);
            seriesMap.put("新增七留", newUserSevenDayRetentionList);
        } else if ("other".equals(tabId)) {
            seriesMap.put("导入率", importRateList);
            seriesMap.put("累计用户数", totalUserCountList);
        }

        resultMap.put("legends", legends);
        resultMap.put("series", seriesMap);
        return resultMap;
    }

    private List<Object> getLinesData(List<DatawareFinalGameInfo> list, String tabId, String parameter) {
        List<Object> resultList = Lists.newArrayList();
        Map<String, Object> dayRateMap = new HashMap<>();
        Map<String, Object> weekRateMap = new HashMap<>();
        for (DatawareFinalGameInfo info : list) {
            String yesterDay = DateUtils.formatDate(DateUtils.getNextDate(DateUtils.parseDate(info.getBusinessDate()), -1));
            String weekDay = DateUtils.formatDate(DateUtils.getNextDate(DateUtils.parseDate(info.getBusinessDate()), -7));
            dayRateMap.put("businessDate", yesterDay);
            dayRateMap.put("parentId", info.getParentId());
            dayRateMap.put("gameType", info.getGameType());

            weekRateMap.put("businessDate", weekDay);
            weekRateMap.put("parentId", info.getParentId());
            weekRateMap.put("gameType", info.getGameType());
            List<DatawareFinalGameInfo> yesList = datawareFinalGameInfoService.findInfoByDate(dayRateMap);
            List<DatawareFinalGameInfo> weekList = datawareFinalGameInfoService.findInfoByDate(weekRateMap);

            if ("allUsers".equals(tabId)) {
                if ("DAU".equals(parameter)) {
                    String str = "";
                    str += info.getDau();
                    str += ";";
                    if (CollectionUtils.isEmpty(yesList)) {
                        str += "日环比：0%;";
                    } else {
                        if (yesList.get(0) == null) {
                            str += "日环比：0%;";
                        } else {
                            if (yesList.get(0).getDau() == 0) {
                                str += "日环比：0%;";
                            } else {
                                str += "日环比: ";
                                str += BigDecimalUtil.div((info.getDau() - yesList.get(0).getDau()) * 100, yesList.get(0).getDau(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    if (CollectionUtils.isEmpty(weekList)) {
                        str += "周同比：0%;";
                    } else {
                        if (weekList.get(0) == null) {
                            str += "周同比：0%;";
                        } else {
                            if (weekList.get(0).getDau() == 0) {
                                str += "周同比：0%;";
                            } else {
                                str += "周同比: ";
                                str += BigDecimalUtil.div((info.getDau() - weekList.get(0).getDau()) * 100, weekList.get(0).getDau(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    resultList.add(str);
                } else if ("投注人数".equals(parameter)) {
                    String str = "";
                    str += info.getBettingUserCount();
                    str += ";";
                    if (CollectionUtils.isEmpty(yesList)) {
                        str += "日环比：0%;";
                    } else {
                        if (yesList.get(0) == null) {
                            str += "日环比：0%;";
                        } else {
                            if (yesList.get(0).getBettingUserCount() == 0) {
                                str += "日环比：0%;";
                            } else {
                                str += "日环比: ";
                                str += BigDecimalUtil.div((info.getBettingUserCount() - yesList.get(0).getBettingUserCount()) * 100, yesList.get(0).getBettingUserCount(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    if (CollectionUtils.isEmpty(weekList)) {
                        str += "周同比：0%;";
                    } else {
                        if (weekList.get(0) == null) {
                            str += "周同比：0%;";
                        } else {
                            if (weekList.get(0).getBettingUserCount() == 0) {
                                str += "周同比：0%;";
                            } else {
                                str += "周同比: ";
                                str += BigDecimalUtil.div((info.getBettingUserCount() - weekList.get(0).getBettingUserCount()) * 100, weekList.get(0).getBettingUserCount(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    resultList.add(str);
                } else if ("投注转化率".equals(parameter)) {
                    String str = "";
                    str += info.getBettingConversion();
                    str += ";";
                    if (CollectionUtils.isEmpty(yesList)) {
                        str += "日环比：0%;";
                    } else {
                        if (yesList.get(0) == null) {
                            str += "日环比：0%;";
                        } else {
                            if (yesList.get(0).getBettingConversion() == 0) {
                                str += "日环比：0%;";
                            } else {
                                str += "日环比: ";
                                str += BigDecimalUtil.div((info.getBettingConversion() - yesList.get(0).getBettingConversion()) * 100, yesList.get(0).getBettingConversion(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    if (CollectionUtils.isEmpty(weekList)) {
                        str += "周同比：0%;";
                    } else {
                        if (weekList.get(0) == null) {
                            str += "周同比：0%;";
                        } else {
                            if (weekList.get(0).getBettingConversion() == 0) {
                                str += "周同比：0%;";
                            } else {
                                str += "周同比: ";
                                str += BigDecimalUtil.div((info.getBettingConversion() - weekList.get(0).getBettingConversion()) * 100, weekList.get(0).getBettingConversion(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    resultList.add(str);
                } else if ("投注流水".equals(parameter)) {
                    String str = "";
                    str += info.getBettingAmount();
                    str += ";";
                    if (CollectionUtils.isEmpty(yesList)) {
                        str += "日环比：0%;";
                    } else {
                        if (yesList.get(0) == null) {
                            str += "日环比：0%;";
                        } else {
                            if (yesList.get(0).getBettingAmount() == 0) {
                                str += "日环比：0%;";
                            } else {
                                str += "日环比: ";
                                str += BigDecimalUtil.div((info.getBettingAmount() - yesList.get(0).getBettingAmount()) * 100, yesList.get(0).getBettingAmount(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    if (CollectionUtils.isEmpty(weekList)) {
                        str += "周同比：0%;";
                    } else {
                        if (weekList.get(0) == null) {
                            str += "周同比：0%;";
                        } else {
                            if (weekList.get(0).getBettingAmount() == 0) {
                                str += "周同比：0%;";
                            } else {
                                str += "周同比: ";
                                str += BigDecimalUtil.div((info.getBettingAmount() - weekList.get(0).getBettingAmount()) * 100, weekList.get(0).getBettingAmount(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    resultList.add(str);
                } else if ("流水差".equals(parameter)) {
                    String str = "";
                    str += info.getDiffAmount();
                    str += ";";
                    if (CollectionUtils.isEmpty(yesList)) {
                        str += "日环比：0%;";
                    } else {
                        if (yesList.get(0) == null) {
                            str += "日环比：0%;";
                        } else {
                            if (yesList.get(0).getDiffAmount() == 0) {
                                str += "日环比：0%;";
                            } else {
                                str += "日环比: ";
                                str += BigDecimalUtil.div((info.getDiffAmount() - yesList.get(0).getDiffAmount()) * 100, yesList.get(0).getDiffAmount(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    if (CollectionUtils.isEmpty(weekList)) {
                        str += "周同比：0%;";
                    } else {
                        if (weekList.get(0) == null) {
                            str += "周同比：0%;";
                        } else {
                            if (weekList.get(0).getDiffAmount() == 0) {
                                str += "周同比：0%;";
                            } else {
                                str += "周同比: ";
                                str += BigDecimalUtil.div((info.getDiffAmount() - weekList.get(0).getDiffAmount()) * 100, weekList.get(0).getDiffAmount(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    resultList.add(str);
                } else if ("返奖率".equals(parameter)) {
                    String str = "";
                    str += info.getReturnRate();
                    str += ";";
                    if (CollectionUtils.isEmpty(yesList)) {
                        str += "日环比：0%;";
                    } else {
                        if (yesList.get(0) == null) {
                            str += "日环比：0%;";
                        } else {
                            if (yesList.get(0).getReturnRate() == 0) {
                                str += "日环比：0%;";
                            } else {
                                str += "日环比: ";
                                str += BigDecimalUtil.div((info.getReturnRate() - yesList.get(0).getReturnRate()) * 100, yesList.get(0).getReturnRate(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    if (CollectionUtils.isEmpty(weekList)) {
                        str += "周同比：0%;";
                    } else {
                        if (weekList.get(0) == null) {
                            str += "周同比：0%;";
                        } else {
                            if (weekList.get(0).getReturnRate() == 0) {
                                str += "周同比：0%;";
                            } else {
                                str += "周同比: ";
                                str += BigDecimalUtil.div((info.getReturnRate() - weekList.get(0).getReturnRate()) * 100, weekList.get(0).getReturnRate(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    resultList.add(str);
                } else if ("投注笔数".equals(parameter)) {
                    String str = "";
                    str += info.getBettingCount();
                    str += ";";
                    if (CollectionUtils.isEmpty(yesList)) {
                        str += "日环比：0%;";
                    } else {
                        if (yesList.get(0) == null) {
                            str += "日环比：0%;";
                        } else {
                            if (yesList.get(0).getBettingCount() == 0) {
                                str += "日环比：0%;";
                            } else {
                                str += "日环比: ";
                                str += BigDecimalUtil.div((info.getBettingCount() - yesList.get(0).getBettingCount()) * 100, yesList.get(0).getBettingCount(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    if (CollectionUtils.isEmpty(weekList)) {
                        str += "周同比：0%;";
                    } else {
                        if (weekList.get(0) == null) {
                            str += "周同比：0%;";
                        } else {
                            if (weekList.get(0).getBettingCount() == 0) {
                                str += "周同比：0%;";
                            } else {
                                str += "周同比: ";
                                str += BigDecimalUtil.div((info.getBettingCount() - weekList.get(0).getBettingCount()) * 100, weekList.get(0).getBettingCount(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    resultList.add(str);
                } else if ("投注ARPU".equals(parameter)) {
                    String str = "";
                    str += info.getBettingArpu();
                    str += ";";
                    if (CollectionUtils.isEmpty(yesList)) {
                        str += "日环比：0%;";
                    } else {
                        if (yesList.get(0) == null) {
                            str += "日环比：0%;";
                        } else {
                            if (yesList.get(0).getBettingArpu() == 0) {
                                str += "日环比：0%;";
                            } else {
                                str += "日环比: ";
                                str += BigDecimalUtil.div((info.getBettingArpu() - yesList.get(0).getBettingArpu()) * 100, yesList.get(0).getBettingArpu(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    if (CollectionUtils.isEmpty(weekList)) {
                        str += "周同比：0%;";
                    } else {
                        if (weekList.get(0) == null) {
                            str += "周同比：0%;";
                        } else {
                            if (weekList.get(0).getBettingArpu() == 0) {
                                str += "周同比：0%;";
                            } else {
                                str += "周同比: ";
                                str += BigDecimalUtil.div((info.getBettingArpu() - weekList.get(0).getBettingArpu()) * 100, weekList.get(0).getBettingArpu(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    resultList.add(str);
                } else if ("投注ASP".equals(parameter)) {
                    String str = "";
                    str += info.getBettingAsp();
                    str += ";";
                    if (CollectionUtils.isEmpty(yesList)) {
                        str += "日环比：0%;";
                    } else {
                        if (yesList.get(0) == null) {
                            str += "日环比：0%;";
                        } else {
                            if (yesList.get(0).getBettingAsp() == 0) {
                                str += "日环比：0%;";
                            } else {
                                str += "日环比: ";
                                str += BigDecimalUtil.div((info.getBettingAsp() - yesList.get(0).getBettingAsp()) * 100, yesList.get(0).getBettingAsp(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    if (CollectionUtils.isEmpty(weekList)) {
                        str += "周同比：0%;";
                    } else {
                        if (weekList.get(0) == null) {
                            str += "周同比：0%;";
                        } else {
                            if (weekList.get(0).getBettingAsp() == 0) {
                                str += "周同比：0%;";
                            } else {
                                str += "周同比: ";
                                str += BigDecimalUtil.div((info.getBettingAsp() - weekList.get(0).getBettingAsp()) * 100, weekList.get(0).getBettingAsp(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    resultList.add(str);
                }

            } else if ("newUsers".equals(tabId)) {
                if ("新增用户数".equals(parameter)) {
                    String str = "";
                    str += info.getNewUserCount();
                    str += ";";
                    if (CollectionUtils.isEmpty(yesList)) {
                        str += "日环比：0%;";
                    } else {
                        if (yesList.get(0) == null) {
                            str += "日环比：0%;";
                        } else {
                            if (yesList.get(0).getNewUserCount() == 0) {
                                str += "日环比：0%;";
                            } else {
                                str += "日环比: ";
                                str += BigDecimalUtil.div((info.getNewUserCount() - yesList.get(0).getNewUserCount()) * 100, yesList.get(0).getNewUserCount(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    if (CollectionUtils.isEmpty(weekList)) {
                        str += "周同比：0%;";
                    } else {
                        if (weekList.get(0) == null) {
                            str += "周同比：0%;";
                        } else {
                            if (weekList.get(0).getNewUserCount() == 0) {
                                str += "周同比：0%;";
                            } else {
                                str += "周同比: ";
                                str += BigDecimalUtil.div((info.getNewUserCount() - weekList.get(0).getNewUserCount()) * 100, weekList.get(0).getNewUserCount(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    resultList.add(str);
                } else if ("新增投注用户数".equals(parameter)) {
                    String str = "";
                    str += info.getNewUserBettingUserCount();
                    str += ";";
                    if (CollectionUtils.isEmpty(yesList)) {
                        str += "日环比：0%;";
                    } else {
                        if (yesList.get(0) == null) {
                            str += "日环比：0%;";
                        } else {
                            if (yesList.get(0).getNewUserBettingUserCount() == 0) {
                                str += "日环比：0%;";
                            } else {
                                str += "日环比: ";
                                str += BigDecimalUtil.div((info.getNewUserBettingUserCount() - yesList.get(0).getNewUserBettingUserCount()) * 100, yesList.get(0).getNewUserBettingUserCount(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    if (CollectionUtils.isEmpty(weekList)) {
                        str += "周同比：0%;";
                    } else {
                        if (weekList.get(0) == null) {
                            str += "周同比：0%;";
                        } else {
                            if (weekList.get(0).getNewUserBettingUserCount() == 0) {
                                str += "周同比：0%;";
                            } else {
                                str += "周同比: ";
                                str += BigDecimalUtil.div((info.getNewUserBettingUserCount() - weekList.get(0).getNewUserBettingUserCount()) * 100, weekList.get(0).getNewUserBettingUserCount(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    resultList.add(str);
                } else if ("新增投注转化率".equals(parameter)) {
                    String str = "";
                    str += info.getNewUserBettingConversion();
                    str += ";";
                    if (CollectionUtils.isEmpty(yesList)) {
                        str += "日环比：0%;";
                    } else {
                        if (yesList.get(0) == null) {
                            str += "日环比：0%;";
                        } else {
                            if (yesList.get(0).getNewUserBettingConversion() == 0) {
                                str += "日环比：0%;";
                            } else {
                                str += "日环比: ";
                                str += BigDecimalUtil.div((info.getNewUserBettingConversion() - yesList.get(0).getNewUserBettingConversion()) * 100, yesList.get(0).getNewUserBettingConversion(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    if (CollectionUtils.isEmpty(weekList)) {
                        str += "周同比：0%;";
                    } else {
                        if (weekList.get(0) == null) {
                            str += "周同比：0%;";
                        } else {
                            if (weekList.get(0).getNewUserBettingConversion() == 0) {
                                str += "周同比：0%;";
                            } else {
                                str += "周同比: ";
                                str += BigDecimalUtil.div((info.getNewUserBettingConversion() - weekList.get(0).getNewUserBettingConversion()) * 100, weekList.get(0).getNewUserBettingConversion(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    resultList.add(str);
                } else if ("投注流水".equals(parameter)) {
                    String str = "";
                    str += info.getNewUserBettingAmount();
                    str += ";";
                    if (CollectionUtils.isEmpty(yesList)) {
                        str += "日环比：0%;";
                    } else {
                        if (yesList.get(0) == null) {
                            str += "日环比：0%;";
                        } else {
                            if (yesList.get(0).getNewUserBettingAmount() == 0) {
                                str += "日环比：0%;";
                            } else {
                                str += "日环比: ";
                                str += BigDecimalUtil.div((info.getNewUserBettingAmount() - yesList.get(0).getNewUserBettingAmount()) * 100, yesList.get(0).getNewUserBettingAmount(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    if (CollectionUtils.isEmpty(weekList)) {
                        str += "周同比：0%;";
                    } else {
                        if (weekList.get(0) == null) {
                            str += "周同比：0%;";
                        } else {
                            if (weekList.get(0).getNewUserBettingAmount() == 0) {
                                str += "周同比：0%;";
                            } else {
                                str += "周同比: ";
                                str += BigDecimalUtil.div((info.getNewUserBettingAmount() - weekList.get(0).getNewUserBettingAmount()) * 100, weekList.get(0).getNewUserBettingAmount(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    resultList.add(str);
                } else if ("流水差".equals(parameter)) {
                    String str = "";
                    str += info.getNewUserDiffAmount();
                    str += ";";
                    if (CollectionUtils.isEmpty(yesList)) {
                        str += "日环比：0%;";
                    } else {
                        if (yesList.get(0) == null) {
                            str += "日环比：0%;";
                        } else {
                            if (yesList.get(0).getNewUserDiffAmount() == 0) {
                                str += "日环比：0%;";
                            } else {
                                str += "日环比: ";
                                str += BigDecimalUtil.div((info.getNewUserDiffAmount() - yesList.get(0).getNewUserDiffAmount()) * 100, yesList.get(0).getNewUserDiffAmount(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    if (CollectionUtils.isEmpty(weekList)) {
                        str += "周同比：0%;";
                    } else {
                        if (weekList.get(0) == null) {
                            str += "周同比：0%;";
                        } else {
                            if (weekList.get(0).getNewUserDiffAmount() == 0) {
                                str += "周同比：0%;";
                            } else {
                                str += "周同比: ";
                                str += BigDecimalUtil.div((info.getNewUserDiffAmount() - weekList.get(0).getNewUserDiffAmount()) * 100, weekList.get(0).getNewUserDiffAmount(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    resultList.add(str);
                } else if ("返奖率".equals(parameter)) {
                    String str = "";
                    str += info.getNewUserReturnRate();
                    str += ";";
                    if (CollectionUtils.isEmpty(yesList)) {
                        str += "日环比：0%;";
                    } else {
                        if (yesList.get(0) == null) {
                            str += "日环比：0%;";
                        } else {
                            if (yesList.get(0).getNewUserReturnRate() == 0) {
                                str += "日环比：0%;";
                            } else {
                                str += "日环比: ";
                                str += BigDecimalUtil.div((info.getNewUserReturnRate() - yesList.get(0).getNewUserReturnRate()) * 100, yesList.get(0).getNewUserReturnRate(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    if (CollectionUtils.isEmpty(weekList)) {
                        str += "周同比：0%;";
                    } else {
                        if (weekList.get(0) == null) {
                            str += "周同比：0%;";
                        } else {
                            if (weekList.get(0).getNewUserReturnRate() == 0) {
                                str += "周同比：0%;";
                            } else {
                                str += "周同比: ";
                                str += BigDecimalUtil.div((info.getNewUserReturnRate() - weekList.get(0).getNewUserReturnRate()) * 100, weekList.get(0).getNewUserReturnRate(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    resultList.add(str);
                } else if ("投注笔数".equals(parameter)) {
                    String str = "";
                    str += info.getNewUserBettingCount();
                    str += ";";
                    if (CollectionUtils.isEmpty(yesList)) {
                        str += "日环比：0%;";
                    } else {
                        if (yesList.get(0) == null) {
                            str += "日环比：0%;";
                        } else {
                            if (yesList.get(0).getNewUserBettingCount() == 0) {
                                str += "日环比：0%;";
                            } else {
                                str += "日环比: ";
                                str += BigDecimalUtil.div((info.getNewUserBettingCount() - yesList.get(0).getNewUserBettingCount()) * 100, yesList.get(0).getNewUserBettingCount(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    if (CollectionUtils.isEmpty(weekList)) {
                        str += "周同比：0%;";
                    } else {
                        if (weekList.get(0) == null) {
                            str += "周同比：0%;";
                        } else {
                            if (weekList.get(0).getNewUserBettingCount() == 0) {
                                str += "周同比：0%;";
                            } else {
                                str += "周同比: ";
                                str += BigDecimalUtil.div((info.getNewUserBettingCount() - weekList.get(0).getNewUserBettingCount()) * 100, weekList.get(0).getNewUserBettingCount(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    resultList.add(str);
                } else if ("投注ARPU".equals(parameter)) {
                    String str = "";
                    str += info.getNewUserBettingArpu();
                    str += ";";
                    if (CollectionUtils.isEmpty(yesList)) {
                        str += "日环比：0%;";
                    } else {
                        if (yesList.get(0) == null) {
                            str += "日环比：0%;";
                        } else {
                            if (yesList.get(0).getNewUserBettingArpu() == 0) {
                                str += "日环比：0%;";
                            } else {
                                str += "日环比: ";
                                str += BigDecimalUtil.div((info.getNewUserBettingArpu() - yesList.get(0).getNewUserBettingArpu()) * 100, yesList.get(0).getNewUserBettingArpu(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    if (CollectionUtils.isEmpty(weekList)) {
                        str += "周同比：0%;";
                    } else {
                        if (weekList.get(0) == null) {
                            str += "周同比：0%;";
                        } else {
                            if (weekList.get(0).getNewUserBettingArpu() == 0) {
                                str += "周同比：0%;";
                            } else {
                                str += "周同比: ";
                                str += BigDecimalUtil.div((info.getNewUserBettingArpu() - weekList.get(0).getNewUserBettingArpu()) * 100, weekList.get(0).getNewUserBettingArpu(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    resultList.add(str);
                } else if ("投注ASP".equals(parameter)) {
                    String str = "";
                    str += info.getNewUserBettingAsp();
                    str += ";";
                    if (CollectionUtils.isEmpty(yesList)) {
                        str += "日环比：0%;";
                    } else {
                        if (yesList.get(0) == null) {
                            str += "日环比：0%;";
                        } else {
                            if (yesList.get(0).getNewUserBettingAsp() == 0) {
                                str += "日环比：0%;";
                            } else {
                                str += "日环比: ";
                                str += BigDecimalUtil.div((info.getNewUserBettingAsp() - yesList.get(0).getNewUserBettingAsp()) * 100, yesList.get(0).getNewUserBettingAsp(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    if (CollectionUtils.isEmpty(weekList)) {
                        str += "周同比：0%;";
                    } else {
                        if (weekList.get(0) == null) {
                            str += "周同比：0%;";
                        } else {
                            if (weekList.get(0).getNewUserBettingAsp() == 0) {
                                str += "周同比：0%;";
                            } else {
                                str += "周同比: ";
                                str += BigDecimalUtil.div((info.getNewUserBettingAsp() - weekList.get(0).getNewUserBettingAsp()) * 100, weekList.get(0).getNewUserBettingAsp(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    resultList.add(str);
                }


            } else if ("retention".equals(tabId)) {
                if ("全量次留".equals(parameter)) {
                    String str = "";
                    str += info.getOneDayRetention();
                    str += ";";
                    if (CollectionUtils.isEmpty(yesList)) {
                        str += "日环比：0%;";
                    } else {
                        if (yesList.get(0) == null) {
                            str += "日环比：0%;";
                        } else {
                            if (yesList.get(0).getOneDayRetention() == 0) {
                                str += "日环比：0%;";
                            } else {
                                str += "日环比: ";
                                str += BigDecimalUtil.div((info.getOneDayRetention() - yesList.get(0).getOneDayRetention()) * 100, yesList.get(0).getOneDayRetention(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    if (CollectionUtils.isEmpty(weekList)) {
                        str += "周同比：0%;";
                    } else {
                        if (weekList.get(0) == null) {
                            str += "周同比：0%;";
                        } else {
                            if (weekList.get(0).getOneDayRetention() == 0) {
                                str += "周同比：0%;";
                            } else {
                                str += "周同比: ";
                                str += BigDecimalUtil.div((info.getOneDayRetention() - weekList.get(0).getOneDayRetention()) * 100, weekList.get(0).getOneDayRetention(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    resultList.add(str);
                } else if ("全量三留".equals(parameter)) {
                    String str = "";
                    str += info.getThreeDayRetention();
                    str += ";";
                    if (CollectionUtils.isEmpty(yesList)) {
                        str += "日环比：0%;";
                    } else {
                        if (yesList.get(0) == null) {
                            str += "日环比：0%;";
                        } else {
                            if (yesList.get(0).getThreeDayRetention() == 0) {
                                str += "日环比：0%;";
                            } else {
                                str += "日环比: ";
                                str += BigDecimalUtil.div((info.getThreeDayRetention() - yesList.get(0).getThreeDayRetention()) * 100, yesList.get(0).getThreeDayRetention(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    if (CollectionUtils.isEmpty(weekList)) {
                        str += "周同比：0%;";
                    } else {
                        if (weekList.get(0) == null) {
                            str += "周同比：0%;";
                        } else {
                            if (weekList.get(0).getThreeDayRetention() == 0) {
                                str += "周同比：0%;";
                            } else {
                                str += "周同比: ";
                                str += BigDecimalUtil.div((info.getThreeDayRetention() - weekList.get(0).getThreeDayRetention()) * 100, weekList.get(0).getThreeDayRetention(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    resultList.add(str);
                } else if ("全量七留".equals(parameter)) {
                    String str = "";
                    str += info.getSevenDayRetention();
                    str += ";";
                    if (CollectionUtils.isEmpty(yesList)) {
                        str += "日环比：0%;";
                    } else {
                        if (yesList.get(0) == null) {
                            str += "日环比：0%;";
                        } else {
                            if (yesList.get(0).getSevenDayRetention() == 0) {
                                str += "日环比：0%;";
                            } else {
                                str += "日环比: ";
                                str += BigDecimalUtil.div((info.getSevenDayRetention() - yesList.get(0).getSevenDayRetention()) * 100, yesList.get(0).getSevenDayRetention(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    if (CollectionUtils.isEmpty(weekList)) {
                        str += "周同比：0%;";
                    } else {
                        if (weekList.get(0) == null) {
                            str += "周同比：0%;";
                        } else {
                            if (weekList.get(0).getSevenDayRetention() == 0) {
                                str += "周同比：0%;";
                            } else {
                                str += "周同比: ";
                                str += BigDecimalUtil.div((info.getSevenDayRetention() - weekList.get(0).getSevenDayRetention()) * 100, weekList.get(0).getSevenDayRetention(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    resultList.add(str);
                } else if ("新增次留".equals(parameter)) {
                    String str = "";
                    str += info.getNewUserOneDayRetention();
                    str += ";";
                    if (CollectionUtils.isEmpty(yesList)) {
                        str += "日环比：0%;";
                    } else {
                        if (yesList.get(0) == null) {
                            str += "日环比：0%;";
                        } else {
                            if (yesList.get(0).getNewUserOneDayRetention() == 0) {
                                str += "日环比：0%;";
                            } else {
                                str += "日环比: ";
                                str += BigDecimalUtil.div((info.getNewUserOneDayRetention() - yesList.get(0).getNewUserOneDayRetention()) * 100, yesList.get(0).getNewUserOneDayRetention(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    if (CollectionUtils.isEmpty(weekList)) {
                        str += "周同比：0%;";
                    } else {
                        if (weekList.get(0) == null) {
                            str += "周同比：0%;";
                        } else {
                            if (weekList.get(0).getNewUserOneDayRetention() == 0) {
                                str += "周同比：0%;";
                            } else {
                                str += "周同比: ";
                                str += BigDecimalUtil.div((info.getNewUserOneDayRetention() - weekList.get(0).getNewUserOneDayRetention()) * 100, weekList.get(0).getNewUserOneDayRetention(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    resultList.add(str);
                } else if ("新增三留".equals(parameter)) {
                    String str = "";
                    str += info.getNewUserThreeDayRetention();
                    str += ";";
                    if (CollectionUtils.isEmpty(yesList)) {
                        str += "日环比：0%;";
                    } else {
                        if (yesList.get(0) == null) {
                            str += "日环比：0%;";
                        } else {
                            if (yesList.get(0).getNewUserThreeDayRetention() == 0) {
                                str += "日环比：0%;";
                            } else {
                                str += "日环比: ";
                                str += BigDecimalUtil.div((info.getNewUserThreeDayRetention() - yesList.get(0).getNewUserThreeDayRetention()) * 100, yesList.get(0).getNewUserThreeDayRetention(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    if (CollectionUtils.isEmpty(weekList)) {
                        str += "周同比：0%;";
                    } else {
                        if (weekList.get(0) == null) {
                            str += "周同比：0%;";
                        } else {
                            if (weekList.get(0).getNewUserThreeDayRetention() == 0) {
                                str += "周同比：0%;";
                            } else {
                                str += "周同比: ";
                                str += BigDecimalUtil.div((info.getNewUserThreeDayRetention() - weekList.get(0).getNewUserThreeDayRetention()) * 100, weekList.get(0).getNewUserThreeDayRetention(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    resultList.add(str);
                } else if ("新增七留".equals(parameter)) {
                    String str = "";
                    str += info.getNewUserSevenDayRetention();
                    str += ";";
                    if (CollectionUtils.isEmpty(yesList)) {
                        str += "日环比：0%;";
                    } else {
                        if (yesList.get(0) == null) {
                            str += "日环比：0%;";
                        } else {
                            if (yesList.get(0).getNewUserSevenDayRetention() == 0) {
                                str += "日环比：0%;";
                            } else {
                                str += "日环比: ";
                                str += BigDecimalUtil.div((info.getNewUserSevenDayRetention() - yesList.get(0).getNewUserSevenDayRetention()) * 100, yesList.get(0).getNewUserSevenDayRetention(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    if (CollectionUtils.isEmpty(weekList)) {
                        str += "周同比：0%;";
                    } else {
                        if (weekList.get(0) == null) {
                            str += "周同比：0%;";
                        } else {
                            if (weekList.get(0).getNewUserSevenDayRetention() == 0) {
                                str += "周同比：0%;";
                            } else {
                                str += "周同比: ";
                                str += BigDecimalUtil.div((info.getNewUserSevenDayRetention() - weekList.get(0).getNewUserSevenDayRetention()) * 100, weekList.get(0).getNewUserSevenDayRetention(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    resultList.add(str);
                }
            } else if ("other".equals(tabId)) {
                if ("导入率".equals(parameter)) {
                    String str = "";
                    str += info.getImportRate();
                    str += ";";
                    if (CollectionUtils.isEmpty(yesList)) {
                        str += "日环比：0%;";
                    } else {
                        if (yesList.get(0) == null) {
                            str += "日环比：0%;";
                        } else {
                            if (yesList.get(0).getImportRate() == 0) {
                                str += "日环比：0%;";
                            } else {
                                str += "日环比: ";
                                str += BigDecimalUtil.div((info.getImportRate() - yesList.get(0).getImportRate()) * 100, yesList.get(0).getImportRate(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    if (CollectionUtils.isEmpty(weekList)) {
                        str += "周同比：0%;";
                    } else {
                        if (weekList.get(0) == null) {
                            str += "周同比：0%;";
                        } else {
                            if (weekList.get(0).getImportRate() == 0) {
                                str += "周同比：0%;";
                            } else {
                                str += "周同比: ";
                                str += BigDecimalUtil.div((info.getImportRate() - weekList.get(0).getImportRate()) * 100, weekList.get(0).getImportRate(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    resultList.add(str);
                } else if ("累计用户数".equals(parameter)) {
                    String str = "";
                    str += info.getTotalUserCount();
                    str += ";";
                    if (CollectionUtils.isEmpty(yesList)) {
                        str += "日环比：0%;";
                    } else {
                        if (yesList.get(0) == null) {
                            str += "日环比：0%;";
                        } else {
                            if (yesList.get(0).getTotalUserCount() == 0) {
                                str += "日环比：0%;";
                            } else {
                                str += "日环比: ";
                                str += BigDecimalUtil.div((info.getTotalUserCount() - yesList.get(0).getTotalUserCount()) * 100, yesList.get(0).getTotalUserCount(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    if (CollectionUtils.isEmpty(weekList)) {
                        str += "周同比：0%;";
                    } else {
                        if (weekList.get(0) == null) {
                            str += "周同比：0%;";
                        } else {
                            if (weekList.get(0).getTotalUserCount() == 0) {
                                str += "周同比：0%;";
                            } else {
                                str += "周同比: ";
                                str += BigDecimalUtil.div((info.getTotalUserCount() - weekList.get(0).getTotalUserCount()) * 100, weekList.get(0).getTotalUserCount(), 2);
                                str += "%; ";
                            }
                        }
                    }
                    resultList.add(str);
                }
            }
        }
        return resultList;
    }
}

