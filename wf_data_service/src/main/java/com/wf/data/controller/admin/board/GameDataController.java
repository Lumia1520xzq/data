package com.wf.data.controller.admin.board;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.excel.ExportExcel;
import com.wf.core.utils.type.BigDecimalUtil;
import com.wf.core.web.base.ExtJsController;
import com.wf.data.common.constants.GameDataConstants;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.util.Date;
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

    private final DecimalFormat decimalFormat = new DecimalFormat("##0.00");

    /**
     * 查询列表
     */
    @RequestMapping("/getList")
    public Object list() {
        JSONObject json = getRequestJson();
        String tabId = null;
        List<Long> parentIds = Lists.newArrayList();
        List<Integer> gameTypes = Lists.newArrayList();
        String startTime = null;
        String endTime = null;
        JSONObject data = json.getJSONObject("data");

        if (data != null) {
            tabId = data.getString("tabId");

            JSONArray arr = data.getJSONArray(GameDataConstants.GAMETYPE);
            String js = JSONObject.toJSONString(arr, SerializerFeature.WriteClassName);
            gameTypes = JSONObject.parseArray(js, Integer.class);

            JSONArray parentIdArr = data.getJSONArray(GameDataConstants.PARENTID);
            String parentIdJs = JSONObject.toJSONString(parentIdArr, SerializerFeature.WriteClassName);
            parentIds = JSONObject.parseArray(parentIdJs, Long.class);
            startTime = data.getString("startTime");
            endTime = data.getString("endTime");
        }

        if (CollectionUtils.isEmpty(parentIds)) {
            if (parentIds == null) {
                parentIds = Lists.newArrayList();
            }
            //默认显示全渠道
            parentIds.add(1L);
        }
        if (CollectionUtils.isEmpty(gameTypes)) {
            if (gameTypes == null) {
                gameTypes = Lists.newArrayList();
            }
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
        map.put(GameDataConstants.BEGINDATE, startTime);
        map.put(GameDataConstants.ENDDATE, endTime);

        Map<String, Object> dateMap = new HashMap<>();
        dateMap.put(GameDataConstants.BEGINDATE, startTime);
        dateMap.put(GameDataConstants.ENDDATE, endTime);
        dateMap.put("parentIds", parentIds);
        dateMap.put("gameTypes", gameTypes);
        List<String> datelist = datawareFinalGameInfoService.findDateList(dateMap);

        List<String> titles = Lists.newArrayList();
        resultMap.put("dateList", datelist);
        if (GameDataConstants.ALLUSERS.equals(tabId)) {
            titles.add(GameDataConstants.DAU);
            titles.add(GameDataConstants.USER_COUNT);
            titles.add(GameDataConstants.CONVERSION);
            titles.add(GameDataConstants.BETTING_AMOUNT);
            titles.add(GameDataConstants.DIFF_AMOUNT);
            titles.add(GameDataConstants.RETURN_RATE);
            titles.add(GameDataConstants.BETTING_COUNT);
            titles.add(GameDataConstants.BETTING_ARPU);
            titles.add(GameDataConstants.BETTING_ASP);
            resultMap.put(GameDataConstants.TITLES, titles);
            resultMap.put(GameDataConstants.CHARTSDATA, getAllUsersChartsData(parentIds, gameTypes, map, tabId));
        }
        if (GameDataConstants.NEWUSERS.equals(tabId)) {
            titles.add(GameDataConstants.NEW_USER_COUNT);
            titles.add(GameDataConstants.NEW_USER_BETTING_USER_COUNT);
            titles.add(GameDataConstants.NEW_USER_BETTING_CONVERSION);
            titles.add(GameDataConstants.NEW_USER_BETTING_AMOUNT);
            titles.add(GameDataConstants.NEW_USER_DIFF_AMOUNT);
            titles.add(GameDataConstants.NEW_USER_RETURN_RATE);
            titles.add(GameDataConstants.NEW_USER_BETTING_COUNT);
            titles.add(GameDataConstants.NEW_USER_BETTING_ARPU);
            titles.add(GameDataConstants.NEW_USER_BETTING_ASP);
            resultMap.put(GameDataConstants.TITLES, titles);
            resultMap.put(GameDataConstants.CHARTSDATA, getNewUsersChartsData(parentIds, gameTypes, map, tabId));
        }
        if (GameDataConstants.RETENTION.equals(tabId)) {
            titles.add(GameDataConstants.NEW_USER_ONE_DAY_RETENTION);
            titles.add(GameDataConstants.NEW_USER_THREE_DAY_RETENTION);
            titles.add(GameDataConstants.NEW_USER_SEVEN_DAY_RETENTION);
            titles.add(GameDataConstants.ONE_DAY_RETENTION);
            titles.add(GameDataConstants.THREE_DAY_RETENTION);
            titles.add(GameDataConstants.SEVEN_DAY_RETENTION);
            resultMap.put(GameDataConstants.TITLES, titles);
            resultMap.put(GameDataConstants.CHARTSDATA, getRetentionChartsData(parentIds, gameTypes, map, tabId));
        }
        if (GameDataConstants.OTHER.equals(tabId)) {
            titles.add(GameDataConstants.IMPORT_RATE);
            titles.add(GameDataConstants.TOTAL_USER_COUNT);
            resultMap.put(GameDataConstants.TITLES, titles);
            resultMap.put(GameDataConstants.CHARTSDATA, getOtherChartsData(parentIds, gameTypes, map, tabId));
        }

        return resultMap;
    }

    private Map<String, Object> getAllUsersChartsData(List<Long> parentIds, List<Integer> gameTypes, Map<String, Object> map, String tabId) {
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

        List<String> legends = Lists.newArrayList();
        for (Long parentId : parentIds) {
            for (Integer gameType : gameTypes) {
                map.put(GameDataConstants.GAMETYPE, gameType);
                map.put(GameDataConstants.PARENTID, parentId);
                DataDict dictDto = dataDictService.getDictByValue(GameDataConstants.GAME_TYPE, gameType);

                ChannelInfo channelDto = channelInfoService.get(parentId);
                if (null == channelDto) {
                    channelDto = new ChannelInfo();
                    channelDto.setName("全部");
                }
                legends.add(channelDto.getName() + "-" + dictDto.getLabel());
                List<DatawareFinalGameInfo> list = datawareFinalGameInfoService.findInfoByDate(map);
                dauList.add(getLinesData(list, tabId, GameDataConstants.DAU));
                userCountList.add(getLinesData(list, tabId, GameDataConstants.USER_COUNT));
                conversionList.add(getLinesData(list, tabId, GameDataConstants.CONVERSION));
                bettingAmountList.add(getLinesData(list, tabId, GameDataConstants.BETTING_AMOUNT));
                diffAmountList.add(getLinesData(list, tabId, GameDataConstants.DIFF_AMOUNT));
                returnRateList.add(getLinesData(list, tabId, GameDataConstants.RETURN_RATE));
                bettingCountList.add(getLinesData(list, tabId, GameDataConstants.BETTING_COUNT));
                bettingArpuList.add(getLinesData(list, tabId, GameDataConstants.BETTING_ARPU));
                bettingAspList.add(getLinesData(list, tabId, GameDataConstants.BETTING_ASP));

            }

        }

        seriesMap.put(GameDataConstants.DAU, dauList);
        seriesMap.put(GameDataConstants.USER_COUNT, userCountList);
        seriesMap.put(GameDataConstants.CONVERSION, conversionList);
        seriesMap.put(GameDataConstants.BETTING_AMOUNT, bettingAmountList);
        seriesMap.put(GameDataConstants.DIFF_AMOUNT, diffAmountList);
        seriesMap.put(GameDataConstants.RETURN_RATE, returnRateList);
        seriesMap.put(GameDataConstants.BETTING_COUNT, bettingCountList);
        seriesMap.put(GameDataConstants.BETTING_ARPU, bettingArpuList);
        seriesMap.put(GameDataConstants.BETTING_ASP, bettingAspList);

        resultMap.put(GameDataConstants.LEGENDS, legends);
        resultMap.put(GameDataConstants.SERIES, seriesMap);
        return resultMap;
    }

    private Map<String, Object> getNewUsersChartsData(List<Long> parentIds, List<Integer> gameTypes, Map<String, Object> map, String tagId) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> seriesMap = new HashMap<>();

        List<Object> newUserCountList = Lists.newArrayList();
        List<Object> newUserBettingUserCountList = Lists.newArrayList();
        List<Object> newUserBettingConversionList = Lists.newArrayList();
        List<Object> newUserBettingAmountList = Lists.newArrayList();
        List<Object> newUserDiffAmountList = Lists.newArrayList();
        List<Object> newUserReturnRateList = Lists.newArrayList();
        List<Object> newUserBettingCountList = Lists.newArrayList();
        List<Object> newUserBettingArpuList = Lists.newArrayList();
        List<Object> newUserBettingAspList = Lists.newArrayList();

        List<String> legends = Lists.newArrayList();
        for (Long parentId : parentIds) {
            for (Integer gameType : gameTypes) {
                map.put(GameDataConstants.GAMETYPE, gameType);
                map.put(GameDataConstants.PARENTID, parentId);
                DataDict dictDto = dataDictService.getDictByValue(GameDataConstants.GAME_TYPE, gameType);

                ChannelInfo channelDto = channelInfoService.get(parentId);
                if (null == channelDto) {
                    channelDto = new ChannelInfo();
                    channelDto.setName("全部");
                }
                legends.add(channelDto.getName() + "-" + dictDto.getLabel());
                List<DatawareFinalGameInfo> list = datawareFinalGameInfoService.findInfoByDate(map);
                newUserCountList.add(getLinesData(list, tagId, GameDataConstants.NEW_USER_COUNT));
                newUserBettingUserCountList.add(getLinesData(list, tagId, GameDataConstants.NEW_USER_BETTING_USER_COUNT));
                newUserBettingConversionList.add(getLinesData(list, tagId, GameDataConstants.NEW_USER_BETTING_CONVERSION));
                newUserBettingAmountList.add(getLinesData(list, tagId, GameDataConstants.NEW_USER_BETTING_AMOUNT));
                newUserDiffAmountList.add(getLinesData(list, tagId, GameDataConstants.NEW_USER_DIFF_AMOUNT));
                newUserReturnRateList.add(getLinesData(list, tagId, GameDataConstants.NEW_USER_RETURN_RATE));
                newUserBettingCountList.add(getLinesData(list, tagId, GameDataConstants.NEW_USER_BETTING_COUNT));
                newUserBettingArpuList.add(getLinesData(list, tagId, GameDataConstants.NEW_USER_BETTING_ARPU));
                newUserBettingAspList.add(getLinesData(list, tagId, GameDataConstants.NEW_USER_BETTING_ASP));

            }

        }

        seriesMap.put(GameDataConstants.NEW_USER_COUNT, newUserCountList);
        seriesMap.put(GameDataConstants.NEW_USER_BETTING_USER_COUNT, newUserBettingUserCountList);
        seriesMap.put(GameDataConstants.NEW_USER_BETTING_CONVERSION, newUserBettingConversionList);
        seriesMap.put(GameDataConstants.NEW_USER_BETTING_AMOUNT, newUserBettingAmountList);
        seriesMap.put(GameDataConstants.NEW_USER_DIFF_AMOUNT, newUserDiffAmountList);
        seriesMap.put(GameDataConstants.NEW_USER_RETURN_RATE, newUserReturnRateList);
        seriesMap.put(GameDataConstants.NEW_USER_BETTING_COUNT, newUserBettingCountList);
        seriesMap.put(GameDataConstants.NEW_USER_BETTING_ARPU, newUserBettingArpuList);
        seriesMap.put(GameDataConstants.NEW_USER_BETTING_ASP, newUserBettingAspList);

        resultMap.put(GameDataConstants.LEGENDS, legends);
        resultMap.put(GameDataConstants.SERIES, seriesMap);
        return resultMap;
    }

    private Map<String, Object> getRetentionChartsData(List<Long> parentIds, List<Integer> gameTypes, Map<String, Object> map, String tabId) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> seriesMap = new HashMap<>();

        List<Object> oneDayRetentionList = Lists.newArrayList();
        List<Object> threeDayRetentionList = Lists.newArrayList();
        List<Object> sevenDayRetentionList = Lists.newArrayList();
        List<Object> newUserOneDayRetentionList = Lists.newArrayList();
        List<Object> newUserThreeDayRetentionList = Lists.newArrayList();
        List<Object> newUserSevenDayRetentionList = Lists.newArrayList();

        List<String> legends = Lists.newArrayList();
        for (Long parentId : parentIds) {
            for (Integer gameType : gameTypes) {
                map.put(GameDataConstants.GAMETYPE, gameType);
                map.put(GameDataConstants.PARENTID, parentId);
                DataDict dictDto = dataDictService.getDictByValue(GameDataConstants.GAME_TYPE, gameType);

                ChannelInfo channelDto = channelInfoService.get(parentId);
                if (null == channelDto) {
                    channelDto = new ChannelInfo();
                    channelDto.setName("全部");
                }
                legends.add(channelDto.getName() + "-" + dictDto.getLabel());
                List<DatawareFinalGameInfo> list = datawareFinalGameInfoService.findInfoByDate(map);
                oneDayRetentionList.add(getLinesData(list, tabId, GameDataConstants.ONE_DAY_RETENTION));
                threeDayRetentionList.add(getLinesData(list, tabId, GameDataConstants.THREE_DAY_RETENTION));
                sevenDayRetentionList.add(getLinesData(list, tabId, GameDataConstants.SEVEN_DAY_RETENTION));
                newUserOneDayRetentionList.add(getLinesData(list, tabId, GameDataConstants.NEW_USER_ONE_DAY_RETENTION));
                newUserThreeDayRetentionList.add(getLinesData(list, tabId, GameDataConstants.NEW_USER_THREE_DAY_RETENTION));
                newUserSevenDayRetentionList.add(getLinesData(list, tabId, GameDataConstants.NEW_USER_SEVEN_DAY_RETENTION));

            }

        }

        seriesMap.put(GameDataConstants.ONE_DAY_RETENTION, oneDayRetentionList);
        seriesMap.put(GameDataConstants.THREE_DAY_RETENTION, threeDayRetentionList);
        seriesMap.put(GameDataConstants.SEVEN_DAY_RETENTION, sevenDayRetentionList);
        seriesMap.put(GameDataConstants.NEW_USER_ONE_DAY_RETENTION, newUserOneDayRetentionList);
        seriesMap.put(GameDataConstants.NEW_USER_THREE_DAY_RETENTION, newUserThreeDayRetentionList);
        seriesMap.put(GameDataConstants.NEW_USER_SEVEN_DAY_RETENTION, newUserSevenDayRetentionList);

        resultMap.put(GameDataConstants.LEGENDS, legends);
        resultMap.put(GameDataConstants.SERIES, seriesMap);
        return resultMap;
    }

    private Map<String, Object> getOtherChartsData(List<Long> parentIds, List<Integer> gameTypes, Map<String, Object> map, String tabId) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> seriesMap = new HashMap<>();

        List<Object> importRateList = Lists.newArrayList();
        List<Object> totalUserCountList = Lists.newArrayList();

        List<String> legends = Lists.newArrayList();
        for (Long parentId : parentIds) {
            for (Integer gameType : gameTypes) {
                map.put(GameDataConstants.GAMETYPE, gameType);
                map.put(GameDataConstants.PARENTID, parentId);
                DataDict dictDto = dataDictService.getDictByValue(GameDataConstants.GAME_TYPE, gameType);

                ChannelInfo channelDto = channelInfoService.get(parentId);
                if (null == channelDto) {
                    channelDto = new ChannelInfo();
                    channelDto.setName("全部");
                }
                legends.add(channelDto.getName() + "-" + dictDto.getLabel());
                List<DatawareFinalGameInfo> list = datawareFinalGameInfoService.findInfoByDate(map);
                importRateList.add(getLinesData(list, tabId, GameDataConstants.IMPORT_RATE));
                totalUserCountList.add(getLinesData(list, tabId, GameDataConstants.TOTAL_USER_COUNT));

            }

        }

        seriesMap.put(GameDataConstants.IMPORT_RATE, importRateList);
        seriesMap.put(GameDataConstants.TOTAL_USER_COUNT, totalUserCountList);

        resultMap.put(GameDataConstants.LEGENDS, legends);
        resultMap.put(GameDataConstants.SERIES, seriesMap);
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
            dayRateMap.put(GameDataConstants.PARENTID, info.getParentId());
            dayRateMap.put(GameDataConstants.GAMETYPE, info.getGameType());

            weekRateMap.put("businessDate", weekDay);
            weekRateMap.put(GameDataConstants.PARENTID, info.getParentId());
            weekRateMap.put(GameDataConstants.GAMETYPE, info.getGameType());
            List<DatawareFinalGameInfo> yesList = datawareFinalGameInfoService.findInfoByDate(dayRateMap);
            List<DatawareFinalGameInfo> weekList = datawareFinalGameInfoService.findInfoByDate(weekRateMap);
            if (CollectionUtils.isEmpty(yesList)) {
                yesList = Lists.newArrayList();
                yesList.add(new DatawareFinalGameInfo().init());
            }
            if (CollectionUtils.isEmpty(weekList)) {
                weekList = Lists.newArrayList();
                weekList.add(new DatawareFinalGameInfo().init());
            }
            if (tabId.equals(GameDataConstants.ALLUSERS)) {
                resultList.add(getAllUsersData(info, parameter, yesList, weekList));
            }
            if (tabId.equals(GameDataConstants.NEWUSERS)) {

                resultList.add(getNewUsersData(info, parameter, yesList, weekList));
            }
            if (tabId.equals(GameDataConstants.RETENTION)) {

                resultList.add(getRetentionData(info, parameter, yesList, weekList));
            }
            if (tabId.equals(GameDataConstants.OTHER)) {

                resultList.add(getOtherData(info, parameter, yesList, weekList));
            }
        }
        return resultList;
    }

    private List<Object> getOtherData(DatawareFinalGameInfo info, String parameter, List<DatawareFinalGameInfo> yesList, List<DatawareFinalGameInfo> weekList) {
        List<Object> resultList = Lists.newArrayList();

        if (GameDataConstants.IMPORT_RATE.equals(parameter)) {
            if (info.getImportRate() == null) {
                info.setImportRate(0.0);
            }
            if (yesList.get(0).getImportRate() == null) {
                yesList.get(0).setImportRate(0.0);
            }
            if (weekList.get(0).getImportRate() == null) {
                weekList.get(0).setImportRate(0.0);
            }
            StringBuffer str = new StringBuffer();
            str.append(info.getImportRate().toString()).append(";");
            str.append(getSeriesStr(info.getImportRate(), yesList.get(0).getImportRate(), weekList.get(0).getImportRate()));

            resultList.add(str);
        }
        if (GameDataConstants.TOTAL_USER_COUNT.equals(parameter)) {
            if (info.getTotalUserCount() == null) {
                info.setTotalUserCount(0L);
            }
            if (yesList.get(0).getTotalUserCount() == null) {
                yesList.get(0).setTotalUserCount(0L);
            }
            if (weekList.get(0).getTotalUserCount() == null) {
                weekList.get(0).setTotalUserCount(0L);
            }
            StringBuffer str = new StringBuffer();
            str.append(info.getTotalUserCount().toString()).append(";");
            str.append(getSeriesStr(info.getTotalUserCount().doubleValue(), yesList.get(0).getTotalUserCount().doubleValue(), weekList.get(0).getTotalUserCount().doubleValue()));
            resultList.add(str);
        }
        return resultList;
    }

    private List<Object> getRetentionData(DatawareFinalGameInfo info, String parameter, List<DatawareFinalGameInfo> yesList, List<DatawareFinalGameInfo> weekList) {
        List<Object> resultList = Lists.newArrayList();

        if (GameDataConstants.ONE_DAY_RETENTION.equals(parameter)) {
            StringBuffer str = new StringBuffer();
            str.append(decimalFormat.format(info.getOneDayRetention())).append(";");
            str.append(getSeriesStr(info.getOneDayRetention(), yesList.get(0).getOneDayRetention(), weekList.get(0).getOneDayRetention()));

            resultList.add(str);
        }
        if (GameDataConstants.THREE_DAY_RETENTION.equals(parameter)) {
            StringBuffer str = new StringBuffer();
            str.append(decimalFormat.format(info.getThreeDayRetention())).append(";");
            str.append(getSeriesStr(info.getThreeDayRetention(), yesList.get(0).getThreeDayRetention(), weekList.get(0).getThreeDayRetention()));

            resultList.add(str);
        }
        if (GameDataConstants.SEVEN_DAY_RETENTION.equals(parameter)) {

            StringBuffer str = new StringBuffer();
            str.append(decimalFormat.format(info.getSevenDayRetention())).append(";");
            str.append(getSeriesStr(info.getSevenDayRetention(), yesList.get(0).getSevenDayRetention(), weekList.get(0).getSevenDayRetention()));

            resultList.add(str);
        }
        if (GameDataConstants.NEW_USER_ONE_DAY_RETENTION.equals(parameter)) {
            StringBuffer str = new StringBuffer();
            str.append(decimalFormat.format(info.getNewUserOneDayRetention())).append(";");
            str.append(getSeriesStr(info.getNewUserOneDayRetention(), yesList.get(0).getNewUserOneDayRetention(), weekList.get(0).getNewUserOneDayRetention()));

            resultList.add(str);
        }
        if (GameDataConstants.NEW_USER_THREE_DAY_RETENTION.equals(parameter)) {

            StringBuffer str = new StringBuffer();
            str.append(decimalFormat.format(info.getNewUserThreeDayRetention())).append(";");
            str.append(getSeriesStr(info.getNewUserThreeDayRetention(), yesList.get(0).getNewUserThreeDayRetention(), weekList.get(0).getNewUserThreeDayRetention()));

            resultList.add(str);
        }
        if (GameDataConstants.NEW_USER_SEVEN_DAY_RETENTION.equals(parameter)) {

            StringBuffer str = new StringBuffer();
            str.append(decimalFormat.format(info.getNewUserSevenDayRetention())).append(";");
            str.append(getSeriesStr(info.getNewUserSevenDayRetention(), yesList.get(0).getNewUserSevenDayRetention(), weekList.get(0).getNewUserSevenDayRetention()));

            resultList.add(str);
        }
        return resultList;
    }

    private List<Object> getNewUsersData(DatawareFinalGameInfo info, String parameter, List<DatawareFinalGameInfo> yesList, List<DatawareFinalGameInfo> weekList) {
        List<Object> resultList = Lists.newArrayList();
        if (GameDataConstants.NEW_USER_COUNT.equals(parameter)) {
            StringBuffer str = new StringBuffer();
            str.append(decimalFormat.format(info.getNewUserCount())).append(";");
            str.append(getSeriesStr(info.getNewUserCount().doubleValue(), yesList.get(0).getNewUserCount().doubleValue(), weekList.get(0).getNewUserCount().doubleValue()));
            resultList.add(str);
        }
        if (GameDataConstants.NEW_USER_BETTING_USER_COUNT.equals(parameter)) {
            StringBuffer str = new StringBuffer();
            str.append(decimalFormat.format(info.getNewUserBettingUserCount())).append(";");
            str.append(getSeriesStr(info.getNewUserBettingUserCount().doubleValue(), yesList.get(0).getNewUserBettingUserCount().doubleValue(), weekList.get(0).getNewUserBettingUserCount().doubleValue()));

            resultList.add(str);
        }
        if (GameDataConstants.NEW_USER_BETTING_CONVERSION.equals(parameter)) {

            StringBuffer str = new StringBuffer();
            str.append(decimalFormat.format(info.getNewUserBettingConversion())).append(";");
            str.append(getSeriesStr(info.getNewUserBettingConversion(), yesList.get(0).getNewUserBettingConversion(), weekList.get(0).getNewUserBettingConversion()));

            resultList.add(str);
        }
        if (GameDataConstants.NEW_USER_BETTING_AMOUNT.equals(parameter)) {

            StringBuffer str = new StringBuffer();
            str.append(decimalFormat.format(info.getNewUserBettingAmount())).append(";");
            str.append(getSeriesStr(info.getNewUserBettingAmount(), yesList.get(0).getNewUserBettingAmount(), weekList.get(0).getNewUserBettingAmount()));

            resultList.add(str);
        }
        if (GameDataConstants.NEW_USER_DIFF_AMOUNT.equals(parameter)) {
            StringBuffer str = new StringBuffer();
            str.append(decimalFormat.format(info.getNewUserDiffAmount())).append(";");
            str.append(getSeriesStr(info.getNewUserDiffAmount(), yesList.get(0).getNewUserDiffAmount(), weekList.get(0).getNewUserDiffAmount()));

            resultList.add(str);
        }
        if (GameDataConstants.NEW_USER_RETURN_RATE.equals(parameter)) {
            StringBuffer str = new StringBuffer();
            str.append(decimalFormat.format(info.getNewUserReturnRate())).append(";");
            str.append(getSeriesStr(info.getNewUserReturnRate(), yesList.get(0).getNewUserReturnRate(), weekList.get(0).getNewUserReturnRate()));

            resultList.add(str);
        }
        if (GameDataConstants.NEW_USER_BETTING_COUNT.equals(parameter)) {
            StringBuffer str = new StringBuffer();
            str.append(decimalFormat.format(info.getNewUserBettingCount())).append(";");
            str.append(getSeriesStr(info.getNewUserBettingCount().doubleValue(), yesList.get(0).getNewUserBettingCount().doubleValue(), weekList.get(0).getNewUserBettingCount().doubleValue()));

            resultList.add(str);
        }
        if (GameDataConstants.NEW_USER_BETTING_ARPU.equals(parameter)) {
            StringBuffer str = new StringBuffer();
            str.append(decimalFormat.format(info.getNewUserBettingArpu())).append(";");
            str.append(getSeriesStr(info.getNewUserBettingArpu(), yesList.get(0).getNewUserBettingArpu(), weekList.get(0).getNewUserBettingArpu()));

            resultList.add(str);
        }
        if (GameDataConstants.NEW_USER_BETTING_ASP.equals(parameter)) {
            StringBuffer str = new StringBuffer();
            str.append(decimalFormat.format(info.getNewUserBettingAsp())).append(";");
            str.append(getSeriesStr(info.getNewUserBettingAsp(), yesList.get(0).getNewUserBettingAsp(), weekList.get(0).getNewUserBettingAsp()));
            resultList.add(str);
        }
        return resultList;
    }

    private StringBuffer getSeriesStr(Double paramter, Double yesParamter, Double weekParamter) {
        StringBuffer seriesStr = new StringBuffer();
        DecimalFormat df = new DecimalFormat("0.00");
        if (yesParamter.doubleValue() == 0.0) {
            seriesStr.append("日环比：0%;");
        } else {
            seriesStr.append("日环比:")
                    .append(df.format(BigDecimalUtil.div((paramter - yesParamter.doubleValue()) * 100, yesParamter.doubleValue(), 2)))
                    .append("%; ");
        }

        if (weekParamter.doubleValue() == 0.0) {
            seriesStr.append("周同比：0%;");
        } else {
            seriesStr.append("周同比:")
                    .append(df.format(BigDecimalUtil.div((paramter - weekParamter) * 100, weekParamter, 2)))
                    .append("%; ");
        }
        return seriesStr;
    }

    private List<Object> getAllUsersData(DatawareFinalGameInfo info, String parameter, List<DatawareFinalGameInfo> yesList, List<DatawareFinalGameInfo> weekList) {
        List<Object> resultList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(yesList)) {
            yesList = Lists.newArrayList();
            yesList.add(new DatawareFinalGameInfo().init());
        }
        if (CollectionUtils.isEmpty(weekList)) {
            weekList = Lists.newArrayList();
            weekList.add(new DatawareFinalGameInfo().init());
        }

        if (GameDataConstants.DAU.equals(parameter)) {
            StringBuilder str = new StringBuilder();
            str.append(decimalFormat.format(info.getDau())).append(";");
            str.append(getSeriesStr(info.getDau().doubleValue(), yesList.get(0).getDau().doubleValue(), weekList.get(0).getDau().doubleValue()));
            resultList.add(str);
        }
        if (GameDataConstants.USER_COUNT.equals(parameter)) {
            StringBuilder str = new StringBuilder();
            str.append(decimalFormat.format(info.getBettingUserCount())).append(";");
            str.append(getSeriesStr(info.getBettingUserCount().doubleValue(), yesList.get(0).getBettingUserCount().doubleValue(), weekList.get(0).getBettingUserCount().doubleValue()));
            resultList.add(str);
        }
        if (GameDataConstants.CONVERSION.equals(parameter)) {
            StringBuilder str = new StringBuilder();
            str.append(decimalFormat.format(info.getBettingConversion())).append(";");
            str.append(getSeriesStr(info.getBettingConversion(), yesList.get(0).getBettingConversion(), weekList.get(0).getBettingConversion()));

            resultList.add(str);
        }
        if (GameDataConstants.BETTING_AMOUNT.equals(parameter)) {
            StringBuilder str = new StringBuilder();
            str.append(decimalFormat.format(info.getBettingAmount())).append(";");
            str.append(getSeriesStr(info.getBettingAmount(), yesList.get(0).getBettingAmount(), weekList.get(0).getBettingAmount()));

            resultList.add(str);
        }
        if (GameDataConstants.DIFF_AMOUNT.equals(parameter)) {
            StringBuffer str = new StringBuffer();
            str.append(decimalFormat.format(info.getDiffAmount())).append(";");
            str.append(getSeriesStr(info.getDiffAmount(), yesList.get(0).getDiffAmount(), weekList.get(0).getDiffAmount()));

            resultList.add(str);
        }
        if (GameDataConstants.RETURN_RATE.equals(parameter)) {
            StringBuffer str = new StringBuffer();
            str.append(decimalFormat.format(info.getDiffAmount())).append(";");
            str.append(getSeriesStr(info.getReturnRate(), yesList.get(0).getReturnRate(), weekList.get(0).getReturnRate()));

            resultList.add(str);
        }
        if (GameDataConstants.BETTING_COUNT.equals(parameter)) {
            StringBuffer str = new StringBuffer();
            str.append(decimalFormat.format(info.getBettingCount())).append(";");
            str.append(getSeriesStr(info.getBettingCount().doubleValue(), yesList.get(0).getBettingCount().doubleValue(), weekList.get(0).getBettingCount().doubleValue()));

            resultList.add(str);
        }
        if (GameDataConstants.BETTING_ARPU.equals(parameter)) {
            StringBuffer str = new StringBuffer();
            str.append(decimalFormat.format(info.getBettingArpu())).append(";");
            str.append(getSeriesStr(info.getBettingArpu(), yesList.get(0).getBettingArpu(), weekList.get(0).getBettingArpu()));

            resultList.add(str);
        }
        if (GameDataConstants.BETTING_ASP.equals(parameter)) {
            StringBuffer str = new StringBuffer();
            str.append(info.getBettingAsp().toString()).append(";");
            str.append(getSeriesStr(info.getBettingAsp(), yesList.get(0).getBettingAsp(), weekList.get(0).getBettingAsp()));

            resultList.add(str);
        }
        return resultList;
    }

    @RequestMapping("/export")
    public void export(@RequestParam List<Long> parentIds, @RequestParam List<Long> gameTypes, @RequestParam String startTime, @RequestParam String endTime, HttpServletResponse response) {

        if (StringUtil.isBlank(startTime)) {
            startTime = DateUtils.formatDate(DateUtils.getNextDate(new Date(), -7));
        }
        if (StringUtil.isBlank(endTime)) {
            endTime = DateUtils.getYesterdayDate();
        }
        Map<String, Object> map = new HashMap<>();
        map.put(GameDataConstants.BEGINDATE, startTime);
        map.put(GameDataConstants.ENDDATE, endTime);
        map.put("parentIds", parentIds);
        map.put("gameTypes", gameTypes);
        List<DatawareFinalGameInfo> exportList = datawareFinalGameInfoService.findListByDate(map);
        for (DatawareFinalGameInfo info : exportList) {
            if (1 == info.getParentId()) {
                info.setParentIdName("全部(1)");
            } else {
                ChannelInfo channelInfo = channelInfoService.get(info.getParentId());
                if (channelInfo == null) {
                    channelInfo = new ChannelInfo();
                }
                info.setParentIdName(channelInfo.getName() + "(" + info.getParentId() + ")");
            }
            DataDict dict = dataDictService.getDictByValue(GameDataConstants.GAME_TYPE, info.getGameType());
            if (null == dict) {
                dict = new DataDict();
            }
            info.setGameName(dict.getLabel() + "(" + info.getGameType() + ")");
        }
        try {
            String fileName = "游戏数据总览" + com.wf.core.utils.type.DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel("游戏数据总览", DatawareFinalGameInfo.class).setDataList(exportList).write(response, fileName).dispose();
        } catch (Exception e) {
            logger.error("导出游戏数据总览失败: traceId={}, data={}", TraceIdUtils.getTraceId(), GfJsonUtil.toJSONString(map.toString()));
        }
    }
}

